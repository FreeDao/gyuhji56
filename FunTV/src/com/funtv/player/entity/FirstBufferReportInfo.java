package com.funtv.player.entity;

import java.io.Serializable;

public class FirstBufferReportInfo extends BasicReportInfo implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 6352882717405141370L;
	 /*任务infohash id(ih)：cdn媒体—cid 32位，p2p媒体—hashid 40位
	 server地址：缓冲所连接的server ip地址，ios—填“”，andriod—server ip地址
	 缓冲是否成功(ok)：缓冲信息
			1，首次缓冲成功 ：0；
			2，老版本上报播放出错 ：-1；
			3，在网络正常且出MEDIA_ERROR_UNKNOWN错误时上报： -2；
			4，在没有首次缓冲成功且人为退出： -3；
			5，代码出错导致的首次缓冲失败： -4；
			6，首次缓冲没有成功时且断了网络(第二次播放时上报) ：-5；
			7，首次缓冲没有成功时且程序崩溃(第二次播放时上报)：-6；
			8，由第三方程序干扰或者在没有缓冲成功时候按锁屏且首次缓冲不成功： -7
			9, 在首次缓冲且出错误 MEDIA_ERROR_SERVER_DIED： -8
			10, 首次缓冲且出错误:MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK： -9
			11, 首次缓冲且出错误:MEDIA_INFO_BAD_INTERLEAVING:  -10
			12, 首次缓冲且出错误:MEDIA_INFO_VIDEO_TRACK_LAGGING:  -11
			13, 首次缓冲且出错误:MEDIA_INFO_NOT_SEEKABLE:  -12
			14, 首次缓冲且出错误:MEDIA_INFO_METADATA_UPDATE:  -13
			15，首次缓冲且容灾播放后仍然无法播放： -14
			16，（Android应用使用）在没有首次缓冲成功且因缓冲超时提示用户后用户选择退出： -15
			17, 首次缓冲且出错误:default:  -100
			18，iOS系统播放本地TS流缓冲失败：-101
	  当前缓冲位置(bpos)：媒体文件播放时间轴上的缓冲起始位置，单位：ms
	  视频实际缓冲时长(btm)：缓冲成功表示实际缓冲时间，失败表示用户的等待时间，单位：ms，不包含广告
	  缓冲期间平均下载速度(drate)：单位KB/s，default： -1
	  渠道ID(sid):区分各个渠道商
	  缓冲期间平均网速（nrate）: 单位KB/s, default :-1（android应用使用）
	  媒体服务器是否连接成功（msok）: 媒体服务器连接信息（android应用使用）
	  播放器类型(ptype)：android系统播放器：0；ffmpeg播放器：1；（android应用使用）
	  上报时间(rt)：unix时间戳
	  用户ip(ip)：用户ip
	  影片清晰度(cl)：1-tv,2-dvd,3-highdvd,4-superdvd
	  媒体ID(mid)：
	  分集ID(eid)：长度为32位
	  微视频ID(vid)：
	  视频类型(vt)：1-长视频，2-微视频，3-直播
	  用户ID(fudid)：长度为64位的数字与字母混排字符串，唯一标识用户
	  消息ID(messageid)：推送信息管理系统中的消息id（可为空）
	  媒体类型(type)：播放的媒体类型例电影—movie
	  是否连播(lian)：1-是，0-否
	  缓冲总时长(rtm)：计算广告时间，单位：ms
	  app类型（apptype）：用于标示app的类型
	  协议版本（rprotocol）：从1开始，有变更时递增*/
	public static final String BUFFER_OK = "0";

	public static final String BUFFER_DEFAULT_FAIL = "-100";

	public static final String BUFFER_USER_EXIT_FAIL = "-3";

	public static final String SUPPORT_LIAN_PLAY = "1";

	public static final String NOT_SUPPORT_LIAN_PLAY = "0";

	private String infoHashId;

	private String serverIp;

	private	String bufferOk;

	private String bufferPos;

	private long bufferTotalTime;

	private String downloadRate;

	private	String channelId;

	private	String netRateS;

	private	String mediaServerOk;

	private	String playerType;

	private	String reportTimeStamp;

	private	String userIP;

	private	String clarity;

	private	String mediaId;

	private	String epsoidId;

	private	String microVideoId;

	private	String videoType;

	private	String fudid;

	private	String messageId;

	private	String mediaType;

	private	String continuePlay;

	private	String adTime;

	private	String apptype;

	private	String protocolVersion;

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

	public String getBufferOk() {
		return bufferOk;
	}

	public void setBufferOk(String bufferOk) {
		this.bufferOk = bufferOk;
	}

	public String getBufferPos() {
		return bufferPos;
	}

	public void setBufferPos(String bufferPos) {
		this.bufferPos = bufferPos;
	}

	public String getDownloadRate() {
		return downloadRate;
	}

	public void setDownloadRate(String downloadRate) {
		this.downloadRate = downloadRate;
	}

	public String getNetRateS() {
		return netRateS;
	}

	public void setNetRateS(String netRateS) {
		this.netRateS = netRateS;
	}

	public String getMediaServerOk() {
		return mediaServerOk;
	}

	public void setMediaServerOk(String mediaServerOk) {
		this.mediaServerOk = mediaServerOk;
	}

	public String getPlayerType() {
		return playerType;
	}

	public void setPlayerType(String playerType) {
		this.playerType = playerType;
	}

	public String getReportTimeStamp() {
		return reportTimeStamp;
	}

	public void setReportTimeStamp(String reportTimeStamp) {
		this.reportTimeStamp = reportTimeStamp;
	}

	public String getClarity() {
		return clarity;
	}

	public void setClarity(String clarity) {
		this.clarity = clarity;
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public String getEpsoidId() {
		return epsoidId;
	}

	public void setEpsoidId(String epsoidId) {
		this.epsoidId = epsoidId;
	}

	public String getMicroVideoId() {
		return microVideoId;
	}

	public void setMicroVideoId(String microVideoId) {
		this.microVideoId = microVideoId;
	}

	public String getVideoType() {
		return videoType;
	}

	public void setVideoType(String videoType) {
		this.videoType = videoType;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public String getContinuePlay() {
		return continuePlay;
	}

	public void setContinuePlay(String continuePlay) {
		this.continuePlay = continuePlay;
	}

	public long getBufferTotalTime() {
		return bufferTotalTime;
	}

	public void setBufferTotalTime(long bufferTotalTime) {
		this.bufferTotalTime = bufferTotalTime;
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

	public String getFudid() {
		return fudid;
	}

	public void setFudid(String fudid) {
		this.fudid = fudid;
	}

	public String getAdTime() {
		return adTime;
	}

	public void setAdTime(String adTime) {
		this.adTime = adTime;
	}

	public String getApptype() {
		return apptype;
	}

	public void setApptype(String apptype) {
		this.apptype = apptype;
	}

	@Override
	public String toString() {
		return "FirstBufferReportInfo [infoHashId=" + infoHashId + ", serverIp=" + serverIp + ", bufferOk=" + bufferOk + ", bufferPos="
				+ bufferPos + ", bufferTotalTime=" + bufferTotalTime + ", downloadRate=" + downloadRate + ", channelId=" + channelId
				+ ", netRateS=" + netRateS + ", mediaServerOk=" + mediaServerOk + ", playerType=" + playerType + ", reportTimeStamp="
				+ reportTimeStamp + ", userIP=" + userIP + ", clarity=" + clarity + ", mediaId=" + mediaId + ", epsoidId=" + epsoidId
				+ ", microVideoId=" + microVideoId + ", videoType=" + videoType + ", fudid=" + fudid + ", messageId=" + messageId
				+ ", mediaType=" + mediaType + ", continuePlay=" + continuePlay + ", adTime=" + adTime + ", apptype=" + apptype
				+ ", protocolVersion=" + protocolVersion + "]";
	}

}
