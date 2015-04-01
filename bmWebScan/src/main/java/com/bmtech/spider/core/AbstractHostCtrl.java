package com.bmtech.spider.core;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.bmtech.utils.log.BmtLogHelper;
import com.bmtech.utils.systemWatcher.BmtDamonService;

public abstract class AbstractHostCtrl extends BmtDamonService {
	protected ScanConfig conf = ScanConfig.instance;

	protected final BlockingQueue<Runnable> hostQueue = new LinkedBlockingQueue<Runnable>();
	protected Set<String> hostSet = Collections
			.synchronizedSet(new HashSet<String>());
	protected final ThreadPoolExecutor hostRunnor;
	protected AtomicInteger hasRunned = new AtomicInteger(0),
			requeue = new AtomicInteger(0), putHost = new AtomicInteger(0),
			putBack = new AtomicInteger(0);
	protected AtomicInteger hasFinished = new AtomicInteger(0);
	protected BmtLogHelper log = new BmtLogHelper("hostCtrl");
	protected HostFilter filter;

	public AbstractHostCtrl(int port, String enc, String sysName)
			throws InstantiationException, IllegalAccessException, IOException {
		super(port, enc, sysName);
		filter = conf.hostFilterCls.newInstance();

		hostRunnor = new ThreadPoolExecutor(conf.hostPoolSize,
				conf.hostPoolSize, 100, TimeUnit.SECONDS, hostQueue) {
			void besureParseQueueNotBusy() {
				try {
					while (true) {
						if (ParseThreadPool.getInstance().tooBusy()) {
							Thread.sleep(100);
						} else {
							break;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			protected void afterExecute(Runnable r, Throwable t) {
				if (t != null) {
					log.fatal(t, "after execute %s", r);
				}
				try {
					HostScan sc = (HostScan) r;
					if (sc.isEnd()) {
						try {
							sc.close();
							hasFinished.addAndGet(1);

							log.fatal(
									"finish crawl host %s! has put hosts %s(putback %s, requeue %s), finished %s(task %s), running %s, waiting %s",
									sc.getHostInfo(), putHost.get(),
									putBack.get(), requeue.get(),
									hasFinished.get(), hasRunned.get(),
									hostRunnor.getActiveCount(),
									hostQueue.size());

						} finally {
							removeFromHostSet(sc);
						}
					} else {
						try {
							hasRunned.addAndGet(1);
							besureParseQueueNotBusy();

							if (sc.isPreCrawl()) {
								requeue(sc);
							} else {
								ParseThreadPool.getInstance().submitToParse(sc);
							}
						} catch (Exception e) {
							sc.close();
							removeFromHostSet(sc);
						}

					}
				} catch (Throwable tx) {
					log.fatal(tx, "EEE after run %s", r);

				}
			}
		};

		log.info("watch thread start!");
	}

	public abstract void startHostLoadThread();

	public void requeue(HostScan pot) {
		requeue.addAndGet(1);
		log.info("requeue host %s", pot);
		putToHostSet(pot);
		this.hostRunnor.execute(pot);
	}

	public void execute(HostScan pot) {
		putHost.addAndGet(1);
		log.info("add new host %s", pot);
		putToHostSet(pot);
		this.hostRunnor.execute(pot);
	}

	public void putBack(HostScan pot) {
		putBack.addAndGet(1);
		putToHostSet(pot);
		this.hostRunnor.execute(pot);
	}

	public int hostQueueSize() {
		return this.hostQueue.size();
	}

	private void putToHostSet(HostScan pot) {
		String host = pot.getHostInfo().getHostName();
		hostSet.add(host);
	}

	private void removeFromHostSet(HostScan pot) {
		String host = pot.getHostInfo().getHostName();
		hostSet.remove(host);
	}

	public boolean hostIsRunning(String host) {
		return this.hostSet.contains(new HostInfo(host).getHostName());
	}

	public boolean needFillHostPool() {
		return hostRunnor.getActiveCount() < conf.hostPoolSize
				- conf.hostPoolNeedFillMargin;
	}

	public void initHost(String host) {
		if (this.hostIsRunning(host)) {
			log.error("host '%s' is running, reject to run now", host);
			return;
		}
		if (!needFillHostPool()) {
			return;
		}
		log.warn("initing :%s ", host);
		try {
			HostInitor.instance.initialize(host, filter, new AfterInitor(host));
		} catch (Exception e) {
			log.error(e, "when init %s", host);
			e.printStackTrace();
		}
	}

	class AfterInitor {
		private String host;

		AfterInitor(String host) {
			this.host = host;
		}

		public void afterInitor(HostScan sc) {
			if (sc != null) {
				execute(sc);
			} else {
				log.fatal("init fail for host %s", host);
			}
		}
	}

	public HostFilter getFilter() {
		return filter;
	}

}
