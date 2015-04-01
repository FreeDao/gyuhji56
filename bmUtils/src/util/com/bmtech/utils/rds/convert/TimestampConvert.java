package com.bmtech.utils.rds.convert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

abstract class TimestampConvert extends FieldConvert {

	public TimestampConvert(int code) {
		super(code);
	}

	protected long getTimeStamp(ResultSet rs, int index) throws SQLException {
		Timestamp tm = rs.getTimestamp(index);
		long ret = 0;
		if (tm != null) {
			ret = tm.getTime() / 1000;
		} else {
			log.debug("invalid convert for null timestamp for index %s", index);
		}
		return ret;
	}

}
