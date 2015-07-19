package com.funshion.fsql.expression.ast;

public class EndNode extends ConditionNode {

	public static final EndNode instance = new EndNode();

	private EndNode() {
		super(-1);
	}

	@Override
	public String toString() {
		return "";
	}

	@Override
	public boolean isCompond() {
		return false;
	}

	@Override
	public boolean isEndNode() {
		return true;
	}
}
