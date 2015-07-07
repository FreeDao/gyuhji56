package com.bmtech.nale.script.engine.lang.ast;

import java.util.Collections;
import java.util.List;

import com.bmtech.nale.script.engine.jmo.lang.source.SourcePosition;
import com.bmtech.nale.script.engine.lang.NaleContext;
import com.bmtech.nale.script.engine.lang.ast.itf.NaleCopyable;
import com.bmtech.nale.script.engine.lang.ast.itf.NaleNode;
import com.bmtech.nale.script.engine.lang.obj.NaleObject;
import com.bmtech.nale.script.engine.lang.obj.StringNaleObject;

public class BlockNode extends NaleNode {

	List<NaleNode> nodes;

	public BlockNode(List<NaleNode> nodes, SourcePosition pos) {
		super(pos);
		this.nodes = Collections.unmodifiableList(nodes);
	}

	public NaleObject eval(NaleContext context) {
		NaleObject obj = null;
		for (NaleNode n : nodes) {
			obj = n.eval(context);
		}
		if (obj == null) {
			obj = StringNaleObject.emptyString(getPosition());
		}
		return obj;
	}

	@Override
	public NaleCopyable copyof() {
		return new BlockNode(nodes, getPosition());
	}
}
