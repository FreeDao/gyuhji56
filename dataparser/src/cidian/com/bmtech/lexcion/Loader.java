package com.bmtech.lexcion;

import java.io.IOException;

import textMatcher.Pattern.tools.LineTool;

import com.bmtech.utils.io.LineReader;
import com.bmtech.utils.io.LineWriter;

public class Loader {

	LineReader lr ;
	LineWriter lw ;
	Loader() throws IOException{
		lr = new LineReader("cidian/xdhycd.txt");
		lw = new LineWriter("cidian/words.txt", false);
	}
	
	void loadCi() throws IOException{
		while(lr.hasNext()){
			String line = lr.next().trim();
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
		Loader l = new Loader();
		l.loadCi();
		
		String x = "（ｚｈｕóｍó";
		for(int i = 0; i < x.length(); i ++){
			System.out.println(x.charAt(i) + " " + Character.isLetter(x.charAt(i)) + " " +(int)(x.charAt(i)));
		}
		x = LineTool.lineFormat(x);
		System.out.println(":" + x);
		for(int i = 0; i < x.length(); i ++){
			System.out.println(x.charAt(i) + " " + Character.isLetter(x.charAt(i)) + " " +(int)(x.charAt(i)));
		}
	}
}
