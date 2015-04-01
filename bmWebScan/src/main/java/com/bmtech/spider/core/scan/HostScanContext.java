package com.bmtech.spider.core.scan;

import com.bmtech.spider.core.ScoredUrlRecord;
import com.bmtech.utils.http.CrawlContext;

public class HostScanContext {
	private boolean isEnd = false;

	private boolean preCrawl = true;
	private ScoredUrlRecord currentUrl;
	private CrawlContext crawlContext;
	private long fileSeq;

	public void setPreStage(boolean isPre) {
		if (isPre) {
			setPreCrawl(true);
			setCurrentUrl(null);
			setCrawlContext(null);
			setFileSeq(0);
		} else {
			this.setPreCrawl(false);
		}
	}

	public boolean isEnd() {
		return isEnd;
	}

	public void setEnd(boolean isEnd) {
		this.isEnd = isEnd;
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

	public long getFileSeq() {
		return fileSeq;
	}

	public void setFileSeq(long fileSeq) {
		this.fileSeq = fileSeq;
	}

	public boolean isPreCrawl() {
		return preCrawl;
	}

	public void setPreCrawl(boolean preCrawl) {
		this.preCrawl = preCrawl;
	}
}