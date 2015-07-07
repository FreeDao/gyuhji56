package textmatchers.pattern.def;

import java.util.ArrayList;
import java.util.Iterator;

public class QPatternInner implements QMatchable {
	protected MatchableListAllMatch matches = new MatchableListAllMatch();

	static class MatchableListAllMatch extends MatchableList {
		ArrayList<QMatchable> matches = new ArrayList<QMatchable>();

		@Override
		public int size() {
			return matches.size();
		}

		public void add(QMatchable a) {
			matches.add(a);
		}

		@Override
		public QMatchable get(int matcherIdx) {
			return matches.get(matcherIdx);
		}

		public Iterator<QMatchable> iterator() {
			return this.matches.iterator();

		}

		@Override
		public boolean canStop(int matcherIdx) {
			return this.matches.size() == matcherIdx + 1;
		}

		//
		// @Override
		// public String toString() {
		// return QPatternInner.this.toString();
		// }

		@Override
		public boolean shouldExpandAndMatcher() {
			return true;
		}

	}

	public int matchNum() {
		return this.matches.size();
	}

	public QPatternInner() {

	}

	public void addMatcher(QMatchable... aMatcher) {
		for (QMatchable ma : aMatcher) {
			this.addMatcher(ma);
		}

	}

	private final void addMatcher(QMatchable a) {
		if (a == null) {
			throw new RuntimeException("can not add null into QPattern's list");
		}
		matches.add(a);
	}

	@Override
	public QResult match(MString str) {
		return ListMatchTool.match(str, matches);
	}

	public void addMatchers(ArrayList<QMatchable> lst) {
		for (QMatchable qm : lst) {
			this.addMatcher(qm);
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Iterator<QMatchable> amItr = matches.iterator();
		for (; amItr.hasNext();) {
			QMatchable am = amItr.next();
			if (sb.length() > 0) {
				sb.append(' ');
			}
			sb.append(am);
		}
		return sb.toString();

	}

}
