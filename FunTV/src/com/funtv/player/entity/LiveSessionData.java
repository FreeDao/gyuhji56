package com.funtv.player.entity;

import java.util.Arrays;

public class LiveSessionData {
//	  {
//          "name": "斯托克城 VS 曼城",
//          "session_id": "8116718",
//          "start_time": 1408118400,
//          "end_time": 1408125300,
//          "streams": [
//              {
//                  "stream_clarity": "high-dvd",
//                  "stream_url": "fsp://38475AF599FD8CF1B1F54D3186750EA6FB68171E|mn=|sz=|t="
//              }
//          ]
//      }

	private String name;

	private String session_id;

	private long start_time;

	private long end_time;

	private LiveStream[] streams;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSession_id() {
		return session_id;
	}

	public void setSession_id(String session_id) {
		this.session_id = session_id;
	}

	public long getStart_time() {
		return start_time;
	}

	public void setStart_time(long start_time) {
		this.start_time = start_time;
	}

	public long getEnd_time() {
		return end_time;
	}

	public void setEnd_time(long end_time) {
		this.end_time = end_time;
	}

	public LiveStream[] getStreams() {
		return streams;
	}

	public void setStreams(LiveStream[] streams) {
		this.streams = streams;
	}

	@Override
	public String toString() {
		return "LiveSessionData [name=" + name + ", session_id=" + session_id + ", start_time=" + start_time + ", end_time=" + end_time
				+ ", streams=" + Arrays.toString(streams) + "]";
	}

}
