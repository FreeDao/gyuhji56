package com.bmtech.utils.systemWatcher;

import java.io.IOException;
import java.text.SimpleDateFormat;

import com.bmtech.utils.io.TchFileTool;
import com.bmtech.utils.log.LogHelper;
/**
 * the sub-class works as:<br>
 * init<br>
 * do your work {@link #work()}<br>
 * {@link #block()}
 */
public abstract class BmtDamonService {
	public final SystemWatcher watcher;
	final String envName;
	public final LogHelper logd;
	
	public BmtDamonService(int port, String enc, String sysName) throws IOException { 
		if(sysName != null) {
			logd = new LogHelper(sysName);
		}else{
			logd = new LogHelper("daemon");
		}
		
		watcher = SystemWatcher.regWatcher(port, enc, sysName + "");
		String envNameStr = TchFileTool.get("config", "envName");
		this.envName = envNameStr == null ? "unknowEnv_" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()) : envNameStr;
		logd.info("envName is %s", this.envName);
		System.setProperty("BmtDamonService_env", envName);
	}
	public static String getEnv(){
		return System.getProperty("BmtDamonService_env");
	}
	
	/**
	 * recommended method.
	 * equals:<code>
	 * <br> {@link #work(Object[])}; <br>{@link #block()}<br>
	 * </code>
	 * @param paras
	 * @throws Exception
	 */
	protected void startDamon(Object[]paras) throws Exception{
		work(paras);
		block();
	}
	/**
	 * work thread.
	 * instead calling this method,
	 * it is recommended to use {@link #startDamon(Object[])} 
	 * @param paras
	 * @throws Exception
	 */
	protected abstract void work(Object[]paras)throws Exception ;

	public void block() {
		watcher.block();
		//unreachable code, protect toRun not be collected by GC
	}

	protected void shutdown() throws IOException {
		RemoteWatchClient clt = new RemoteWatchClient(watcher.server.getPort(),
				watcher.getShutDownAction().key);
		clt.writeCommand(watcher.getShutDownAction().cmd);
	}
}
