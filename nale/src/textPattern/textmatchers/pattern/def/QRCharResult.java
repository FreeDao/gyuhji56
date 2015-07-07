package textmatchers.pattern.def;

import java.util.ArrayList;

import textmatchers.pattern.def.reg.RCharInner;

public class QRCharResult extends QResult {
	ArrayList<Integer> result = new ArrayList<Integer>();
	public RCharInner matcher;

	public QRCharResult(RCharInner matcher) {
		this.matcher = matcher;
	}

	public void addMatch(int end) {
		result.add(end);
	}

	@Override
	public int getTotalMatch() {
		return this.result.size();
	}

	@Override
	public void removeMatch(int idx) {
		this.result.remove(idx);
	}

	@Override
	public MatchNode getMatch(int idx) {
		return new MatchNode(0, result.get(idx), this.matcher, null, 0, null);
	}
}
