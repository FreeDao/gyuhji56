package textMatcher.Pattern.tools;

import java.util.ArrayList;

import textMatcher.Pattern.def.BooleanAndMatcher;
import textMatcher.Pattern.def.BooleanOrMatcher;
import textMatcher.Pattern.def.QPattern;
import textMatcher.Pattern.def.qresult.QResult;
import textMatcher.Pattern.def.reg.NotRchar;
import textMatcher.Pattern.def.reg.RChar;
import textMatcher.Pattern.def.reg.RKB;
import textMatcher.Pattern.def.reg.RSZ;
import textMatcher.Pattern.inner.Xuhao;
import textMatcher.Pattern.inner.Youkuohao;
import textMatcher.Pattern.inner.Zuokuohao;

import com.bmtech.utils.Consoler;

public class InnerXuhao extends QPattern{

	@Override
	protected void setMatchers() {
		BooleanAndMatcher am1 = new BooleanAndMatcher(
				new RKB(1, 1),
				new Xuhao(),
				new NotRchar(2, 15, new RSZ()),
				new RChar(':'));

		BooleanAndMatcher am2 = new BooleanAndMatcher(
				new Zuokuohao(1, 1),
				new Xuhao(),
				new Youkuohao(1, 1),
				new NotRchar(2, 15, new RSZ()),
				new RChar(':'));
		BooleanOrMatcher om = new BooleanOrMatcher(am2, am1);

		addMatcher(om);
		
	}

	public void split(String str){
		int end = str.length() - 3;
		ArrayList<Integer>rs = new ArrayList<Integer>();
		rs.add(0);
		for(int x = 2; x < end; x ++){
			if(str.charAt(x) == ' '){
				String sub = str.substring(x);
//				System.out.println(sub);
				QResult qr = this.match(sub);
				if(qr.isMatch()){
					System.out.println(qr.getMatched(0, 0, sub));
//					System.out.println(qr.getSubMatch(0, 1, sub));
					rs.add(x);
					x = x + qr.getMatchEnd(0);

					x --;
				}
			}
		}
		if(rs.size() > 1){
			System.out.println("ORG:" + str);
			for(int x = 1; x < rs.size(); x ++){
				System.out.println("SPL:" + str.substring(rs.get(x - 1), rs.get(x)));
			}
			if(rs.get(rs.size() - 1) < str.length()){
				System.out.println("SPL:" + str.substring(rs.get(rs.size() - 1)));
			}
			Consoler.readString(":");
		}
	}
}
