package com.funtv.player.entity;

public class BootStartReportInfo extends BasicReportInfo {

	//  启动方式（btype）:
		// 0—其它启动；1—手动启动；2—ios平台：推送启动，android平台：调用播放器播放本地文件；3—ios平台：有角标启动上报（其它启动不包括有角标启动上报），android平台：推送通知栏启动；4—android平台：后台下载进入下载管理界面；5—android平台：百度调起app；6—android平台：按home键应用进入后台后再次回到前台；7—android平台：推送桌面弹窗启动；8—android平台：本地通知启动；9–android平台：通过引入第三方push
		// sdk创建的通知栏启动；10--android平台：通过引入第三方push
		// sdk创建的桌面弹窗启动；11—android平台：二维码扫描调起app；12—android平台：媒体桌面快捷键启动；13—android平台：豌豆荚调起app；14—android平台：360手机助手调起app；15—android平台：网页调起app
	//  启动耗时（btime）：从点击到主框架加载完毕耗时，单位：ms；不走启动流程default =-1
	//  启动是否成功（ok）：1—成功，-1—失败
	//  屏幕分辨率（sr）：屏幕分辨率，N*M
	//  设备内存空间（mem）：单位MB
	//  设备存储空间（tdisk）：单位MB
	//  设备剩余空间(fdisk)：单位MB
	//  渠道ID(sid)：区分各个渠道商
	//  启动时间戳（rt）：unix时间戳
	//  用户ip（ip）：用户ip
	//  是否越狱（broken）：0-非越狱，非0-越狱（为渠道号），android-可为空
	//  设备IMEI（imei）：设备IMEI号 (aphone，apad)
	//  安装时间戳（installt）：unix时间戳 (aphone)
	//  用户ID(fudid)：长度为64位的数字与字母混排字符串，唯一标识用户
	//  消息ID(messageid)：推送信息管理系统中的消息id（可以为空）
	//  app类型（apptype）：用于标示app的类型
	//  协议版本（rprotocol）：从1开始，有变更时递增

	public final static String BOOT_OK = "1";
	public final static String BOOT_NOT_OK = "0";
	public final static String BOOT_TYPE_MANUAL_START = "1";

	// 共十一个参数
	private String bootType;

	private long boottime;

	private String bootOk;

	private String screenResolution;

	private double memorySize;

	private double totalDisk;

	private String channelId;

	private double freeDisk;

	private long bootTimeStamp;

	private String userIP;

	private String broken;

	private String deviceIMEI;

	private long installTimeStamp;

	private String fudid;

	private String messageId;

	private String appType;

	private String protocolVersion;

	public String getBootType() {
		return bootType;
	}

	public void setBootType(String bootType) {
		this.bootType = bootType;
	}

	public long getBoottime() {
		return boottime;
	}

	public void setBoottime(long boottime) {
		this.boottime = boottime;
	}

	public String getBootOk() {
		return bootOk;
	}

	public void setBootOk(String bootOk) {
		this.bootOk = bootOk;
	}

	public String getScreenResolution() {
		return screenResolution;
	}

	public void setScreenResolution(String screenResolution) {
		this.screenResolution = screenResolution;
	}

	public long getBootTimeStamp() {
		return bootTimeStamp;
	}

	public void setBootTimeStamp(long bootTimeStamp) {
		this.bootTimeStamp = bootTimeStamp;
	}

	public String getBroken() {
		return broken;
	}

	public void setBroken(String broken) {
		this.broken = broken;
	}

	public long getInstallTimeStamp() {
		return installTimeStamp;
	}

	public void setInstallTimeStamp(long installTimeStamp) {
		this.installTimeStamp = installTimeStamp;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getProtocolVersion() {
		return protocolVersion;
	}

	public void setProtocolVersion(String protocolVersion) {
		this.protocolVersion = protocolVersion;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getUserIP() {
		return userIP;
	}

	public void setUserIP(String userIP) {
		this.userIP = userIP;
	}

	public String getDeviceIMEI() {
		return deviceIMEI;
	}

	public void setDeviceIMEI(String deviceIMEI) {
		this.deviceIMEI = deviceIMEI;
	}

	public String getFudid() {
		return fudid;
	}

	public void setFudid(String fudid) {
		this.fudid = fudid;
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public void setMemorySize(double memorySize) {
		this.memorySize = memorySize;
	}

	public void setTotalDisk(double totalDisk) {
		this.totalDisk = totalDisk;
	}

	public void setFreeDisk(double freeDisk) {
		this.freeDisk = freeDisk;
	}

	public double getMemorySize() {
		return memorySize;
	}

	public double getTotalDisk() {
		return totalDisk;
	}

	public double getFreeDisk() {
		return freeDisk;
	}

	@Override
	public String toString() {
		return "BootStartReportInfo [bootType=" + bootType + ", boottime=" + boottime + ", bootOk=" + bootOk + ", screenResolution="
				+ screenResolution + ", memorySize=" + memorySize + ", totalDisk=" + totalDisk + ", channelId=" + channelId + ", freeDisk="
				+ freeDisk + ", bootTimeStamp=" + bootTimeStamp + ", userIP=" + userIP + ", broken=" + broken + ", deviceIMEI="
				+ deviceIMEI + ", installTimeStamp=" + installTimeStamp + ", fudid=" + fudid + ", messageId=" + messageId + ", appType="
				+ appType + ", protocolVersion=" + protocolVersion + "]";
	}
}
