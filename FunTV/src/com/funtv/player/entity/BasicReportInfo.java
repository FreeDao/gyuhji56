package com.funtv.player.entity;

public class BasicReportInfo {
	//  设备类型(dev)：<aphone/apad/iphone/ipad>_<操作系统>_<设备型号>
	//  设备mac地址(mac)：长度为16的大写字符串（待确认）
	//  app版本号(ver)：类ip地址的字符串
	//  网络类型(nt)：1—wifi，2—移动网络，3—其它，-1—无网络

	private String deviceType;

	private String deviceMac;

	private String appVersion;

	private String netType;

	private int stype; // 任务类型(stype): 1—cdn，2—p2p

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getDeviceMac() {
		return deviceMac;
	}

	public void setDeviceMac(String deviceMac) {
		this.deviceMac = deviceMac;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getNetType() {
		return netType;
	}

	public void setNetType(String netType) {
		this.netType = netType;
	}

	public int getStype() {
		return stype;
	}

	public void setStype(int stype) {
		this.stype = stype;
	}
}
