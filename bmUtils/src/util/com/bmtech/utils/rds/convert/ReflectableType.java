package com.bmtech.utils.rds.convert;

import java.util.HashMap;
import java.util.Map;

enum ReflectableType {
	INT, STRING, LONG, TIMESTAMP, BOOLEAN;

	static final Map<Class<?>, ReflectableType> classTypeMap = new HashMap<Class<?>, ReflectableType>();
	static {
		classTypeMap.put(int.class, ReflectableType.INT);
		classTypeMap.put(Integer.class, ReflectableType.INT);

		classTypeMap.put(Long.class, ReflectableType.LONG);
		classTypeMap.put(long.class, ReflectableType.LONG);

		classTypeMap.put(String.class, ReflectableType.STRING);

		classTypeMap.put(Boolean.class, ReflectableType.BOOLEAN);
		classTypeMap.put(boolean.class, ReflectableType.BOOLEAN);
	}

	public static ReflectableType getType(Class<?> type) {
		return classTypeMap.get(type);
	}
}