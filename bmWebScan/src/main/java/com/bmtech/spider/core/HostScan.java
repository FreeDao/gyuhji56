package com.bmtech.spider.core;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.bmtech.htmls.parser.Parser;
import com.bmtech.htmls.parser.util.NodeList;
import com.bmtech.spider.core.scan.HostScanContext;
import com.bmtech.spider.core.scan.HostScanCrawlOut;
import com.bmtech.spider.core.scan.HostScanOutputStream;
import com.bmtech.spider.core.scan.HostScanSiteOut;
import com.bmtech.spider.core.scan.HostScanUrlIn;
import com.bmtech.spider.core.scan.HostScanUrlOut;
import com.bmtech.utils.Charsets;
import com.bmtech.utils.Misc;
import com.bmtech.utils.http.CrawlContext;
import com.bmtech.utils.http.HttpHandler;
import com.bmtech.utils.http.RURL;
import com.bmtech.utils.log.BmtLogHelper;

public class HostScan implements Runnable {

	@Override
	public String toString() {
		return "host scan " + hostInfo;
	}

	private HostScanUrlIn urlIn;
	private BmtLogHelper log;
	private ScanConfig conf;
	private HostScanUrlOut urlOut;
	private final HostScanSiteOut siteOut;
	private boolean isInit = false;
	private final HostScanContext hostContext = new HostScanContext();

	int roundCrawl = 0, roundCrawOKlNum = 0;
	private final HostInfo hostInfo;

	public HostScan(HostInfo hostInfo) throws IOException {
		this.hostInfo = hostInfo;
		siteOut = new HostScanSiteOut(hostInfo);
	}

	private void init() {
		if (this.isInit) {
			return;
		}
		this.isInit = true;
		try {
			final String hostName = hostInfo.getHostName();
			this.conf = ScanConfig.instance;
			log = new BmtLogHelper(hostName);

			if (siteOut.savedFileCount() >= ScanConfig.instance.maxPagePerHost) {
				this.hostContext.setEnd(true);
				return;
			}
			urlOut = new HostScanUrlOut(hostInfo);

			log.info("start crawl:%s", hostName);
			if (!Connectioner.instance().checkHostValid(hostName)) {
				log.warn("host valid check false!");
				hostContext.setEnd(true);
			}
			urlIn = new HostScanUrlIn(hostInfo);

		} catch (Throwable e) {
			this.hostContext.setEnd(true);
		}
	}

	public void close() {
		saveCount();
		refreshHostStatus();
		if (roundCrawOKlNum < 5 && siteOut.getOkCount() > 10) {
			setHostStatusClose();// FIXME should check round numbers, if round >
									// x
									// and number < y then close
			log.fatal("find too few pages, round get %s,  has already %s",
					roundCrawOKlNum, siteOut.getOkCount());
		}
		if (urlOut != null) {
			urlOut.close();
		}
		this.siteOut.close();
		if (urlIn != null) {
			urlIn.close();
		}
	}

	private long lastRun = 0;

	@Override
	public void run() {

		if (!this.isInit) {
			this.init();
		}
		if (this.roundCrawl > conf.maxCrawlPagesPerRound) {
			this.hostContext.setEnd(true);
			return;
		}

		if (hostContext.isEnd()) {
			return;
		}

		if (hostContext.isPreCrawl()) {

			try {
				preCrawl();
			} catch (Exception e) {
				log.fatal(e, "when pre-crawl %s", hostContext.getCurrentUrl());
				hostContext.setEnd(true);
				return;
			}
		} else {
			try {
				afterCrawl();
			} catch (Throwable e) {
				log.fatal(e, "when after crawl for %s",
						hostContext.getCurrentUrl());
				hostContext.setEnd(true);
			} finally {
				hostContext.setPreStage(true);
			}
		}
	}

	protected void preCrawl() throws Exception {

		long now = System.currentTimeMillis();
		long passed = now - lastRun;
		long left = conf.urlCrawlItvSecond - passed;
		if (left > 0) {
			Misc.sleep(10);
			return;
		}
		lastRun = now;
		hostContext.setCurrentUrl(urlIn.nextUrl());
		if (hostContext.getCurrentUrl() == null) {
			log.warn("ERROR: no more URL");
			hostContext.setEnd(true);
			return;
		} else {
			log.info("crawling url %s", hostContext.getCurrentUrl());
		}
		roundCrawl++;

		log.info("crawl next url %s", hostContext.getCurrentUrl());

		HttpHandler hdl = HttpHandler.getCrawlHandler();
		hdl.setConnectTimeout(conf.crawlConnectTimeout);
		hdl.setReadTimeout(conf.crawlReadTimeout);

		HostScanCrawlOut out = new HostScanCrawlOut(new HostScanOutputStream());

		hostContext.setCrawlContext(new CrawlContext(hostContext
				.getCurrentUrl().getUrl(), out, hdl, log,
				ScanConfig.instance.allowDownload));
		hostContext.getCrawlContext().run();
		hostContext.setPreStage(false);

	}

	protected void afterCrawl() throws Exception {
		HostScanCrawlOut out = (HostScanCrawlOut) hostContext.getCrawlContext()
				.getCrawlOut();

		boolean crawled = true;
		try {
			if (!hostContext.getCrawlContext().isOkRun()) {
				if (hostContext.getCurrentUrl().getErrorCount() < conf.maxErrorForEveryPage) {
					hostContext.getCurrentUrl().increaseFailTime();
					hostContext.getCurrentUrl().setRefered(0);
					urlOut.hasFailedFlush(hostContext.getCurrentUrl());
					crawled = false;
				}
				return;
			} else if (hostContext.getCrawlContext().isDownload()) {
				log.warn(" downloadType, url=%s", hostContext.getCurrentUrl()
						.getUrl());
				if (conf.allowDownload) {
					siteOut.saveOkUrlDir(out, hostContext.getCurrentUrl());
				}
				return;
			} else if (out.tooBig()) {
				log.warn("skip too big file %sKB, url=%s", out.sizeInKB(),
						hostContext.getCurrentUrl().getUrl());
				return;
			}

			byte[] bs = out.getBytes();
			Charset cs = Charsets.getCharset(bs, true);
			String html = new String(bs, cs);

			Parser p = new Parser(html);
			NodeList htmlNodeList = p.parse(null);
			ArrayList<LinkClass> links = CoreUtil.lnkExtract(htmlNodeList);

			HtmlScore scorer = conf.scorerCls.newInstance();

			ArrayList<ScoredUrlRecord> surlNews = new ArrayList<ScoredUrlRecord>();
			HashMap<String, ScoredUrlRecord> urlMap = new HashMap<String, ScoredUrlRecord>();
			ForeignUrlChecker fhc = null;
			try {
				fhc = conf.foreignUrlCheckClass.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
			for (LinkClass link : links) {
				try {
					URL newURL = new URL(hostContext.getCurrentUrl().getUrl(),
							link.getLink());
					String iurl = RURL.rewrite(newURL.toString(), cs);
					newURL = new URL(iurl);
					if (hostInfo.isMyUrl(newURL)) {
						int score = scorer.urlScore(link.text, link.title,
								newURL.toString());
						ScoredUrlRecord old = urlMap.get(newURL.toString());
						ScoredUrlRecord sul = new ScoredUrlRecord(newURL, score);
						if (old == null) {
							surlNews.add(sul);
							urlMap.put(newURL.toString(), sul);
						} else {
							old.setScore(Math.max(old.getScore(), score));
						}
					} else {
						if (fhc != null) {
							try {
								fhc.collect(newURL);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				} catch (MalformedURLException e) {
					log.info("error url fmt %s", e);
				} catch (Exception e) {
					e.printStackTrace();
					log.fatal(e, "when parse url's content %s",
							hostContext.getCurrentUrl());
				}
			}
			Collections.sort(surlNews, ScoredUrlRecord.urlHashCmp);

			try {
				for (ScoredUrlRecord sunew : surlNews) {
					urlOut.newUrlOutOffer(sunew);
				}
			} finally {
				urlOut.newUrlOutFlush();
			}

			int listScore = scorer.listScore(htmlNodeList, hostContext
					.getCurrentUrl().getUrl());
			int pageScore = scorer.pageScore(htmlNodeList, hostContext
					.getCurrentUrl().getUrl());

			if (scorer.isGoodScore(pageScore, listScore)) {
				siteOut.saveOkUrlDir(out, hostContext.getCurrentUrl());
			}
		} catch (Exception e) {
			log.error(e, "when after crawl %s", e);
		} finally {
			if (!conf.allowSaveTmpFile) {
				out.clear();
			}
			if (crawled) {
				roundCrawOKlNum++;
				int okCrawlNum = this.hostInfo.inOkCrawled(1);
				boolean endCrawl = okCrawlNum >= ScanConfig.instance.maxPagePerHost;
				this.refreshHostStatus();
				int okCount = siteOut.getOkCount();
				if ((okCount > 0 && okCount % 10 == 0) || endCrawl) {
					saveCount();
				}

				urlOut.hasCrawledFlush(hostContext.getCurrentUrl());
			}

		}
	}

	private void saveCount() {
		try {
			int count = siteOut.getOkCount();
			Connectioner.instance()
					.setHasCrawled(hostInfo.getHostName(), count);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	private void refreshHostStatus() {
		try {
			int okCrawlNum = siteOut.getOkCount();
			if (okCrawlNum >= ScanConfig.instance.maxPagePerHost) {
				setHostStatusClose();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setHostStatusClose() {
		Connectioner.instance().setHostEndStatus(hostInfo.getHostName(),
				HostFilter.Status_Number_Match);
	}

	public boolean isEnd() {
		return this.hostContext.isEnd();
	}

	public boolean isPreCrawl() {
		return this.hostContext.isPreCrawl();
	}

	public HostInfo getHostInfo() {
		return hostInfo;
	}
}
