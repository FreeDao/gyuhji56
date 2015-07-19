package com.bmtech.fsql.expression.ast;

import com.bmtech.fsql.expression.Token;

public class ItemConditionNode extends ConditionNode {
	static final String IntT = "IntT", LongT = "LongT", FloatT = "FloatT";

	private FieldNameNode fieldName;
	private Token relation;
	private FiledValueNode fieldValue;

	public ItemConditionNode(int position, FieldNameNode fieldName, Token relation, FiledValueNode valueNode) {
		super(position);
		this.fieldName = fieldName;
		this.setFieldValue(valueNode);
		this.relation = relation;
	}

	public void setFieldName(FieldNameNode fieldName) {
		this.fieldName = fieldName;
	}

	public Token getRelation() {
		return relation;
	}

	public void setValueNode(FiledValueNode valueNode) {
		this.setFieldValue(valueNode);
	}

	@Override
	public String toString() {
		return this.myConjunct + fieldName.name + " " + relation.type + " " + fieldValue + super.appendString();
	}

	public void setValue(String value) {
		int pos = -1;
		if (getFieldValue() != null) {
			pos = fieldValue.position;
		}
		setFieldValue(new FiledValueNode(pos, value));
	}

	@Override
	public boolean isCompond() {
		return false;
	}

	@Override
	public boolean isEndNode() {
		return false;
	}

	public String getStandardFieldName() {
		return this.fieldName.name.toLowerCase();
	}

	public String getFieldValue() {
		return fieldValue.getValue().toString();
	}

	public void setFieldValue(FiledValueNode fieldValue) {
		this.fieldValue = fieldValue;
	}
}
