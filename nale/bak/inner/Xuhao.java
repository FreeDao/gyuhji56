package textMatcher.Pattern.inner;

import textMatcher.Pattern.def.QPattern;
import textMatcher.Pattern.def.reg.REnum;

public class Xuhao extends QPattern{

	@Override
	protected void setMatchers() {
		//序号数字【. , 、】
		XuhaoShuzi xhsz = new XuhaoShuzi();
		REnum biaodan = new REnum(0, 1, 
				new char[]{
				'.',
				'、',
		});
		this.addMatcher(xhsz);
		this.addMatcher(biaodan);
	}

}
