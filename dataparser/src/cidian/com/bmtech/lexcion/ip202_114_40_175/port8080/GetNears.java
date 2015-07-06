package com.bmtech.lexcion.ip202_114_40_175.port8080;

import java.io.File;
import java.io.IOException;

import com.bmtech.utils.io.LineReader;
import com.bmtech.utils.io.LineWriter;

public class GetNears {

	public static void main(String[] args) throws Exception {
		File f = new File("E:\\xxxxxx498\\xxxxxx498.txt");
		LineReader lr = new LineReader(f);
		LineWriter lw = new LineWriter("/tops.txt", false);
		while(lr.hasNext()){
			String line = lr.next();
			System.out.println(lr.currentLineNumber() + " "+ line);

			int pos = line.indexOf("\t");
			pos = line.indexOf("\t", pos + 1);
			line = line.substring(pos + 1).trim();

			String tokens[] = line.split(" ");
			if(tokens.length < 2){
				lw.writeLine(line);
			}else{
				String s1, s2;
				s1 = tokens[0] + " " +tokens[1];
				s2 = tokens[tokens.length - 2] + " " + tokens[tokens.length - 1];
//				System.out.println("\t" + s1);
//				System.out.println("\t" + s2);
				lw.writeLine(s1);
				lw.writeLine(s2);
			}
		}
		lr.close();
		lw.close();
	}
}
