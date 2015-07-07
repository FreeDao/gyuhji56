package textmatchers.pattern.def;

public class ListMatchTool {

	protected MatchableList matches;

	private ListMatchTool(MatchableList list) {
		this.matches = list;
	}

	private QResult match(MString str) {
		MatchNode mnode = makeMatchTree(str);
		return mnode.toMResult();
	}

	private MatchNode makeMatchTree(MString toMatch) {
		MatchNode head = new MatchNode(0, 0, null, null, -1, null);
		makeMatchLink(head, toMatch, 0);
		return head;
	}

	public static QResult match(MString str, MatchableList matches) {
		ListMatchTool i = new ListMatchTool(matches);
		return i.match(str);
	}

	private void makeMatchLink(MatchNode leftNode, MString toMatch,
			int matcherIdx) {
		if (matcherIdx >= this.matches.size()) {
			return;
		}
		QMatchable nextMatcher = this.matches.get(matcherIdx);
		MString subStr = toMatch.substring(leftNode.end());
		QResult mResult = nextMatcher.match(subStr);

		int nextIdx = matcherIdx + 1;
		boolean expChild = false;
		// if (nextMatcher instanceof BooleanAndMatcher) {
		// expChild = true;
		// }
		for (int x = 0; x < mResult.getTotalMatch(); x++) {
			MatchNode mNode;
			if (expChild) {
				mNode = mResult.getMatch(x);
			} else {
				int newMatchLen = mResult.getMatchEnd(x);
				MatchNode subHead = mResult.getMatch(x);
				mNode = new MatchNode(leftNode.end(), newMatchLen, nextMatcher,
						leftNode.m == null ? null : leftNode,
						leftNode.depth + 1, subHead);
			}
			if (nextIdx == this.matches.size()) {
				leftNode.addRightBranch(mNode);
			} else if (nextIdx < this.matches.size()) {
				if (this.matches.canStop(matcherIdx)) {
					MatchNode premature = mNode.copy();
					leftNode.addRightBranch(premature);
				}

				makeMatchLink(mNode, toMatch, nextIdx);
				if (mNode.rightBranchNumber() > 0) {
					leftNode.addRightBranch(mNode);
				} else {
					// not fully match all expression, not collect into list
				}
			} else {
				System.out.println("ERRORFFGHJKL:");
			}
		}

	}
}
