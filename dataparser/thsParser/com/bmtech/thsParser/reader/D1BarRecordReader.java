package com.bmtech.thsParser.reader;

import java.util.ArrayList;

import com.bmtech.thsParser.CodeInfo;
import com.bmtech.thsParser.D1BarRecord;
import com.bmtech.utils.log.LogHelper;

public abstract class D1BarRecordReader{
	public final String code;
	static final LogHelper log = new LogHelper("reader");
	protected ArrayList<D1BarRecord>records;
	private int pos = 0;
	public boolean isCodeInfo(CodeInfo ci){
		return ci.code.equalsIgnoreCase(code);
	}
	public boolean isCodeInfo(String c){
		return code.equalsIgnoreCase(c);
	}
	public D1BarRecordReader(String code) throws Exception{
		this.code = code;
	}
	
	protected abstract ArrayList<D1BarRecord> readRecords() throws Exception ;
	
	public int seekDay(int day){
		while(pos < this.records.size()){
			D1BarRecord rec = this.records.get(pos);
			int dayGet = rec.getDate();
			if(dayGet < day){
				pos ++;
				continue;
			}else{
				return pos;
			}
		}
		return -1;
	}
	public D1BarRecord next() {
		pos ++;
		if(pos < records.size()){
			return records.get(pos);
		}else{
			return null;
		}
	}
	public boolean hasNextRecord() {
		boolean ret = pos + 1 < records.size();
		return ret;
	}
	public D1BarRecord getRecord(int pos) {
		if(pos < records.size()){
			return records.get(pos);
		}
		return null;
	}
	public D1BarRecord getRecord() {
		return getRecord(pos);
	}
	
	public int getPosition() {
		return this.pos;
	}

}
