package com.bmtech.nale.script.engine.lang.obj;

import com.bmtech.nale.script.engine.jmo.lang.source.SourcePosition;
import com.bmtech.nale.script.engine.lang.NaleException;

import textmatchers.pattern.def.QMatchable;

public class MatchableNaleObject extends NaleObject {
	final QMatchable mat;

	public MatchableNaleObject(QMatchable mat, SourcePosition pos) {
		super(pos);
		this.mat = mat;
	}

	@Override
	public String toString() {
		return mat.toString();
	}

	@Override
	public QMatchable toQMatchable() throws NaleException {
		return mat;
	}

	@Override
	public boolean booleanValue() {
		throw new NaleException("Match expression isn't boolean value: '" + mat
				+ "'", getPos());
	}

}
