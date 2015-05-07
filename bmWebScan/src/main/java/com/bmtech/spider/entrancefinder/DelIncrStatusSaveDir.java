package com.bmtech.spider.entrancefinder;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bmtech.spider.core.HostInfo;
import com.bmtech.spider.core.ScanConfig;
import com.bmtech.utils.Misc;
import com.bmtech.utils.rds.RDS;

public class DelIncrStatusSaveDir {
	public List<HostInfo> getHostInfos() throws SQLException {
		RDS getNumberMatchHost = RDS.getRDSByKey("getIncrStatus");
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

	public void run() throws Exception {
		List<HostInfo> lstHost = getHostInfos();
		for (HostInfo info : lstHost) {
			final File dir = ScanConfig.instance.getSaveDir(info);
			System.out.println("checking " + dir);
			if (!dir.exists()) {
				System.out.println("skip empty " + dir);
				continue;
			}
			try {
				System.out.println("deleting " + dir);
				if ("delOrg".equalsIgnoreCase(System.getProperty("isDelOrg"))) {
					System.out.println("deleteing ................... " + dir);
					Misc.del(dir);
				} else {
					System.out.println("NOT ALLOWED ");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws Exception {
		DelIncrStatusSaveDir del = new DelIncrStatusSaveDir();
		del.run();
	}
}
