package com.bmtech.nale.script.engine.lang.ast;

import com.bmtech.nale.script.engine.jmo.lang.source.SourcePosition;
import com.bmtech.nale.script.engine.lang.NaleContext;
import com.bmtech.nale.script.engine.lang.NaleException;
import com.bmtech.nale.script.engine.lang.ast.itf.NaleCopyable;
import com.bmtech.nale.script.engine.lang.ast.itf.NaleNode;
import com.bmtech.nale.script.engine.lang.ast.itf.ReferNode;
import com.bmtech.nale.script.engine.lang.obj.NaleObject;

public class ExpNode extends NaleNode {
	final ReferNode var;

	public ExpNode(SourcePosition position, ReferNode var) {
		super(position);
		this.var = var;
	}

	@Override
	public NaleCopyable copyof() {
		return new ExpNode(this.getPosition(), (ReferNode) var.copyof());
	}

	@Override
	public NaleObject eval(NaleContext context) throws NaleException {
		NaleObject obj = var.eval(context);

		context.export(this.var.getVarName(), obj);
		return obj;
	}

	@Override
	public String toString() {
		return String.format("exp %s", var);
	}
}
