package com.bmtech.utils.systemWatcher.innerAction;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

import com.bmtech.utils.log.BmtLogger;
import com.bmtech.utils.systemWatcher.SystemWatcher;
import com.bmtech.utils.systemWatcher.WatcherAction;

public final class ShutDownAction extends WatcherAction{
		public ShutDownAction(String key) {
			super(STOP, key);
		}
		ArrayList<Runnable> stopHook = new ArrayList<Runnable>();
		public void addStopHook(Runnable nnn){
			stopHook.add(nnn);
		}

		@Override
		public void run(String[] paras, Socket clientSocket) {
			BmtLogger.log.warn("stopping system " + SystemWatcher.WatherLogName);
			InetSocketAddress address = 
				(InetSocketAddress) clientSocket.getRemoteSocketAddress();

			BmtLogger.log.warn(
					"Stop command got, dst address = %s:%d",
					address.getAddress().getHostAddress(),
					address.getPort()
			);
			String reply = cmd + "-->OK" ;
			for(Runnable r : stopHook) {
				if(r != null) {
					BmtLogger.log.warn("running stop hook %s", r);
					r.run();
				}else {
					BmtLogger.log.warn("skip null stop hook");
				}
			}
			try {
				this.writeBack(reply, clientSocket);
				clientSocket.close();
			}catch(Exception e) {
				BmtLogger.log.error(e, "got error by %s", cmd);
			}
			System.exit(0);
		}
	}