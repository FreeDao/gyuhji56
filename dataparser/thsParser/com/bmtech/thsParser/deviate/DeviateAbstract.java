package com.bmtech.thsParser.deviate;

import java.io.IOException;

import com.bmtech.thsParser.D1BarRecord;
import com.bmtech.thsParser.reader.D1BarRecordReader;

public abstract class DeviateAbstract {
	public static final String szCode = "1A0001";
	protected final D1BarRecordReader r1, r2;
	private final int fromTime ;
	protected int sumRange = 1;
	private int curDate;
	protected int step = 1;
	
	public DeviateAbstract(int sumRange, D1BarRecordReader r1, D1BarRecordReader r2){
		this(20110101, sumRange, r1, r2);
	}
	public DeviateAbstract(int fromTime, int sumRange, D1BarRecordReader r1, D1BarRecordReader r2){
		this.fromTime = fromTime;
		this.sumRange = sumRange;
		this.r1 = r1;
		this.r2 = r2;
		seekBeginTime();
		skipHead();
	}
	protected abstract void skipHead();
	protected void seekBeginTime(){
		r1.seekDay(getFromTime() + 2 * sumRange);
		r2.seekDay(getFromTime() + 2 * sumRange);
	}
	public double currentValue(D1BarRecordReader r12) {
		return value(r12, r12.getPosition());
	}

	public double prevValue(D1BarRecordReader r12) {
		return value(r12, r12.getPosition() - 1);
	}
	protected abstract double value(D1BarRecordReader r12, int pos);

	public boolean hasNextDeviateValue() throws IOException{
		return align();
	}
	public double nextDeviateValue(){
		double var1 = changedValue(r1);
		double var2 = changedValue(r2);
		nextInner(this.step);
		return  Math.abs(var2 - var1);
	}

	protected void nextInner(int step) {
		for(int x = 0; x < step; x ++){
			r1.next();
			r2.next();
		}
	}
	protected double changedValue(D1BarRecordReader r12) {
		double c = currentValue(r12);
		double c2 = prevValue(r12);
		return c2 - c;
	}
	private boolean align() throws IOException {
		while(true){
			D1BarRecord rec1 = r1.getRecord();
			if(rec1 == null){
				return false;
			}

			D1BarRecord rec2 = r2.getRecord();
			if(rec2 == null){
				return false;
			}
			if(rec1.getDate() == rec2.getDate()){
				curDate = rec1.getDate();
				return true;
			}else{
				if(rec1.getDate() > rec2.getDate()){
					if(r2.hasNextRecord()){
						r2.next();
						continue;
					}else{
						return false;
					}
				}else{
					if(r1.hasNextRecord()){
						r1.next();
						continue;
					}else{
						return false;
					}
				}
			}
		}
	}
	public int getSumRange() {
		return sumRange;
	}
	public int getCurDate() {
		return curDate;
	}
	public int getFromTime() {
		return fromTime;
	}
	public int getStep() {
		return step;
	}
	public void setStep(int step) {
		this.step = step;
	}
	
}
