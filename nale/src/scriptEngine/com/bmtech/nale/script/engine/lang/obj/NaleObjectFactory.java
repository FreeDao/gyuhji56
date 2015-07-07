package com.bmtech.nale.script.engine.lang.obj;

import com.bmtech.nale.script.engine.jmo.lang.source.SourcePosition;

import textmatchers.pattern.def.QMatchable;
import textmatchers.pattern.def.reg.SString;

public class NaleObjectFactory {
	private static NaleObjectFactory instance = new NaleObjectFactory();

	public NaleObject byClass(Class<?> filterClass, String alia,
			SourcePosition pos) {
		return new ClazzNaleObject(filterClass, alia, pos);
	}

	public NaleObject byNumber(int number, SourcePosition pos) {
		return new NumberNaleObject(number, pos);
	}

	public static NaleObjectFactory getInstance() {
		return instance;
	}

	public NaleObject byExpression(QMatchable mat, SourcePosition pos) {
		if (mat instanceof SString) {
			return new StringNaleObject(mat.toString(), pos);
		}
		return new MatchableNaleObject(mat, pos);
	}
}
