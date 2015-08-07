package com.funtv.player.entity;

public class PlayStuckReportInfo extends BasicReportInfo{
//		任务infohash id(ih)：cdn媒体p2p媒体—hashid 40位
//		server地址：缓冲所连接的server ip地址，ios—填“”，andriod—server ip地址
//		本次卡是否缓冲完成(ok)：0—缓冲完成，-1—缓冲失败
//		本次卡位置(stkpos)：媒体文件的播放时间轴上的卡位置，单位：ms
//		本次卡时间(stktm)：缓冲完成表示实卡时间，缓冲失败表示用户的等待时间，单位：ms
//		缓冲期间平均下载速度(drate)：单位KB/s，default： -1
//		渠道ID(sid):区分各个渠道商
//		播放卡原因(stkres)：播放卡原因信息（Android应用使用）
//			1，Default    0
//			2，网络中断或者网络环境差导致播放卡    -1
//			3，	络不稳定时启动重试机制后产生播放卡  -2
//			4，片源码率过高时播放卡  -3
//		播放器类型(ptype)：android系统播放器：0；ffmpeg播放器：1；（android应用使用）
//		上报时间(rt)：unix时间戳
//		用户ip(ip)：用户ip
//		影片清晰度(cl):  1-tv,2-dvd,3-highdvd,4-superdvd
//		用户ID(fudid)：长度为64位的数字与字母混排字符串，唯一标识用户
//		app类型（apptype）：用于标示app的类型
//		协议版本（rprotocol）：从1开始，有变更时递增

	public static final String STUCK_BUFFER_SUCCESS = "0";

	public static final String STUCK_BUFFER_FAIL = "-1";

	private String hashId;

	private String serverIp;

	private String ok;

	private String stkpos;

	private String stktm;

	private String drate;

	private String sid;

	private String stkres;

	private String playerType;

	private String rt;

	private String userIp;

	private String clarity;

	private String fudid;

	private String appType;

	private String protocolversion;

	public String getHashId() {
		return hashId;
	}

	public void setHashId(String hashId) {
		this.hashId = hashId;
	}

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public String getStkpos() {
		return stkpos;
	}

	public void setStkpos(String stkpos) {
		this.stkpos = stkpos;
	}

	public String getStktm() {
		return stktm;
	}

	public void setStktm(String stktm) {
		this.stktm = stktm;
	}

	public String getDrate() {
		return drate;
	}

	public void setDrate(String drate) {
		this.drate = drate;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getStkres() {
		return stkres;
	}

	public void setStkres(String stkres) {
		this.stkres = stkres;
	}

	public String getPlayerType() {
		return playerType;
	}

	public void setPlayerType(String playerType) {
		this.playerType = playerType;
	}

	public String getRt() {
		return rt;
	}

	public void setRt(String rt) {
		this.rt = rt;
	}

	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	public String getClarity() {
		return clarity;
	}

	public void setClarity(String clarity) {
		this.clarity = clarity;
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

	public String getProtocolversion() {
		return protocolversion;
	}

	public void setProtocolversion(String protocolversion) {
		this.protocolversion = protocolversion;
	}


	public String getOk() {
		return ok;
	}

	public void setOk(String ok) {
		this.ok = ok;
	}

	@Override
	public String toString() {
		return "PlayStuckReportInfo [hashId=" + hashId + ", serverIp=" + serverIp + ", ok=" + ok + ", stkpos=" + stkpos + ", stktm="
				+ stktm + ", drate=" + drate + ", sid=" + sid + ", stkres=" + stkres + ", playerType=" + playerType + ", rt=" + rt
				+ ", userIp=" + userIp + ", clarity=" + clarity + ", fudid=" + fudid + ", appType=" + appType + ", protocolversion="
				+ protocolversion + "]";
	}
}
