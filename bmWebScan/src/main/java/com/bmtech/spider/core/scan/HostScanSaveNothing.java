package com.bmtech.spider.core.scan;

import java.io.IOException;

import com.bmtech.spider.core.HostInfo;
import com.bmtech.spider.core.ScoredUrlRecord;
import com.bmtech.utils.bmfs.MDir;

public class HostScanSaveNothing extends HostScanSiteOutItf {

	public HostScanSaveNothing(HostInfo hostInfo) throws IOException {
		super(hostInfo);
	}

	@Override
	public void close() {
	}

	@Override
	protected MDir getMDir(HostInfo hostInfo) throws IOException {
		return null;
	}

	@Override
	public void saveHtmlPage(HostScanCrawlOut out, ScoredUrlRecord currentUrl)
			throws IOException {

	}

}
