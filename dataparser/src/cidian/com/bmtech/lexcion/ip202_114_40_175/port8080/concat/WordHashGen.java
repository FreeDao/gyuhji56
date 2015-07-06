package com.bmtech.lexcion.ip202_114_40_175.port8080.concat;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import com.bmtech.utils.io.LineReader;
import com.bmtech.utils.io.LineWriter;

public class WordHashGen {
	static class VO{
		String word;
		LinkedList<Integer>lst = new LinkedList<Integer>();
		VO(String word){
			this.word = word;
		}
		void add(int add){
			lst.add(add);
		}
		public String toString(){
			return word + "\t" + lst;
		}
	}
	HashMap<String, VO>map = new HashMap<String, VO>(2500 * 10000);
	void makeBuckets() throws IOException{
		LineReader lr = new LineReader("/ulks/ulk-refine");
		map.clear();
		while(lr.hasNext()){
			if(0 == lr.currentLineNumber() % 10000){
				System.out.println(currentRound + " " + lr.currentLineNumber());
			}
			String line = lr.next();
			String []tokens = line.trim().split(" ");
			for(String x : tokens){
				x = x.trim();
				if(x.length() == 0){
					continue;
				}
				if(!accept(x)){
					continue;
				}
				VO vo = map.get(x);
				if(vo == null){
					vo = new VO(x);
					map.put(x, vo);
				}
				vo.add(lr.currentLineNumber());
			}
		}
		lr.close();
		LineWriter lw = new LineWriter("/ulks/refine-words/" + this.currentRound, false);
		Iterator<VO> itr = map.values().iterator();
		while(itr.hasNext()){
			VO vo = itr.next();
			lw.writeLine(vo);
		}
		lw.close();
	}

	private boolean accept(String x) {
		if(Math.abs(x.hashCode()) % maxRound == currentRound){
			return true;
		}
		return false;
	}
	final int maxRound = 8;
	int currentRound = 0;
	void makeBucketsAll() throws IOException{
		for(currentRound = 0; currentRound < this.maxRound; this.currentRound ++){
			makeBuckets();
		}
	}
	public static void main(String[]args) throws IOException{
		WordHashGen h = new WordHashGen();
		h.makeBucketsAll();
	}
}
