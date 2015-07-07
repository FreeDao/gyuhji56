package com.bmtech.nale.script.engine.lang.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.bmtech.nale.script.engine.jmo.lang.source.SourcePosition;
import com.bmtech.nale.script.engine.lang.NaleContext;
import com.bmtech.nale.script.engine.lang.NaleException;
import com.bmtech.nale.script.engine.lang.ast.itf.NaleCopyable;
import com.bmtech.nale.script.engine.lang.ast.itf.NaleNode;
import com.bmtech.nale.script.engine.lang.ast.itf.ReferNode;
import com.bmtech.nale.script.engine.lang.ast.itf.ValueNode;
import com.bmtech.nale.script.engine.lang.obj.NaleObject;

public class FunctionCallNode extends NaleNode implements ValueNode {

	final List<ReferNode> paras;
	final String funcName;
	final VarNode var;
	SourcePosition pos;

	public FunctionCallNode(SourcePosition position, VarNode varNode,
			String funcName, List<ReferNode> paras) {
		super(position);
		this.var = varNode;
		this.funcName = funcName;
		this.paras = Collections.unmodifiableList(paras);
		this.pos = position;
	}

	@Override
	public NaleObject eval(NaleContext context) throws NaleException {

		NaleObject obj;
		if (var == null) {
			obj = context.getVar("env", pos);
		} else {
			obj = var.eval(context);
		}

		List<NaleObject> parasValue = new ArrayList<NaleObject>(this.paras
				.size());
		for (int x = 0; x < this.paras.size(); x++) {
			NaleObject value = paras.get(x).eval(context);
			parasValue.add(value);
		}
		return obj.call(var, this.funcName, paras, parasValue, pos, context);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (ValueNode vn : paras) {
			if (sb.length() != 0) {
				sb.append(", ");
			}
			sb.append(vn);
		}
		return String.format("%s.%s(%s)", var, funcName, sb);
	}

	@Override
	public NaleCopyable copyof() {
		VarNode varObject = null;
		if (this.var != null) {
			varObject = (VarNode) this.var.copyof();
		}
		List<ReferNode> lst = new ArrayList<ReferNode>();
		for (ValueNode var : paras) {
			lst.add((ReferNode) var.copyof());
		}
		return new FunctionCallNode(super.getPosition(), varObject,
				this.funcName, lst);
	}
}
