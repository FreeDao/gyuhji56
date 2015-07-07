package textmatchers.pattern.def;


public class BooleanOrMatcher implements QMatchable {
	QMatchable[] matcher;

	public BooleanOrMatcher(QMatchable... match) {
		this.matcher = match;
	}

	@Override
	public QResult match(MString str) {

		QEnumResult cmbResult = new QEnumResult();
		for (QMatchable rule : matcher) {
			QResult result = rule.match(str);
			for (int x = 0; x < result.getTotalMatch(); x++) {
				MatchNode end = result.getMatch(x);
				cmbResult.addMatch(end);
			}
		}
		return cmbResult;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (QMatchable qm : this.matcher) {
			if (sb.length() > 0) {
				sb.append("|");
			}
			sb.append(qm);

		}
		return "{" + sb + "}";

	}
}
