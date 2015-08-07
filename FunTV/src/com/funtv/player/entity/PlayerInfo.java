package com.funtv.player.entity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;

public class PlayerInfo implements Serializable{

	private static final long serialVersionUID = -7825029838456421082L;

	private String mid;

	private String name_cn;

	private String mtype;

	private String[] country;

	private String releasedate;

	private String karma_h;

	private String karma_l;

	private String picture;

	private String plots;

	private String tag4editor;

	private String playnum;

	private String isHotProgram;

	private boolean isall;

	private String order_type;

	private Map<String,String> clarity_map;

	private Map<String,String> lang_map;

	private EpisodeInfo[] torrents;

	private RelatedMedia relate_media[];

	private String user_selected_index;

	private String user_selected_clarity;

	private String user_selected_language;

	private int play_position;  // JS页面传入的播放历史位置

	private long time; // 直播时间校验

	private String user_selected_session_id; // 直播场次Id

	private LiveSessionData[] live_sessions; // 直播场次信息

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getName_cn() {
		return name_cn;
	}

	public void setName_cn(String name_cn) {
		this.name_cn = name_cn;
	}

	public String getMtype() {
		return mtype;
	}

	public void setMtype(String mtype) {
		this.mtype = mtype;
	}

	public String[] getCountry() {
		return country;
	}

	public void setCountry(String[] country) {
		this.country = country;
	}

	public String getReleasedate() {
		return releasedate;
	}

	public void setReleasedate(String releasedate) {
		this.releasedate = releasedate;
	}

	public String getKarma_h() {
		return karma_h;
	}

	public void setKarma_h(String karma_h) {
		this.karma_h = karma_h;
	}

	public String getKarma_l() {
		return karma_l;
	}

	public void setKarma_l(String karma_l) {
		this.karma_l = karma_l;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getPlots() {
		return plots;
	}

	public void setPlots(String plots) {
		this.plots = plots;
	}

	public String getTag4editor() {
		return tag4editor;
	}

	public void setTag4editor(String tag4editor) {
		this.tag4editor = tag4editor;
	}

	public String getPlaynum() {
		return playnum;
	}

	public void setPlaynum(String playnum) {
		this.playnum = playnum;
	}

	public String getIsHotProgram() {
		return isHotProgram;
	}

	public void setIsHotProgram(String isHotProgram) {
		this.isHotProgram = isHotProgram;
	}

	public boolean getIsall() {
		return isall;
	}

	public void setIsall(boolean isall) {
		this.isall = isall;
	}

	public String getOrder_type() {
		return order_type;
	}

	public void setOrder_type(String order_type) {
		this.order_type = order_type;
	}

	public Map<String, String> getClarity_map() {
		return clarity_map;
	}

	public void setClarity_map(Map<String, String> clarity_map) {
		this.clarity_map = clarity_map;
	}

	public Map<String, String> getLang_map() {
		return lang_map;
	}

	public void setLang_map(Map<String, String> lang_map) {
		this.lang_map = lang_map;
	}

	public String getUser_selected_index() {
		return user_selected_index;
	}

	public void setUser_selected_index(String user_selected_index) {
		this.user_selected_index = user_selected_index;
	}

	public String getUser_selected_clarity() {
		return user_selected_clarity;
	}

	public void setUser_selected_clarity(String user_selected_clarity) {
		this.user_selected_clarity = user_selected_clarity;
	}

	public String getUser_selected_language() {
		return user_selected_language;
	}

	public void setUser_selected_language(String user_selected_language) {
		this.user_selected_language = user_selected_language;
	}

	public int getPlay_position() {
		return play_position;
	}

	public void setPlay_position(int play_position) {
		this.play_position = play_position;
	}

	public EpisodeInfo[] getTorrents() {
		return torrents;
	}

	public void setTorrents(EpisodeInfo[] torrents) {
		this.torrents = torrents;
	}

	public RelatedMedia[] getRelate_media() {
		return relate_media;
	}

	public void setRelate_media(RelatedMedia[] relate_media) {
		this.relate_media = relate_media;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public LiveSessionData[] getLive_sessions() {
		return live_sessions;
	}

	public void setLive_sessions(LiveSessionData[] live_sessions) {
		this.live_sessions = live_sessions;
	}

	public String getUser_selected_session_id() {
		return user_selected_session_id;
	}

	public void setUser_selected_session_id(String user_selected_session_id) {
		this.user_selected_session_id = user_selected_session_id;
	}

	@Override
	public String toString() {
		return "PlayerInfo [mid=" + mid + ", name_cn=" + name_cn + ", mtype=" + mtype + ", country=" + Arrays.toString(country)
				+ ", releasedate=" + releasedate + ", karma_h=" + karma_h + ", karma_l=" + karma_l + ", picture=" + picture + ", plots="
				+ plots + ", tag4editor=" + tag4editor + ", playnum=" + playnum + ", isHotProgram=" + isHotProgram + ", isall=" + isall
				+ ", order_type=" + order_type + ", clarity_map=" + clarity_map + ", lang_map=" + lang_map + ", torrents="
				+ Arrays.toString(torrents) + ", relate_media=" + Arrays.toString(relate_media) + ", user_selected_index="
				+ user_selected_index + ", user_selected_clarity=" + user_selected_clarity + ", user_selected_language="
				+ user_selected_language + ", play_position=" + play_position + ", time=" + time + ", user_selected_session_id="
				+ user_selected_session_id + ", live_sessions=" + Arrays.toString(live_sessions) + "]";
	}
}
