package com.bmtech.lexcion;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;

import com.bmtech.utils.counter.Counter;
import com.bmtech.utils.counter.NumCount;
import com.bmtech.utils.io.LineReader;
import com.bmtech.utils.segment.Segment;
import com.bmtech.utils.segment.TokenHandler;

public class WordsCount {
	LineReader lr ;
	Segment seg;
	Counter<String>counter = new Counter<String>();
	WordsCount() throws IOException{
		lr = new LineReader("cidian/xdhycd.txt");
		
		seg = Segment.getSegment(new File("cidian/words.txt"));
	}
	
	void loadCi() throws IOException{
		int index = 0;
		while(lr.hasNext()){
			String line = lr.next().trim();
			if(!line.startsWith("【")){
				continue;
			}
			int pos = line.indexOf("】");
		//	System.out.println(line);
			String name = line.substring(1, pos);
			String explain = line.substring(pos + 1);
			System.out.println((++index) + name + " : " + explain);
			TokenHandler handler = seg.segment(explain);
			while(handler.hasNext()){
				String han = handler.next();
				counter.count(han);
			}
			
		}
	}
	public static void main(String[] args) throws IOException {
		WordsCount l = new WordsCount();
		l.loadCi();
		List<Entry<String, NumCount>> x = l.counter.topEntry(100);
		int idx = 0;
		for(Entry<String, NumCount>var  :x){
			System.out.println((++idx) + "\t" + var);
		}
		
	}
}
