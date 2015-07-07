package com.bmtech.nale.script.engine.lang.ast;

import com.bmtech.nale.script.engine.jmo.lang.source.SourcePosition;
import com.bmtech.nale.script.engine.lang.NaleContext;
import com.bmtech.nale.script.engine.lang.NaleException;
import com.bmtech.nale.script.engine.lang.ast.itf.NaleCopyable;
import com.bmtech.nale.script.engine.lang.ast.itf.NaleNode;
import com.bmtech.nale.script.engine.lang.ast.itf.RegexNode;
import com.bmtech.nale.script.engine.lang.obj.NaleObject;
import com.bmtech.nale.script.engine.lang.obj.NaleObjectFactory;
import com.bmtech.nale.script.engine.lang.regex.RegexExpression;

import textmatchers.pattern.def.QMatchable;

public class RegexExpressionNode extends NaleNode implements RegexNode {
	final RegexExpression nodes;

	public RegexExpressionNode(SourcePosition position, RegexExpression regexOr) {
		super(position);
		this.nodes = regexOr;
	}

	@Override
	public String toString() {
		return nodes.toString();
	}

	@Override
	public NaleCopyable copyof() {
		return new RegexExpressionNode(super.getPosition(),
				(RegexExpression) this.nodes.copyof());
	}

	@Override
	public NaleObject eval(NaleContext context) throws NaleException {
		QMatchable mat = nodes.toMatcher(context, this.position);

		return NaleObjectFactory.getInstance().byExpression(mat,
				this.getPosition());
	}
}
