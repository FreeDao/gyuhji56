package com.bmtech.fsql.expression.ast;

import com.bmtech.fsql.expression.TokenType;

public abstract class ConditionNode extends Node {
	public ConjunctNode myConjunct;
	public ConditionNode nextCondition;

	public ConditionNode(int position) {
		super(position);
	}

	public String appendString() {
		if (this.nextCondition != null) {
			return " " + nextCondition.toString();
		} else {
			return "";
		}
	}

	public abstract boolean isCompond();

	public abstract boolean isEndNode();

	public TokenType getConjunctType() {
		return this.myConjunct.type;
	}
}
