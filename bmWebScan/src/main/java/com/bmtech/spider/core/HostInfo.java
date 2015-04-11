package com.bmtech.spider.core;

import java.net.URL;

public class HostInfo {

	private int totalCrawled = 0;
	private final String hostName;
	private final String myHostSuffix;

	public HostInfo(String host, int totalCrawled) {
		this.hostName = host.toLowerCase().trim();
		this.setHostTotalCrawled(totalCrawled);
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

	public int getTotalCrawled() {
		return totalCrawled;
	}

	public void setHostTotalCrawled(int totalCrawled) {
		this.totalCrawled = totalCrawled;
	}

	@Override
	public String toString() {
		return "HostInfo [totalCrawled=" + totalCrawled + ", hostName="
				+ hostName + "]";
	}

	public int incTotalCrawled() {
		this.totalCrawled++;
		return this.totalCrawled;
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
