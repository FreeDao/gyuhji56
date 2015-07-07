package com.bmtech.nale.script.engine.lang.obj;

import com.bmtech.nale.script.engine.jmo.lang.source.SourcePosition;
import com.bmtech.nale.script.engine.lang.NaleException;

import textmatchers.pattern.def.QMatchable;
import textmatchers.pattern.def.reg.SString;

public class NumberNaleObject extends NaleObject {
	private final int number;

	public NumberNaleObject(int number, SourcePosition pos) {
		super(pos);
		this.number = number;
	}

	@Override
	public String toString() {
		return Integer.toString(this.number);
	}

	@Override
	public QMatchable toQMatchable() throws NaleException {
		return new SString(number + "");
	}

	@Override
	public boolean booleanValue() {
		return number != 0;
	}

}
