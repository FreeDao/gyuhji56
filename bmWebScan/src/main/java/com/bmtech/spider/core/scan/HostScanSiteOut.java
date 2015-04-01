package com.bmtech.spider.core.scan;

import java.io.File;
import java.io.IOException;

import com.bmtech.spider.core.HostInfo;
import com.bmtech.spider.core.ScanConfig;
import com.bmtech.spider.core.ScoredUrlRecord;
import com.bmtech.utils.bmfs.MDir;
import com.bmtech.utils.log.LogHelper;

public class HostScanSiteOut {
	private final MDir mdir;
	private final HostInfo hostInfo;
	LogHelper log;

	public HostScanSiteOut(HostInfo hostInfo) throws IOException {
		this.hostInfo = hostInfo;
		File f = ScanConfig.instance.getSaveDir(hostInfo.getMyHostSuffix());
		mdir = MDir.makeMDir(f, true);
		log = new LogHelper("outOf-" + hostInfo.getClass());
	}

	public int savedFileCount() {
		return mdir.size();
	}

	public void close() {
		MDir.closeMDir(mdir);
	}

	public int getOkCount() {
		int count = hostInfo.getOkCrawled();
		if (this.mdir != null) {
			int xcount = mdir.size();
			if (xcount != count) {
				log.warn("mismatch! host size size %s != dir size %s", count,
						xcount);
			}
			count = xcount;
		}
		return count;
	}

	public void saveOkUrlDir(HostScanCrawlOut out, long fileSeq,
			ScoredUrlRecord currentUrl) throws IOException {
		ScanConfig.instance.saveOkUrlDir(out, fileSeq, currentUrl, mdir);
	}
}
