package com.bmtech.spider.core.scan;

import com.bmtech.spider.core.ScoredUrlRecord;
import com.bmtech.utils.http.CrawlContext;

public class HostScanContext {
	private boolean isEnd = false;

	private boolean preCrawl = true;
	private ScoredUrlRecord currentUrl;
	private CrawlContext crawlContext;

	public void setPreStage(boolean isPre) {
		if (isPre) {
			setPreCrawl(true);
			setCurrentUrl(null);
			setCrawlContext(null);
		} else {
			this.setPreCrawl(false);
		}
	}

	public boolean isEnd() {
		return isEnd;
	}

	public void setEnd() {
		this.isEnd = true;
	}

	public CrawlContext getCrawlContext() {
		return crawlContext;
	}

	public void setCrawlContext(CrawlContext crawlContext) {
		this.crawlContext = crawlContext;
	}

	public ScoredUrlRecord getCurrentUrl() {
		return currentUrl;
	}

	public void setCurrentUrl(ScoredUrlRecord currentUrl) {
		this.currentUrl = currentUrl;
	}

	public boolean isPreCrawl() {
		return preCrawl;
	}

	public void setPreCrawl(boolean preCrawl) {
		this.preCrawl = preCrawl;
	}
}