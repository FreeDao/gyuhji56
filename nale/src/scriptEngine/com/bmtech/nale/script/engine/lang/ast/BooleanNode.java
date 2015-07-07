package com.bmtech.nale.script.engine.lang.ast;

import com.bmtech.nale.script.engine.jmo.lang.source.SourcePosition;
import com.bmtech.nale.script.engine.lang.NaleContext;
import com.bmtech.nale.script.engine.lang.NaleException;
import com.bmtech.nale.script.engine.lang.ast.itf.NaleCopyable;
import com.bmtech.nale.script.engine.lang.ast.itf.NaleNode;
import com.bmtech.nale.script.engine.lang.ast.itf.ValueNode;
import com.bmtech.nale.script.engine.lang.obj.BooleanNaleObject;
import com.bmtech.nale.script.engine.lang.obj.NaleObject;

public class BooleanNode extends NaleNode implements ValueNode {
	final boolean bool;

	public BooleanNode(boolean bool, SourcePosition position) {
		super(position);
		this.bool = bool;
	}

	@Override
	public NaleCopyable copyof() {
		return new BooleanNode(bool, this.getPosition());
	}

	@Override
	public NaleObject eval(NaleContext context) throws NaleException {

		return new BooleanNaleObject(bool, this.getPosition());
	}

	@Override
	public String toString() {
		return String.format(Boolean.toString(bool));
	}
}
