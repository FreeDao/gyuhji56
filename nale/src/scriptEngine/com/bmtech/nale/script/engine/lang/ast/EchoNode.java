package com.bmtech.nale.script.engine.lang.ast;

import com.bmtech.nale.script.engine.jmo.lang.source.SourcePosition;
import com.bmtech.nale.script.engine.lang.NaleContext;
import com.bmtech.nale.script.engine.lang.NaleException;
import com.bmtech.nale.script.engine.lang.ast.itf.NaleCopyable;
import com.bmtech.nale.script.engine.lang.ast.itf.NaleNode;
import com.bmtech.nale.script.engine.lang.obj.NaleObject;

public class EchoNode extends NaleNode {

	final NaleNode node;

	public EchoNode(NaleNode node, SourcePosition position) {
		super(position);
		this.node = node;
	}

	@Override
	public NaleCopyable copyof() {
		return new EchoNode(node, this.getPosition());
	}

	@Override
	public NaleObject eval(NaleContext context) throws NaleException {

		NaleObject obj = this.node.eval(context);
		String str = obj.toString() + "'\t@" + this.getPosition();
		System.out.println("\nECHO: '" + str);

		return obj;
	}
}
