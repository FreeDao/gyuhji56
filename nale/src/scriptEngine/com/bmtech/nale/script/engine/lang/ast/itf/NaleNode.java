package com.bmtech.nale.script.engine.lang.ast.itf;

import com.bmtech.nale.script.engine.jmo.lang.source.SourcePosition;

/**
 * Node in the abstract syntax tree.
 * 
 */
public abstract class NaleNode implements NaleCopyableNode {
	protected final SourcePosition position;

	public NaleNode(SourcePosition position) {
		this.position = position;
	}

	public SourcePosition getPosition() {
		return position;
	}

}
