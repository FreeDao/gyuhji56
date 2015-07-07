package com.bmtech.nale.script.engine.lang.ast;

import com.bmtech.nale.script.engine.jmo.lang.source.SourcePosition;
import com.bmtech.nale.script.engine.lang.NaleContext;
import com.bmtech.nale.script.engine.lang.NaleException;
import com.bmtech.nale.script.engine.lang.ast.itf.NaleNode;
import com.bmtech.nale.script.engine.lang.ast.itf.ReferNode;
import com.bmtech.nale.script.engine.lang.obj.NaleObject;

public abstract class AssignableNode extends NaleNode implements ReferNode {
	private final VarNode var;

	public AssignableNode(SourcePosition position, VarNode var) {
		super(position);
		this.var = var;
	}

	@Override
	public NaleObject eval(NaleContext context) throws NaleException {
		NaleObject obj = evalRight(context);
		if (var.getParNode() == null) {
			context.putVar(var.getVarName(), obj, position);
		} else {
			NaleObject obj0 = var.getParNode().eval(context);
			obj0.setMember(var.getVarName(), obj);
		}
		return obj;
	}

	protected abstract NaleObject evalRight(NaleContext context)
			throws NaleException;

	@Override
	public String getVarName() {
		return var.getVarName();
	}

	public VarNode getVar() {
		return var;
	}
}
