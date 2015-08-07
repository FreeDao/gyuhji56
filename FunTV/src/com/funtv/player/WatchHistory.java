package com.funtv.player;

public class WatchHistory {

	// FIXME should save max_id for favorite

	public static class UserInfo {
		public final int virtual_user_id; // 虚拟用户Id eg：爸爸，宝宝 默认0
		public final int user_id; // 用户Id 默认0

		public UserInfo(int virtual_user_id, int user_id) {
			this.virtual_user_id = virtual_user_id;
			this.user_id = user_id;
		}
	}

	public static class MediaInfo {
		public final String media_id; // 媒体Id
		public final String name_cn; // 媒体名字
		public final String display_type; // 媒体类型
		public final int max_index; // 最大的Index，完结的填0

		public MediaInfo(String media_id, String name_cn, String display_type, int max_index) {
			this.media_id = media_id;
			this.name_cn = name_cn;
			this.display_type = display_type;
			this.max_index = max_index;
		}
	}

	public static class SerialInfo {
		public final String serial_id; // 分集Id
		public final String serial_index; // 分集index
		public final String serial_title; // 分集title
		public final String serial_clarity; // 最后播放媒体的清晰度
		public final String serial_language; // 当前播放的语言

		public SerialInfo(String serial_id, String serial_index, String title, String clarity, String language) {
			this.serial_id = serial_id;
			this.serial_index = serial_index;
			this.serial_title = title;
			this.serial_clarity = clarity;
			this.serial_language = language;
		}
	}

	public final SerialInfo serialInfo;
	public final UserInfo userInfo;
	public final MediaInfo mediaInfo;
	public final int play_time_in_seconds;

	public WatchHistory(MediaInfo mediaInfo, UserInfo userInfo, SerialInfo serialInfo, int play_time_in_seconds) {
		this.mediaInfo = mediaInfo;
		this.userInfo = userInfo;
		this.serialInfo = serialInfo;
		this.play_time_in_seconds = play_time_in_seconds;
	}
}
