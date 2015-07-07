package com.bmtech.nale.script.engine.lang;

import java.io.IOException;

import com.bmtech.nale.script.engine.jmo.lang.LexerException;
import com.bmtech.nale.script.engine.jmo.lang.source.CombinReader;
import com.bmtech.nale.script.engine.jmo.lang.source.PeekReader;
import com.bmtech.nale.script.engine.jmo.lang.source.SourcePosition;

/**
 * The <a href="http://en.wikipedia.org/wiki/Lexical_analysis#Scanner">lexer</a>
 * is used to read characters and identify tokens and pass them to the parser
 */
public class NaleLexer {
	static final protected int END_OF_FILE = -1;
	public static String escaped = " \t\r\n=.+-*/%^(){}[],:;!&|<>$@";
	private final int lineNo;
	private int columnNo = 1;
	private PeekReader in;

	public NaleLexer(CombinReader in, int lineNo) throws IOException {
		this.in = new PeekReader(in, 2);
		this.lineNo = lineNo;
	}

	protected int lookAhead(int i) {
		return in.peek(i);
	}

	protected int read() {
		try {
			int c = in.read();
			// if (c == '\n') {
			// lineNo++;
			// columnNo = 0;
			// }
			columnNo++;
			return c;
		} catch (IOException e) {
			throw new LexerException(e.getMessage(),
					in.getRelativeSourcePosition(lineNo, columnNo));
		}
	}

	protected void close() {
		try {
			in.close();
		} catch (IOException e) {
		}
	}

	protected int next() {
		read();
		return lookAhead(1);
	}

	protected char match(char c) {
		int input = read();
		if (input != c) {
			String inputChar = (input != END_OF_FILE) ? "" + (char) input
					: "END_OF_FILE";
			throw new LexerException("Expected '" + c + "' but got '"
					+ inputChar + "'", in.getRelativeSourcePosition(lineNo,
					columnNo));
		}
		return c;
	}

	protected String match(String str) {
		for (int i = 0; i < str.length(); i++) {
			match(str.charAt(i));
		}
		return str;
	}

	protected Token createToken(TokenType type, char c) {
		SourcePosition pos = in.getRelativeSourcePosition(lineNo, columnNo);
		match(c);
		return new Token(pos, type, "" + c);
	}

	protected Token createToken(TokenType type, String str) {
		SourcePosition pos = in.getRelativeSourcePosition(lineNo, columnNo);
		match(str);
		return new Token(pos, type, str);
	}

	protected boolean isSegmentCharactor(char next) {
		if (escaped.indexOf(next) == -1) {
			return false;
		}
		return true;
	}

	protected SourcePosition getRelativeSourcePosition() {
		return in.getRelativeSourcePosition(lineNo, columnNo);
	}
}
