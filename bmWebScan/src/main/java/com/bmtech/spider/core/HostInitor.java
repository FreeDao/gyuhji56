package com.bmtech.spider.core;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.bmtech.spider.core.AbstractHostCtrl.AfterInitor;
import com.bmtech.utils.Misc;
import com.bmtech.utils.log.LogHelper;

public class HostInitor {

	class TRun implements Runnable {

		String host;
		AfterInitor afterRunnor;
		private int runNum = 0;

		TRun(String host, AfterInitor afterRunnor) {
			this.host = host;
			this.afterRunnor = afterRunnor;
		}

		@Override
		public void run() {
			log.fatal("is initing %s", host);

			if (runNum > 0) {
				new Exception("????????????????/").printStackTrace();
			}
			runNum++;
			try {
				HostInitorTool tool = new HostInitorTool(host, sortFactor);
				try {
					HostScan hs = tool.initHost();
					log.fatal("init ok %s with hostbase %s", host,
							ScanConfig.instance.getHostBase(hs.getHostInfo()));
					afterRunnor.afterInitor(hs);
				} finally {
					tool.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.fatal(e, "when init host %s", host);
			}
		}
	}

	public static HostInitor instance = new HostInitor();
	private static int sortFactor = ScanConfig.instance.sortFactor;
	BlockingQueue<Runnable> bq = new LinkedBlockingQueue<Runnable>();
	private final LogHelper log = new LogHelper("hostInitor");
	private ThreadPoolExecutor exe = new ThreadPoolExecutor(
			ScanConfig.instance.hostInitThreads,
			ScanConfig.instance.hostInitThreads, 10, TimeUnit.SECONDS, bq);

	private HostInitor() {
		new Thread() {
			@Override
			public void run() {
				while (true) {
					Misc.sleep(10000);
					log.fatal("initing %s, waiting %s", exe.getActiveCount(),
							bq.size());
				}
			}
		}.start();
	}

	public void initialize(String host, AfterInitor afterRunnor)
			throws Exception {
		TRun tr = new TRun(host, afterRunnor);
		exe.execute(tr);
	}

	public int initingNumber() {
		return bq.size() + exe.getActiveCount();
	}

	public boolean isBusy() {
		return bq.size() > 0;
	}
}
