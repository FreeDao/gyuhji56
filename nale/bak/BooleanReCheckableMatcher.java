//package textMatcher.Pattern.def;
//
//import java.util.ArrayList;
//
//public class BooleanReCheckableMatcher implements QMatchable{
////	public static enum QMatchIn{
////		FullMatch, contains, startsWith, endWith;
////	}
//	public static enum QMatchType{
//		MUST, NOT;
//		public String defineInfo(){
//			if(this == MUST){
//				return "+";
//			}else{
//				return "-";
//			}
//		}
//	}
//	public static CheckRule getRule(QMatchType type, QMatchable qm){
//		return new CheckRule(type, qm);
//	}
//	
//	public static class CheckRule{
//		//
//		QMatchType matchType;
//		QMatchable matcher;
//		public CheckRule(QMatchType ismatch, QMatchable matcher) {
//			this.matchType = ismatch;
//			this.matcher = matcher;
//		}
//		public Object defineInfo() {
//			return null;
//		}
//	}
//	private ArrayList<CheckRule>checkRules = new ArrayList<CheckRule>();
//	
//	public void addCheckRule(QMatchType ismatch, QMatchable matcher){
//		this.checkRules.add(new CheckRule(ismatch, matcher));
//	}
//	final QMatchable qm;
//	public BooleanReCheckableMatcher(QMatchable qm){
//		this.qm = qm;
//	}
//
//	@Override
//	public QResult match(String str) {
//		QResult qr = this.qm.match(str);
//		for(int x = 0; x < qr.getTotalMatch(); x ++){
//			String sub = str.substring(0, qr.getMatchEnd(x));
//			boolean checkPass = true;
//			for(CheckRule cr : this.checkRules){
//				QResult qrCheck = cr.matcher.match(sub);
//				if(qrCheck.isMatch()){
//					if(QMatchType.MUST != cr.matchType){
//						checkPass = false;
//						break;
//					}
//				}else{
//					if(QMatchType.NOT != cr.matchType){
//						checkPass = false;
//						break;
//					}
//				}
//			}
//			if(!checkPass){
//				qr.removeMatch(x);
//				x --;
//			}
//			
//		}
//		return qr;
//	}
//
//	@Override
//	public String defineInfo() {
//		StringBuilder sb = new StringBuilder();
//		sb.append(qm.defineInfo());
//		for(CheckRule qm2 : this.checkRules){
//			sb.append(qm2.defineInfo());
//			
//		}
//		return "Check(" + sb + ")";
//	}
//
// }
