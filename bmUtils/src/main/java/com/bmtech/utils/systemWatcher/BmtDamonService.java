package com.bmtech.utils.systemWatcher;

import java.io.IOException;

import com.bmtech.utils.log.LogHelper;

/**
 * the sub-class works as:<br>
 * init<br>
 * do your work {@link #work()}<br>
 * {@link #block()}
 */
public abstract class BmtDamonService {
	public final SystemWatcher watcher;
	public final LogHelper logd;

	public BmtDamonService() throws IOException {
		watcher = SystemWatcher.regWatcher();
		logd = new LogHelper("daemon." + watcher.conf.getSysName());

		logd.info("sysName is %s", watcher.conf.getSysName());
		System.setProperty("BmtDamonService_sysName", watcher.conf.getSysName());
	}

	public static String getEnv() {
		return System.getProperty("BmtDamonService_env");
	}

	/**
	 * recommended method. equals:<code>
	 * <br> {@link #work(Object[])}; <br>{@link #block()}<br>
	 * </code>
	 * 
	 * @param paras
	 * @throws Exception
	 */
	protected void startDamon(Object[] paras) throws Exception {
		work(paras);
		block();
	}

	/**
	 * work thread. instead calling this method, it is recommended to use
	 * {@link #startDamon(Object[])}
	 * 
	 * @param paras
	 * @throws Exception
	 */
	protected abstract void work(Object[] paras) throws Exception;

	public void block() {
		watcher.block();
		// unreachable code, protect toRun not be collected by GC
	}

	protected void shutdown() throws IOException {
		RemoteWatchClient clt = new RemoteWatchClient(watcher.server.getPort(),
				watcher.getShutDownAction().key);
		clt.writeCommand(watcher.getShutDownAction().cmd);
	}
}
