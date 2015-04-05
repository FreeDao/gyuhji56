package com.bmtech.spider.core;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.bmtech.utils.rds.RDS;
import com.bmtech.utils.rds.SourceDefine;

public class Connectioner {
	// private RDS rds_addHas;
	private RDS rds_setHas;
	// private RDS rds_insert;
	private RDS rds_path;
	private RDS rds_chechHost;
	private RDS rds_3, getAllowsAllow;
	private RDS rdsGetUnInjected, rdsSetToInjected;
	private Connection conn;
	private static Connectioner instance = new Connectioner();

	public static Connectioner instance() {
		return instance;
	}

	private Connectioner() {
		try {
			getConnected();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private synchronized void getConnected() throws SQLException {
		if (conn != null) {
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				conn = null;
			}
		}

		conn = SourceDefine.instance().getDataSourceDefine("webscan")
				.getNewConnection();

		rds_setHas = RDS.getRDSByKey("setHas", conn);
		// .getRDSByDefine(
		// "webscan",
		// "UPDATE webscan_host_info set HasDetected=? , scanRound = (scanRound + 1) where host=?",
		// conn);
		// rds_addHas = RDS
		// .getRDSByDefine(
		// "webscan",
		// "UPDATE webscan_host_info set HasDetected=( HasDetected +?) where host=?",
		// conn);
		//
		// rds_insert = RDS
		// .getRDSByDefine(
		// "webscan",
		// "insert into webscan_host_crawler_log(urlHash,host,url,list_score,detail_page_score,start_time,file_seq)values(?,?,?,?,?,?,?)",
		// conn);

		rds_path = RDS.getRDSByDefine("webscan",
				"select filePath from webscan_host_info where host = ?", conn);

		rds_chechHost = RDS.getRDSByDefine("webscan",
				"select * from webscan_host_info where host = ?", conn);

		rds_3 = RDS.getRDSByDefine("webscan",
				"update webscan_host_info set status=? where host = ?", conn);

		rdsGetUnInjected = RDS
				.getRDSByDefine(
						"webscan",
						"select * from webscan_host_inject where type = 1 and host = ?",
						conn);

		rdsSetToInjected = RDS.getRDSByDefine("webscan",
				"update webscan_host_inject set type = -1 where id = ?", conn);
		this.getAllowsAllow = RDS
				.getRDSByDefine("webscan",
						"select url from webscan_host_url_allows where host = ? and status = 1");

	}

	public class URLToInject {
		public final int id;
		public final String url;

		public URLToInject(ResultSet rs) throws SQLException {
			id = rs.getInt("id");
			url = rs.getString("url");
		}

		@Override
		public String toString() {
			return "URLToInject:" + id + " " + url;
		}
	}

	public synchronized ArrayList<URLToInject> getUnInjected(String host)
			throws SQLException {
		ArrayList<URLToInject> ret = new ArrayList<URLToInject>();
		this.rdsGetUnInjected.setString(1, host);
		ResultSet rs = this.rdsGetUnInjected.executeQuery();
		while (rs.next()) {
			URLToInject ut = new URLToInject(rs);
			ret.add(ut);
		}
		return ret;
	}

	public synchronized void setToInjected(int id) throws SQLException {
		rdsSetToInjected.setInt(1, id);
		rdsSetToInjected.execute();
	}

	// public synchronized void addHasCrawled(String host, int inc)
	// throws SQLException {
	// try {
	// rds_addHas.setInt(1, inc);
	// rds_addHas.setString(2, host);
	// rds_addHas.execute();
	// } catch (SQLException e) {
	// e.printStackTrace();
	// this.getConnected();
	// }
	// }

	public synchronized void setHasCrawled(String host, int num)
			throws SQLException {
		try {
			rds_setHas.setInt(1, num);
			rds_setHas.setString(2, host);
			rds_setHas.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			this.getConnected();
		}
	}

	public synchronized String findPath(String host) throws SQLException {
		String path = null;
		rds_path.setString(1, host);
		ResultSet rs = rds_path.load();
		if (rs.next()) {
			path = rs.getString(1);
		}
		return path;
	}

	public synchronized boolean checkHostValid(String host) {
		try {
			rds_chechHost.setString(1, host);
			ResultSet rs = rds_chechHost.load();
			if (rs.next()) {
				if (rs.getInt("status") == 1) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} catch (Exception e) {
			try {
				this.getConnected();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return false;
		}
	}

	public synchronized void setHostEndStatus(String host, int status) {
		try {
			rds_3.setInt(1, status);
			rds_3.setString(2, host);
			rds_3.execute();
		} catch (SQLException e) {
			try {
				this.getConnected();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	public synchronized ArrayList<String> getAlwaysAllow(String host)
			throws Exception {
		try {
			getAllowsAllow.setString(1, host);
			ResultSet rs = getAllowsAllow.executeQuery();
			ArrayList<String> ret = new ArrayList<String>();
			while (rs.next()) {
				ret.add(rs.getString(1));
			}
			return ret;
		} catch (Exception e) {
			this.getConnected();
			throw e;
		}
	}

}
