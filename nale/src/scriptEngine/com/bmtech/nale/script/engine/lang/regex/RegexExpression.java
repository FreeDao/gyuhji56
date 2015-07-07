package com.bmtech.nale.script.engine.lang.regex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.bmtech.nale.script.engine.jmo.lang.source.SourcePosition;
import com.bmtech.nale.script.engine.lang.NaleContext;
import com.bmtech.nale.script.engine.lang.NaleException;
import com.bmtech.nale.script.engine.lang.ast.itf.NaleCopyable;

import textmatchers.pattern.def.BooleanAndMatcher;
import textmatchers.pattern.def.BooleanOrMatcher;
import textmatchers.pattern.def.QMatchable;

public class RegexExpression extends Regex {
	final List<Regex[]> regs;

	public RegexExpression(List<Regex[]> regs, SourcePosition pos) {
		super(pos);
		this.regs = Collections.unmodifiableList(regs);
	}

	@Override
	public QMatchable toMatcher(NaleContext cont, SourcePosition pos)
			throws NaleException {
		QMatchable[] mx = new QMatchable[regs.size()];

		for (int x = 0; x < mx.length; x++) {
			Regex ex[] = regs.get(x);
			QMatchable[] m = new QMatchable[ex.length];
			for (int y = 0; y < ex.length; y++) {
				m[y] = ex[y].toMatcher(cont, pos);
			}
			if (m.length == 1) {
				mx[x] = m[0];
			} else {
				BooleanAndMatcher am = new BooleanAndMatcher(m);
				mx[x] = am;
			}
		}
		if (mx.length == 1) {
			return mx[0];
		} else {
			return new BooleanOrMatcher(mx);
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		boolean needOr = false;
		for (Regex x[] : regs) {
			if (needOr) {
				sb.append("|");
			}
			needOr = true;
			for (Regex re : x) {
				sb.append(re);
			}

		}
		return sb.toString();
	}

	public int size() {
		return this.regs.size();
	}

	@Override
	public NaleCopyable copyof() {
		List<Regex[]> regs = new ArrayList<Regex[]>(this.regs.size());
		for (Regex[] r : this.regs) {
			Regex[] rx = new Regex[r.length];
			for (int x = 0; x < r.length; x++) {
				rx[x] = (Regex) r[x].copyof();
			}
			regs.add(rx);
		}
		return new RegexExpression(regs, pos);
	}
}
