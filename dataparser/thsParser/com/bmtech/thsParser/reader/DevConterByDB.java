package com.bmtech.thsParser.reader;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.sql.Connection;

import com.bmtech.thsParser.CodeInfo;
import com.bmtech.thsParser.deviate.DeviateAverageWeightedPriceBaseOnMa2;
import com.bmtech.utils.Consoler;
import com.bmtech.utils.rds.SourceDefine;

public class DevConterByDB {
	public static void main(String[] args) throws Exception {
		final int startTime = 20130101;
		final int maDayNumber = 1;
		Clipboard clipBoard = Toolkit.getDefaultToolkit().getSystemClipboard();
		Connection conn = SourceDefine.getNewConnection("vars");
		try{
			while(true){
				D1BarRecordReader szReader = new D1BarRecordReaderFromDB("1A0001", conn);
				try{
					String name = Consoler.readString("code:");
					CodeInfo ci = CodeInfo.getCodeInfo(name);
					if(ci == null){
						System.out.println("error code: '" + name + "'");
						continue;
					}
					D1BarRecordReader r1 = new D1BarRecordReaderFromDB(name.trim(), conn);

					DeviateAverageWeightedPriceBaseOnMa2 dap = new DeviateAverageWeightedPriceBaseOnMa2(startTime, maDayNumber, r1, szReader);
					
					int little = 100000;

					StringBuilder sb = new StringBuilder();
					while(dap.hasNextDeviateValue()){
						double var = dap.nextDeviateValue();
						double d = ((int)(little * var)) ;
						String str = dap.getCurDate()%10000 + "\t" + d;
						System.out.println(str);
						sb.append(str);
						sb.append("\n");
					}
					StringSelection stringSelection = new StringSelection(sb.toString());
					clipBoard.setContents(stringSelection, null);
					System.out.println(ci);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}finally{
			conn.close();
		}
	}
}
