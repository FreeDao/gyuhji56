package com.bmtech.spider.core.scan;

import com.bmtech.spider.core.ScoredUrlRecord;
import com.bmtech.utils.http.CrawlContext;

public class HostScanContext {
	private boolean isEnd = false;

	private boolean preCrawl = true;
	private ScoredUrlRecord currentUrl;
	private CrawlContext crawlContext;

	public void setStage(boolean isPre) {
		if (isPre) {
			this.preCrawl = true;
			setCurrentUrl(null);
			setCrawlContext(null);
		} else {
			this.preCrawl = false;
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

}