package com.bmtech.nale.script.engine.lang.ast;

import com.bmtech.nale.script.engine.jmo.lang.source.SourcePosition;
import com.bmtech.nale.script.engine.lang.NaleContext;
import com.bmtech.nale.script.engine.lang.NaleException;
import com.bmtech.nale.script.engine.lang.ast.itf.NaleCopyable;
import com.bmtech.nale.script.engine.lang.ast.itf.NaleNode;
import com.bmtech.nale.script.engine.lang.debug.NaleDebugger;
import com.bmtech.nale.script.engine.lang.obj.NaleObject;
import com.bmtech.nale.script.engine.lang.obj.StringNaleObject;
import com.bmtech.utils.log.LogHelper;

public class DebuggerNode extends NaleNode {

	public DebuggerNode(SourcePosition position) {
		super(position);
	}

	@Override
	public NaleCopyable copyof() {
		return new DebuggerNode(this.getPosition());
	}

	@Override
	public NaleObject eval(NaleContext context) throws NaleException {
		NaleDebugger deb = context.getDebugger();
		if (deb == null) {
			LogHelper.log.warn("can not find debugger, skip debug at %s", super
					.getPosition());
			return new StringNaleObject("debugger not found finished",
					getPosition());
		}
		System.out.println("\n     <<<DEBUG......@" + this.getPosition()
				+ ">>>");
		try {
			deb.run();
		} catch (Exception e) {
			throw new NaleException(e, "debug executor fail", getPosition());
		}
		return new StringNaleObject("debug finished", getPosition());
	}

	@Override
	public String toString() {
		return String.format("debug at %s", getPosition());
	}
}
