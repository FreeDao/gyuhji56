package com.bmtech.utils.rds.convert;

import java.lang.reflect.Method;

class ReflectRelation {
	private int index;
	final ReflectableType type;
	final Method m;
	public FieldConvert convertCode;

	ReflectRelation(ReflectableType type, Method m) {
		this.type = type;
		this.m = m;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}