package com.bmtech.nale.script.engine.lang.ast;

import com.bmtech.nale.script.engine.jmo.lang.JmoException;
import com.bmtech.nale.script.engine.jmo.lang.ParseException;
import com.bmtech.nale.script.engine.jmo.lang.source.SourcePosition;
import com.bmtech.nale.script.engine.lang.NaleContext;
import com.bmtech.nale.script.engine.lang.NaleException;
import com.bmtech.nale.script.engine.lang.ast.itf.NaleCopyableNode;
import com.bmtech.nale.script.engine.lang.obj.NaleObject;
import com.bmtech.nale.script.engine.lang.obj.NaleObjectFactory;

public class AssignAtNode extends AssignableNode {

	final Class<?> filterClass;

	public AssignAtNode(SourcePosition position, VarNode varNode, String nextStr)
			throws JmoException {
		super(position, varNode);
		try {
			filterClass = Class.forName(nextStr);
		} catch (Exception e) {
			throw new ParseException("undefined class by " + nextStr, position);
		}

	}

	@Override
	public NaleObject evalRight(NaleContext context) throws NaleException {
		return NaleObjectFactory.getInstance().byClass(this.filterClass,
				getVar().toString(), super.getPosition());

	}

	@Override
	public String toString() {
		return String.format("%s = @ %s", getVar(), filterClass.getName());
	}

	@Override
	public NaleCopyableNode copyof() {
		return new AssignAtNode(this.getPosition(), this.getVar(),
				this.filterClass.getName());
	}
}
