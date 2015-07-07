package com.bmtech.nale.script.engine.jmo.lang;

import com.bmtech.nale.script.engine.jmo.lang.source.SourcePosition;

public class JmoException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public JmoException(String message) {
		super(message);
	}

	public JmoException(String message, SourcePosition position) {
		super(message + " @" + position);
	}

	public String printErrorMsg() {
		String ret = "JmoError: " + this.getMessage();
		System.out.println(ret);
		return ret;
	}

}
