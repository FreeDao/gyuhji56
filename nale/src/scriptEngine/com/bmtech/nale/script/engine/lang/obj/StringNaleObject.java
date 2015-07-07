package com.bmtech.nale.script.engine.lang.obj;

import java.util.List;

import com.bmtech.nale.script.engine.jmo.lang.source.SourcePosition;
import com.bmtech.nale.script.engine.lang.NaleContext;
import com.bmtech.nale.script.engine.lang.NaleException;
import com.bmtech.nale.script.engine.lang.ast.VarNode;
import com.bmtech.nale.script.engine.lang.ast.itf.ReferNode;

import textmatchers.pattern.def.QMatchable;
import textmatchers.pattern.def.reg.SString;

public class StringNaleObject extends NaleObject {
	public static StringNaleObject emptyString(SourcePosition pos) {
		return new StringNaleObject("", pos);
	}

	private final String str;

	public StringNaleObject(String str, SourcePosition pos) {
		super(pos);
		this.str = str;
		this.addFunction(new NaleFunc("replace", this) {

			@Override
			public NaleObject call(VarNode var, List<ReferNode> parasNames,
					List<NaleObject> parasValue, SourcePosition pos,
					NaleContext ctx) throws NaleException {
				return new StringNaleObject(StringNaleObject.this.str.replace(
						parasValue.get(0).toString(), parasValue.get(1)
								.toString()), pos);
			}

		});
	}

	@Override
	public QMatchable toQMatchable() throws NaleException {
		return new SString(str);
	}

	@Override
	public String toString() {
		return str;
	}

	@Override
	public boolean booleanValue() {
		return str.length() > 0;
	}
}
