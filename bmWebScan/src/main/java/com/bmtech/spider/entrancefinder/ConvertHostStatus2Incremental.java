package com.bmtech.spider.entrancefinder;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bmtech.spider.core.Connectioner;
import com.bmtech.spider.core.HostFilter;
import com.bmtech.spider.core.HostInfo;
import com.bmtech.spider.core.HostInitorTool;
import com.bmtech.spider.core.HostInjectTool;
import com.bmtech.spider.core.ScanConfig;
import com.bmtech.utils.Misc;
import com.bmtech.utils.bmfs.MDir;
import com.bmtech.utils.rds.RDS;

public class ConvertHostStatus2Incremental {

	ConvertHostStatus2Incremental() throws SQLException {

	}

	List<HostInfo> setted = new ArrayList<HostInfo>();
	List<HostInfo> lstHost;

	public List<HostInfo> getHostInfos() throws SQLException {
		RDS getNumberMatchHost = RDS.getRDSByKey("getNumberMatchHost");
		try {
			ResultSet rs = getNumberMatchHost.executeQuery();
			List<HostInfo> lst = new ArrayList<HostInfo>();
			while (rs.next()) {
				String host = rs.getString(1);
				if (host.length() < 4) {
					System.out.println("SKIP HOST " + host);
					continue;
				}
				lst.add(new HostInfo(host));
			}
			return lst;
		} finally {
			RDS.close(getNumberMatchHost);
		}

	}

	List<PageInfo> getTop(File dir, HostInfo info, int topNum) throws Exception {
		MDir mdir = MDir.open(dir);
		try {
			SiteMDirEntranceFinder finder = new SiteMDirEntranceFinder(mdir,
					info);
			finder.caculate();
			List<PageInfo> top = finder.top(topNum);
			MDir.closeMDir(mdir);
			return top;
		} finally {
			MDir.closeMDir(mdir);
		}
	}

	public void run(int topNum, int minEntrane) throws Exception {
		lstHost = getHostInfos();
		HostInjectTool tool = new HostInjectTool();
		for (HostInfo info : lstHost) {
			final File dir = ScanConfig.instance.getSaveDir(info);
			System.out.println("checking " + dir);
			if (!dir.exists()) {
				System.out.println("skip empty " + dir);
				continue;
			}
			try {
				List<PageInfo> top = this.getTop(dir, info, topNum);
				for (PageInfo pi : top) {
					System.out.println(pi);

				}
				if (top.size() < minEntrane) {
					System.out.println("skip " + info + ", too few entrance "
							+ top.size());
				} else {
					// Consoler.readString("-----press any key to continue>");
					for (PageInfo pi : top) {
						try {
							tool.injectAlwaysAllow(info, pi.getUrl());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					setted.add(info);
				}

				HostInitorTool initor = new HostInitorTool(info.getHostName(),
						1024 * 32);
				try {
					initor.markAllUrlToCrawled();
				} finally {
					initor.close();
				}
				Connectioner.instance().setHostStatus(info.getHostName(),
						HostFilter.Status_Incr_watch);
				if ("delOrg".equalsIgnoreCase(System.getProperty("isDelOrg"))) {
					System.out.println("deleteing ................... " + dir);
					Misc.del(dir);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		tool.close();
	}

	public static void main(String[] args) throws Exception {
		ConvertHostStatus2Incremental scanor = new ConvertHostStatus2Incremental();
		scanor.run(20, 10);
		for (HostInfo hi : scanor.setted) {
			System.out.println("\t\t" + hi);
		}
		System.out.println("total scannor " + scanor.lstHost.size());
		System.out.println("convertted " + scanor.setted.size());

	}
}
