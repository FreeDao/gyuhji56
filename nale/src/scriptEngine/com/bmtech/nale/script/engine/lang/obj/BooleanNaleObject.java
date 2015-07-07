package com.bmtech.nale.script.engine.lang.obj;

import com.bmtech.nale.script.engine.jmo.lang.source.SourcePosition;
import com.bmtech.nale.script.engine.lang.NaleException;

import textmatchers.pattern.def.QMatchable;
import textmatchers.pattern.def.reg.SString;

public class BooleanNaleObject extends NaleObject {
	private final boolean bool;

	public BooleanNaleObject(boolean b, SourcePosition pos) {
		super(pos);
		this.bool = b;
	}

	@Override
	public String toString() {
		return Boolean.toString(is());
	}

	@Override
	public QMatchable toQMatchable() throws NaleException {
		return new SString(Boolean.toString(is()));
	}

	public boolean is() {
		return bool;
	}

	@Override
	public boolean booleanValue() {
		return bool;
	}
}
