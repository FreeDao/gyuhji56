package com.bmtech.thsParser.deviate;

import java.io.IOException;

import com.bmtech.thsParser.D1BarRecord;
import com.bmtech.utils.io.LineReader;

public class ScanRange {
	LineReader lr;
	int currentPos;
	final String code;
	final long total;
	ScanRange(String code, long total){
		this.code = code;
		this.total = total;
		int codeI = Integer.parseInt(code);
		if(codeI < 1 || codeI > 999999){
			throw new RuntimeException("invalid code '" + code + "'");
		}
	}

	boolean hasNext(){
		return lr.hasNext();
	}



}
