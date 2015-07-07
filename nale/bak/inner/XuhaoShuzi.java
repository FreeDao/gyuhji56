package textMatcher.Pattern.inner;

import textMatcher.Pattern.def.BooleanOrMatcher;
import textMatcher.Pattern.def.QPattern;
import textMatcher.Pattern.def.qresult.QResult;
import textMatcher.Pattern.def.reg.RSZ;

public class XuhaoShuzi extends QPattern{

	@Override
	protected void setMatchers() {
		BooleanOrMatcher orM = new BooleanOrMatcher(
				new RSZ(1, 2),
				new HanziShuzi(1, 3),
				new ALBQuanjiaoShuzi(1, 2)
				);
		addMatcher(orM);
		
	}

	

	public static void main(String[]args){
		String a = "12";
		XuhaoShuzi xs = new XuhaoShuzi(){
			public void setMatchers(){
				super.setMatchers();
//				this.addNotMatcher(new RSZ(1).toQPattern(true));
			}
		};
		xs.setFullMatch(false);
		QResult qr = xs.match(a);
		
		System.out.println(qr.matchInfo(a));
	}

}
