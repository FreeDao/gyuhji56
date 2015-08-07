package com.funtv.player.entity;

public class ReportAssistant {

	public static final int STYPE_CDN = 1;

	public static final int STYPE_P2P = 2;

	public int stuckNum;// 总卡次数计数

	public int pauseNum;// 总暂停次数计数

	public long totalStuckTime;// 总卡时间

	public long currentStuckTime; // 本次卡时间

	public long startPauseTime;// 暂停开始时刻

	public long endPauseTime;// 暂停结束时刻

	public long totalPauseTime;// 暂停总时间

	public long startStuckTime;// 拖动卡开始时刻

	public long endStuckTime;// 拖动卡结束时刻

	public String clarity;// 视频清晰度

	public String playerType;// 播放器类型

	public String infoHashId;// 当前播放任务hashid

	public String videoType;// 视频类型

	public String mediaType;// 媒体类型

	public String serverIP;// 服务器ip地址

	public long dragStartPostion;// 拖动起始位置

	public long bufferingPostion;// 当前缓冲位置

	public String mediaId;// 媒体ID

	public String serialId;// 分集ID

	public long firstBufferTime;// 首次缓冲的时刻

	public long currentNetRate;// 当前的网速

	public long startPostion;// 播放的起始位置

	public long endPostion;// 播放的结束位置

	public int stype; //任务类型(stype): 1—cdn，2—p2p

	public String getClarity() {
		return clarity;
	}

	public void setClarity(String clarity) {
		this.clarity = clarity;
	}

	public String getPlayerType() {
		return playerType;
	}

	public void setPlayerType(String playerType) {
		this.playerType = playerType;
	}

	public String getInfoHashId() {
		return infoHashId;
	}

	public void setInfoHashId(String infoHashId) {
		this.infoHashId = infoHashId;
	}

	public String getVideoType() {
		return videoType;
	}

	public void setVideoType(String videoType) {
		this.videoType = videoType;
	}

	public String getServerIP() {
		return serverIP;
	}

	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}

	public long getDragStartPostion() {
		return dragStartPostion;
	}

	public void setDragStartPostion(long dragStartPostion) {
		this.dragStartPostion = dragStartPostion;
	}

	public long getBufferingPostion() {
		return bufferingPostion;
	}

	public void setBufferingPostion(long bufferingPostion) {
		this.bufferingPostion = bufferingPostion;
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public String getSerialId() {
		return serialId;
	}

	public void setSerialId(String serialId) {
		this.serialId = serialId;
	}

	public long getFirstBufferTime() {
		return firstBufferTime;
	}

	public void setFirstBufferTime(long firstBufferTime) {
		this.firstBufferTime = firstBufferTime;
	}

	public long getCurrentNetRate() {
		return currentNetRate;
	}

	public void setCurrentNetRate(long currentNetRate) {
		this.currentNetRate = currentNetRate;
	}

	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public long getStartPostion() {
		return startPostion;
	}

	public void setStartPostion(long startPostion) {
		this.startPostion = startPostion;
	}

	public long getEndPostion() {
		return endPostion;
	}

	public void setEndPostion(long endPostion) {
		this.endPostion = endPostion;
	}

	@Override
	public String toString() {
		return "ReportAssistant [stuckNum=" + stuckNum + ", pauseNum=" + pauseNum + ", totalStuckTime=" + totalStuckTime
				+ ", startPauseTime=" + startPauseTime + ", endPauseTime=" + endPauseTime + ", totalPauseTime=" + totalPauseTime
				+ ", startStuckTime=" + startStuckTime + ", endStuckTime=" + endStuckTime + ", clarity=" + clarity + ", playerType="
				+ playerType + ", infoHashId=" + infoHashId + ", videoType=" + videoType + ", mediaType=" + mediaType + ", serverIP="
				+ serverIP + ", dragStartPostion=" + dragStartPostion + ", bufferingPostion=" + bufferingPostion + ", mediaId=" + mediaId
				+ ", serialId=" + serialId + ", firstBufferTime=" + firstBufferTime + ", currentNetRate=" + currentNetRate
				+ ", startPostion=" + startPostion + ", endPostion=" + endPostion + "]";
	}
}
