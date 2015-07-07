package com.bmtech.nale.script.engine.lang.ast;

import com.bmtech.nale.script.engine.jmo.lang.source.SourcePosition;
import com.bmtech.nale.script.engine.lang.NaleContext;
import com.bmtech.nale.script.engine.lang.NaleException;
import com.bmtech.nale.script.engine.lang.ast.itf.NaleCopyable;
import com.bmtech.nale.script.engine.lang.ast.itf.ValueNode;
import com.bmtech.nale.script.engine.lang.obj.NaleObject;

public class AssignNode extends AssignableNode {
	ValueNode expression;

	public AssignNode(SourcePosition position, VarNode var, ValueNode expression) {
		super(position, var);
		this.expression = expression;
	}

	@Override
	public String toString() {
		return String.format("%s = %s", getVar(), expression);
	}

	@Override
	public NaleCopyable copyof() {
		return new AssignNode(super.getPosition(), (VarNode) this.getVar()
				.copyof(), (ValueNode) this.expression.copyof());
	}

	@Override
	protected NaleObject evalRight(NaleContext context) throws NaleException {
		return expression.eval(context);
	}

}
