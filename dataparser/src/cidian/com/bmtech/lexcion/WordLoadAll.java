package com.bmtech.lexcion;

import java.io.IOException;

import com.bmtech.utils.io.LineReader;
import com.bmtech.utils.io.LineWriter;

public class WordLoadAll {

	LineReader lr ;
	LineWriter lw ;
	WordLoadAll() throws IOException{
		lr = new LineReader("cidian/xdhycd.txt");
		lw = new LineWriter("cidian/items.txt", false);
	}
	
	void loadCi() throws IOException{
		while(lr.hasNext()){
			String line = lr.next().trim();
			if(line.startsWith("*")){
				String word = line.substring(1, 2);
				lw.writeLine(word);
				System.out.println(word);
				continue;
			}
			if(!line.startsWith("【")){
				continue;
			}
			int pos = line.indexOf("】");
		//	System.out.println(line);
			String name = line.substring(1, pos);
			String explain = line.substring(pos + 1);
			System.out.println(name + " : " + explain);
			lw.writeLine(name);
		}
		lw.close();
	}
	public static void main(String[] args) throws IOException {
		WordLoadAll l = new WordLoadAll();
		l.loadCi();
		
	}
}
