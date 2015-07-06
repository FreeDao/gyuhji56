package com.bmtech.lexcion.ip202_114_40_175.port8080.concat;

import java.io.IOException;
import java.util.HashMap;

import com.bmtech.utils.io.LineReader;
import com.bmtech.utils.io.LineWriter;
import com.bmtech.utils.log.LogHelper;

public class WordHashConcat {
	int blockSize = 1024 * 32;
	int slotSize = 1024;
	String arr[][] = new String[slotSize][];
	int size = 0;
	synchronized void addTo(String value){
		int[]pos = getPos(size);
		besureMemoryExists(pos);
		arr[pos[0]][pos[1]] = value;
		size ++;
	}
	public String get(int index){
		int[] pos = getPos(index);
		if(arr[pos[0]] == null){
			return null;
		}
		return arr[pos[0]][pos[1]];
	}
	private void besureMemoryExists(int []pos) {
		if(arr[pos[0]] == null){
			arr[pos[0]] = new String[blockSize];
		}		
	}
	private int[] getPos(int x) {
		return new int[]{
				x / blockSize,
				x % blockSize,	
		};
	}
	void loadLines() throws IOException{
		LineReader lr = new LineReader("/ulks/ulk-refine");
		while(lr.hasNext()){
			if(0 == lr.currentLineNumber() % 10000){
				System.out.println("loading " + lr.currentLineNumber());
			}
			String line = lr.next();
			addTo(line);
		}
		lr.close();
	}

	LogHelper log = new LogHelper("concat");
	LineWriter lw;
	HashMap<Integer, int[]>map = new HashMap();
	public void concat() throws IOException{
		LineReader lr = new LineReader("/ulks/sims-concat.txt");
		while(lr.hasNext()){
			String line = lr.next();
			String []token = line.split("\\+");
			int[]arr = new int[]{
					Integer.parseInt(token[0]),
					Integer.parseInt(token[1]),
					Integer.parseInt(token[2]),
			};
			map.put(arr[0], arr);
			map.put(arr[1], arr);
		}
	}
	public static void main(String[]args) throws IOException{
		WordHashConcat h = new WordHashConcat();
		h.loadLines();
		h.concat();
	}
}
