package com.bmtech.lexcion.ip202_114_40_175.port8080;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;

import com.bmtech.utils.counter.Counter;
import com.bmtech.utils.counter.NumCount;
import com.bmtech.utils.io.LineReader;
import com.bmtech.utils.io.LineWriter;

public class TopWords {

	public static void main(String[]args) throws IOException{
		LineReader lr = new LineReader("/ulk-refine");
		Counter<String>cnt = new Counter<String>();
		while(lr.hasNext()){
			String line = lr.next();
			String tokens[] = line.split(" ");
			for(String x : tokens){
				x = x.trim();
				int pos = x.lastIndexOf('/');
				if(x.length() == 0){
					continue;
				}
				if(pos == -1){
					System.out.println("errorToken");
					continue;
				}
				x = x.substring(0, pos).trim();
				if(x.length() == 0){
					continue;
				}
				if(x.indexOf('/') != -1){
					System.out.println("errorToken2");
					continue;
				}
				if(x.length() == 1){
					if(!Character.isLetter(x.charAt(0))){
						continue;
					}
				}
				cnt.count(x);
			}
		}
		LineWriter lw = new LineWriter(new File("cidian/tops-new.txt"), false);
		Iterator<Entry<String, NumCount>> itr2 = cnt.topEntry().iterator();
		while(itr2.hasNext()){
			Entry e = itr2.next();
			System.out.println(e);
			lw.writeLine(e.getKey());
		}
		lw.close();
		
		
		
	}
}
