package com.bmtech.nale.script.engine.lang.regex;

import com.bmtech.nale.script.engine.jmo.lang.source.SourcePosition;
import com.bmtech.nale.script.engine.lang.NaleContext;
import com.bmtech.nale.script.engine.lang.ast.itf.NaleCopyable;

import textmatchers.pattern.def.QMatchable;
import textmatchers.pattern.def.reg.SString;

public class LiteralPartial extends Regex {

	private final String str;

	public LiteralPartial(String str, SourcePosition pos) {
		super(pos);
		this.str = str;
	}

	public String getStr() {
		return str;
	}

	@Override
	public QMatchable toMatcher(NaleContext context, SourcePosition pos) {
		return new SString(str);
	}

	@Override
	public String toString() {
		return str;
	}

	@Override
	public NaleCopyable copyof() {
		LiteralPartial p = new LiteralPartial(this.str, pos);
		return p;
	}
}
