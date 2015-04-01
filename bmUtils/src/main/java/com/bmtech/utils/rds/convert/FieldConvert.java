package com.bmtech.utils.rds.convert;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.bmtech.utils.log.LogHelper;

abstract class FieldConvert {
	protected static final LogHelper log = new LogHelper("convert");
	final int code;

	public FieldConvert(int code) {
		this.code = code;
	}

	public abstract Object convert(ResultSet rs, int index) throws SQLException;
}