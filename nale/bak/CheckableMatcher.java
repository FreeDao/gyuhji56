//package textMatcher.Pattern.def;
//
//import java.util.ArrayList;
//
//public class CheckableMatcher implements QMatchable {
//	public static enum QMatchType {
//		MUST, NUSTNOT;
//	}
//
//	public static CheckRule getRule(QMatchType type, QMatchable qm) {
//		return new CheckRule(type, qm);
//	}
//
//	public static class CheckRule {
//		QMatchType ismatch;
//		QMatchable matcher;
//
//		public CheckRule(QMatchType ismatch, QMatchable matcher) {
//			this.ismatch = ismatch;
//			this.matcher = matcher;
//		}
//	}
//
//	private ArrayList<CheckRule> checkRules = new ArrayList<CheckRule>();
//
//	public CheckableMatcher(QMatchable qm) {
//
//	}
//
//	public void addCheckRule(QMatchType ismatch, QMatchable matcher) {
//		this.checkRules.add(new CheckRule(ismatch, matcher));
//	}
//
//	@Override
//	public QResult match(String str) {
//		return null;
//	}
//
//	@Override
//	public String defineInfo() {
//		return null;
//	}
//
// }
