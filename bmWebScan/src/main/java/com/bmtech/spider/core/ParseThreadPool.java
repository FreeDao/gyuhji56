package com.bmtech.spider.core;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.bmtech.utils.log.LogHelper;

public class ParseThreadPool {
	private static ParseThreadPool instance = new ParseThreadPool();
	final BlockingQueue<Runnable> parseQueue = new LinkedBlockingQueue<Runnable>();
	final ThreadPoolExecutor parsePool;
	private AtomicInteger allRun = new AtomicInteger(0);
	LogHelper log = new LogHelper("parsePool");

	public ParseThreadPool() {
		int threadNum = ScanConfig.instance.parseThread;
		parsePool = new ThreadPoolExecutor(threadNum, threadNum, 100,
				TimeUnit.SECONDS, parseQueue) {
			@Override
			protected void afterExecute(Runnable r, Throwable t) {
				if (t != null) {
					log.fatal(t, "when parse");
				}
				try {
					int allRuned = allRun.addAndGet(1);

					if (allRuned % 10 == 0) {
						log.fatal(
								"parsePool has allRuned:%s, getTaskCount:%s, now running parser:%s, waiting parse tasks: %s",
								allRuned, parsePool.getTaskCount(),
								parsePool.getActiveCount(), parseQueue.size());
					}
				} finally {
					HostCtrlImpl.getInstance().putBack((HostScan) r);
				}
			}
		};
	}

	public void submitToParse(Runnable toParse) {
		parsePool.execute(toParse);
	}

	public static ParseThreadPool getInstance() {
		return instance;
	}

	public boolean tooBusy() {
		return this.parseQueue.size() > ScanConfig.instance.parseQueueMaxSize;
	}
}
