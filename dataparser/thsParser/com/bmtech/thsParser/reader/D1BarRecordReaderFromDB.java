package com.bmtech.thsParser.reader;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.bmtech.thsParser.D1BarRecord;
import com.bmtech.utils.rds.RDS;
import com.bmtech.utils.rds.SourceDefine;

public class D1BarRecordReaderFromDB extends D1BarRecordReader{
	final Connection conn;
	public D1BarRecordReaderFromDB(String code) throws Exception {
		this(code, null);
	}
	public D1BarRecordReaderFromDB(String code, Connection conn) throws Exception {
		super(code);
		if(conn == null){
			this.conn = SourceDefine.getNewConnection("vars");
		}else{
			this.conn = conn;
		}
		records = readRecords();
	}
	@Override
	protected ArrayList<D1BarRecord> readRecords() throws SQLException {
		ArrayList<D1BarRecord>records = new ArrayList<D1BarRecord>();
		String tableName = DB.getTableName(code);

		RDS rds = RDS.getRDSByDefine("vars", "select * from " + tableName + " where sid = ? order by day", conn);
		rds.setString(1, code);
		ResultSet rs = rds.load();
		while(rs.next()){
			D1BarRecord rec = new D1BarRecord(
					rs.getInt("day"),
					rs.getDouble("open"),
					rs.getDouble("high"),
					rs.getDouble("low"),
					rs.getDouble("close"),
					rs.getDouble("amount"),
					rs.getLong("volume"));
			records.add(rec);
		}

		rds.close();
		return records;
	}

	public static void main(String[] args) throws Exception {
		String x = "600000";
		D1BarRecordReaderFromDB reader = new D1BarRecordReaderFromDB(x);
		while(reader.hasNextRecord()){
			D1BarRecord rec = reader.next();
			System.out.println(rec);
		}
	}
}
