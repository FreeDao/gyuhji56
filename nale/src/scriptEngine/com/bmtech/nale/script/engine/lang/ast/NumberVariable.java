package com.bmtech.nale.script.engine.lang.ast;

import com.bmtech.nale.script.engine.jmo.lang.source.SourcePosition;
import com.bmtech.nale.script.engine.lang.NaleContext;
import com.bmtech.nale.script.engine.lang.NaleException;
import com.bmtech.nale.script.engine.lang.ast.itf.NaleCopyable;
import com.bmtech.nale.script.engine.lang.ast.itf.NaleNode;
import com.bmtech.nale.script.engine.lang.ast.itf.ReferNode;
import com.bmtech.nale.script.engine.lang.obj.NaleObject;
import com.bmtech.nale.script.engine.lang.obj.NaleObjectFactory;

public class NumberVariable extends NaleNode implements ReferNode {
	public final int number;

	public NumberVariable(int number, SourcePosition position) {
		super(position);
		this.number = number;
	}

	@Override
	public NaleCopyable copyof() {
		return new NumberVariable(number, super.getPosition());
	}

	@Override
	public NaleObject eval(NaleContext context) throws NaleException {
		return NaleObjectFactory.getInstance().byNumber(number,
				super.getPosition());
	}

	@Override
	public String toString() {
		return number + "";
	}

	@Override
	public String getVarName() {
		return "num";
	}
}
