package com.bmtech.nale.script.engine.lang.obj;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import com.bmtech.nale.script.engine.jmo.lang.JmoException;
import com.bmtech.nale.script.engine.jmo.lang.source.SourcePosition;
import com.bmtech.nale.script.engine.lang.NaleContext;
import com.bmtech.nale.script.engine.lang.NaleException;
import com.bmtech.nale.script.engine.lang.ast.VarNode;
import com.bmtech.nale.script.engine.lang.ast.itf.ReferNode;

import textmatchers.pattern.def.BooleanOrMatcher;
import textmatchers.pattern.def.MString;
import textmatchers.pattern.def.QMatchable;
import textmatchers.pattern.def.QPattern;
import textmatchers.pattern.def.QResult;
import textmatchers.pattern.def.reg.RCharGroup;
import textmatchers.pattern.def.reg.REnum;

public abstract class NaleObject {

	private final HashMap<String, NaleFunc> map = new HashMap<String, NaleFunc>();
	private final HashMap<String, NaleObject> members = new HashMap<String, NaleObject>();
	private ArrayNaleObject notFilter;
	private final SourcePosition pos;

	public NaleObject(SourcePosition pos) {
		this.pos = pos;
		NaleFunc func = new NotFunction(this, "not");
		addFunction(func);

		this.addFunction(new NaleFunc("cat", this) {

			@Override
			public NaleObject call(VarNode var, List<ReferNode> parasNames,
					List<NaleObject> parasValue, SourcePosition pos,
					NaleContext ctx) throws NaleException {
				StringBuilder sb = new StringBuilder();
				for (NaleObject obj : parasValue) {
					sb.append(obj.toString());
				}
				return new StringNaleObject(sb.toString(), pos);
			}

		});

		this.addFunction(new NaleFunc("replace", this) {

			@Override
			public NaleObject call(VarNode var, List<ReferNode> parasNames,
					List<NaleObject> parasValue, SourcePosition pos,
					NaleContext ctx) throws NaleException {
				return new StringNaleObject(NaleObject.this.toString().replace(
						parasValue.get(0).toString(),
						parasValue.get(1).toString()), pos);
			}

		});

		this.addFunction(new NaleFunc("has", this) {

			@Override
			public NaleObject call(VarNode var, List<ReferNode> parasNames,
					List<NaleObject> parasValue, SourcePosition pos,
					NaleContext ctx) throws NaleException {
				expect(parasValue, 1, pos);
				NaleObject node = parasValue.get(0);
				boolean has = true;
				if (node == null) {
					has = false;
				} else {
					has = node.booleanValue();
				}

				return new BooleanNaleObject(has, pos);
			}

		});

		this.addFunction(new NaleFunc("print", this) {

			@Override
			public NaleObject call(VarNode var, List<ReferNode> parasNames,
					List<NaleObject> parasValue, SourcePosition pos,
					NaleContext ctx) throws NaleException {
				Object name, value;
				if (parasValue.size() == 0) {
					name = var.toString();
					value = NaleObject.this;
				} else {
					name = parasNames.get(0);
					expect(parasValue, 1, pos);
					value = parasValue.get(0);
				}
				String str = name + " = '" + value + "'\t@" + pos;
				System.out.println(str);
				return new StringNaleObject(str, pos);
			}

		});
		this.addFunction(new NaleFunc("chars", this) {

			@Override
			public NaleObject call(VarNode var, List<ReferNode> parasNames,
					List<NaleObject> parasValue, SourcePosition pos,
					NaleContext ctx) throws NaleException {
				expect(parasValue, 1, pos);
				NaleObject v1 = parasValue.get(0);
				final String s1 = v1.toString();
				char[] org = s1.toCharArray();
				char trimed[] = new char[org.length];

				int index = 0;
				for (char c : s1.toCharArray()) {
					if (c == ' ' || c == '\t')
						continue;
					trimed[index++] = c;
				}
				final char[] arr = Arrays.copyOf(trimed, index);

				return new LazyNaleObject(pos) {

					@Override
					public QMatchable toQMatchable() throws NaleException {
						return new REnum(arr);
					}

				};

			}

		});

		this.addFunction(new NaleFunc("range", this) {

			@Override
			public NaleObject call(VarNode var, List<ReferNode> parasNames,
					List<NaleObject> parasValue, SourcePosition pos,
					NaleContext ctx) throws NaleException {
				expect(parasValue, 2, pos);
				NaleObject v1 = parasValue.get(0), v2 = parasValue.get(1);
				final String s1 = v1.toString();
				final String s2 = v2.toString();
				if (s1.length() == 1 && s2.length() == 1) {
					return new LazyNaleObject(pos) {

						@Override
						public QMatchable toQMatchable() throws NaleException {
							return new RCharGroup(1) {

								@Override
								protected char[][] makePattern() {

									return new char[][] { new char[] {
											s1.charAt(0), s2.charAt(0) } };
								}

								@Override
								public String defineInfo() {
									char[] c = makePattern()[0];
									return "range(" + c[0] + "," + c[1] + ")";
								}

							};
						}
					};
				} else {
					throw new NaleException(
							"paras must one word for function range, but they are"
									+ parasValue, pos);
				}
			}

		});

		this.addFunction(new NaleFunc("isMatch", this) {

			@Override
			public NaleObject call(VarNode var, List<ReferNode> parasNames,
					List<NaleObject> parasValue, SourcePosition pos,
					NaleContext ctx) throws NaleException {
				return new BooleanNaleObject(false, pos);
			}

		});

		this.addFunction(new NaleFunc("match", this) {

			@Override
			public NaleObject call(VarNode var, List<ReferNode> parasNames,
					List<NaleObject> parasValue, SourcePosition pos,
					NaleContext ctx) throws NaleException {
				expect(parasValue, 1, pos);
				String toMatch = parasValue.get(0).toString();

				StringMatchedInfo[] qr = scanMatcher(toMatch);
				return new QResultNaleObject(qr, pos);
			}

		});

		this.addFunction(new NaleFunc("pattern", this) {

			@Override
			public NaleObject call(VarNode var, List<ReferNode> parasNames,
					List<NaleObject> parasValue, SourcePosition pos,
					NaleContext ctx) throws NaleException {
				expect(parasValue, 0, pos);

				return new StringNaleObject(toQMatchable().toString(), pos);
			}

		});

	}

	public void addFunction(NaleFunc func) {
		map.put(func.getName(), func);
	}

	public NaleObject call(VarNode var, String funcName, List<ReferNode> paras,
			List<NaleObject> parasValue, SourcePosition pos, NaleContext ctx)
			throws NaleException {
		NaleFunc func = map.get(funcName);
		if (func == null) {
			throw new NaleException("no function named '" + funcName + "'", pos);
		}
		NaleObject ret = func.call(var, paras, parasValue, pos, ctx);

		return ret;
	}

	protected final ArrayNaleObject getNotFilter() {
		if (this.notFilter == null) {
			this.notFilter = new ArrayNaleObject(new ArrayList<QMatchable>(2),
					pos);
		}
		return this.notFilter;
	}

	public NaleObject callMember(String memberName, SourcePosition pos)
			throws NaleException {
		if (memberName.equalsIgnoreCase("not")) {
			return this.getNotFilter();
		} else if (memberName.equalsIgnoreCase("members")
				|| memberName.equalsIgnoreCase("member")) {

			StringBuilder sb = new StringBuilder();
			sb.append(this);
			sb.append("\nnot = ");
			sb.append(this.getNotFilter());
			for (Entry<String, NaleObject> e : members.entrySet()) {
				sb.append("\n");
				sb.append(e.getKey() + " = ");
				sb.append(e.getValue());
			}
			return new StringNaleObject(sb.toString(), pos);
		} else {
			NaleObject obj = members.get(memberName);
			if (obj == null) {
				return new StringNaleObject("", pos);
			} else {
				return obj;
			}
		}
	}

	public abstract QMatchable toQMatchable() throws NaleException;

	public StringMatchedInfo[] scanMatcher(String str) {
		QPattern qp = this.toPattern();
		ArrayList<StringMatchedInfo> qrList = new ArrayList<StringMatchedInfo>();
		MString mstrBasic = new MString(str);
		for (int x = 0; x < str.length(); x++) {
			MString mstr = mstrBasic.substring(x);
			QResult qr = qp.match(mstr);
			if (qr.isMatch()) {
				StringMatchedInfo mi = new StringMatchedInfo(qr, mstr);
				qrList.add(mi);
			}
		}
		StringMatchedInfo ret[] = new StringMatchedInfo[qrList.size()];
		qrList.toArray(ret);
		return ret;
	}

	private QPattern toPattern() {
		QMatchable matcher = this.toQMatchable();
		QPattern qp = new QPattern(matcher);
		ArrayNaleObject not = getNotFilter();

		if (not.size() > 0) {
			NaleObject[] objs = not.toArray();

			QMatchable qms[] = new QMatchable[objs.length];
			for (int x = 0; x < objs.length; x++) {
				qms[x] = objs[x].toQMatchable();
			}

			BooleanOrMatcher brm = new BooleanOrMatcher(qms);
			qp.addNotMatcher(brm);
		}
		return qp;
	}

	public QResult match(String str) throws NaleException {
		QPattern qp = toPattern();
		return qp.match(new MString(str, 0));
	}

	public NaleObject setMember(String name, NaleObject obj) {
		if (name.equals("not")) {
			throw new JmoException(
					"set 'not' member is forbidden! you can call obj.not(xx) to add a not filter");
		}
		return members.put(name, obj);
	}

	public Set<Entry<String, NaleObject>> listMembers() {
		return members.entrySet();
	}

	public void print() {
		System.out.println(this);
	}

	public static Integer number(String var) {
		try {
			return Integer.parseInt(var);
		} catch (Exception e) {
		}
		return null;
	}

	public SourcePosition getPos() {
		return pos;
	}

	public abstract boolean booleanValue();
}
