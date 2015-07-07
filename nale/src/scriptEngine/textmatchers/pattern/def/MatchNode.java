package textmatchers.pattern.def;

import java.util.ArrayList;


public class MatchNode {
	public final MatchNode leftNode;
	private final MatchNode subNode;
	public final int offset;
	public final int matchLen;
	public final QMatchable m;
	public final int depth;
	private final ArrayList<MatchNode> rightBranchs = new ArrayList<MatchNode>();

	public void addRightBranch(MatchNode m) {
		this.rightBranchs.add(m);
	}

	public MatchNode(int offset, int matchLen, QMatchable m,
			MatchNode leftNode, int depth, MatchNode subNode) {
		this.offset = offset;
		this.matchLen = matchLen;
		this.m = m;
		this.leftNode = leftNode;
		this.depth = depth;
		this.subNode = subNode;
	}

	public int rightBranchNumber() {
		return this.rightBranchs.size();
	}

	public QEnumResult toMResult() {
		ArrayList<MatchNode> tailLst = new ArrayList<MatchNode>();
		for (MatchNode mNode : rightBranchs) {
			collectTail(tailLst, mNode);
		}
		QEnumResult qResult = new QEnumResult();
		for (MatchNode x : tailLst) {
			qResult.addMatch(x);
		}
		return qResult;
	}

	void collectTail(ArrayList<MatchNode> tailLst, MatchNode node) {
		if (node.rightBranchs.size() == 0) {
			tailLst.add(node);
		} else {
			for (MatchNode next : node.rightBranchs) {
				collectTail(tailLst, next);
			}
		}
	}

	public Integer end() {
		return offset + matchLen;
	}

	@Override
	public String toString() {
		return this.m + "[" + offset + "," + matchLen + "]";
	}

	public MatchNode getSubHead() {
		return subNode;
	}

	public MatchNode copy() {
		return new MatchNode(this.offset, this.matchLen, this.m, this.leftNode,
				this.depth, this.subNode);
	}

}