package com.bmtech.spider.core;

import java.net.URL;

import com.bmtech.utils.io.diskMerge.MRecord;

public class URLRecord extends MRecord {
	URL url;
	int hash;

	@Override
	protected void init(String str) throws Exception {
		url = new URL(str);
		hash = CoreUtil.urlHost(url).hashCode();
	}

	@Override
	public String serialize() {
		return url.toString();
	}

}