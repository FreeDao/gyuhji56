package com.funshion.fsql.expression.ast;

public class CompondCondtionNode extends ConditionNode {
	public final ConditionNode thisCond;

	public CompondCondtionNode(int position, ConditionNode condNodeSub) {
		super(position);
		thisCond = condNodeSub;
	}

	@Override
	public String toString() {
		return this.myConjunct + "(" + thisCond + ")" + super.appendString();
	}

	@Override
	public boolean isCompond() {
		return true;
	}

	@Override
	public boolean isEndNode() {
		return false;
	}

	// @Override
	// public Query toQueryElement(FsqlInterpreter interpreter) throws Exception
	// {
	// BooleanQuery bq = new BooleanQuery();
	// QueryMakeableConditionNode node = (QueryMakeableConditionNode)
	// this.thisCond;
	// while(node != null){
	// Query sub;
	// if(node instanceof CompondCondtionNode){
	// sub = ((CompondCondtionNode)node).toQueryElement(interpreter);
	// }else{
	// sub = node.toQueryElement(interpreter);
	// }
	// bq.add(sub, node.occur());
	// node = (QueryMakeableConditionNode) node.nextCondition;
	// }
	// return bq;
	// }
}
