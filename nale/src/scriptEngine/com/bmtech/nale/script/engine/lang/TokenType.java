package com.bmtech.nale.script.engine.lang;

/**
 * The type of token
 * 
 */
public enum TokenType {
	// \t\r\n=.+-*/%^(){}[],:;!&|<>
	COMMENT("/*...*/"), ASSIGN("="), DOT("."),
	// PLUS("+"),
	MINUS("-"),
	// MULTIPLY(// "*"), DIVIDE("/"), MOD("%"), POWER("^"), // Math operators
	LPAREN("("), RPAREN(")"), // 小括号
	LBRACE("{"), RBRACE("}"), // 大括号
	DOLLAR("$"), SHARP("#"),
	// LBRACKET("["), RBRACKET("]"), // 中括号
	COMMA(","),
	// COLON(":"), SEMICOLON(";"), NOT("!"), AND("&"),
	OR("|"), //
	// Boolean
	// operators
	// LESS_THEN("<"),
	// LESS_EQUAL(">"),
	// EQUAL("=="),
	// GREATER_EQUAL(">="),
	// GREATER_THEN(
	// "<="), NOT_EQUAL("!="), // Comparison operators
	// STRING_LITERAL("str"),
	TRUE("true"), FALSE("false"), // Constant
	// types
	IF("if"), FI("fi"),
	// , ELSE("else"), WHILE("while"),
	// FOR_EACH("foreach"), AS("as"), // Control
	// structures
	VARIABLE("variable"), FUNCTION("function"), RETURN("return"), EXIT("exit"), BREAK(
			"break"), CONTINUE("continue"), CATCH("catch"), EQ("eq"), AT("@"), WHITE(
			" "), EXPORT("exp"), DEBUG("debug"), echo("echo");

	public final String str;

	private TokenType(String str) {
		this.str = str;
	}

	@Override
	public String toString() {
		return this.name();
	}
}
