package com.bmtech.spider.core;

import com.bmtech.spider.core.HostFilter;

public class HostFilterAllAccept extends HostFilter{
	public HostFilterAllAccept(){

	}
	@Override
	public boolean isForbidden(String host) {
		return false;
	}



}
