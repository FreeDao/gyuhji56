package com.bmtech.spider.core;

public abstract class HostFilter {
	public static final int Status_OK = 0;
	public static final int Status_FBD = -1;
	public static final int Status_Number_Match = -9;

	/**
	 * should override this implements
	 * 
	 * @param host
	 * @return
	 */
	protected abstract boolean isForbidden(String host);

	protected void forbidden(String host) {
		Connectioner.instance().setHostEndStatus(host, Status_FBD);
	}

	public boolean checkAndForbidden(String host) {
		boolean ret = this.isForbidden(host);
		if (ret) {
			forbidden(host);
		}
		return ret;
	}
}
