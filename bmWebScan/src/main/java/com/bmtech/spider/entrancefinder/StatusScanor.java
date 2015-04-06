package com.bmtech.spider.entrancefinder;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bmtech.spider.core.HostInfo;
import com.bmtech.spider.core.ScanConfig;
import com.bmtech.utils.bmfs.MDir;
import com.bmtech.utils.rds.RDS;

public class StatusScanor {

	StatusScanor() throws SQLException {

	}

	public List<HostInfo> getHostInfos() throws SQLException {
		RDS getNumberMatchHost = RDS.getRDSByKey("getNumberMatchHost");
		try {
			ResultSet rs = getNumberMatchHost.executeQuery();
			List<HostInfo> lst = new ArrayList<HostInfo>();
			while (rs.next()) {
				String host = rs.getString(1);
				lst.add(new HostInfo(host));
			}
			return lst;
		} finally {
			RDS.close(getNumberMatchHost);
		}

	}

	List<PageInfo> getTop(File dir, HostInfo info) throws Exception {
		MDir mdir = MDir.open(dir);
		try {
			SiteMDirEntranceFinder finder = new SiteMDirEntranceFinder(mdir,
					info);
			List<PageInfo> top = finder.top(20);
			return top;
		} finally {
			MDir.closeMDir(mdir);
		}
	}

	public void run(int minEntrane) throws Exception {
		List<HostInfo> lst = getHostInfos();

		for (HostInfo info : lst) {
			File dir = ScanConfig.instance.getHostBase(info);
			System.out.println(dir);
			if (!dir.exists()) {
				System.out.println("skip empty " + dir);
				continue;
			}

			List<PageInfo> top = this.getTop(dir, info);
			for (PageInfo pi : top) {
				System.out.println(pi);
			}
			if (top.size() < minEntrane) {
				System.out.println("skip " + info + ", too few entrance "
						+ top.size());
			}
		}
	}

	public static void main(String[] args) throws Exception {
		StatusScanor scanor = new StatusScanor();
		scanor.run(10);
	}
}
