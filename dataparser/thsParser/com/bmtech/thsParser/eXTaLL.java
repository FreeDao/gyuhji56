package com.bmtech.thsParser;

import java.io.File;
import java.io.IOException;

import com.bmtech.utils.io.LineWriter;

public class eXTaLL {
	public static void main(String[]args) throws IOException{
		String fstr = "D:\\program files\\同花顺软件\\同花顺\\history\\shase\\day\\";
		File fx = new File(fstr);
		File []fs = fx.listFiles();
		File to = new File("/ext");
		if(!to.exists()){
			to.mkdirs();
		}
		for(File f : fs){
			if(f.length() < 100 * 1024){
				continue;
			}
			if(!f.getName().startsWith("6")){
				continue;
			}
			System.out.println("ext " + f);
			File ft = new File(to, f.getName());
			LineWriter lw = new LineWriter(ft, false);
			RecordIterator itr = new RecordIterator(f);
			while(itr.hasNext()){
				D1BarRecord rec = itr.next();
				lw.writeLine(rec);
//				System.out.println(rec);
			}
			lw.close();
			itr.close();
		}
	}
}
