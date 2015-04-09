package com.bmtech.spider.core;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.bmtech.utils.Consoler;
import com.bmtech.utils.log.BmtLogHelper;
import com.bmtech.utils.rds.RDS;

public class HostInjectTool {

	RDS checkHasHost, checkHasUrl, checkAllway, insertAlways;
	RDS insertHost, injectInsertUrl;
	BmtLogHelper log = new BmtLogHelper("hostInject");
	ScanConfig conf = ScanConfig.instance;
	HostFilter flt;
	Connection conn;

	public HostInjectTool() throws SQLException, InstantiationException,
			IllegalAccessException {
		checkHasHost = RDS.getRDSByDefine("webscan",
				"select * from webscan_host_info where host = ?");
		conn = checkHasHost.getMyConnection();
		insertHost = RDS
				.getRDSByDefine(
						"webscan",
						"insert into webscan_host_info(host, hashId, filepath, intime, level)values(?, ?, ?, unix_timestamp(NOW()), ?)",
						conn);

		checkHasUrl = RDS.getRDSByDefine("webscan",
				"select url from webscan_host_inject where host = ?", conn);
		injectInsertUrl = RDS.getRDSByDefine("webscan",
				"insert into webscan_host_inject(host, type, url, score)values(?, 1, ?, "
						+ conf.injectedUrlValue + ")", conn);
		log.info("inject ready!");
		flt = conf.hostFilterCls.newInstance();

		checkAllway = RDS.getRDSByKey("checkAllway", conn);

		insertAlways = RDS.getRDSByKey("insertAlways", conn);
	}

	@Override
	public void finalize() {
		this.close();
	}

	public void close() {
		try {
			if (conn != null) {
				this.conn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private synchronized HashSet<String> getHasInjected(String host)
			throws SQLException {
		this.checkHasUrl.setString(1, host);
		ResultSet rs = this.checkHasUrl.executeQuery();
		HashSet<String> ret = new HashSet<String>();
		while (rs.next()) {
			ret.add(rs.getString(1));
		}
		return ret;
	}

	public synchronized boolean injectAlwaysAllow(HostInfo host, String surl)
			throws SQLException {
		checkAllway.setString(1, surl);
		ResultSet rs = checkAllway.executeQuery();
		if (rs.next()) {
			log.warn("skip has set url %s", surl);
			return false;
		}
		insertAlways.setString(1, host.getHostName());
		insertAlways.setString(2, surl);
		insertAlways.setInt(3, 4);
		insertAlways.execute();
		return true;
	}

	public synchronized int injectUrl(String host, List<URL> urls, int level)
			throws SQLException, IOException {
		log.debug("injecting %s, url size %s", host, urls.size());
		checkHasHost.setString(1, host);
		boolean canInject = true;
		ResultSet rs = checkHasHost.executeQuery();
		File base = conf.getHostBase(new HostInfo(host));
		if (rs.next()) {
			int status = rs.getInt("status");
			if (status != 1) {
				canInject = false;
				log.error(
						"can not inject urls for host %s, because host status = %s",
						host, status);
			}
		} else {
			insertHost.setString(1, host);
			insertHost.setInt(2, conf.hostHash(host));
			insertHost.setString(3,
					base.getParentFile().getName() + "/" + base.getName());
			insertHost.setInt(4, level);
			insertHost.execute();
		}
		if (canInject) {
			if (flt.checkAndForbidden(host)) {
				log.error(
						"can not inject urls for host %s, because hostFilter reject!",
						host);
				canInject = false;
			}
		}
		int injected = 0;
		int skiped = 0;
		if (canInject) {
			Set<String> set = getHasInjected(host);
			conn.setAutoCommit(false);
			for (URL u : urls) {
				String uStr = u.toString();
				if (uStr.length() > 500) {
					System.out.print("trunck " + uStr);
					uStr = uStr.substring(0, 500);
					System.out.println(" to " + uStr);
				}
				if (set.contains(uStr)) {
					skiped++;
					log.debug("skip already host %s", u);
				} else {
					injected++;
					injectInsertUrl.setString(1, host);
					injectInsertUrl.setString(2, uStr);
					injectInsertUrl.execute();
				}

			}
			conn.commit();
			conn.setAutoCommit(true);

		}
		log.info("inject OK for host %s, tot %s, ok %s, skip %s", host,
				urls.size(), injected, skiped);
		return urls.size();
	}

	public void injectFile(File f, int level) throws Exception {
		URLHostBuffer buffer = new URLHostBuffer(f);

		int injected = 0, lastprnt = 0;
		while (true) {
			List<URL> lst = buffer.next();
			if (lst == null) {
				break;
			}
			injected += injectUrl(CoreUtil.urlHost(lst.get(0)), lst, level);
			if (injected - lastprnt > 1000) {
				lastprnt = injected;
				log.warn("total injected %s", injected);
			}
		}
		buffer.close();
	}

	public static void main(String[] args) throws Exception {
		HostInjectTool instance = new HostInjectTool();
		int level = Consoler.readInt("host level:", 0);
		if (!Consoler.confirm("leve is " + level + "?")) {
			System.out.println("confirm fail!");
			System.exit(0);
		}
		while (true) {
			File f = null;
			while (true) {
				Thread.sleep(100);
				String path = Consoler.readString("import file path : ");
				f = new File(path);
				if (!f.exists()) {
					instance.log.error("file is not exists! %s", f
							.getCanonicalFile().getAbsolutePath());
					continue;
				}
				break;
			}

			inject(f, instance, level);
			Consoler.readString("ok injected! press any key to continue ...");
		}
	}

	public static void inject(File file, HostInjectTool instance, int level) {
		if (file.isDirectory()) {
			File[] fs = file.listFiles();
			for (File f : fs) {
				inject(f, instance, level);
			}
		} else {
			try {
				instance.injectFile(file, level);
			} catch (Exception e) {
				e.printStackTrace();
				instance.log.error(e, "for file %s", file);
			}
		}
	}
}
