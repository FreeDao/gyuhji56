package com.bmtech.nale.script.engine.lang;

import java.util.LinkedList;

import com.bmtech.nale.script.engine.jmo.lang.source.SourcePosition;

/**
 * Buffer of Tokens. Used to provide lookahead into the stream from the lexer.
 * Also filters out comment tokens.
 *
 */
public class TokenBuffer {
	private LinkedList<Token> tokenQueue;
	private Lexer lexer;
	private Token last;

	public TokenBuffer(Lexer lexer) {
		this.lexer = lexer;
		tokenQueue = new LinkedList<Token>();

		// init queue
		for (;;) {
			Token token = nextToken();
			if (token == null) {
				break;
			}
			tokenQueue.addLast(token);
		}
	}

	private Token nextToken() {
		Token token = lexer.getNextToken();
		while (token != null && token.getType() == TokenType.COMMENT) {
			token = lexer.getNextToken();
		}
		return token;
	}

	public boolean isEmpty() {
		return tokenQueue.isEmpty();
	}

	public int size() {
		return tokenQueue.size();
	}

	public Token getToken(int i) {
		return i >= this.size() ? null : tokenQueue.get(i);
	}

	/**
	 * Read the next token from the lexer
	 */
	public Token takeToken() {
		if (tokenQueue.isEmpty()) {
			return null;
		}
		Token token = tokenQueue.removeFirst();
		this.last = token;
		// Add another token to the queue
		Token newToken = nextToken();
		if (newToken != null) {
			tokenQueue.addLast(newToken);
		}
		return token;
	}

	/**
	 * find valid token including current node
	 * 
	 * @return
	 */
	public Token findValid() {
		while (true) {
			Token t = this.getToken(0);
			if (t == null) {
				return null;
			}
			if (t.getType() == TokenType.WHITE) {
				this.takeToken();
				continue;
			}
			return t;
		}
	}

	public SourcePosition currentPosition() {
		if (last == null) {
			if (this.tokenQueue == null || this.tokenQueue.size() == 0) {
				return null;
			}
			return this.tokenQueue.get(0).getPosition();
		}

		return last.getPosition();
	}

	@Override
	public String toString() {
		return "TokenBuffer:" + this.tokenQueue;
	}
}
