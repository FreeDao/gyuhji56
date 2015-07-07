package com.bmtech.nale.script.engine.jmo.lang;

import com.bmtech.nale.script.engine.jmo.lang.source.SourcePosition;

/**
 * An exception that occurred in the parser stage
 *
 */
public class ParseException extends JmoException {
	private static final long serialVersionUID = 7505060960165209530L;

	public ParseException(String message) {
		super(message);
	}

	public ParseException(String message, SourcePosition position) {
		super(message, position);
	}
}
