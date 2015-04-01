package com.bmtech.spider.core;

import java.net.URL;

import com.bmtech.htmls.parser.util.NodeList;
public class HTMLScoreImpNo extends HtmlScore{

	@Override
	public int listScore(NodeList nl, URL url) {
		return 0;
	}

	@Override
	public int pageScore(NodeList nl, URL url) {
		return 0;
	}

	@Override
	public int urlScore(String linkText, String attribute, String link) {
		return 0;
	}

	@Override
	public boolean isGoodScore(int pageScore, int listScore) {
		return true;
	}

}
