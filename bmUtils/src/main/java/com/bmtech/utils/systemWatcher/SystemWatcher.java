package com.bmtech.utils.systemWatcher;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.bmtech.utils.Misc;
import com.bmtech.utils.io.ConfigReader;
import com.bmtech.utils.io.FileGet;
import com.bmtech.utils.log.BmtLogger;
import com.bmtech.utils.security.BmAes;
import com.bmtech.utils.systemWatcher.innerAction.EchoAction;
import com.bmtech.utils.systemWatcher.innerAction.GCAction;
import com.bmtech.utils.systemWatcher.innerAction.ShutDownAction;
import com.bmtech.utils.systemWatcher.innerAction.SysInfoAction;
import com.bmtech.utils.systemWatcher.innerAction.SysPropAction;
import com.bmtech.utils.tcp.TCPServer;

/**
 * a TCP based System stopper,
 * 
 * @author Fisher@Beiming
 *
 */
public class SystemWatcher {
	public static final String VERSION = "bmWatcher 1.1";
	public static final String WatherLogName = "sysWatcher";

	static class WatcherVo {
		final String encKey;
		final int port;
		final String sysName;

		public WatcherVo(String enc, int port, String sysName) {
			this.encKey = enc;
			this.port = port;
			this.sysName = sysName;
		}
	}

	public static final File sysWatcherConfigFile = new File(
			"./config/sysWatcher.conf");

	public static WatcherVo getWatcherConfig() throws IOException {
		return getWatcherConfig(sysWatcherConfigFile);
	}

	public static WatcherVo getWatcherConfig(File confFile) throws IOException {
		ConfigReader crx = new ConfigReader(confFile, "watcher");
		int port = crx.getInt("port", -1);
		if (port < 0) {
			throw new IOException("port config fail! in file "
					+ sysWatcherConfigFile);
		}
		String enc = crx.getValue("encKey");
		String sysName = crx.getValue("sysName");
		return new WatcherVo(enc, port, sysName);
	}

	private Map<String, WatcherAction> actions = Collections
			.synchronizedMap(new HashMap<String, WatcherAction>());

	public static final String OK = "OK";
	public static final int MAX_BLOCK_SIZE_IN_BYTES = 1400;
	protected TCPServer server;
	protected Integer connected = 0;
	protected Boolean hasStarted = false;
	protected final BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();
	protected final ThreadPoolExecutor pool;
	protected final long expireTime;
	private final boolean isCheckVer = false;
	public final WatcherVo conf;

	private SystemWatcher() throws IOException {
		this(getWatcherConfig());
	}

	private SystemWatcher(WatcherVo vo) throws IOException {
		this.conf = vo;
		BmtLogger.log.warn("start watcher using port %s", vo.port);
		pool = new ThreadPoolExecutor(10, 100, 100, TimeUnit.SECONDS, queue) {
			@Override
			protected void afterExecute(Runnable r, Throwable t) {
				ActionDealor run = (ActionDealor) r;
				try {
					run.clientSocket.close();
				} catch (IOException e) {
					BmtLogger.instance().log(e, "when close socket");
				}
			}

			@Override
			protected void beforeExecute(Thread t, Runnable r) {
			}
		};
		if (isCheckVer) {
			File pFile = new File("config/profile/" + vo.port + ".pf");
			if (!pFile.exists()) {
				throw new IOException(pFile + " not exist!");
			}

			byte[] pf;
			String tmp = FileGet.getStr(pFile);
			pf = Misc.strToBytes(tmp);
			pf = BmAes.decrypt(vo.encKey.getBytes(), pf);
			String str = new String(pf);
			int starPos = str.indexOf("*");
			if (starPos == -1) {
				throw new IOException("Not good run.pf format");
			}
			String toDay = str.substring(starPos + 1);
			str = str.substring(0, starPos);
			String user_dir = System.getProperty("user.dir");
			File udir = new File(user_dir);
			File encDir = new File(str);
			if (str.trim().length() == 0 || !encDir.equals(udir)) {
				throw new IOException(
						"Not match user dir, this may disorder the system");
			}
			try {
				expireTime = Long.parseLong(toDay);
			} catch (Exception e) {
				throw new IOException("unkown expire day");
			}
			if (expireTime < System.currentTimeMillis()) {
				throw new IOException("has expired! please update BMFrame:"
						+ new java.util.Date(expireTime));
			}
		} else {
			expireTime = Long.MAX_VALUE;
		}
		server = new TCPServer(vo.port) {
			@Override
			public void doYourJob(final Socket clientSocket) {
				ActionDealor ad = new ActionDealor(clientSocket);
				pool.execute(ad);
			}
		};
		this.regAction(new ShutDownAction(vo.encKey));
		this.regAction(new SysInfoAction(vo.encKey, vo.sysName));
		this.regAction(new GCAction(vo.encKey));
		this.regAction(new EchoAction(vo.encKey));
		this.regAction(new SysPropAction(vo.encKey));
		this.regAction(new WatcherAction(WatcherAction.HELP, vo.encKey) {
			@Override
			public void run(String[] paras, Socket clientSocket)
					throws Exception {
				String reply = actions().toString();
				this.writeBack(reply, clientSocket);
			}
		});

		this.regAction(new WatcherAction("axp", vo.encKey) {
			@Override
			public void run(String[] paras, Socket clientSocket)
					throws Exception {
				long exp = expireTime - System.currentTimeMillis();
				exp = exp / 24 / 60 / 60 / 100;
				double leftDay = exp / 10.0;
				if (leftDay < 30) {
					this.writeBack("Urgent!", clientSocket);
				}
				this.writeBack(leftDay + "", clientSocket);
			}
		});
	}

	public Set<String> actions() {
		return this.actions.keySet();
	}

	public void regAction(final Watchable watcher) {
		regAction(new WatcherAction(watcher.getCmd(), conf.encKey) {

			@Override
			public void run(String[] paras, Socket clientSocket)
					throws Exception {
				String reply = watcher.run(paras);
				writeBack(reply, clientSocket);
			}

		});
	}

	public void regAction(WatcherAction act) {
		if (this.actions.containsKey(act.cmd.toLowerCase())) {
			BmtLogger.log.warn("%s already registed, skip", act.cmd);
			return;
		}
		this.actions.put(act.cmd.toLowerCase(), act);
	}

	public ShutDownAction getShutDownAction() {
		return (ShutDownAction) this.actions.get(WatcherAction.STOP
				.toLowerCase());
	}

	public synchronized void start() {
		if (this.hasStarted) {
			BmtLogger.log.warn("has already started! skip! listen at %s",
					this.server.getPort());
		} else {
			BmtLogger.log.info("try starting.... listen at %s",
					this.server.getPort());
			server.start();
		}
	}

	/**
	 * get a new StopHook and reg it to StopAction.<br>
	 * 
	 * @param maxWait
	 *            0 or less means wait exist signal
	 * @return
	 */
	public StopHook regStopHook(long maxWait) {
		StopHook sh = new StopHook(maxWait);
		this.getShutDownAction().addStopHook(sh);
		return sh;
	}

	public StopHook regStopHook() {
		StopHook sh = new StopHook(0);
		this.getShutDownAction().addStopHook(sh);
		return sh;
	}

	/**
	 * block this thread, never exist! may be used when need block
	 */
	public void block() {
		BmtLogger.log.warn("WatherLogName blocking start");
		while (true) {
			try {
				Thread.sleep(60 * 60 * 1000);
			} catch (InterruptedException e) {
				BmtLogger.instance().log(e, "sleep error");
			}
		}
	}

	/**
	 * reg and start the watcher
	 * 
	 * @param port
	 * @param key
	 * @return
	 * @throws IOException
	 */
	public static SystemWatcher regWatcher() throws IOException {
		SystemWatcher stp = new SystemWatcher();
		stp.start();
		return stp;
	}

	class ActionDealor implements Runnable {
		final Socket clientSocket;

		ActionDealor(Socket clientSocket) {
			this.clientSocket = clientSocket;
		}

		@Override
		public void run() {
			InetSocketAddress address = null;
			try {
				address = (InetSocketAddress) clientSocket
						.getRemoteSocketAddress();
				BmtLogger.log.warn("linked in socket, dst address = %s:%d",
						address.getAddress().getHostAddress(),
						address.getPort());
				clientSocket.setSoTimeout(1000);
				InputStream is = clientSocket.getInputStream();
				byte[] bs = new byte[MAX_BLOCK_SIZE_IN_BYTES];
				int read = is.read(bs);
				if (read == -1) {
					BmtLogger.log.warn("read bytes %d", read);
					return;
				}
				String cmd = BmAes.decrypt(conf.encKey, bs, 0, read);
				final String cmds[] = cmd.split("\n");
				final WatcherAction act = actions.get(cmds[0].toLowerCase());
				if ("true".equals(System.getProperty("logcmd"))) {
					BmtLogger.log.warn("dst address = %s:%d, cmd='%s'", address
							.getAddress().getHostAddress(), address.getPort(),
							cmd.replace('\n', '^'));
				}
				if (act != null) {
					act.run(cmds, clientSocket);

				} else {
					BmtLogger.log.warn(
							"Unkonwn command got '%s', dst address = %s:%d",
							cmd, address.getAddress().getHostAddress(),
							address.getPort());
					WatcherAction wa = new WatcherAction(WatcherAction.Echo,
							conf.encKey) {

						@Override
						public void run(String[] paras, Socket clientSocket)
								throws IOException {
							this.writeBack("bad cmd " + paras[0], clientSocket);
						}
					};
					wa.run(cmds, clientSocket);
				}
			} catch (Exception e) {
				BmtLogger.log.warn(e, "got error when del socket", this);
			} finally {
				if (null != clientSocket) {
					try {
						if (!clientSocket.isClosed()) {
							clientSocket.close();
						}
					} catch (Exception e) {
						BmtLogger.log.warn(e, "got error when close socket");
					}
				}
			}
		}
	}
}