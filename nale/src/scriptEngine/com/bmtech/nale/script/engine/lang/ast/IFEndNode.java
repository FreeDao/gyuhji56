package com.bmtech.nale.script.engine.lang.ast;

import com.bmtech.nale.script.engine.jmo.lang.source.SourcePosition;
import com.bmtech.nale.script.engine.lang.NaleContext;
import com.bmtech.nale.script.engine.lang.NaleException;
import com.bmtech.nale.script.engine.lang.ast.itf.NaleCopyable;
import com.bmtech.nale.script.engine.lang.ast.itf.NaleNode;
import com.bmtech.nale.script.engine.lang.obj.NaleObject;

public class IFEndNode extends NaleNode {

	public IFEndNode(SourcePosition position) {
		super(position);
	}

	@Override
	public NaleCopyable copyof() {
		return new IFEndNode(getPosition());
	}

	@Override
	public NaleObject eval(NaleContext context) throws NaleException {
		return null;
	}

}
