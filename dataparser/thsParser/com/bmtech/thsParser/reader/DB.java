package com.bmtech.thsParser.reader;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.bmtech.utils.io.FileGet;
import com.bmtech.utils.rds.RDS;

public class DB {

	public static String getTableName(String sid){
		String sub = sid.substring(sid.length() - 2, sid.length());
		int var  = Integer.parseInt(sub);
		int tableName = var % 100;
		return String.format("d1r_%03d", tableName);
	}
	public static String createTableSql(String tableName) throws IOException{
		return FileGet.getStr("./config/rds/createTableTemplate.sql").replace("TABLE-NAME-HERE", tableName);
	}
	public static String getTableName(File f) throws Exception{
		String sid = getSid(f);
		return getTableName(sid);
	}
	public static boolean besureTableExists(Connection conn, String tableName) throws SQLException, IOException{
		if(tableExists(conn, tableName)){
			return false;
		}

		String sql = createTableSql(tableName);
		conn.createStatement().execute(sql);
		return true;
	}
	public static String getSid(File f){
		return f.getName().replace(".day", "");
	}
	
	static boolean tableExists(Connection conn, String tableName) throws SQLException{
		RDS v = RDS.getRDSByDefine("vars", "show tables like ?", conn);
		v.setString(1, tableName);
		ResultSet rs = v.executeQuery();
		boolean found = false;
		while(rs.next()){
			String str = rs.getString(1);
			if(tableName.equals(str)){
				found = true;
				break;
			}
		}
		v.close();
		return found;
	}
}
