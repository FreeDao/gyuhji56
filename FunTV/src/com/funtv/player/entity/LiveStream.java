package com.funtv.player.entity;

import java.io.Serializable;

import com.funtv.utils.StringUtil;

public class LiveStream implements Serializable{

	// {
	// "stream_clarity": "high-dvd",
	// "stream_url": "fsp://{infohash}|mn=|sz=|t="
	// },

	/**
	 *
	 */
	private static final long serialVersionUID = 6775625561569912757L;

	private String stream_clarity;

	private String stream_url;

	public String getStream_clarity() {
		return stream_clarity;
	}

	public void setStream_clarity(String stream_clarity) {
		this.stream_clarity = stream_clarity;
	}

	public String getStream_url() {
		return stream_url;
	}

	public void setStream_url(String stream_url) {
		if(!StringUtil.isEmpty(stream_url)  && stream_url.startsWith("fhp://")){
			String source = stream_url.replace("fhp:", "http:");
			this.stream_url = source;
		}else{
			this.stream_url = stream_url;
		}
	}

	@Override
	public String toString() {
		return "LiveStream [stream_clarity=" + stream_clarity + ", stream_url=" + stream_url + "]";
	}
}
