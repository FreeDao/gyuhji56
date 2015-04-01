package com.bmtech.spider.core.scan;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.bmtech.spider.core.ScanConfig;
import com.bmtech.utils.http.CrawlContext.CrawlOut;

public class HostScanCrawlOut extends CrawlOut {

	public String name;

	public HostScanCrawlOut(HostScanOutputStream stream) {
		super(stream);
	}

	public long size() {
		return ((HostScanOutputStream) super.out).size;
	}

	public long sizeInKB() {
		return size() / 1024;
	}

	public boolean tooBig() {
		return size() > ScanConfig.instance.maxFileLen;
	}

	public byte[] getBytes() {
		return ((HostScanOutputStream) super.out).toByteArray();
	}

	public void clear() {
	}

	public InputStream getInputStream() {
		return new ByteArrayInputStream(this.getBytes());
	}

	public void setName(String name) {
		this.name = name;

	}
}