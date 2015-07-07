package com.bmtech.nale.script.engine.lang.ast;

import com.bmtech.nale.script.engine.jmo.lang.source.SourcePosition;
import com.bmtech.nale.script.engine.lang.NaleContext;
import com.bmtech.nale.script.engine.lang.NaleException;
import com.bmtech.nale.script.engine.lang.ast.itf.NaleCopyable;
import com.bmtech.nale.script.engine.lang.ast.itf.NaleNode;
import com.bmtech.nale.script.engine.lang.obj.NaleObject;

public class IFNode extends NaleNode {
	final NaleNode var;
	private BlockNode body;

	public IFNode(NaleNode var, SourcePosition position) {
		super(position);
		this.var = var;
	}

	@Override
	public NaleCopyable copyof() {
		IFNode ifnd = new IFNode((NaleNode) var.copyof(), getPosition());
		if (body != null)
			ifnd.body = (BlockNode) body.copyof();
		return ifnd;
	}

	@Override
	public NaleObject eval(NaleContext context) throws NaleException {
		NaleObject obj = var.eval(context);
		if (obj.booleanValue()) {
			return this.body.eval(context);
		}
		return obj;
	}

	public void setBody(BlockNode ifBody) {
		this.body = ifBody;
	}

}
