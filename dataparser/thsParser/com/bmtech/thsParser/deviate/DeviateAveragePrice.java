package com.bmtech.thsParser.deviate;

import java.io.File;
import java.io.IOException;

import com.bmtech.thsParser.D1BarRecord;
import com.bmtech.thsParser.reader.D1BarRecordReader;
import com.bmtech.thsParser.reader.D1BarRecordReaderFromFile;

public class DeviateAveragePrice extends DeviateAbstract{

	public DeviateAveragePrice(int sumRange, D1BarRecordReader r1, D1BarRecordReader r2) {
		super(sumRange, r1, r2);
	}

	protected double value(D1BarRecordReader r12, int pos){
		D1BarRecord rec = r12.getRecord(pos);
		double nowp = rec.averagePrice();
		double prev = r12.getRecord(pos - 1).averagePrice();
		return (nowp - prev) / prev;
	}
	public static void main(String[] args) throws Exception {
		File base = new File("E:\\all\\ext");
		File fs [] = base.listFiles();
		
		File f1 = fs[0];
		File f2 = fs[1];
		D1BarRecordReader r1 = new D1BarRecordReaderFromFile(f1);
		D1BarRecordReader r2 = new D1BarRecordReaderFromFile(f2);
		
		DeviateAveragePrice dap = new DeviateAveragePrice(1, r1, r2);
		int little = 1000000;
		while(dap.hasNextDeviateValue()){
			double var = dap.nextDeviateValue();
			double d = ((int)(little * var)) ;
			System.out.println(dap.getCurDate() + "\t" + d);
		}
	}

	@Override
	protected void skipHead() {
		// TODO Auto-generated method stub
		
	}

}
