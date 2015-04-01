package Scorer.scorers;

abstract class ScorerItem {
	final int score;
	final int repeat;
	final String exp;

	ScorerItem(String exp, int score, int repeat) {
		this.exp = exp.trim();
		this.score = score;
		this.repeat = repeat;
	}

	public abstract int score(String line);

	public int score(int repeatNum) {
		if (repeatNum < 1) {
			return 0;
		} else if (repeatNum == 1) {
			return this.score;
		} else {
			int repeat = Math.min(repeatNum, this.repeat);
			return repeat * score;
		}
	}
}