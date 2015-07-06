package com.bmtech.lexcion.ip202_114_40_175.port8080.concat;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import com.bmtech.lexcion.ip202_114_40_175.port8080.concat.WordHashGen.VO;
import com.bmtech.utils.counter.Counter;
import com.bmtech.utils.counter.NumCount;
import com.bmtech.utils.io.LineReader;
import com.bmtech.utils.io.LineWriter;
import com.bmtech.utils.log.LogHelper;

public class WordHashLoad {
	int maxWordFrq = 4000;
	File base = new File("/ulks/refine-words/" );
	HashMap<String, int[]>hash = new HashMap<String, int[]>();
	int maxId = 0;
	void load(File file) throws IOException{
		LineReader lr = new LineReader(file);
		while(lr.hasNext()){
			String line = lr.next();
			int pos = line.indexOf('\t');
			String word = line.substring(0, pos);
			String txt = line.substring(pos + 1);
			assert txt.charAt(0) == '[';
			assert txt.charAt(txt.length() - 1) == ']';
			txt = txt.substring(1, txt.length() - 1);
			String[] tokens = txt.split(", ");
			if(tokens.length > maxWordFrq){
				skip ++;
				continue;
			}
			not ++;
			int[]arr = new int[tokens.length];
			for(int x = 0; x < tokens.length; x ++){
				arr[x] = Integer.parseInt(tokens[x]);
				if(arr[x] > maxId){
					maxId = arr[x];
				}
			}
			hash.put(word, arr);
		}
		lr.close();
	}

	int skip = 0, not = 0;;
	void loadAll() throws IOException{
		for(File f : base.listFiles()){
			System.out.println("load " + f);
			load(f);
		}
		System.out.println("hash.size(): " + hash.size() + ", maxid: " + maxId);
		System.out.println( skip + " " + not);
	}

	void getSims() throws IOException{

		final LineReader lr = new LineReader("/ulks/ulk-refine");
		final LineWriter lw = new LineWriter("/ulks/sims.txt", false);
		for(int x = 0; x < 4; x ++){
			Thread t = new Thread(){

				public void run(){
					StringBuilder sb = new StringBuilder();
					while(true){

						String line;
						int number;
						synchronized(lr){
							if(!lr.hasNext()){
								break;
							}
							number = lr.currentLineNumber();

							line = lr.next();

						}
						if(number % 1000 == 0){
							System.out.println(number + " " + line);
						}
						String []tokens = line.trim().split(" ");
						Counter<Integer>counter = new Counter<Integer>();
						for(String x : tokens){
							x = x.trim();
							if(x.length() == 0){
								continue;
							}

							int arr[] = hash.get(x);
							if(arr == null){
//								System.out.println("null for '" + x + "'");
								continue;
							}
							if(arr.length > maxWordFrq){
								//								System.out.println("skip " + x + ", size " + arr.length);
								continue;
							}
							for(int var : arr){
								counter.count(var);
							}
						}
						List<Entry<Integer, NumCount>> lst = counter.topEntry(32);
						sb.setLength(0);
						sb.append(number);
						sb.append('\t');
						for(Entry<Integer, NumCount> e : lst){
							if(e.getValue().intValue() < minSim){
								break;
							}
							sb.append(e);
							sb.append('\t');
						}
						synchronized(lw){
							try {
								lw.writeLine(sb);
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}

					}
				}
			};
			t.start();
		}
		Runtime.getRuntime().addShutdownHook(new Thread(){
			public void run(){
				lw.close();
				lr.close();
			}
		});
		new Thread(){
			public void run(){
				while(true){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					log.info("--------------------------------------");
				}
			}
		}.start();
	}
	LogHelper log = new LogHelper("log");
	int minSim = 2;
	public static void main(String[]args) throws IOException{
		WordHashLoad h = new WordHashLoad();
		h.loadAll();
		h.getSims();
	}
}
