package com.bmtech.spider.core.scan;

import java.io.File;
import java.io.IOException;

import com.bmtech.spider.core.HostInfo;
import com.bmtech.spider.core.ScanConfig;
import com.bmtech.spider.core.ScoredUrlRecord;
import com.bmtech.utils.io.diskMerge.MOut;

public class HostScanUrlOut {
	private MOut hasCrawled, hasFailed, newUrlOut;

	public HostScanUrlOut(HostInfo info) throws IOException {
		File baseDir = ScanConfig.instance.getHostBase(info.getHostName());
		ScanConfig conf = ScanConfig.instance;
		File hasCrawledOutBuffer = new File(baseDir, conf.crawledUrlTmp);
		hasCrawled = new MOut(hasCrawledOutBuffer);

		File newUrlDir = new File(baseDir, conf.newUrlDir);
		if (!newUrlDir.exists()) {
			newUrlDir.mkdir();
		}
		newUrlOut = new MOut(new File(newUrlDir, "newURL.txt"));
		hasFailed = new MOut(new File(baseDir, conf.hasFailedFileName));
	}

	public void close() {
		if (hasCrawled != null) {
			this.hasCrawled.close();
		}
		if (this.hasFailed != null) {
			this.hasFailed.close();
		}
		if (this.newUrlOut != null) {
			this.newUrlOut.close();
		}
	}

	public void hasFailedFlush(ScoredUrlRecord currentUrl) throws IOException {
		hasFailed.flush(currentUrl);
	}

	public void hasCrawledFlush(ScoredUrlRecord currentUrl) throws IOException {
		hasCrawled.flush(currentUrl);
	}

	public void newUrlOutFlush() throws IOException {
		newUrlOut.flush();

	}

	public void newUrlOutOffer(ScoredUrlRecord sunew) throws IOException {
		newUrlOut.offer(sunew);
	}
}
