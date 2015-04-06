package com.bmtech.spider.core;

import java.net.URL;

public class HostInfo {

	private int okCrawled = 0;
	private final String hostName;
	private final String myHostSuffix;

	public HostInfo(String host, int okCrawled) {
		this.hostName = host.toLowerCase().trim();
		this.setOkCrawled(okCrawled);
		if (hostName.startsWith("www.")) {
			myHostSuffix = hostName.substring(4);
		} else {
			myHostSuffix = hostName;
		}
	}

	public HostInfo(String host) {
		this(host, 0);
	}

	public String getHostName() {
		return hostName;
	}

	public int getOkCrawled() {
		return okCrawled;
	}

	public void setOkCrawled(int okCrawled) {
		this.okCrawled = okCrawled;
	}

	@Override
	public String toString() {
		return "HostInfo [okCrawled=" + okCrawled + ", hostName=" + hostName
				+ "]";
	}

	public int inOkCrawled(int i) {
		this.okCrawled++;
		return this.okCrawled;
	}

	public String getMyHostSuffix() {
		return myHostSuffix;
	}

	public boolean isMyUrl(URL url) {

		String prot = url.getProtocol();
		if (!"http".equalsIgnoreCase(prot)) {
			return false;
		}

		return CoreUtil.urlHost(url).endsWith(getMyHostSuffix());
	}
}
