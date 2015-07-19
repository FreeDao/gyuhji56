package com.bmtech.fsql.expression.ast;

import com.bmtech.fsql.expression.TokenType;

public class FiledValueNode{
	public final int position;
	private Object value;
	public FiledValueNode(int position, Object value) {
		this.position = position;
		this.value = value;
	}

	public boolean valueIsSet(){
		return value != TokenType.VALUEUNSET;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
}
