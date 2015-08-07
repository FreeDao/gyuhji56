package com.funtv.player.entity;

import java.io.Serializable;
import java.util.Arrays;

public class EpisodeInfo implements Serializable{

	private static final long serialVersionUID = -6676514179344084222L;

	private String serialid;

	private String index;

	private String taskname;

	private String title;

	private String picurl;

	private ClarityFsp[] clarity_fsps;

	public String getSerialid() {
		return serialid;
	}

	public void setSerialid(String serialid) {
		this.serialid = serialid;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getTaskname() {
		return taskname;
	}

	public void setTaskname(String taskname) {
		this.taskname = taskname;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPicurl() {
		return picurl;
	}

	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}

	public ClarityFsp[] getClarity_fsps() {
		return clarity_fsps;
	}

	public void setClarity_fsps(ClarityFsp[] clarity_fsps) {
		this.clarity_fsps = clarity_fsps;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "EpisodeInfo [serialid=" + serialid + ", index=" + index + ", taskname=" + taskname + ", title=" + title + ", picurl="
				+ picurl + ", clarity_fsps=" + Arrays.toString(clarity_fsps) + "]";
	}


}
