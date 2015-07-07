package textmatchers.pattern.def.reg;

import textmatchers.pattern.def.MString;
import textmatchers.pattern.def.MatchNode;
import textmatchers.pattern.def.QEnumResult;
import textmatchers.pattern.def.QMatchable;

public class SString implements QMatchable {
	final String pattern;

	public SString(String pattern) {
		this.pattern = pattern;
	}

	@Override
	public QEnumResult match(MString str) {
		QEnumResult match = new QEnumResult();
		String line = str.getStr().toLowerCase();
		String pattern = this.pattern.toLowerCase();

		if (line.startsWith(pattern)) {
			match.addMatch(new MatchNode(0, pattern.length(), this, null, 0,
					null));
		}

		return match;
	}

	@Override
	public String toString() {
		return pattern;
	}
}
