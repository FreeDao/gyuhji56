package com.bmtech.spider.core.scan;

import java.io.File;
import java.io.IOException;

import com.bmtech.spider.core.HostInfo;
import com.bmtech.spider.core.ScanConfig;
import com.bmtech.spider.core.ScoredUrlRecord;
import com.bmtech.spider.core.util.SynCombin;
import com.bmtech.utils.bmfs.MDir;
import com.bmtech.utils.io.InputStreamCombin;
import com.bmtech.utils.log.LogHelper;

public class HostScanSiteOut {
	private final MDir mdir;
	LogHelper log;

	public HostScanSiteOut(HostInfo hostInfo) throws IOException {
		File f = ScanConfig.instance.getSaveDir(hostInfo);
		mdir = MDir.makeMDir(f, true);
		log = new LogHelper("outOf-" + hostInfo.getHostName());
	}

	public void close() {
		MDir.closeMDir(mdir);
	}

	public void saveHtmlPage(HostScanCrawlOut out, ScoredUrlRecord currentUrl)
			throws IOException {

		InputStreamCombin cmbIpt = SynCombin.getCombin(currentUrl.getUrl(),
				out.getInputStream());
		// FIXME shouldWithSUffix out.getSuffix();
		mdir.addFile(cmbIpt, ScanConfig.instance.useMFileGzip);
	}

}
