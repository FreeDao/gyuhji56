package com.funtv.player.entity;

import java.io.Serializable;

public class LangFsp implements Serializable{

	private static final long serialVersionUID = -3583374050268985145L;

	private String lang;

	private String fsp;

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getFsp() {
		return fsp;
	}

	public void setFsp(String fsp) {
		this.fsp = fsp;
	}

	@Override
	public String toString() {
		return "LangFsp [lang=" + lang + ", fsp=" + fsp + "]";
	}
}
