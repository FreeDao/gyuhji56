package textmatchers.pattern.def.qresult;

import java.util.ArrayList;
import java.util.List;

import textmatchers.pattern.def.MatchNode;

public class CombinResult extends QResult {
	ArrayList<MatchNode> result = new ArrayList<MatchNode>();

	public CombinResult(List<QResult> qlst) {
		for (QResult qr : qlst) {
			for (int x = 0; x < qr.getTotalMatch(); x++) {
				MatchNode end = qr.getMatch(x);
				result.add(end);
			}
		}
	}

	@Override
	public int getTotalMatch() {
		return result.size();
	}

	@Override
	public void removeMatch(int idx) {
		this.result.remove(idx);
	}

	@Override
	public MatchNode getMatch(int idx) {
		return this.result.get(idx);
	}
}
