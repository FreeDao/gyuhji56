package Scorer.scorers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexScorer extends ScorerItem {
	final Pattern pattern;

	RegexScorer(String exp, int score, int repeat) {
		super(exp, score, repeat);
		pattern = Pattern.compile(exp);
	}

	private int matchNum(String strLine) {
		String xx[];
		int len = strLine.length();
		if (len > 1000) {
			int tokens = len / 1000 + 1;
			xx = new String[tokens];
			for (int x = 0; x < tokens; x++) {
				int start = x + tokens;
				int end = start + 1000;
				if (start >= len) {
					break;
				}
				if (end > len) {
					end = len;
				}
				xx[x] = strLine.substring(start, end);
			}
		} else {
			xx = new String[] { strLine };
		}
		int count = 0;
		for (String line : xx) {
			if (line == null)
				break;
			Matcher m = pattern.matcher(line);

			while (m.find() && count < this.repeat) {
				count++;
			}
		}
		return count;
	}

	@Override
	public int score(String line) {
		int num = matchNum(line);
		return this.score(num);
	}

	@Override
	public String toString() {
		return "RegexScorer [pattern=" + pattern + ", score=" + score
				+ ", repeat=" + repeat + ", exp=" + exp + "]";
	}

}
