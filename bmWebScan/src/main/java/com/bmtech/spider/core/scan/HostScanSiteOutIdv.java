package com.bmtech.spider.core.scan;

import java.io.File;
import java.io.IOException;

import com.bmtech.spider.core.HostInfo;
import com.bmtech.spider.core.ScanConfig;
import com.bmtech.spider.core.ScoredUrlRecord;
import com.bmtech.spider.core.util.SynCombin;
import com.bmtech.utils.bmfs.MDir;
import com.bmtech.utils.io.InputStreamCombin;

public class HostScanSiteOutIdv extends HostScanSiteOutItf {

	public HostScanSiteOutIdv(HostInfo hostInfo) throws IOException {
		super(hostInfo);
	}

	@Override
	public void close() {
		MDir.closeMDir(mdir);
	}

	@Override
	protected MDir getMDir(HostInfo hostInfo) throws IOException {
		File f = ScanConfig.instance.getSaveDir(hostInfo);
		return MDir.makeMDir(f, true);
	}

	@Override
	public void saveHtmlPage(HostScanCrawlOut out, ScoredUrlRecord currentUrl)
			throws IOException {

		InputStreamCombin cmbIpt = SynCombin.getCombin(currentUrl.getUrl(),
				out.getInputStream());
		mdir.addFile(cmbIpt, ScanConfig.instance.useMFileGzip);

	}

}