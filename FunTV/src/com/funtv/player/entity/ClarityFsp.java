package com.funtv.player.entity;

import java.io.Serializable;
import java.util.Arrays;

public class ClarityFsp implements Serializable{

	private static final long serialVersionUID = 2285077313302444307L;

	private String clarity;

	private LangFsp[] lang_fsps;

	public String getClarity() {
		return clarity;
	}

	public void setClarity(String clarity) {
		this.clarity = clarity;
	}

	public LangFsp[] getLang_fsps() {
		return lang_fsps;
	}

	public void setLang_fsps(LangFsp[] lang_fsps) {
		this.lang_fsps = lang_fsps;
	}

	@Override
	public String toString() {
		return "ClarityFsp [clarity=" + clarity + ", lang_fsps=" + Arrays.toString(lang_fsps) + "]";
	}

}
