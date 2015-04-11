package com.bmtech.spider.ext.scorer.scorers;

import java.util.ArrayList;
import java.util.List;

import com.bmtech.utils.io.ConfigReader;
import com.bmtech.utils.log.LogHelper;

class ScorerConfig {
	private LogHelper log = new LogHelper("scorerConfig");
	final List<String> expressions = new ArrayList<String>();
	final int score;
	final int countRepeatPerWord;

	public ScorerConfig(ConfigReader cr) {
		this.score = cr.getInt("score", 5);
		countRepeatPerWord = cr.getInt("countRepeatPerWord", 1);
		String tokens[] = cr.getValue("exp").trim().replace('\t', ' ')
				.split(" ");
		for (String x : tokens) {
			x = x.trim();
			if (x.length() > 1) {
				expressions.add(x);
			} else if (x.length() == 1) {
				log.error("too short word '%s', skip!", x);
			}
		}
	}

	public List<ScorerItem> toList(boolean regex) {
		List<ScorerItem> ret = new ArrayList<ScorerItem>(expressions.size());
		for (String x : this.expressions) {
			ScorerItem item;
			if (regex) {
				item = new RegexScorer(x, score, countRepeatPerWord);
			} else {
				item = new StringScorer(x, score, countRepeatPerWord);
			}
			ret.add(item);
		}
		return ret;
	}

}