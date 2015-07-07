package com.bmtech.nale.script.engine.lang;

import java.io.File;
import java.io.IOException;

import com.bmtech.nale.script.engine.jmo.lang.LexerException;
import com.bmtech.nale.script.engine.jmo.lang.source.CombinReader;
import com.bmtech.nale.script.engine.jmo.lang.source.SourcePosition;

/**
 * The <a href="http://en.wikipedia.org/wiki/Lexical_analysis#Scanner">lexer</a>
 * is used to read characters and identify tokens and pass them to the parser
 * 
 * TODO 未定TOKEN 均被看做token；+操作重载，只有参与各方均可以为数字时进行加法运算，否则看做字符串拼接操作 TODO 变量使用$前缀
 * 
 */
public class Lexer extends NaleLexer {

	public Lexer(CombinReader in, int lineNo) throws IOException {
		super(in, lineNo);
	}

	public Token getNextToken() {
		int character = lookAhead(1);

		switch (character) {
		case END_OF_FILE: {
			close();
			return null;
		}
		case '/': {
			int char2 = lookAhead(2);
			if (char2 == '/') {
				return matchLineComment();
			} else if (char2 == '*') {
				return matchBlockComment();
			} else {
				return matchVariables();
			}
		}

		case '{': {
			return createToken(TokenType.LBRACE, '{');
		}
		case '}': {
			return createToken(TokenType.RBRACE, '}');
		}
		case '(': {
			return createToken(TokenType.LPAREN, '(');
		}
		case ')': {
			return createToken(TokenType.RPAREN, ')');
		}
		case ',': {
			return createToken(TokenType.COMMA, ',');
		}
		case '|': {
			return createToken(TokenType.OR, '|');
		}
		case '$': {
			return createToken(TokenType.DOLLAR, '$');
		}
		case '=': {
			return createToken(TokenType.ASSIGN, '=');
		}
		case '-': {
			return createToken(TokenType.MINUS, '-');
		}
		case '@': {
			return createToken(TokenType.AT, '@');
		}
		case '.': {
			return createToken(TokenType.DOT, '.');
		}
		case '#': {
			return createToken(TokenType.SHARP, '#');
		}
		default: {
			return matchVariables();
		}
		}
	}

	private Token matchLineComment() {
		SourcePosition pos = getRelativeSourcePosition();
		match("//");
		StringBuilder sb = new StringBuilder();
		int character = lookAhead(1);
		while (character != '\r' && character != '\n'
				&& character != END_OF_FILE) {
			sb.append((char) character);
			character = next();
		}
		return new Token(pos, TokenType.COMMENT, sb.toString());
	}

	private Token matchBlockComment() {
		SourcePosition pos = getRelativeSourcePosition();
		match("/*");
		StringBuilder sb = new StringBuilder();
		int character = lookAhead(1);
		while (true) {
			if (character == END_OF_FILE) {
				throw new LexerException("Expecting */ but found end of file",
						getRelativeSourcePosition());
			}
			if (lookAhead(1) == '*' && lookAhead(2) == '/') {
				break;
			}
			sb.append((char) character);
			character = next();
		}
		match("*/");
		return new Token(pos, TokenType.COMMENT, sb.toString());
	}

	/**
	 * An identifier is either a keyword, function, or variable
	 * 
	 * @return Token
	 */
	private Token matchVariables() {
		SourcePosition pos = getRelativeSourcePosition();
		StringBuilder sb = new StringBuilder();
		int character = lookAhead(1);
		do {
			if (character == '\\') {

				int cx = escape(lookAhead(2));
				if (cx != 0) {
					next();
					sb.append((char) cx);
					character = cx;
				} else {
					sb.append((char) character);
				}
			} else {
				sb.append((char) character);
			}
			next();

			if (isSegmentCharactor((char) character)) {
				break;
			}
			character = lookAhead(1);
			if (character == -1 || isSegmentCharactor((char) character)) {
				break;
			}
		} while (true);
		String word = sb.toString();
		// System.out.println(word);
		if (word.length() == 1) {
			char c = word.charAt(0);
			if (c == ' ' || c == '\t' || c == '\n') {
				return new Token(pos, TokenType.WHITE, word);
			}
		}
		if (word.equals("if")) {
			return new Token(pos, TokenType.IF, word);
		} else if (word.equals("fi")) {
			return new Token(pos, TokenType.FI, word);
		} else if (word.equals("true")) {
			return new Token(pos, TokenType.TRUE, word);
		} else if (word.equals("false")) {
			return new Token(pos, TokenType.FALSE, word);
		} else if (word.equals("exp")) {
			return new Token(pos, TokenType.EXPORT, word);
		} else if (word.equals("debug")) {
			return new Token(pos, TokenType.DEBUG, word);
		} else if (word.equals("echo")) {
			return new Token(pos, TokenType.echo, word);
		} else {
			return new Token(pos, TokenType.VARIABLE, word);
		}
	}

	private int escape(int lookAhead) {
		if (lookAhead == '$') {
			return '$';
		} else if (lookAhead == '{') {
			return '{';
		}
		return 0;
	}

	public static void main(String[] args) throws IOException {
		CombinReader cr = new CombinReader();
		// cr.addSource(new File("./config/nale/basic.nale"));
		cr.addSource(new File("./config/nale/example.nale"));

		Lexer lex = new Lexer(cr, 1);

		while (true) {
			Token t = lex.getNextToken();
			if (t == null) {
				break;
			}
			System.out.println("\t" + t.getText());
		}
	}
}
