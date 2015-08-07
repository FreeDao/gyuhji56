package com.funtv.player.entity;

public class PlayTimeReportInfo extends BasicReportInfo {

	//  任务infohash id(ih)：cdn媒体p2p媒体—hashid 40位
	//  播放起始位置(spos)：媒体文件的播放时间轴上的起始位置，单位：ms
	//  播放结束位置(epos)：媒体文件的播放时间轴上的结束位置，单位：ms
	//  实际观看时间(vtm)：在播放器界面停留的时间，单位：ms
	//					 包括暂停时间，包括广告时间，包括首次缓冲时间，包括小播放器+大播放器的播放时间
	// 					不包括切至后台等待时间，不包括第三方打扰时间
	//  渠道ID(sid):区分各个渠道商
	//  播放总卡次数(pn): 播放时的总卡次数
	//  播放总卡时长(tu): 播放时的总卡时长，单位：ms
	//  播放器类型(ptype)：android系统播放器：0；ffmpeg播放器：1；（android应用使用）
	//  播放中断原因(pbre):退出播放器原因，0：播放结束，1：用户主动退出，
	// 					 2：进入到后台退出，3：多剧集媒体用户切换退出，10：其他
	//  上报时间(rt)：unix时间戳
	//  用户ip(ip)：用户ip
	//  影片清晰度(cl): 1-tv,2-dvd,3-highdvd,4-superdvd
	//  用户ID(fudid)：长度为64位的数字与字母混排字符串，唯一标识用户
	//  视频类型(vt):1-长视频，2-微视频，3-直播
	//  媒体类型(type): 播放的媒体类型例电影—movie
	//  媒体ID(mid):视频类型为微视频时，指微视频id
	//  分集ID(eid):长度为32位
	//  播放总暂停次数(sn)：播放时的总暂停次数
	//  播放总暂停时长(st)：播放时的总暂停时长，单位：ms；大于2小时default：-1
	//  app类型（apptype）：用于标示app的类型
	//  协议版本（rprotocol）：从1开始，有变更时递增

	public static final String PLAY_BREAK_REASHON_END = "0";

	public static final String PLAY_BREAK_REASHON_USER_EXIT = "1";

	public static final String PLAY_BREAK_REASHON_BACKGROUND = "2";

	public static final String PLAY_BREAK_REASHON_CHANGE_EPISODE = "3";

	public static final String PLAY_BREAK_REASHON_OTHER = "10";

	private String InfoHashID;

	private String startPos;

	private String endPos;

	private long realWatchTime;

	private String totalStuckNumber;

	private long totalStuckTime;

	private String playerType;

	private String playBreakReason;

	private long reportTimeStamp;

	private String userIP;

	private String clarity;

	private String videoType;

	private String mediaType;

	private String mediaId;

	private String episodeId;

	private String totalPauseNum;

	private long totalPauseTime;

	private String protocolVersion;

	public String getInfoHashID() {
		return InfoHashID;
	}

	public void setInfoHashID(String infoHashID) {
		InfoHashID = infoHashID;
	}

	public String getStartPos() {
		return startPos;
	}

	public void setStartPos(String startPos) {
		this.startPos = startPos;
	}

	public String getEndPos() {
		return endPos;
	}

	public void setEndPos(String endPos) {
		this.endPos = endPos;
	}

	public long getRealWatchTime() {
		return realWatchTime;
	}

	public void setRealWatchTime(long realWatchTime) {
		this.realWatchTime = realWatchTime;
	}

	public String getTotalStuckNumber() {
		return totalStuckNumber;
	}

	public void setTotalStuckNumber(String totalStuckNumber) {
		this.totalStuckNumber = totalStuckNumber;
	}

	public long getTotalStuckTime() {
		return totalStuckTime;
	}

	public void setTotalStuckTime(long totalStuckTime) {
		this.totalStuckTime = totalStuckTime;
	}

	public String getPlayerType() {
		return playerType;
	}

	public void setPlayerType(String playerType) {
		this.playerType = playerType;
	}

	public String getPlayBreakReason() {
		return playBreakReason;
	}

	public void setPlayBreakReason(String playBreakReason) {
		this.playBreakReason = playBreakReason;
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

	public String getVideoType() {
		return videoType;
	}

	public void setVideoType(String videoType) {
		this.videoType = videoType;
	}

	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public String getEpisodeId() {
		return episodeId;
	}

	public void setEpisodeId(String episodeId) {
		this.episodeId = episodeId;
	}

	public String getTotalPauseNum() {
		return totalPauseNum;
	}

	public void setTotalPauseNum(String totalPauseNum) {
		this.totalPauseNum = totalPauseNum;
	}

	public long getTotalPauseTime() {
		return totalPauseTime;
	}

	public void setTotalPauseTime(long totalPauseTime) {
		this.totalPauseTime = totalPauseTime;
	}

	public String getProtocolVersion() {
		return protocolVersion;
	}

	public void setProtocolVersion(String protocolVersion) {
		this.protocolVersion = protocolVersion;
	}

	public String getUserIP() {
		return userIP;
	}

	public void setUserIP(String userIP) {
		this.userIP = userIP;
	}

	@Override
	public String toString() {
		return "PlayTimeReportInfo [InfoHashID=" + InfoHashID + ", startPos=" + startPos + ", endPos=" + endPos + ", realWatchTime="
				+ realWatchTime + ", totalStuckNumber=" + totalStuckNumber + ", totalStuckTime=" + totalStuckTime + ", playerType="
				+ playerType + ", playBreakReason=" + playBreakReason + ", reportTimeStamp=" + reportTimeStamp + ", userIP=" + userIP
				+ ", clarity=" + clarity + ", videoType=" + videoType + ", mediaType=" + mediaType + ", mediaId=" + mediaId
				+ ", episodeId=" + episodeId + ", totalPauseNum=" + totalPauseNum + ", totalPauseTime=" + totalPauseTime
				+ ", protocolVersion=" + protocolVersion + "]";
	}
}
