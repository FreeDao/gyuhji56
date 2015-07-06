package com.bmtech.lexcion.ip202_114_40_175.port8080.concat;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.bmtech.utils.bmfs.util.ReadProtocol;
import com.bmtech.utils.bmfs.util.WriteProtocol;
import com.bmtech.utils.counter.Counter;
import com.bmtech.utils.counter.NumCount;
import com.bmtech.utils.io.LineReader;
import com.bmtech.utils.io.LineWriter;

public class WordHashGen2reader4merge {
	LineReader lr;
	LineWriter lw;
	int roundSize = 16;
	HashMap<Integer, Counter<Integer>> map = new HashMap<Integer, Counter<Integer>>(2400 * 10000/ roundSize);

	void makeBuckets(int round) throws IOException{
		new File("/ulks//bkts.simGroup/").mkdirs();
		lr = new LineReader("/ulks//bkts.simGroup");
		map.clear();
		while(lr.hasNext()){
			if(lr.currentLineNumber() % 10000 == 0){
				System.out.println(round + " " + lr.currentLineNumber());
			}
			String line = lr.next().trim();
			String tokens[] = line.split(" ");
			int ids[] = new int[tokens.length];
			for(int x = 0; x < ids.length; x ++){
				ids[x] = Integer.parseInt(tokens[x]);
			}
			for(int x = 0; x < ids.length; x ++){
				if(ids[x] % roundSize != round){
					continue;
				}
				Counter c = map.get(ids[x]);
				if(c == null){
					c = new Counter<Integer>();
					map.put(ids[x], c);
				}
				for(int y = 0; y < ids.length; y ++){
					c.count(ids[y]);
				}
			}
		}
		Iterator<Entry<Integer, Counter<Integer>>>  itr = map.entrySet().iterator();
		lw = new LineWriter("/ulks//bkts.simGroup.merge", false);
		StringBuilder sb = new StringBuilder();
		while(itr.hasNext()){
			Entry<Integer, Counter<Integer>> e = itr.next();
			sb.setLength(0);
			List<Entry<Integer, NumCount>> tn = e.getValue().topEntry();
			sb.append(e.getKey());
			for(Entry<Integer, NumCount> ex : tn){
				sb.append("\t");
				sb.append(ex);
			}
			lw.writeLine(sb);
		}
		

	}

	public static void main(String[]args) throws IOException{
		WordHashGen2reader4merge h = new WordHashGen2reader4merge();
		h.makeBuckets();
	}

	private void makeBuckets() throws IOException {
		for(int x = 0; x < this.roundSize; x ++){
			System.gc();
			this.makeBuckets(x);
		}
		lw.close();
	}
}
