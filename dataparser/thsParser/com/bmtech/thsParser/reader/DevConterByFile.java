package com.bmtech.thsParser.reader;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.File;

import com.bmtech.thsParser.deviate.DeviateAverageWeightedPriceBaseOnMa2;
import com.bmtech.utils.Consoler;

public class DevConterByFile {
	public static void main(String[] args) throws Exception {
		int startTime = 20130101;
		int maDayNumber = 1;
		File sz = new File("/all/1A0001.day.explain");
		D1BarRecordReader szReader = new D1BarRecordReaderFromFile(sz);
		File base = new File("E:\\all\\ext");
		while(true){

			try{
				String name = Consoler.readString("code:");
				File f1 = new File(base, name + ".day");
				D1BarRecordReader r1 = new D1BarRecordReaderFromFile(f1);

				DeviateAverageWeightedPriceBaseOnMa2 dap = new DeviateAverageWeightedPriceBaseOnMa2(startTime, maDayNumber, r1, szReader);
				int little = 100000;
				System.out.println(dap.getCode().code);
				StringBuilder sb = new StringBuilder();
				while(dap.hasNextDeviateValue()){
					double var = dap.nextDeviateValue();
					double d = ((int)(little * var)) ;
					//			System.out.println(dap.getCurDate() + "\t" + d);
					String str = dap.getCurDate()%10000 + "\t" + d;
					System.out.println(str);
					sb.append(str);
					sb.append("\n");
					
					StringSelection stringSelection = new StringSelection(sb.toString());
					Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
				}
				System.out.println();
			}catch(Exception e){
				e.printStackTrace();
			}
		}


	}
}
