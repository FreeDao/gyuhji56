package textmatchers.pattern.def;

import java.util.ArrayList;

public class BooleanMayMatcher implements QMatchable {
	QMatchable matcher;

	public BooleanMayMatcher(QMatchable match) {
		this.matcher = match;
	}

	@Override
	public QResult match(MString str) {

		ArrayList<QResult> rlst = new ArrayList<QResult>();

		{// add non-match
			MatchNode head = new MatchNode(0, 0, null, null, -1, null);
			MatchNode mNode;
			mNode = new MatchNode(head.end(), 0, this, null, head.depth + 1,
					null);
			head.addRightBranch(mNode);
			rlst.add(head.toMResult());
		}

		QResult result = matcher.match(str);
		rlst.add(result);

		QEnumResult cmbResult = new QEnumResult(rlst);
		return cmbResult;
	}

	@Override
	public String toString() {
		return matcher + ".0-1";
	}

}
