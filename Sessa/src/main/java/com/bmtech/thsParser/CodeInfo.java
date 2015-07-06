package com.bmtech.thsParser;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.bmtech.utils.log.LogHelper;
import com.bmtech.utils.rds.RDS;

public class CodeInfo {
	static LogHelper log = new LogHelper("ci");
	public static final String szCode = "1A0001";
	public final String code;
	public final long volume;
	public CodeInfo(String name, long volume){
		this.code = name;
		this.volume = volume;
	}
	public boolean acceptCode(){
		return acceptCode(code);
	}
	public static boolean acceptCode(String fName){
		return fName.startsWith("1A") || fName.startsWith("00") || fName.startsWith("60");
	}

	public String toString(){
		return "code : " + code + ", volume : " + volume;
	}
	
	private static Map<String, CodeInfo> map;
	static{
		try {
			map = CodeInfo.loadInfos();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	public static CodeInfo getCodeInfo(String code){
		return map.get(code.trim().toLowerCase());
	}
	
	private static Map<String, CodeInfo> loadInfos() throws SQLException {
		RDS rds = RDS.getRDSByDefine("vars", "select * from sname");
		ResultSet rs = rds.load();
		Map<String, CodeInfo> ret = new HashMap<String, CodeInfo>();
		while(rs.next()){
			CodeInfo ci = new CodeInfo(rs.getString("code").trim(), rs.getLong("volume"));
			ret.put(ci.code.toLowerCase(), ci);
		}
		rds.close();
		return ret;
	}
	public static void main(String[] args) throws Exception {
		Map<String, CodeInfo> map = loadInfos();
		System.out.println(map.toString().replace(",", "\n"));
	}
	public static Collection<CodeInfo> getCodeInfos() {
		return map.values();
	}
}
