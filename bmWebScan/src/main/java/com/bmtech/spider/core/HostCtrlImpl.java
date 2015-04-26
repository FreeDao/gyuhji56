package com.bmtech.spider.core;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.bmtech.utils.Misc;
import com.bmtech.utils.rds.RDS;

public class HostCtrlImpl extends AbstractHostCtrl {

	public HostCtrlImpl() throws Exception {
		super();
		Misc.del(ScanConfig.instance.tmpCrawlDir.listFiles());
	}

	ArrayList<String> getHost() throws SQLException {
		RDS rdsLoadHost = null;
		try {
			ArrayList<String> hostList = new ArrayList<String>();

			rdsLoadHost = RDS.getRDSByKey("wsLoadHostSql");// "select host from webscan_host_info where `status` =1 order by level desc,  HasDetected limit 0, 500");
			ResultSet rs = rdsLoadHost.load();
			while (rs.next()) {
				hostList.add(rs.getString(1));
			}
			return hostList;
		} finally {
			rdsLoadHost.close();
		}
	}

	@Override
	public void startHostLoadThread() {
		Thread t = new Thread() {
			void checkRound() throws Exception {

				log.fatal("hostScan check adding host");
				log.fatal("hostScan now running %s",
						hostRunnor.getActiveCount());
				boolean needFillHostPool = needFillHostPool();
				log.fatal(
						"check add new hosts, now  hostQueue.size() %s, conf.hostPoolNeedFillMargin %s, needFillHostPool %s",
						hostQueue.size(), conf.hostPoolNeedFillMargin,
						needFillHostPool);
				if (!needFillHostPool) {
					return;
				}
				if (HostInitor.instance.isBusy()) {
					log.warn("initor is busy, quit new host initor");
					return;
				}

				ArrayList<String> hostList;
				hostList = getHost();
				log.info("load new host :%s", hostList.size());
				for (String host : hostList) {

					if (!needFillHostPool()
							|| HostInitor.instance.initingNumber()
									+ hostRunnor.getActiveCount() >= conf.hostPoolSize) {
						log.warn(
								"need NOT init %s, now put full hostQueue.size() %s, conf.hostPoolNeedFillMargin %s",
								host, hostQueue.size(),
								conf.hostPoolNeedFillMargin);
						break;
					}
					initHost(host);
				}

			}

			@Override
			public void run() {
				while (true) {
					try {
						sleep(conf.controllerCheckItv);
					} catch (Exception e) {
						log.fatal(e, " %s when sleep", this.getClass());
						e.printStackTrace();
					}
					try {
						checkRound();
					} catch (Throwable e) {
						log.fatal(e, "when reload hosts");
						e.printStackTrace();
					}
				}
			}
		};
		t.start();
		log.info("host reload thread started");
	}

	@Override
	protected void work(Object[] arg0) throws Exception {
		startHostLoadThread();
	}

	private static HostCtrlImpl hci;

	public static HostCtrlImpl getInstance() {
		return hci;
	}

	public static void main(String[] args) throws Exception {
		hci = new HostCtrlImpl();
		hci.startDamon(null);
	}

}
