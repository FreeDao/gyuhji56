package com.bmtech.nale.script.engine.lang.regex;

import com.bmtech.nale.script.engine.jmo.lang.source.SourcePosition;
import com.bmtech.nale.script.engine.lang.NaleContext;
import com.bmtech.nale.script.engine.lang.NaleException;
import com.bmtech.nale.script.engine.lang.ast.itf.NaleCopyable;

import textmatchers.pattern.def.QMatchable;

public abstract class Regex implements NaleCopyable {
	protected SourcePosition pos;

	public Regex(SourcePosition pos) {
		this.pos = pos;
	}

	public abstract QMatchable toMatcher(NaleContext context, SourcePosition pos)
			throws NaleException;
}