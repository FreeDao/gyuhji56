package textmatchers.pattern.def.reg;

import textmatchers.pattern.def.MString;
import textmatchers.pattern.def.MatchNode;
import textmatchers.pattern.def.QMatchable;
import textmatchers.pattern.def.QResult;

public class LineStart implements QMatchable {

	@Override
	public QResult match(MString str) {
		MatchNode head = new MatchNode(0, 0, null, null, -1, null);

		if (str.lineOffset == 0) {

			MatchNode mNode;
			mNode = new MatchNode(head.end(), 0, this, null, head.depth + 1,
					null);
			head.addRightBranch(mNode);

		}
		return head.toMResult();
	}

	@Override
	public String toString() {
		return "$STARTL";
	}
}
