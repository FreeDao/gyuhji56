package com.bmtech.nale.script.engine.lang.obj;

import java.util.ArrayList;
import java.util.List;

import com.bmtech.nale.script.engine.jmo.lang.source.SourcePosition;
import com.bmtech.nale.script.engine.lang.NaleException;

import textmatchers.pattern.def.QMatchable;
import textmatchers.pattern.def.QResult;

public class QResultNaleObject extends NaleObject {

	private final StringMatchedInfo[] infos;

	public QResultNaleObject(StringMatchedInfo[] infos, SourcePosition pos) {
		super(pos);
		this.infos = infos;
	}

	/**
	 * 
	 * @param index
	 *            matched index
	 * @return
	 * @throws NaleException
	 */
	private List<String> matched(int index, SourcePosition pos)
			throws NaleException {
		if (index <= 0) {
			throw new NaleException("matched" + index
					+ " not allowed! negetive index", pos);
		}
		index--;
		List<String> ret = new ArrayList<String>(infos.length);
		if (index >= infos.length) {
			return ret;
		}

		QResult result = infos[index].qr;
		String toMatch = infos[index].toMatch.getStr();
		int subMatchedIndex = result.getSubNodeNumber();

		for (int x = 0; x < subMatchedIndex; x++) {
			String r = result.getSubMatched(x, toMatch);

			ret.add(r);
		}

		return ret;
	}

	/**
	 * 
	 * @param matcherIdx
	 *            matcher index
	 * @return
	 * @throws NaleException
	 */
	private List<String> groups(int matcherIdx, SourcePosition pos)
			throws NaleException {
		if (matcherIdx <= 0) {
			throw new NaleException("g" + matcherIdx
					+ " not allowed! negetive index", getPos());
		}

		List<String> ret = new ArrayList<String>(infos.length);
		matcherIdx--;
		for (int x = 0; x < infos.length; x++) {
			QResult result = infos[x].qr;
			if (matcherIdx >= result.getSubNodeNumber()) {
				ret.add("");
			} else {

				String r = result.getSubMatched(matcherIdx, infos[x].toMatch
						.getStr());
				ret.add(r);
			}
		}
		return ret;
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		for (int x = 0; x < infos.length; x++) {
			if (sb.length() > 0) {
				sb.append('\n');
			}
			QResult result = infos[x].qr;

			String toMatch = infos[x].toMatch.getStr();
			int end = result.getMatchEnd();
			String r = toMatch.substring(0, end);
			sb.append(r);

		}
		return sb.toString();

	}

	@Override
	public NaleObject callMember(String funcName, SourcePosition pos)
			throws NaleException {
		Integer index = null;
		if (funcName.charAt(0) == 'g') {
			String sub = funcName.substring(1);
			index = number(sub);// Integer.parseInt(sub);
			if (index != null) {
				return new ArrayNaleObject(groups(index, pos), pos);

			}
		} else {
			index = number(funcName);
			if (index != null) {
				return new ArrayNaleObject(matched(index, pos), pos);
			}
		}
		if (funcName.equals("size")) {
			return new NumberNaleObject(this.infos.length, pos);
		}
		return super.callMember(funcName, pos);
	}

	@Override
	public QMatchable toQMatchable() throws NaleException {
		throw new NaleException("QResult can not match anything", this.getPos());
	}

	@Override
	public boolean booleanValue() {
		return infos.length > 0;
	}

}
