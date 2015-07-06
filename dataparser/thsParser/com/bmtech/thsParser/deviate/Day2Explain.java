package com.bmtech.thsParser.deviate;

import java.io.File;
import java.io.IOException;

import com.bmtech.thsParser.D1BarRecord;
import com.bmtech.thsParser.RecordIterator;
import com.bmtech.utils.Consoler;
import com.bmtech.utils.io.LineWriter;
import com.bmtech.utils.log.LogHelper;

public class Day2Explain {
	static final LogHelper log = new LogHelper("reader");
	public static void main(String[]args) throws IOException{
		File f = new File(Consoler.readString("fileName : "));

		File ft = new File(f.getParent(), f.getName() + ".explain");
		LineWriter lw = new LineWriter(ft, false);
		RecordIterator itr = new RecordIterator(f);
		while(itr.hasNext()){
			D1BarRecord rec = itr.next();
			lw.writeLine(rec);
		}
		lw.close();
		itr.close();
	
	}
}
