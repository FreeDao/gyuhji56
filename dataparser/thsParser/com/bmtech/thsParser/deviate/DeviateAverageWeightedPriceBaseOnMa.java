package com.bmtech.thsParser.deviate;

import java.io.File;

import com.bmtech.thsParser.CodeInfo;
import com.bmtech.thsParser.D1BarRecord;
import com.bmtech.thsParser.reader.D1BarRecordReader;
import com.bmtech.thsParser.reader.D1BarRecordReaderFromFile;

public class DeviateAverageWeightedPriceBaseOnMa extends DeviateAbstract{
	private CodeInfo  code1, code2;
	public DeviateAverageWeightedPriceBaseOnMa(int sumRange, D1BarRecordReader r1, D1BarRecordReader r2) 
			throws Exception {
		super(sumRange, r1, r2);
		code1 = CodeInfo.getCodeInfo(r1.code);
		code2 = CodeInfo.getCodeInfo(r2.code);
	}
	protected double changedValue(D1BarRecordReader r12) {
		return this.value(r12, r12.getPosition());
	}
	protected double value(D1BarRecordReader r12, int pos){
		D1BarRecord rec = r12.getRecord(pos);
		long volumn = 0;
		if(r12.isCodeInfo(szCode)){
			
		}else if(r12.isCodeInfo(code1)){
			volumn = code1.volume;
		}else if(r12.isCodeInfo(code2)){
			volumn = code2.volume;
		}else{
			throw new RuntimeException("no volumn for " + r12.code);
		}
			
		double maX = getMa(r12, pos, volumn);
		double nowp = rec.averagePrice() ;
		double volumnChange = rec.volumeChangeAverage(volumn);
		return (nowp - maX) * volumnChange / maX;
	}
	private double getMa(D1BarRecordReader r12, int pos, long volumn) {
		long total = 0;
		for(int x = 0; x < maDayNumber; x ++){
			total += r12.getRecord(pos - x - 1).getVolume();//.averagePriceChange(volumn);
		}
		double avePrice = 0;
		for(int x = 0; x < maDayNumber; x ++){
			double frag = r12.getRecord(pos - x - 1).getVolume()/total;//.averagePriceChange(volumn);
			avePrice = frag * r12.getRecord(pos - x - 1).averagePrice();
		}
		return avePrice;
	}
	static int maDayNumber = 5;
	
	public static void main(String[] args) throws Exception {
		File base = new File("E:\\all\\ext");
		File fs [] = base.listFiles();
		
		File f1 = fs[0];
		File f2 = fs[1];
		D1BarRecordReader r1 = new D1BarRecordReaderFromFile(f1);
		D1BarRecordReader r2 = new D1BarRecordReaderFromFile(f2);
		
		DeviateAverageWeightedPriceBaseOnMa dap = new DeviateAverageWeightedPriceBaseOnMa(1, r1, r2);
		int little = 10000;
		while(dap.hasNextDeviateValue()){
			double var = dap.nextDeviateValue();
			double d = ((int)(little * var)) ;
			System.out.println(dap.getCurDate() + "\t" + d);
		}
	}

	@Override
	protected void skipHead() {
		this.setStep(maDayNumber);
	}

}
