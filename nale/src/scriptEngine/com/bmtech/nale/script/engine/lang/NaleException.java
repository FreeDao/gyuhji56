package com.bmtech.nale.script.engine.lang;

import com.bmtech.nale.script.engine.jmo.lang.JmoException;
import com.bmtech.nale.script.engine.jmo.lang.source.SourcePosition;

public class NaleException extends JmoException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NaleException(String message, SourcePosition position) {
		super(message, position);
	}

	public NaleException(Exception e, String string, SourcePosition pos) {
		super(e.getMessage() + ":" + string, pos);
	}
}
