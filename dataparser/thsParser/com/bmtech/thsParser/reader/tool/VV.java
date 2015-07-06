package com.bmtech.thsParser.reader.tool;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.bmtech.utils.io.LineReader;
import com.bmtech.utils.io.LineWriter;
import com.bmtech.utils.rds.RDS;
import com.bmtech.utils.rds.SourceDefine;

public class VV {

	public static void main(String[] args) throws IOException, SQLException {
		Connection conn = SourceDefine.instance().getDataSourceDefine("vars").getNewConnection();
		conn.setAutoCommit(false);
		Statement stmt = conn.createStatement();
		LineReader lr = new LineReader("C:\\Documents and Settings\\Administrator\\桌面\\vars.sql");
		int cnt = 0;
		while(lr.hasNext()){
			String line = lr.next();
			cnt += (line.length() + 1);
			if(line.startsWith("INSERT INTO")){
				stmt.execute(line);
			}else{
				System.out.println("skip " + line);
			}
			if(lr.currentLineNumber() % 10000 == 0){
				System.out.println(lr.currentLineNumber() + ", " + (cnt /1024/1024.0)+ "MB");
			}
		}
		conn.commit();
		conn.close();
		
//		
//		File f = new File("d:/1");
//		LineWriter lw = new LineWriter("d:/2.txt", false);
//		for(int x = 0;x < 100; x ++){
//			String num = String.format("%03d", x);
//			
//			LineReader lr = new LineReader(f);
//			while(lr.hasNext()){
//				String line = lr.next();
//				line = line.replace("_000", num);
//				lw.writeLine(line);
//			}
//		}
//		lw.close();
	}
}
