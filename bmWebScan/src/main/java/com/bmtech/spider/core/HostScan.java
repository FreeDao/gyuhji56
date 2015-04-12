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
import com.bmtech.utils.log.LogHelper;

public class HostScan implements Runnable {

	@Override
	public String toString() {
		return "host scan " + hostInfo;
	}

	private final LogHelper log;
	private final ScanConfig conf;
	private final HostScanContext hostContext = new HostScanContext();
	private final HostInfo hostInfo;

	private HostScanUrlIn urlIn;
	private HostScanUrlOut urlOut;
	private HostScanSiteOut siteOut;
	private boolean isInit = false;

	int roundCrawl = 0, roundCrawOKlNum = 0;
	private long lastRun = 0;

	public HostScan(HostInfo hostInfo) throws IOException {
		this.hostInfo = hostInfo;
		final String hostName = hostInfo.getHostName();
		this.conf = ScanConfig.instance;
		log = new BmtLogHelper(hostName);
	}

	private void init() {
		if (this.isInit) {
			return;
		}
		this.isInit = true;
		try {

			log.info("initing:%s", hostInfo);
			if (!Connectioner.instance().checkHostValid(hostInfo.getHostName())) {
				throw new Exception("host valid check false!");
			}
			urlIn = new HostScanUrlIn(hostInfo);

			siteOut = new HostScanSiteOut(hostInfo);
			if (ScanConfig.instance.isHostTotalPageReached(hostInfo)) {
				throw new Exception("host max page reached");
			}
			urlOut = new HostScanUrlOut(hostInfo);
		} catch (Throwable e) {
			log.fatal(e, "when initing %s", hostInfo);
			this.hostContext.setEnd();
		}
	}

	public void close() {
		saveCount();
		if (ScanConfig.instance.isHostTotalPageReached(hostInfo)) {
			setHostStatusClose();
		}
		if (roundCrawOKlNum < 5 && hostInfo.getTotalCrawled() > 10) {
			setHostStatusClose();
			log.fatal("find too few pages, round get %s,  has already %s",
					roundCrawOKlNum, hostInfo.getTotalCrawled());
		}
		if (urlOut != null) {
			urlOut.close();
		}
		if (this.siteOut != null) {
			this.siteOut.close();
		}

		if (urlIn != null) {
			urlIn.close();
		}
	}

	@Override
	public void run() {

		if (!this.isInit) {
			this.init();
			hostContext.setStage(true);
		}

		if (hostContext.isEnd()
				|| this.roundCrawl >= conf.maxCrawlPagesPerRound
				|| ScanConfig.instance.isHostTotalPageReached(hostInfo)) {
			this.hostContext.setEnd();
			return;
		}
		try {
			if (hostContext.isPreCrawl()) {

				long now = System.currentTimeMillis();
				long passed = (now - lastRun) / 1000;
				long left = conf.urlCrawlItvSecond - passed;
				if (left > 0) {
					Misc.sleep(10);
					return;
				}
				lastRun = now;
				hostContext.setCurrentUrl(urlIn.nextUrl());
				if (hostContext.getCurrentUrl() == null) {
					log.warn("ERROR: no more URL");
					hostContext.setEnd();
					return;
				}
				try {
					runCrawlStage();
				} finally {
					hostContext.setStage(false);
				}
			} else {
				try {
					runAfterCrawl();
				} finally {
					hostContext.setStage(true);
				}
			}
		} catch (Throwable e) {
			log.fatal(e, "crawl for %s", hostContext.getCurrentUrl());
			e.printStackTrace();
			hostContext.setEnd();
		}

	}

	protected void runCrawlStage() throws Exception {

		roundCrawl++;
		log.info("crawling %s", hostContext.getCurrentUrl());

		HttpHandler hdl = HttpHandler.getCrawlHandler();
		hdl.setConnectTimeout(conf.crawlConnectTimeout);
		hdl.setReadTimeout(conf.crawlReadTimeout);

		HostScanCrawlOut out = new HostScanCrawlOut(new HostScanOutputStream());

		hostContext.setCrawlContext(new CrawlContext(hostContext
				.getCurrentUrl().getUrl(), out, hdl, log,
				ScanConfig.instance.allowDownload));
		hostContext.getCrawlContext().run();

	}

	protected void runAfterCrawl() throws Exception {
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
					// FIXME should use saveDownload page
					siteOut.saveHtmlPage(out, hostContext.getCurrentUrl());
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
				siteOut.saveHtmlPage(out, hostContext.getCurrentUrl());
			}
		} catch (Exception e) {
			log.error(e, "when after crawl %s", e);
		} finally {
			if (crawled) {
				roundCrawOKlNum++;
				hostInfo.incTotalCrawled();
				if (roundCrawOKlNum % 10 == 0) {
					saveCount();
				}
				urlOut.hasCrawledFlush(hostContext.getCurrentUrl());
			}
		}
	}

	private void saveCount() {
		try {
			int count = hostInfo.getTotalCrawled();
			Connectioner.instance()
					.setHasCrawled(hostInfo.getHostName(), count);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	private void setHostStatusClose() {
		Connectioner.instance().setHostEndStatus(hostInfo.getHostName());
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
