package com.bmtech.spider.core.scan;

import java.io.File;

import com.bmtech.spider.core.HostInfo;
import com.bmtech.spider.core.ScanConfig;
import com.bmtech.spider.core.ScoredUrlRecord;
import com.bmtech.utils.io.diskMerge.RecordReader;

public class HostScanUrlIn {
	private File ReadyToCrawl;
	private File tmpHtmlsDir;
	private RecordReader urlReader;

	public HostScanUrlIn(HostInfo info) throws Exception {
		this.ReadyToCrawl = new File(ScanConfig.instance.getHostBase(info),
				ScanConfig.instance.readyToCrawl);
		this.setTmpHtmlsDir(ScanConfig.instance.tmpCrawlDir);
		if (!getTmpHtmlsDir().exists()) {
			getTmpHtmlsDir().mkdirs();
		}
		urlReader = new RecordReader(ReadyToCrawl, ScoredUrlRecord.class);
	}

	public void close() {
		urlReader.close();
	}

	public File getTmpHtmlsDir() {
		return tmpHtmlsDir;
	}

	public void setTmpHtmlsDir(File tmpHtmlsDir) {
		this.tmpHtmlsDir = tmpHtmlsDir;
	}

	public ScoredUrlRecord nextUrl() throws Exception {
		return (ScoredUrlRecord) urlReader.take();
	}
}
