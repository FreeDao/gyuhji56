package com.bmtech.thsParser.deviate;

import java.io.File;
import java.io.IOException;

import com.bmtech.thsParser.CodeInfo;
import com.bmtech.thsParser.D1BarRecord;
import com.bmtech.thsParser.reader.D1BarRecordReader;
import com.bmtech.thsParser.reader.D1BarRecordReaderFromFile;
import com.bmtech.utils.Consoler;

public class DeviateAverageWeightedPriceBaseOnMa2 extends DeviateAbstract{
	private CodeInfo  code;
	public DeviateAverageWeightedPriceBaseOnMa2(int startTime, int sumRange, D1BarRecordReader r1, D1BarRecordReader r2) throws IOException {
		super(startTime, sumRange, r1, r2);

		setCode(CodeInfo.getCodeInfo(r1.code));
	}
	protected double changedValue(D1BarRecordReader r12) {
		return this.value(r12, r12.getPosition());
	}
	protected double value(D1BarRecordReader r12, int pos){return 0;}
	public double nextDeviateValue(){
		int pos = r1.getPosition();
		D1BarRecord rec = r1.getRecord(pos);
		long volumn = getCode().volume;


		double maX = getMa(r1, pos);
		double nowp = rec.averagePrice() ;
		double volumnChange = rec.volumeChangeAverage(volumn);
		double nowchange = (nowp - maX) / maX;

		double szChange = r2.getRecord().priceChangeRatio(
				r2.getRecord(r2.getPosition() - 1).getClose());

		nextInner(1);
		double devAbs =  Math.abs(nowchange - szChange) ;

		return Math.sqrt(devAbs* volumnChange);
	}
	private double getMa(D1BarRecordReader r12, int pos) {
		long total = 0;
		for(int x = 0; x < sumRange; x ++){
			total += r12.getRecord(pos - x - 1).getVolume();//.averagePriceChange(volumn);
		}
		double avePrice = 0;
		for(int x = 0; x < sumRange; x ++){
			double frag = r12.getRecord(pos - x - 1).getVolume()/total;//.averagePriceChange(volumn);
			avePrice += frag * r12.getRecord(pos - x - 1).averagePrice();
		}
		return avePrice;
	}

	static int startTime = 20130600;
	static int endTime = 20131100;

	public static void main(String[] args) throws Exception {
		int maDayNumber = 1;
		File sz = new File("/all/1A0001.day.explain");
		D1BarRecordReader szReader = new D1BarRecordReaderFromFile(sz);
		File base = new File("E:\\all\\ext");
		File fs [] = base.listFiles();
		int index = 0;
		String name = Consoler.readString(":");
		File f1 = new File(base, name + ".day");
		D1BarRecordReader r1 = new D1BarRecordReaderFromFile(f1);

		DeviateAverageWeightedPriceBaseOnMa2 dap = new DeviateAverageWeightedPriceBaseOnMa2(startTime, maDayNumber, r1, szReader);
		int little = 10000;
		System.out.println(dap.getCode().code);
		while(dap.hasNextDeviateValue()){
			double var = dap.nextDeviateValue();
			double d = ((int)(little * var)) ;
			//			System.out.println(dap.getCurDate() + "\t" + d);
			System.out.println(dap.getCurDate()%10000 + "\t" + d);
		}
		System.out.println();
	}

	@Override
	protected void skipHead() {
		this.setStep(sumRange);
	}
	public CodeInfo getCode() {
		return code;
	}
	public void setCode(CodeInfo code) {
		this.code = code;
	}

}
