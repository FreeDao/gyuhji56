package com.bmtech.thsParser.deviate;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.sql.SQLException;

import com.bmtech.utils.io.LineReader;
import com.bmtech.utils.rds.RDS;

public class xxx {
	public static void main(String[] args) throws HeadlessException, NumberFormatException, SQLException, IOException {
		String var = "1\t9\n2\t3\n";
		StringSelection stringSelection = new StringSelection(var);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
		RDS rds = RDS.getRDSByDefine("vars", "insert into sname(code, volume)values(?,?)");
		LineReader lr = new LineReader("/all/10k.txt");
		while(lr.hasNext()){
			String line = lr.next();
			String[]ts = line.trim().split("\t");
			String code = ts[0];
			try{
				long volumn = (long) (10000 * Double.parseDouble(ts[1]));
				rds.setString(1, code);
				rds.setLong(2, volumn);
				rds.execute();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
