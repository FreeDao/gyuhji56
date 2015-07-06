package com.bmtech.thsParser.reader;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Collection;

import com.bmtech.thsParser.CodeInfo;
import com.bmtech.thsParser.deviate.DeviateAverageWeightedPriceBaseOnMa2;
import com.bmtech.utils.io.LineWriter;
import com.bmtech.utils.rds.SourceDefine;

public class MaxValues {
	final double minDev;
	final int little = 100000;
	final int startTime;
	final int maDayNumber;
	final int []checkDays;
	final Connection conn;

	public MaxValues(final int checkDays[]) throws Exception{
		this(4500, checkDays);
	}
	public MaxValues(double minDev, final int checkDays[]) throws Exception{
		this(minDev, checkDays, 1);
	}
	public SimpleDateFormat getSimpleDateFormator(){
		return new SimpleDateFormat("yyyyMMdd");
	}
	public MaxValues(double minDev, final int checkDays[], int maDayNumber) throws Exception{
		this.maDayNumber = maDayNumber;
		conn = SourceDefine.getNewConnection("vars");
		this.checkDays = checkDays;
		this.minDev = minDev;
		int min = checkDays[0];
		for(int d : checkDays){
			if(d < min)
				min = d;
		}
		SimpleDateFormat sdf = getSimpleDateFormator();
		long mill = sdf.parse(min + "").getTime();
		mill -= 1000L * 60 * 60 * 24 * 15;
		this.startTime = Integer.parseInt(sdf.format(mill));
	}
	double[] checkCode(CodeInfo ci) throws Exception{
		double[]ret = new double[checkDays.length];
		D1BarRecordReader szReader = new D1BarRecordReaderFromDB(CodeInfo.szCode, conn);
		D1BarRecordReader r1 = new D1BarRecordReaderFromDB(ci.code, conn);

		DeviateAverageWeightedPriceBaseOnMa2 dap = new DeviateAverageWeightedPriceBaseOnMa2(startTime, maDayNumber, r1, szReader);

		while(dap.hasNextDeviateValue()){
			double var = dap.nextDeviateValue();
			double d = ((int)(little * var)) ;
			int v = dap.getCurDate();
			for(int x = 0; x < this.checkDays.length; x ++){
				if(checkDays[x] == v){
					ret[x] = d;
				}
			}
		}
		return ret;
	}

	public static void main(String[] args) throws Exception {
		LineWriter lw = new LineWriter("/maxValue", false);
		int checkDay[] = new int[25];
		for(int x = 1; x < 26; x ++){
			checkDay[x - 1] = 20140300 + x;
		}
		Collection<CodeInfo> infos = CodeInfo.getCodeInfos();
		MaxValues mv = new MaxValues(checkDay);
		int index = 0;
		for(CodeInfo ci : infos){
			if(!ci.acceptCode()){
				continue;
			}
			try{
				double ds[] = mv.checkCode(ci);
				System.out.println((++index) + "--------------------");
				System.out.println(ci);
				boolean has = false;
				for(int x = 0; x < checkDay.length; x ++){
					System.out.println("\t" + checkDay[x] + "\t" + ds[x]);
					if(ds[x]>= mv.minDev){
						has = true;
					}
				}
				if(has){
					lw.writeLine("--------------------");
					lw.writeLine(ci);
					for(int x = 0; x < checkDay.length; x ++){
						lw.writeLine(
								String.format("\t%d\t%05d" , checkDay[x], (int)ds[x]));
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

}
