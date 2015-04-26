package com.bmtech.utils.systemWatcher;

public class WatcherVo {
	private final String encKey;
	private final int port;
	private final String sysName;

	public WatcherVo(String enc, int port, String sysName) {
		this.encKey = enc;
		this.port = port;
		this.sysName = sysName;
	}

	public String getSysName() {
		return sysName;
	}

	public String getEncKey() {
		return encKey;
	}

	public int getPort() {
		return port;
	}
}