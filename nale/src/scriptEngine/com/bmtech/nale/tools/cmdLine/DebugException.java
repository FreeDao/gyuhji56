package com.bmtech.nale.tools.cmdLine;

import com.bmtech.nale.script.engine.jmo.lang.JmoException;
import com.bmtech.nale.script.engine.jmo.lang.source.SourcePosition;

public class DebugException extends JmoException {

	public DebugException(String message) {
		super(message);
	}

	public DebugException(String message, SourcePosition pos) {
		super(message, pos);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
