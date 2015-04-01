package com.bmtech.spider.core;

import java.net.URL;

public abstract class ForeignUrlChecker {

	public abstract void collect(URL url)throws Exception;
}
