package textmatchers.pattern.def;

public class BooleanRepeatMatcher implements QMatchable {
	private final QMatchable basicPattern;
	private final int min, max;

	public BooleanRepeatMatcher(QMatchable qm, int min, int max)
			throws Exception {
		this.basicPattern = qm;
		if (min < 1 || min > max) {
			throw new Exception("repeat matcher's number error! " + qm + "."
					+ min + "-" + max);
		}
		this.min = min;
		this.max = max;
	}

	@Override
	public String toString() {
		return basicPattern + "." + min + "-"
				+ (Integer.MAX_VALUE == max ? "" : max);
	}

	@Override
	public QResult match(MString str) {
		MatchableList mi = new MatchableList() {

			@Override
			public int size() {
				return max;
			}

			@Override
			public QMatchable get(int matcherIdx) {
				return basicPattern;
			}

			@Override
			public boolean canStop(int matcherIdx) {
				return matcherIdx + 1 >= min;
			}

			@Override
			public String toString() {
				return BooleanRepeatMatcher.this.toString();
			}

			@Override
			public boolean shouldExpandAndMatcher() {
				return false;
			}

		};

		return ListMatchTool.match(str, mi);
	}

}
