package com.bmtech.thsParser.reader;

import java.io.File;
import java.util.ArrayList;

import com.bmtech.thsParser.D1BarRecord;
import com.bmtech.utils.io.LineReader;

public class D1BarRecordReaderFromFile extends D1BarRecordReader{
	LineReader lr;
	public D1BarRecordReaderFromFile(File f) throws Exception {
		super(f.getName().replace(".day", ""));
		lr = new LineReader(f);
	}
	public D1BarRecordReaderFromFile(String code, File f) throws Exception {
		super(code);
		lr = new LineReader(f);
		records = readRecords();
	}

	@Override
	protected ArrayList<D1BarRecord> readRecords() {
		ArrayList<D1BarRecord>records = new ArrayList<D1BarRecord>();
		while(lr.hasNext()){
			String line = lr.next();
			String tokens[] = line.split("\t");
			D1BarRecord record = new D1BarRecord(
					Integer.parseInt(tokens[0]),
					Double.parseDouble(tokens[1]),
					Double.parseDouble(tokens[2]),
					Double.parseDouble(tokens[3]),
					Double.parseDouble(tokens[4]),
					Double.parseDouble(tokens[5]),
					(long)Double.parseDouble(tokens[6])
					);
			records.add(record);
		}
		return records;
	}
	
}
