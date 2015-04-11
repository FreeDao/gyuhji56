package com.bmtech.spider.ext.salon;

import java.net.URL;

import com.bmtech.htmls.parser.util.NodeList;
import com.bmtech.spider.core.CoreUtil;
import com.bmtech.spider.core.HtmlScore;
import com.bmtech.spider.ext.scorer.MDirScorer;
import com.bmtech.spider.ext.scorer.scorers.MultiScorer;
import com.bmtech.utils.HtmlRemover;

/**
 * score for incremental crawler
 * 
 * @author Administrator
 *
 */
public class SalonHtmlScore extends HtmlScore {
	private static MultiScorer instance = MultiScorer.getInstance();

	@Override
	public int listScore(NodeList nl, URL url) {
		return 0;
	}

	@Override
	public int pageScore(NodeList nl, URL url) {
		String text = nl.asString();
		int score = instance.score(text);
		if (score >= MDirScorer.level1) {

			String title = CoreUtil.getHtmlTitle(nl);

			OnlineSavor.getInstance().save(title, text, url, score);
		}
		return score;
	}

	@Override
	public int urlScore(String linkText, String title, String link) {
		linkText = fmt(linkText);
		title = fmt(title);
		return Math.max(instance.score(linkText), instance.score(title));
	}

	@Override
	public boolean isGoodScore(int pageScore, int listScore) {
		return true;
	}

	private String fmt(String str) {
		if (str == null) {
			return "";
		}
		str = HtmlRemover.htmlToLineFormat(str).lines;
		str = str.replaceAll("[ \t　\r\n]{1,}", "");
		return str;
	}
}
