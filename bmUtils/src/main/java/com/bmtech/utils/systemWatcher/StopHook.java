package com.bmtech.utils.systemWatcher;

import com.bmtech.utils.log.BmtLogger;

/**
 * StopHook wait until toWait time reached or {@link #canStop()} called
 * @author Fisher@Beiming
 *
 */
public class StopHook implements Runnable{
	final Object STOPOBJECT = new Object();
	final long toWait;
	StopHook(long toWait){
		if(toWait < 1) {
			toWait = 0;
		}
		this.toWait = toWait;
	}
	
	protected boolean blockStop = true;
	
	private void runIt() {
		synchronized(STOPOBJECT) {
			try {
				BmtLogger.log.warn(
						"waiting for stop hook signal, wait = %s, I'm %s",
						toWait, this);
				if(toWait == 0) {
					synchronized (STOPOBJECT) {
						STOPOBJECT.wait();
					}
				}else {
					synchronized (STOPOBJECT) {
						STOPOBJECT.wait(toWait);
					}
				}
				BmtLogger.log.warn(
						"finishing stopping, wait = %s",
						toWait, this);
			} catch (Throwable e) {
				BmtLogger.log.warn(
						e,
						"stop signal got or stop timeoutted, wait = %s, I'm %s",
						toWait, this);
			}
		}
	}
	/**
	 * tell StopHook that it can stop running now.
	 */
	public void canStop() {
		synchronized(STOPOBJECT) {
			STOPOBJECT.notifyAll();
		}
		BmtLogger.log.debug("I'm told that I can stop");
	}
	public void block(){
		this.blockStop(true);
	}
	public void unblock(){
		this.blockStop(false);
	}
	/**
	 * when call this method with {@link #block()} true, 4ms will
	 * be used to synchronize with other threads
	 * @param block
	 */
	public synchronized void blockStop(boolean block) {
		this.blockStop = block;
		BmtLogger.log.warn(
				"set block flag to %s", this.blockStop);
		if(this.blockStop) {
			try {
				Thread.sleep(4);
			} catch (InterruptedException e) {
				BmtLogger.instance().log(e, "when eric block stop flag");
			}
		}else {
			canStop();
		}
	}
	@Override
	public void run() {
		BmtLogger.log.warn(
				"running stopHook with block flag %s", this.blockStop);
		if(this.blockStop) {
			this.runIt();
		}else {
			return;
		}
	}
}