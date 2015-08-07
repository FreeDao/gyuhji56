package com.funtv.player.entity;

public class DragBufferReportInfo extends BasicReportInfo {
	//  任务infohash id(ih)：cdn媒体—cid 32位，p2p媒体—hashid 40位
	//  server地址：缓冲所连接的server ip地址，ios—填“”，andriod—server ip地址
	//  缓冲是否成功(ok)：0—缓冲成功，-1—缓冲失败
	//  当前下载位置(dpos)：当前已经下载数据支持的最大可播放位置，单位：ms
	//  拖动起始位置(spos)：拖动前的播放位置，单位：ms
	//  当前缓冲位置(bpos)：媒体文件播放时间轴上的缓冲起始位置，单位：ms
	//  缓冲时间(btm)：缓冲成功表示实际缓冲时间，失败表示用户的等待时间，单位：ms
	//  缓冲期间平均下载速度(drate)：单位KB/s，default： -1
	//  渠道ID(sid):区分各个渠道商
	//  播放器类型(ptype)：android系统播放器：0；ffmpeg播放器：1；（android应用使用）
	//  上报时间(rt)：unix时间戳
	//  用户ip(ip)：用户ip
	//  影片清晰度(cl)：1-tv,2-dvd,3-highdvd,4-superdvd
	//  用户ID(fudid)：长度为64位的数字与字母混排字符串，唯一标识用户
	//  app类型（apptype）：用于标示app的类型
	//  协议版本（rprotocol）：从1开始，有变更时递增

	private String infoHashId;

	private String serverIp;

	private String dragOkS;

	private String downloadPos;

	private String startPos;

	private String bufferPos;

	private long bufferTotalTime;

	private String downloadRate;

	private String channelId;

	private String playerType;

	private long reportTimeStamp;

	private String userIp;

	private String clarity;

	private String fudid;

	private String appType;

	private String protocolversion;

	public String getInfoHashId() {
		return infoHashId;
	}

	public void setInfoHashId(String infoHashId) {
		this.infoHashId = infoHashId;
	}

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public String getDragOkS() {
		return dragOkS;
	}

	public void setDragOkS(String dragOkS) {
		this.dragOkS = dragOkS;
	}

	public String getDownloadPos() {
		return downloadPos;
	}

	public void setDownloadPos(String downloadPos) {
		this.downloadPos = downloadPos;
	}

	public String getStartPos() {
		return startPos;
	}

	public void setStartPos(String startPos) {
		this.startPos = startPos;
	}

	public String getBufferPos() {
		return bufferPos;
	}

	public void setBufferPos(String bufferPos) {
		this.bufferPos = bufferPos;
	}

	public long getBufferTotalTime() {
		return bufferTotalTime;
	}

	public void setBufferTotalTime(long bufferTotalTime) {
		this.bufferTotalTime = bufferTotalTime;
	}

	public String getDownloadRate() {
		return downloadRate;
	}

	public void setDownloadRate(String downloadRate) {
		this.downloadRate = downloadRate;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getPlayerType() {
		return playerType;
	}

	public void setPlayerType(String playerType) {
		this.playerType = playerType;
	}

	public long getReportTimeStamp() {
		return reportTimeStamp;
	}

	public void setReportTimeStamp(long reportTimeStamp) {
		this.reportTimeStamp = reportTimeStamp;
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

	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	@Override
	public String toString() {
		return "DragBufferReportInfo [infoHashId=" + infoHashId + ", serverIp=" + serverIp + ", dragOkS=" + dragOkS + ", downloadPos="
				+ downloadPos + ", startPos=" + startPos + ", bufferPos=" + bufferPos + ", bufferTotalTime=" + bufferTotalTime
				+ ", downloadRate=" + downloadRate + ", channelId=" + channelId + ", playerType=" + playerType + ", reportTimeStamp="
				+ reportTimeStamp + ", userIp=" + userIp + ", clarity=" + clarity + ", fudid=" + fudid + ", appType=" + appType
				+ ", protocolversion=" + protocolversion + "]";
	}
}
