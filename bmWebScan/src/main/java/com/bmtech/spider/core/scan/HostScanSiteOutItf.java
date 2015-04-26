package com.bmtech.spider.core.scan;

import java.io.IOException;

import com.bmtech.spider.core.HostInfo;
import com.bmtech.spider.core.ScanConfig;
import com.bmtech.spider.core.ScoredUrlRecord;
import com.bmtech.spider.core.util.SynCombin;
import com.bmtech.utils.bmfs.MDir;
import com.bmtech.utils.io.InputStreamCombin;
import com.bmtech.utils.log.LogHelper;

public abstract class HostScanSiteOutItf {
	protected final MDir mdir;
	LogHelper log;

	public HostScanSiteOutItf(HostInfo hostInfo) throws IOException {
		mdir = getMDir(hostInfo);
		log = new LogHelper("outOf-" + hostInfo.getHostName());
	}

	protected abstract MDir getMDir(HostInfo hostInfo) throws IOException;

	public abstract void close();

	public void saveHtmlPage(HostScanCrawlOut out, ScoredUrlRecord currentUrl)
			throws IOException {

		InputStreamCombin cmbIpt = SynCombin.getCombin(currentUrl.getUrl(),
				out.getInputStream());
		mdir.addFile(cmbIpt, ScanConfig.instance.useMFileGzip);
	}
}
