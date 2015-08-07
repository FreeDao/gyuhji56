package com.funtv.player.entity;

import java.io.Serializable;

public class RelatedMedia implements Serializable{
	
	private static final long serialVersionUID = 8329038422892509039L;
	
	// 相关媒体Id
	private String mediaid;
	
	// 相关媒体名字
	private String name_cn;
	
	// 相关媒体海报图
	private String poster;
	
	// 相关媒体跳转链接
	private String media_url;

	public String getMediaid() {
		return mediaid;
	}

	public void setMediaid(String mediaid) {
		this.mediaid = mediaid;
	}

	public String getName_cn() {
		return name_cn;
	}

	public void setName_cn(String name_cn) {
		this.name_cn = name_cn;
	}

	public String getPoster() {
		return poster;
	}

	public void setPoster(String poster) {
		this.poster = poster;
	}

	public String getMedia_url() {
		return media_url;
	}

	public void setMedia_url(String media_url) {
		this.media_url = media_url;
	}

	@Override
	public String toString() {
		return "RelatedMedia [mediaid=" + mediaid + ", name_cn=" + name_cn + ", poster=" + poster + ", media_url=" + media_url + "]";
	}
}
