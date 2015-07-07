package com.bmtech.nale.script.engine.jmo.lang;

import com.bmtech.nale.script.engine.jmo.lang.source.SourcePosition;

/**
 * An exception that occured in the Lexer stage
 *
 */
public class LexerException extends JmoException {
    private static final long serialVersionUID = -6905527358249165699L;

    public LexerException(String message, SourcePosition position) {
        super(message, position);
    }
}
