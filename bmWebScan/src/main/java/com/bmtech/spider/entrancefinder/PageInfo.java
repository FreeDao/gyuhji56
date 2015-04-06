package com.bmtech.spider.entrancefinder;

import java.net.URL;

public class PageInfo {

	@Override
	public String toString() {
		return "PageInfo [refer=" + refer + ", linkOut=" + linkOut + ", url="
				+ getUrl() + "]";
	}

	private int refer = 0;
	private int linkOut = 0;
	private final String url;

	public PageInfo(URL url) {
		this.url = url.toString();
	}

	@Override
	public boolean equals(Object obj) {
		return this.getUrl().equals(((PageInfo) obj).getUrl());
	}

	public int hashcode() {
		return getUrl().hashCode();
	}

	public int getRefer() {
		return refer;
	}

	public void addRefer(int num) {
		this.refer += num;
	}

	public int getLinkOut() {
		return linkOut;
	}

	public void addLinkOut(int num) {
		this.linkOut += num;
	}

	public String getUrl() {
		return url;
	}
}
