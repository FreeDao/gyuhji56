package com.bmtech.nale.script.engine.lang.ast;

import com.bmtech.nale.script.engine.jmo.lang.source.SourcePosition;
import com.bmtech.nale.script.engine.lang.NaleContext;
import com.bmtech.nale.script.engine.lang.NaleException;
import com.bmtech.nale.script.engine.lang.ast.itf.NaleCopyable;
import com.bmtech.nale.script.engine.lang.ast.itf.NaleNode;
import com.bmtech.nale.script.engine.lang.ast.itf.ReferNode;
import com.bmtech.nale.script.engine.lang.obj.NaleObject;

public class VarNode extends NaleNode implements ReferNode {
	private final String varName;
	private final VarNode parNode;

	public VarNode(SourcePosition position, String varName) {
		this(position, null, varName);
	}

	public VarNode(SourcePosition position, VarNode varNode, String member) {
		super(position);
		this.parNode = varNode;
		this.varName = member;
	}

	@Override
	public NaleObject eval(NaleContext context) throws NaleException {
		if (this.parNode == null) {
			return context.getVar(varName, this.getPosition());
		} else {
			NaleObject obj = this.parNode.eval(context);
			return obj.callMember(varName, super.getPosition());
		}

	}

	@Override
	public String getVarName() {
		return varName;
	}

	@Override
	public String toString() {
		if (parNode == null) {
			return String.format("$%s", varName);
		} else {
			return String.format("%s.%s", parNode, varName);
		}
	}

	@Override
	public NaleCopyable copyof() {
		VarNode par = null;
		if (this.parNode != null) {
			par = (VarNode) this.parNode.copyof();
		}
		VarNode copy = new VarNode(super.getPosition(), par, this.varName);
		return copy;
	}

	public VarNode getParNode() {
		return parNode;
	}

}
