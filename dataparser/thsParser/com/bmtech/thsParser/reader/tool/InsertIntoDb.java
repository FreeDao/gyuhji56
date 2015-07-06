package com.bmtech.thsParser.reader.tool;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import com.bmtech.thsParser.CodeInfo;
import com.bmtech.thsParser.D1BarRecord;
import com.bmtech.thsParser.RecordIterator;
import com.bmtech.thsParser.reader.DB;
import com.bmtech.utils.Consoler;
import com.bmtech.utils.log.LogHelper;
import com.bmtech.utils.rds.RDS;
import com.bmtech.utils.rds.SourceDefine;

public class InsertIntoDb {
	LogHelper log = new LogHelper("ins");
	RDS check, insert;
	private Connection conn;
	InsertIntoDb(Connection conn) throws SQLException{
		this.conn = conn;
	}
	int loadIndex = 0;
	void loadFile(String from, File f) throws IOException, SQLException{
		log.info("load %s file %s with from %s", ++loadIndex, f, from);
		
		String sid = f.getName().replace(".day", "");
		String tableName;
		try {
			tableName = DB.getTableName(sid);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		DB.besureTableExists(conn, tableName);
		check = 
				RDS.getRDSByDefine("vars", "select day from " + tableName + " where sid=?",
						conn);
		insert =
				RDS.getRDSByDefine("vars", "insert into " + tableName + "(se, sid, day, open, low, high, close, volume, amount)values(?,?,?,?,?,?,?,?,?)",
						conn);

		check.setString(1, sid);
		ResultSet rs = check.executeQuery();
		Set<Integer>days = new HashSet<Integer>();
		while(rs.next()){
			days.add(rs.getInt(1));
		}
		
		RecordIterator reader = new RecordIterator(f);
		int skiped = 0, inserted = 0;
		while(reader.hasNext()){
			D1BarRecord rec = reader.next();
			if(days.contains(rec.getDate())){
//				log.debug("skip %s", rec);
				skiped ++;
				continue;
				//FIXME should we check the value?
			}
			insert.setString(1, from);
			insert.setString(2, sid);
			insert.setInt(3, rec.getDate());
			insert.setDouble(4, rec.getOpen());
			insert.setDouble(5, rec.getLow());
			insert.setDouble(6, rec.getHigh());
			insert.setDouble(7, rec.getClose());
			insert.setLong(8, rec.getVolume());
			insert.setDouble(9, rec.getAmount());
			insert.execute();
			inserted ++;
		}
		reader.close();
		check.close();
		insert.close();
		log.info("insert %s, skip %s",  inserted, skiped);
	}
	void loadDir(File dir) throws IOException, SQLException{
		String name = dir.getName();
		
		File dayDir = new File(dir, "day");
		File [] fs = dayDir.listFiles();
		for(File x : fs){
			String fName = x.getName();
			if(!CodeInfo.acceptCode(fName)){
				log.error("bad file %s", x);
				return;
			}
			loadFile(name, x);
		}
	}
	
	
	public static void main(String[] args) throws SQLException, IOException {
		Connection conn = SourceDefine.instance().getDataSourceDefine("vars").getNewConnection();
		String toScan = Consoler.readString("toscanDir:");
		File dir = new File(toScan);
		File d[] = dir.listFiles();
		InsertIntoDb id = new InsertIntoDb(conn);
		for(File x : d){
			id.loadDir(x);
		}
		conn.close();
	}
}
