package textmatchers.pattern.def;

import java.util.ArrayList;

public class QPattern extends QPatternInner {
	/**
	 * filter the results
	 */
	private boolean fullMatch = false;
	protected ArrayList<QMatchable> notMatches = new ArrayList<QMatchable>();

	/**
	 * 构造QMatchable的list
	 * 
	 * @return
	 */

	public QPattern(QMatchable qms) {
		this(false, qms);
	}

	public QPattern(boolean fullMatch, QMatchable qm) {
		this.fullMatch = fullMatch;
		if (qm instanceof BooleanAndMatcher) {
			for (QMatchable m : ((BooleanAndMatcher) qm).matcher) {
				this.addMatcher(m);
			}
		} else {
			this.addMatcher(qm);
		}
	}

	public void addNotMatcher(QMatchable notMatcher) {
		notMatches.add(notMatcher);
	}

	@Override
	public QResult match(MString str) {
		QResult qr = super.match(str);
		if (fullMatch) {
			for (int x = 0; x < qr.getTotalMatch(); x++) {
				int end = qr.getMatchEnd(x);
				if (end != str.length()) {
					qr.removeMatch(x);
					x--;
				}
			}
		}
		notFilter(qr, str);
		return qr;
	}

	protected void notFilter(QResult qr, MString str) {
		if (this.notMatches == null || this.notMatches.size() == 0) {
			return;
		}
		BooleanAndMatcher am = new BooleanAndMatcher(this.notMatches);
		for (int x = 0; x < qr.getTotalMatch(); x++) {
			// int end = qr.getMatchEnd(x);
			// String sub = str.substring(0, end);
			QResult qrsub = am.match(str);
			if (qrsub.isMatch()) {
				qr.removeMatch(x);
				x--;
			}
		}
	}

	public void setFullMatch(boolean fullMatch) {
		this.fullMatch = fullMatch;
	}

	public boolean isFullMatch() {
		return fullMatch;
	}
}
