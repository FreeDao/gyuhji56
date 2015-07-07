//package textmatchers.pattern.def.addon;
//
//import textmatchers.pattern.def.BooleanOrMatcher;
//import textmatchers.pattern.def.MString;
//import textmatchers.pattern.def.QMatchable;
//import textmatchers.pattern.def.QResult;
//import textmatchers.pattern.def.reg.RAny;
//
//public class QAnyOrNothigMatcher implements QMatchable {
//	BooleanOrMatcher mt = new BooleanOrMatcher(new RAny(), new QEnd());
//
//	@Override
//	public QResult match(MString str) {
//		return mt.match(str);
//	}
//
//	@Override
//	public String toString() {
//		return "@" + this.getClass().getName();
//	}
// }
