package com.bmtech.nale.script.engine.lang.obj;

import com.bmtech.nale.script.engine.jmo.lang.source.SourcePosition;
import com.bmtech.nale.script.engine.lang.NaleException;

import textmatchers.pattern.def.QMatchable;

public class ClazzNaleObject extends NaleObject {
	private final Class<?> filterClass;
	private final String aliaName;

	public ClazzNaleObject(Class<?> filterClass, String aliaName,
			SourcePosition pos) {
		super(pos);
		this.filterClass = filterClass;
		this.aliaName = aliaName;
	}

	@Override
	public String toString() {
		return aliaName;
	}

	@Override
	public QMatchable toQMatchable() throws NaleException {
		Object obj;
		try {
			obj = this.filterClass.newInstance();
		} catch (Exception e) {
			throw new NaleException("can not instance class "
					+ this.filterClass, getPos());
		}
		if (obj instanceof QMatchable) {
			QMatchable qm = (QMatchable) obj;

			return qm;

		} else {
			throw new NaleException("not QMatchale :" + this.filterClass,
					getPos());
		}
	}

	@Override
	public boolean booleanValue() {
		throw new NaleException("clazz is not boolean Value", getPos());
	}
}
