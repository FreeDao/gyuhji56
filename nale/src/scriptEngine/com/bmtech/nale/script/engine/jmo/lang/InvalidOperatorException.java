package com.bmtech.nale.script.engine.jmo.lang;

import com.bmtech.nale.script.engine.jmo.lang.source.SourcePosition;

/**
 * The operator is invalid.
 *
 */
public class InvalidOperatorException extends JmoException {
    private static final long serialVersionUID = -57261291654807212L;

    public InvalidOperatorException(SourcePosition pos) {
        super("Invalid operator", pos);
    }
}
