package com.bmtech.spider.ext.scorer.scorers;

public class StringScorer extends ScorerItem {

	StringScorer(String exp, int score, int repeat) {
		super(exp.toLowerCase(), score, repeat);
	}

	@Override
	public String toString() {
		return "StringScorer [score=" + score + ", repeat=" + repeat + ", exp="
				+ exp + "]";
	}

	@Override
	public int score(String line) {
		throw new RuntimeException("not support score(String) in "
				+ this.getClass());
	}

}
