package com.bmtech.nale.script.engine.lang;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import com.bmtech.nale.script.engine.jmo.lang.JmoException;
import com.bmtech.nale.script.engine.jmo.lang.ParseException;
import com.bmtech.nale.script.engine.jmo.lang.source.CombinReader;
import com.bmtech.nale.script.engine.jmo.lang.source.SourcePosition;
import com.bmtech.nale.script.engine.jmo.lang.source.SourceReader;
import com.bmtech.nale.script.engine.lang.ast.AssignAtNode;
import com.bmtech.nale.script.engine.lang.ast.AssignNode;
import com.bmtech.nale.script.engine.lang.ast.AssignableNode;
import com.bmtech.nale.script.engine.lang.ast.BooleanNode;
import com.bmtech.nale.script.engine.lang.ast.DebuggerNode;
import com.bmtech.nale.script.engine.lang.ast.EchoNode;
import com.bmtech.nale.script.engine.lang.ast.ExpNode;
import com.bmtech.nale.script.engine.lang.ast.FunctionCallNode;
import com.bmtech.nale.script.engine.lang.ast.IFEndNode;
import com.bmtech.nale.script.engine.lang.ast.IFNode;
import com.bmtech.nale.script.engine.lang.ast.RegexExpressionNode;
import com.bmtech.nale.script.engine.lang.ast.VarNode;
import com.bmtech.nale.script.engine.lang.ast.itf.NaleNode;
import com.bmtech.nale.script.engine.lang.ast.itf.ReferNode;
import com.bmtech.nale.script.engine.lang.ast.itf.ValueNode;
import com.bmtech.nale.script.engine.lang.regex.LiteralPartial;
import com.bmtech.nale.script.engine.lang.regex.Regex;
import com.bmtech.nale.script.engine.lang.regex.RegexExpression;
import com.bmtech.nale.script.engine.lang.regex.VarsPartial;

public abstract class LineParser0 {

	Lexer lexer;
	private final int lineNo;
	final TokenBuffer buf;
	private final String sourceName;

	public LineParser0(String sourceName, int lineNo, String line)
			throws NaleException {
		this.sourceName = sourceName;
		try {
			line = line.trim();
			SourceReader sr = new SourceReader(sourceName, new StringReader(
					line));
			CombinReader cr = new CombinReader();
			cr.addSource(sr);
			this.lineNo = lineNo;
			lexer = new Lexer(cr, lineNo);
			buf = new TokenBuffer(lexer);
		} catch (Exception exp) {
			exp.printStackTrace();
			throw new NaleException(exp.getMessage(), new SourcePosition(
					sourceName, lineNo, 0));
		}

	}

	public NaleNode parse() throws JmoException {
		NaleNode node = statement0();
		return node;
	}

	private NaleNode assign(VarNode varNode, boolean isSharp)
			throws JmoException {

		Token next = buf.findValid();
		checkType(next, null);
		if (isSharp) {
			if (next.getType() == TokenType.AT) {
				buf.takeToken();
				String nextStr = collectToEnd();
				return new AssignAtNode(buf.currentPosition(), varNode, nextStr);
			} else {
				RegexExpressionNode expression = regexNode(next.getPosition());
				return new AssignNode(buf.currentPosition(), varNode,
						expression);
			}
		} else {
			NaleNode node = expression(varNode);
			if (node instanceof ValueNode) {
				return new AssignNode(buf.currentPosition(), varNode,
						(ValueNode) node);
			} else {
				throw new RuntimeException("not value node "
						+ buf.currentPosition());
			}

		}
	}

	protected abstract NaleNode expression(VarNode var);

	protected NaleNode parseVariable() throws JmoException {
		VarNode varNode = varNode();
		while (true) {
			Token t = buf.findValid();
			if (t == null) {
				return varNode;
			}
			if (t.getType() == TokenType.LPAREN) {// function
				expectValid(TokenType.LPAREN);
				List<ReferNode> paras = funcParasNode();
				expectValid(TokenType.RPAREN);
				FunctionCallNode fncN = new FunctionCallNode(t.getPosition(),
						varNode.getParNode(), varNode.getVarName(), paras);
				expectLineEnd();
				return fncN;
			} else if (t.getType() == TokenType.DOT) {
				buf.takeToken();
				VarNode varsub = varNode();
				varNode = new VarNode(varNode.getPosition(), varNode, varsub
						.getVarName());
			} else {

				return varNode;
			}
		}
	}

	private void expectLineEnd() throws JmoException {
		Token t = buf.getToken(0);
		if (t != null) {
			throw new ParseException("expect to lineEnd but get token " + t, t
					.getPosition());
		}
	}

	private void expectEnd() {
		Token tx = buf.findValid();

		if (tx != null) {
			throw new JmoException(
					"expect EOF, but not expected token after debug " + tx, tx
							.getPosition());
		}
	}

	private NaleNode statement0() throws JmoException {

		Token t = buf.findValid();
		if (t == null) {
			return null;
		}
		if (t.getType() == TokenType.IF) {
			buf.takeToken();
			Token tx = buf.findValid();
			this.expectNotNull(tx);
			NaleNode nq;
			if (tx.getType() == TokenType.TRUE) {
				nq = new BooleanNode(true, tx.getPosition());
				buf.takeToken();
			} else if (tx.getType() == TokenType.FALSE) {
				nq = new BooleanNode(false, tx.getPosition());
				buf.takeToken();
			} else {
				nq = parseVariable();
			}
			this.expectLineEnd();
			return new IFNode(nq, t.getPosition());
		} else if (t.getType() == TokenType.FI) {
			NaleNode node = new IFEndNode(t.getPosition());
			buf.takeToken();
			expectEnd();
			return node;
		} else if (t.getType() == TokenType.DEBUG) {
			buf.takeToken();
			expectEnd();
			return new DebuggerNode(t.getPosition());

		} else if (t.getType() == TokenType.EXPORT) {
			buf.takeToken();
			NaleNode nn = statement();
			if (nn instanceof VarNode) {

				VarNode vn = (VarNode) nn;
				if (vn.getParNode() != null) {
					throw new ParseException(
							"can not export varNode with member:" + vn, t
									.getPosition());
				}
				return new ExpNode(t.getPosition(), (VarNode) nn);
			} else if (nn instanceof AssignableNode) {
				return new ExpNode(t.getPosition(), (AssignableNode) nn);
			} else {
				throw new ParseException("can not export non-varNode! " + nn, t
						.getPosition());
			}
		} else if (t.getType() == TokenType.echo) {
			buf.takeToken();
			Token n = buf.findValid();
			this.expectNotNull(n);
			NaleNode node = regexNode(t.getPosition());
			return new EchoNode(node, t.getPosition());
		} else {
			return statement();
		}
	}

	private NaleNode statement() throws JmoException {

		Token t = buf.findValid();
		checkType(t, null);
		if (t.getType() == TokenType.SHARP) {
			buf.takeToken();
			t = buf.findValid();
			checkType(t, TokenType.VARIABLE);

			NaleNode nn = parseVariable();
			if (nn instanceof VarNode) {
				buf.findValid();
				expect(TokenType.ASSIGN);

				t = buf.findValid();
				checkType(t, null);
				return this.assign((VarNode) nn, true);

			} else {
				throw new ParseException("expect $ or exp but get " + nn, t
						.getPosition());
			}

		} else {
			if (t.getType() == TokenType.DOLLAR) {
				buf.takeToken();
			}
			t = buf.findValid();
			checkType(t, null);
			if (t.getType() == TokenType.VARIABLE) {
				NaleNode nn = parseVariable();
				if (nn instanceof VarNode) {
					Token next = buf.findValid();
					if (next == null) {
						return new FunctionCallNode(t.getPosition(),
								(VarNode) nn, "print",
								new ArrayList<ReferNode>(0));
					} else {
						expect(TokenType.ASSIGN);

						t = buf.findValid();
						checkType(t, null);
						return this.assign((VarNode) nn, false);
					}
				} else if (nn instanceof FunctionCallNode) {
					return nn;
				} else {
					throw new ParseException("expect $ or exp but get " + nn, t
							.getPosition());
				}
			} else {
				throw new ParseException("expect variable, but get " + t, t
						.getPosition());
			}
		}

	}

	private String readUntil(TokenType... stopTypes) {
		StringBuilder sb = new StringBuilder();
		while (true) {
			Token t = buf.getToken(0);
			if (t == null) {
				break;
			}
			boolean shouldStop = false;
			for (TokenType type : stopTypes) {
				if (t.getType() == type) {
					shouldStop = true;
				}
			}
			if (shouldStop) {
				break;
			}
			buf.takeToken();
			sb.append(t.getText());
		}
		return sb.toString();
	}

	private Regex varPartial() throws JmoException {
		VarsPartial varRegex = new VarsPartial(buf.currentPosition());
		boolean hasNext = true;
		while (true) {
			Token next = buf.getToken(0);
			if (next == null) {
				throw new RuntimeException("expect } at line end");
			}
			if (next.getType() == TokenType.RBRACE) {
				break;
			} else if (next.getType() == TokenType.OR) {
				buf.takeToken();
				continue;
			} else if (next.getType() == TokenType.WHITE) {
				buf.takeToken();
				continue;
			} else {
				if (next.getType() == TokenType.DOLLAR) {
					buf.takeToken();
					next = buf.findValid();
				}

				VarNode vn = this.varNode();
				Token expNode = buf.findValid();
				if (expNode == null) {
					throw new ParseException("'}' NOT found", next
							.getPosition());
				}
				// var.0 不允许是
				// var.0- may
				// var.1- no less than
				// var.2-4 [2,4]个
				int min = -1, max = -1;
				if (expNode.getType() == TokenType.DOT) {
					buf.takeToken();
					Token expNum = buf.findValid();

					try {
						min = Integer.parseInt(expNum.getText());
					} catch (Exception e) {
						throw new ParseException(
								"not a valid number min repeat number for "
										+ vn + ", token is '" + expNum + "'",
								expNum.getPosition());
					}
					buf.takeToken();

					Token expMinus = buf.findValid();
					if (expMinus.getType() == TokenType.MINUS) {
						buf.takeToken();
						Token expMax = buf.findValid();
						if (expMax.getType() == TokenType.RBRACE
								|| expMax.getType() == TokenType.OR) {
							max = Integer.MAX_VALUE;
						} else {
							try {
								max = Integer.parseInt(expMax.getText());
							} catch (Exception e) {
								throw new ParseException(
										"not a valid number max repeat number for "
												+ vn + ", token is '" + expNum
												+ "'", expMax.getPosition());
							}
							buf.takeToken();
						}

					}

				}

				varRegex.addVar(expNode.getPosition(), vn, min, max);
				hasNext = false;
			}
		}
		if (hasNext) {
			throw new ParseException("expect regex expression, but not found",
					buf.currentPosition());
		}
		return varRegex;
	}

	private List<Regex> regexPartial(SourcePosition pos) throws JmoException {
		List<Regex> regexList = new ArrayList<Regex>();
		while (true) {
			Token t = buf.getToken(0);
			if (t == null) {
				break;
			}
			if (t.getType() == TokenType.LBRACE) {
				// { found
				buf.takeToken();
				Regex rx = varPartial();
				expect(TokenType.RBRACE);
				regexList.add(rx);
			} else {
				String str = this.readUntil(TokenType.OR, TokenType.LBRACE);
				Regex literal = new LiteralPartial(str, pos);
				regexList.add(literal);

				Token current = buf.findValid();
				if (current == null || current.getType() == TokenType.OR) {
					break;
				}
			}
		}
		return regexList;
	}

	private RegexExpressionNode regexNode(SourcePosition position)
			throws JmoException {
		List<Regex[]> regs = new ArrayList<Regex[]>();
		while (true) {
			Token t = buf.getToken(0);
			if (t == null) {
				break;
			}
			if (t.getType() == TokenType.OR) {
				if (regs.size() == 0) {
					throw new ParseException("'|' must NOT be first element",
							buf.findValid().getPosition());
				}
				buf.takeToken();
			}
			List<Regex> rx = this.regexPartial(buf.currentPosition());
			if (rx.size() == 0) {
				throw new ParseException("'|' has no succeed token", buf
						.currentPosition());
			}
			regs.add(rx.toArray(new Regex[rx.size()]));
		}
		RegexExpression or = new RegexExpression(regs, position);
		return new RegexExpressionNode(position, or);
	}

	protected abstract List<ReferNode> funcParasNode() throws JmoException;

	private String collectToEnd() {
		StringBuilder sb = new StringBuilder();
		buf.findValid();
		do {
			Token t = buf.getToken(0);
			if (t == null)
				break;
			sb.append(t.getText());

		} while (buf.takeToken() != null);
		return sb.toString();
	}

	private void expectValid(TokenType type) {
		buf.findValid();
		expect(type);
	}

	private void expect(TokenType type) {
		Token t = buf.takeToken();
		if (t == null) {
			throw new ParseException("expect " + type + ", but get token "
					+ null + ", at line " + this.lineNo);
		} else {
			if (type == t.getType()) {
				return;
			} else {
				throw new ParseException("expect " + type + ", but get token "
						+ t, t.getPosition());
			}
		}
	}

	public void expectNotNull(Token t) {
		checkType(t, null);
	}

	public void checkType(Token t, TokenType type) {
		if (t == null) {
			String exp = "a token";
			if (type != null) {
				exp = "expect " + type;
			}
			throw new JmoException("find null, " + exp, new SourcePosition(
					this.sourceName, lineNo, -1));
		}
		if (type != null) {
			if (type != t.getType()) {
				throw new JmoException("tokenType mismatch, expect" + type
						+ ", but get " + t, new SourcePosition(this.sourceName,
						lineNo, -1));
			}
		}

	}

	protected VarNode varNode() throws JmoException {
		Token t = buf.findValid();
		checkType(t, null);
		String name = t.getText();
		if (validMethodName(name)) {
			buf.takeToken();
			return new VarNode(t.getPosition(), name);
		}
		throw new ParseException("invalid varNode " + t, t.getPosition());
	}

	public static String method_escaped = " \t\r\n=.+-/%^(){}[],:;!&|<>$@";

	private boolean validMethodName(String name) {
		char[] xx = name.toCharArray();
		for (char c : method_escaped.toCharArray()) {
			for (char x : xx)
				if (c == x) {
					return false;
				}
		}

		return true;
	}
}
