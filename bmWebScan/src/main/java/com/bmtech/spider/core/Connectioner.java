package com.bmtech.spider.core;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.bmtech.utils.rds.RDS;
import com.bmtech.utils.rds.SourceDefine;

public class Connectioner {
	private RDS rds_setHasDetected;
	private RDS rds_chechHostStatus;
	private RDS rds_setHostStatus, rds_setHostEndStatus, rds_getAllowsAllow;
	private RDS rds_getUnInjected, rds_setToInjected;
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

		rds_setHasDetected = RDS.getRDSByKey("setHasDetected", conn);
		rds_chechHostStatus = RDS.getRDSByKey("chechHostStatus", conn);
		rds_setHostStatus = RDS.getRDSByKey("setHostStatus", conn);
		rds_setHostEndStatus = RDS.getRDSByKey("setHostEndStatus", conn);
		rds_getUnInjected = RDS.getRDSByKey("getUnInjected", conn);
		rds_setToInjected = RDS.getRDSByKey("setToInjected", conn);
		rds_getAllowsAllow = RDS.getRDSByKey("getAllowsAllow", conn);

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
		this.rds_getUnInjected.setString(1, host);
		ResultSet rs = this.rds_getUnInjected.executeQuery();
		while (rs.next()) {
			URLToInject ut = new URLToInject(rs);
			ret.add(ut);
		}
		return ret;
	}

	public synchronized void setToInjected(int id) throws SQLException {
		rds_setToInjected.setInt(1, id);
		rds_setToInjected.execute();
	}

	public synchronized void setHasCrawled(String host, int num)
			throws SQLException {
		try {
			rds_setHasDetected.setInt(1, num);
			rds_setHasDetected.setString(2, host);
			rds_setHasDetected.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			this.getConnected();
		}
	}

	public synchronized boolean checkHostValid(String host) {
		try {
			rds_chechHostStatus.setString(1, host);
			ResultSet rs = rds_chechHostStatus.load();
			if (rs.next()) {
				return true;
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

	public synchronized void setHostStatus(String host, int status) {
		try {
			rds_setHostStatus.setInt(1, status);
			rds_setHostStatus.setString(2, host);
			rds_setHostStatus.execute();
		} catch (SQLException e) {
			try {
				this.getConnected();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	public synchronized void setHostEndStatus(String host) {
		try {
			rds_setHostEndStatus.setString(1, host);
			rds_setHostEndStatus.execute();
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
			rds_getAllowsAllow.setString(1, host);
			ResultSet rs = rds_getAllowsAllow.executeQuery();
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
