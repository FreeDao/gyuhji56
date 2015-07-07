package com.bmtech.nale.script.engine.lang.obj;

import com.bmtech.nale.script.engine.jmo.lang.source.SourcePosition;
import com.bmtech.nale.script.engine.lang.NaleException;

public abstract class LazyNaleObject extends NaleObject {

	public LazyNaleObject(SourcePosition pos) {
		super(pos);
	}

	@Override
	public String toString() {
		return toQMatchable().toString();
	}

	@Override
	public boolean booleanValue() {
		throw new NaleException("ReflectNaleObject isn't boolean value",
				getPos());
	}

}
