package nale.script.engine.lang.obj;

import nale.script.engine.lang.NaleException;
import textmatchers.pattern.def.MatchNode;
import textmatchers.pattern.def.QPattern;
import textmatchers.pattern.def.qresult.QResult;

import com.funshion.gamma.jmo.lang.source.SourcePosition;

public class QResultNaleNode extends NaleObject {

	private final QResult result;
	private final String toMatch;

	private int matchedIndex = 0;
	SourcePosition pos;

	public QResultNaleNode(QResult result, String toMatch, SourcePosition pos) {
		this.result = result;
		this.pos = pos;
		this.toMatch = toMatch;
		if (this.result.isMatch()) {
			matchedIndex = this.result.getMaxMatchIndex();
		}
	}

	private String g(int index) throws NaleException {
		if (index < 0) {
			throw new NaleException("g" + index
					+ " not allowed! negetive index", pos);
		}
		if (result.isMatch()) {

			if (index == 0) {
				int end = result.getMatchEnd(this.matchedIndex);
				return this.toMatch.substring(0, end);
			}
			MatchNode mn = result.getSubNode(this.matchedIndex, index);
			if (mn == null) {
				return "";
			} else {
				return this.toMatch.substring(mn.offset, mn.end());
			}

		} else {
			return "";
		}
	}

	@Override
	public NaleObject callMember(String varName) throws NaleException {
		if (varName.charAt(0) == 'g') {
			String sub = varName.substring(1);
			Integer index = null;
			try {
				index = Integer.parseInt(sub);
			} catch (Exception e) {
			}
			if (index != null) {
				return new StringNaleObject(g(index));
			}
		}
		return super.callMember(varName);
	}

	@Override
	public QPattern toQPattern() throws NaleException {
		return new QPattern();
	}

	@Override
	public String toString() {
		try {
			return g(0);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
