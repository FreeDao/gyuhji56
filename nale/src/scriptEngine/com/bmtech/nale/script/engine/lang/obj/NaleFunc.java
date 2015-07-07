package com.bmtech.nale.script.engine.lang.obj;

import java.util.List;

import com.bmtech.nale.script.engine.jmo.lang.source.SourcePosition;
import com.bmtech.nale.script.engine.lang.NaleContext;
import com.bmtech.nale.script.engine.lang.NaleException;
import com.bmtech.nale.script.engine.lang.ast.VarNode;
import com.bmtech.nale.script.engine.lang.ast.itf.ReferNode;

public abstract class NaleFunc {
	private final String name;
	protected NaleObject obj;

	public NaleFunc(String name, NaleObject obj) {
		this.name = name;
		this.obj = obj;
	}

	public abstract NaleObject call(VarNode var, List<ReferNode> parasNames,
			List<NaleObject> parasValue, SourcePosition pos, NaleContext ctx)
			throws NaleException;

	public String getName() {
		return name;
	}

	protected void expect(List<NaleObject> parasValue, int i, SourcePosition pos)
			throws NaleException {
		if (parasValue.size() != i) {
			throw new NaleException("expect " + i + " paras, but get "
					+ parasValue, pos);
		}

	}

}
