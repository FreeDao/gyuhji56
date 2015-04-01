package com.bmtech.utils.rds.convert;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bmtech.utils.log.LogHelper;

public class ResultSetConvert<E> {
	protected static final LogHelper log = new LogHelper("rsConvert");
	static final FieldConvert ConvertTimeStamp2Int = new TimestampConvert(1) {

		@Override
		public Object convert(ResultSet rs, int index) throws SQLException {
			long ret = this.getTimeStamp(rs, index);
			return (int) (ret / 1000);
		}

	}, ConvertTypeStamp2Long = new TimestampConvert(1) {

		@Override
		public Object convert(ResultSet rs, int index) throws SQLException {
			long ret = this.getTimeStamp(rs, index);
			return ret;
		}

	};

	final Class<? extends E> clazz;

	public ResultSetConvert(Class<? extends E> clazz) {
		this.clazz = clazz;
	}

	private Object getValue(ResultSet rs, ReflectRelation rType)
			throws SQLException {
		Object ret = null;
		int index = rType.getIndex();
		ReflectableType type = rType.type;
		if (type != null) {

			switch (type) {
			case INT:
				if (rType.convertCode == null) {
					ret = rs.getInt(index);
				} else {
					ret = rType.convertCode.convert(rs, index);
				}
				break;
			case STRING:
				if (rType.convertCode == null) {
					ret = rs.getString(index);
				} else {
					ret = rType.convertCode.convert(rs, index);
				}
				break;
			case LONG:
				ret = rs.getLong(index);
				break;
			case TIMESTAMP:
				ret = rs.getTimestamp(index);
				break;
			case BOOLEAN:
				ret = rs.getBoolean(index);
				break;
			}
		}
		return ret;
	}

	private class ReflectHelper {
		public Collection<ReflectRelation> refectIterator;

		ReflectHelper(ResultSet rs) throws Exception {
			Map<String, ReflectRelation> map = new HashMap<String, ReflectRelation>();
			Field[] fields = clazz.getDeclaredFields();
			for (Field f : fields) {
				ReflectableType t = ReflectableType.getType(f.getType());
				if (t == null) {
					log.debug("not define for  field %s", f);
					continue;
				}
				String name = f.getName();

				PropertyDescriptor pd = new PropertyDescriptor(name, clazz);
				Method m = pd.getWriteMethod();
				if (m != null) {
					ReflectRelation rt = new ReflectRelation(t, m);
					map.put(name, rt);
				}
			}

			int columns = rs.getMetaData().getColumnCount();
			ResultSetMetaData meta = rs.getMetaData();

			for (int i = 1; i <= columns; i++) {
				String column = meta.getColumnName(i);
				ReflectRelation rt = map.get(column.toLowerCase());
				if (rt != null) {
					rt.setIndex(i);
					if (java.sql.Types.TIMESTAMP == meta.getColumnType(i)) {
						if (rt.type == ReflectableType.INT) {
							rt.convertCode = ConvertTimeStamp2Int;
						} else if (rt.type == ReflectableType.LONG) {
							rt.convertCode = ConvertTypeStamp2Long;
						}
					}

				} else {
					log.debug("missing refect for column %s", column);
				}
			}
			refectIterator = map.values();
		}

		public E toObject(ResultSet rs) throws Exception {
			E e = clazz.newInstance();
			for (ReflectRelation type : refectIterator) {

				if (type.getIndex() == 0) {
					continue;
				}
				Object value = getValue(rs, type);
				if (value != null) {
					type.m.invoke(e, value);
				}
			}
			return e;
		}
	}

	public List<E> getListFromResult(ResultSet rs) throws Exception {
		List<E> eList = new ArrayList<E>();
		ReflectHelper reflector = new ReflectHelper(rs);
		while (rs.next()) {
			E e = reflector.toObject(rs);
			eList.add(e);
		}
		return eList;
	}

}