package com.bmtech.nale.script.engine.lang.obj;

import com.bmtech.nale.script.engine.jmo.lang.JmoException;

import textmatchers.pattern.def.MString;
import textmatchers.pattern.def.QResult;

public class StringMatchedInfo {
	final QResult qr;
	final MString toMatch;

	StringMatchedInfo(QResult qr, MString mstr) {
		this.qr = qr;
		this.toMatch = mstr;
	}

	public String matchedString() {
		return qr.matchedResult(toMatch.getStr());
	}

	public String matchedString(int group) {
		int subNumber = qr.getSubNodeNumber();
		if (group >= subNumber) {
			throw new JmoException("group number is too big:" + group
					+ ", only has " + subNumber + "subnodes ");
		}
		return qr.getSubMatched(group, toMatch.getStr());
	}
}