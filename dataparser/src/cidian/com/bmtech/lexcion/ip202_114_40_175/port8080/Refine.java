package com.bmtech.lexcion.ip202_114_40_175.port8080;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;

import com.bmtech.utils.io.LineReader;
import com.bmtech.utils.io.LineWriter;

public class Refine {

	public static void main(String[] args) throws IOException {
		File refile = new File("/ulk-new");
		File refiled = new File("/ulk-refine");
		LineReader lr = new LineReader(refile);
		
		HashSet<String>set = new HashSet<String>();
		while(lr.hasNext()){
			set.add(lr.next().trim().replace('\t', ' ').replace("  ", " ").replace("  ", " "));
			if(lr.currentLineNumber() % 1000 == 0){
				System.out.println(lr.currentLineNumber());
			}
		}
		System.out.println("load done");
		LineWriter lw = new LineWriter(refiled, false);
		for(String x : set){
			lw.writeLine(x);
		}
		lw.close();
		lr.close();
	}
}
