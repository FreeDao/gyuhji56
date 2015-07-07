package textmatchers.pattern.def;

import java.util.ArrayList;
import java.util.List;


public class QEnumResult extends QResult {

	ArrayList<MatchNode> result = new ArrayList<MatchNode>();

	public QEnumResult(List<QResult> qlst) {
		for (QResult qr : qlst) {
			for (int x = 0; x < qr.getTotalMatch(); x++) {
				MatchNode end = qr.getMatch(x);
				this.addMatch(end);
			}
		}
	}

	public QEnumResult() {
	}

	public void addMatch(MatchNode tail) {
		result.add(tail);
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
		return this.result.get(idx);
	}
}
