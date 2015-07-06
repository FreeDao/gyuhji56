package com.bmtech.thsParser.deviate;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.bmtech.thsParser.CodeInfo;
import com.bmtech.thsParser.D1BarRecord;
import com.bmtech.thsParser.reader.D1BarRecordReader;
import com.bmtech.thsParser.reader.D1BarRecordReaderFromFile;

public class DeviateAverageWeightedPrice extends DeviateAbstract{
	private long volumn1, volumn2;
	public DeviateAverageWeightedPrice(int sumRange, D1BarRecordReader r1, D1BarRecordReader r2) throws Exception {
		super(sumRange, r1, r2);
		volumn1 = CodeInfo.getCodeInfo(r1.code).volume;
		volumn2 = CodeInfo.getCodeInfo(r2.code).volume;
	}

	protected double value(D1BarRecordReader r12, int pos){
		D1BarRecord rec = r12.getRecord(pos);
		double nowp = rec.averagePrice() * rec.volumeChangeAverage(volumn1) * 1000;
		D1BarRecord rec2 = r12.getRecord(pos - 1);
		
		double prev = rec2.averagePrice() * rec2.volumeChangeAverage(volumn2)*1000;
		return (nowp - prev) / prev;
	}
	public static void main(String[] args) throws Exception {
		File base = new File("E:\\all\\ext");
		File fs [] = base.listFiles();
		
		File f1 = fs[0];
		File f2 = fs[1];
		D1BarRecordReader r1 = new D1BarRecordReaderFromFile(f1);
		D1BarRecordReader r2 = new D1BarRecordReaderFromFile(f2);
		
		DeviateAverageWeightedPrice dap = new DeviateAverageWeightedPrice(1, r1, r2);
		int little = 10000;
		while(dap.hasNextDeviateValue()){
			double var = dap.nextDeviateValue();
			double d = ((int)(little * var)) ;
			System.out.println(dap.getCurDate() + "\t" + d);
		}
	}

	@Override
	protected void skipHead() {
		
	}

}
