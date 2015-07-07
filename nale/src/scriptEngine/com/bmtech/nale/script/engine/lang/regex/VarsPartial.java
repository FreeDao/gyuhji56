package com.bmtech.nale.script.engine.lang.regex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.bmtech.nale.script.engine.jmo.lang.JmoException;
import com.bmtech.nale.script.engine.jmo.lang.ParseException;
import com.bmtech.nale.script.engine.jmo.lang.source.SourcePosition;
import com.bmtech.nale.script.engine.lang.NaleContext;
import com.bmtech.nale.script.engine.lang.NaleException;
import com.bmtech.nale.script.engine.lang.ast.VarNode;
import com.bmtech.nale.script.engine.lang.ast.itf.NaleCopyable;
import com.bmtech.nale.script.engine.lang.obj.NaleObject;

import textmatchers.pattern.def.BooleanMayMatcher;
import textmatchers.pattern.def.BooleanOrMatcher;
import textmatchers.pattern.def.BooleanRepeatMatcher;
import textmatchers.pattern.def.QMatchable;
import textmatchers.pattern.def.reg.RCharInner;

public class VarsPartial extends Regex {
	public VarsPartial(SourcePosition pos) {
		super(pos);
	}

	class RepeatPattern implements NaleCopyable {
		final VarNode var;
		final int min, max;

		private RepeatPattern(VarNode var, int min, int max) {
			this.var = var;
			this.min = min;
			this.max = max;
		}

		RepeatPattern(SourcePosition pos, VarNode var, int min, int max)
				throws JmoException {
			this.var = var;

			if (min == -1) {
				if (max == -1) {
					min = 1;
					max = 1;
				} else {
					throw new ParseException("max is not -1 for " + var, pos);
				}
			}

			if (max == -1) {
				max = min;
			} else {
				if (max < min) {
					throw new ParseException("max < min, for " + var, pos);
				}
			}

			this.min = min;
			this.max = max;

		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(var);
			if (min != -1) {
				sb.append('.');
				sb.append(min);
				if (max != -1) {
					sb.append('-');
					if (max != Integer.MAX_VALUE) {
						sb.append(max);
					}
				}
			}
			return sb.toString();
		}

		@Override
		public NaleCopyable copyof() {
			VarNode var = (VarNode) this.var.copyof();
			RepeatPattern rp = new RepeatPattern(var, this.min, this.max);
			return rp;
		}
	}

	List<RepeatPattern> vars = new ArrayList<RepeatPattern>(3);

	public void addVar(SourcePosition pos, VarNode vn, int min, int max)
			throws JmoException {
		vars.add(new RepeatPattern(pos, vn, min, max));
	}

	@Override
	public QMatchable toMatcher(NaleContext context, SourcePosition pos)
			throws NaleException {
		QMatchable ms[] = new QMatchable[vars.size()];
		try {
			for (int x = 0; x < vars.size(); x++) {
				RepeatPattern rp = vars.get(x);
				NaleObject obj = rp.var.eval(context);// context.getVar(rp.var);
				QMatchable mat = obj.toQMatchable();

				if (rp.min < -1) {
					throw new NaleException("min/max error! " + rp.toString(),
							pos);
				} else if (rp.min == -1) {
					if (rp.max == -1) {
						ms[x] = mat;
					} else {
						throw new NaleException("min/max error! "
								+ rp.toString(), pos);
					}
				} else if (rp.min == 0) {
					if (rp.max == -1 || rp.max == 0) {// FIXME forbidden!!
						throw new NaleException(
								"not implements forbidden pattern " + rp, pos);
					} else {
						if (mat instanceof RCharInner) {
							((RCharInner) mat).setRange(0, rp.max);
							ms[x] = mat;
						} else {
							BooleanMayMatcher mm = new BooleanMayMatcher(mat);
							if (rp.max == 1) {
								ms[x] = mm;
							} else {
								QMatchable qm = new BooleanRepeatMatcher(mat,
										1, rp.max);

								ms[x] = new BooleanOrMatcher(mm, qm);
							}
						}
					}
				} else {
					if (rp.min == rp.max && rp.max == 1) {
						ms[x] = mat;
					} else {
						if (mat instanceof RCharInner) {
							((RCharInner) mat).setRange(rp.min, rp.max);
							ms[x] = mat;
						} else {
							ms[x] = new BooleanRepeatMatcher(mat, rp.min,
									rp.max);
						}
					}
				}
			}
		} catch (Exception e) {
			throw new NaleException("error:" + e.getMessage(), pos);
		}
		if (ms.length == 0) {
			throw new NaleException("there is no matcher here! ", pos);
		} else if (ms.length == 1) {
			return ms[0];
		} else {
			BooleanOrMatcher m = new BooleanOrMatcher(ms);
			return m;
		}

	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append('{');
		boolean needOr = false;
		for (RepeatPattern x : vars) {
			if (needOr) {
				sb.append(" | ");
			}
			needOr = true;
			sb.append(x);

		}
		sb.append('}');
		return sb.toString();
	}

	@Override
	public NaleCopyable copyof() {
		VarsPartial exp = new VarsPartial(pos);
		List<RepeatPattern> vars = new ArrayList<RepeatPattern>(this.vars
				.size());
		for (RepeatPattern p : this.vars) {
			vars.add((RepeatPattern) p.copyof());
		}
		exp.vars = Collections.unmodifiableList(vars);
		return exp;
	}
}
