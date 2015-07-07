package com.bmtech.nale.script.engine.lang;

import java.util.ArrayList;
import java.util.List;

import com.bmtech.nale.script.engine.jmo.lang.JmoException;
import com.bmtech.nale.script.engine.jmo.lang.ParseException;
import com.bmtech.nale.script.engine.lang.ast.BooleanNode;
import com.bmtech.nale.script.engine.lang.ast.NumberVariable;
import com.bmtech.nale.script.engine.lang.ast.VarNode;
import com.bmtech.nale.script.engine.lang.ast.itf.NaleNode;
import com.bmtech.nale.script.engine.lang.ast.itf.ReferNode;
import com.bmtech.nale.script.engine.lang.ast.itf.ValueNode;

public class LineParser extends LineParser0 {

	public LineParser(String sourceName, int lineNo, String line)
			throws NaleException {
		super(sourceName, lineNo, line);
	}

	@Override
	protected NaleNode expression(VarNode varNode) {
		Token next = buf.findValid();

		if (next.getType() == TokenType.TRUE) {
			buf.takeToken();
			return new BooleanNode(true, next.getPosition());
		} else if (next.getType() == TokenType.TRUE) {
			buf.takeToken();
			return new BooleanNode(true, next.getPosition());
		} else {
			Integer x = this.parseInt(next);
			if (x == null) {
				if (next.getType() == TokenType.DOLLAR) {
					buf.takeToken();
				}
				NaleNode node = this.parseVariable();
				if (node instanceof ValueNode) {
					return node;
				} else {
					throw new JmoException("can not assign Non-valueNode "
							+ node, buf.currentPosition());
				}
			} else {
				return new NumberVariable(x, buf.currentPosition());
			}
		}

	}

	private Integer parseInt(Token t) {
		try {
			return Integer.parseInt(t.getText());
		} catch (Exception e) {
			return null;
		}

	}

	@Override
	protected List<ReferNode> funcParasNode() throws JmoException {
		List<ReferNode> paras = new ArrayList<ReferNode>(2);

		while (true) {
			Token t = buf.findValid();
			checkType(t, null);
			if (t.getType() == TokenType.COMMA) {
				if (paras.size() == 0) {
					throw new ParseException("missing para before ','", t
							.getPosition());
				}
				buf.takeToken();
				continue;
			} else if (t.getType() == TokenType.RPAREN) {
				break;
			} else {
				if (t.getType() == TokenType.DOLLAR) {
					buf.takeToken();
				}
				t = buf.findValid();
				checkType(t, TokenType.VARIABLE);

				Integer itg = parseInt(t);
				if (itg == null) {

					VarNode var = this.varNode();
					Token tx = buf.findValid();
					if (tx == null) {
						throw new ParseException(
								"expect '.' or other parameters after " + t,
								buf.currentPosition());
					}
					if (tx.getType() == TokenType.DOT) {
						buf.takeToken();
						VarNode vn = this.varNode();
						VarNode vnx = new VarNode(var.getPosition(), var, vn
								.getVarName());
						paras.add(vnx);
					} else {
						paras.add(var);
					}

					continue;
				} else {
					NumberVariable num = new NumberVariable(itg, t
							.getPosition());
					buf.takeToken();
					paras.add(num);
					continue;
				}
			}
		}

		return paras;
	}
}
