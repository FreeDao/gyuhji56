//package textMatcher.Pattern.def;
//
//import java.util.ArrayList;
//
//public abstract class ExplainableRule extends Rule {
//
//	protected final ExplainMetaName metaNames[];
//	protected final ArrayList<Integer> goodLevelExp = new ArrayList<Integer>();
//
//	protected ExplainableRule(String name, ExplainMetaName metaNames[]) {
//		super(name);
//		this.metaNames = metaNames;
//		for (int x = 0; x < metaNames.length; x++) {
//			if (metaNames[x].metaLevel > 0) {
//				goodLevelExp.add(x);
//			} else {
//				continue;
//			}
//		}
//	}
//
//	public ExplainMeta explainResult(QResult qr, int matchedIdx, int metaIdx,
//			String toMatch) {
//		String matched = qr.getMatched(matchedIdx, metaIdx, toMatch);
//		ExplainMeta em = metaNames[metaIdx].toArtMeta(matched);
//
//		return em;
//	}
//
//	public int getTotalMeta() {
//		return goodLevelExp.size();
//	}
//
//	public ExplainMetaName getGoodLevelExp(int index) {
//		return this.metaNames[this.goodLevelExp.get(index)];
//	}
//
//	@Override
//	public QResult match(String toMatch) {
//		QResult qr = super.match(toMatch);
//		return qr;
//	}
//
//	public ExplainResult getExplainResult(String toMatch) {
//		QResult qr = match(toMatch);
//
//		ExplainResult er = new ExplainResult(toMatch);
//		for (int x = 0; x < qr.getTotalMatch(); x++) {
//			for (int y : this.goodLevelExp) {
//
//				ExplainMeta meta = explainResult(qr, x, y, toMatch);
//				er.addMatch(meta);
//			}
//		}
//		return er;
//	}
// }
