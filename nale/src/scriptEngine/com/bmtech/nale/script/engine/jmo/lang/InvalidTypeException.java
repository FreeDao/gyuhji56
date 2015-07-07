package com.bmtech.nale.script.engine.jmo.lang;

import com.bmtech.nale.script.engine.jmo.lang.source.SourcePosition;

/**
 * Type is invalid.
 *
 */
public class InvalidTypeException extends JmoException {
    private static final long serialVersionUID = 9115378805326306069L;

    public InvalidTypeException(String message, SourcePosition position) {
        super(message, position);
    }
}
