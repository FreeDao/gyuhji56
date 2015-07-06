package com.bmtech.lexcion.ip202_114_40_175.port8080.concat.toLine2;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.bmtech.lexcion.ip202_114_40_175.port8080.concat.toLine2.SlowMerge3.VVR;
import com.bmtech.utils.io.LineReader;
import com.bmtech.utils.io.LineWriter;
import com.bmtech.utils.io.diskMerge.MRTool;
import com.bmtech.utils.io.diskMerge.MRecord;
import com.bmtech.utils.io.diskMerge.RecordReader;

public class CopyOfCopyOfSlowMerge4 {
	public static class VVR extends MRecord{
		int group;
		int id;
		String line;
		@Override
		protected void init(String str) throws Exception {
			int pos1 = str.indexOf("\t");
			id = Integer.parseInt(str.substring(0, pos1));
			int pos2 = str.indexOf("\t", pos1 + 1);
			group = Integer.parseInt(str.substring(pos1 + 1, pos2));
			line = str.substring(pos2 + 1).trim();
		}

		@Override
		public String serialize() {
			StringBuilder sb = new StringBuilder();
			sb.append(id);
			sb.append("\t");
			sb.append((int)(Math.random() * 1000000));
			sb.append("\t");
			sb.append(line);
			return sb.toString();
		}
	}
	private static int hash(String[] tokens, int x, int ii) {
		int ret = 0;
		for(int i = 0; i < ii; i ++){
			ret = ret ^ tokens[i + x].hashCode();
		}
		return ret;
	}

	static void dox(int a, RecordReader rr) throws Exception{

		final HashMap<Integer, String[]>tMap = new HashMap<Integer, String[]>();


		HashMap<Integer, Set<Integer>>map = new HashMap<Integer, Set<Integer>>();
		int indexx = 0;
		while(true){
			++indexx;
			if(indexx == max){
				break;
			}
			VVR vr = (VVR) rr.take();
			if(vr == null){
				break;
			}

			if(rr.getReadedRecordNumber() % 10000 == 0){
				System.out.println(a + "\tcurrnet line number " + rr.getReadedRecordNumber() + " hash size " + map.size()+", max "+ max);
			}
			String tokens[]  = vr.line.split(" ");
			tMap.put(rr.getReadedRecordNumber(), tokens);
			for(int x = 0 ; x < tokens.length; x ++){
				if(tokens.length - x < checkLen){
					break;
				}
				int hash = hash(tokens, x, checkLen);
				Set<Integer>l = map.get(hash);
				if(l == null){
					l = new HashSet<Integer>(2);
					map.put(hash, l);
				}
				l.add(vr.id);
			}
		}
		System.out.println("link hash:" + map.size());
		Iterator<Set<Integer>> itg = map.values().iterator();

		int seq = 0;
		StringBuilder sb = new StringBuilder();
		while(itg.hasNext()){
			if(seq % 10000 ==0){
				System.out.println("seq " + seq);
			}
			Set<Integer>l = itg.next();
			if(l.size() == 0){
				continue;
			}
			String[][] ss = new String[l.size()][];
			int index = 0;

			for(int x : l){
				ss[index] = tMap.get(x);
				tMap.remove(x);
				index ++;
			}
			SlowMerge sm = new SlowMerge();
			ArrayList<String[]> str = sm.mergeAll(ss);

			write(lw, str, sb, ++seq);
		}
		Iterator<String[]>itr = tMap.values().iterator();
		System.out.println("tMap.size()" + tMap.size());
		while(itr.hasNext()){
			String[] var = itr.next();
			ArrayList<String[]>ss = new ArrayList<String[]>(1);
			ss.add(var);
			write(lw, ss, sb, ++seq);
		}
	}
	static void write(LineWriter lw, ArrayList<String[]>str, StringBuilder sb, int seq) throws IOException{
		for(String[] var : str){
			sb.setLength(0);
			sb.append((int)(Math.random() * 10000000));
			sb.append("\t");
			sb.append((int)(Math.random() * 10000000));
			sb.append("\t");
			for(String v : var){
				sb.append(v);
				sb.append(" ");
			}
			lw.writeLine(sb.toString().trim());
		}
	}
	static int checkLen = 7;
	static final int max = 800 * 1000;
	static LineWriter lw;
	public static void main(String[] args) throws Exception {
		int var = 2; 
		System.out.println("var=" + var);
		File f1 = new File("E:\\00-beming\\xxxxxx"+var+".txt");
		SlowMerge.minMatch = checkLen;

		RecordReader rr = new RecordReader(f1, VVR.class);
		for(int x = 0; x < 1000; x ++){
			rr.peek();
			System.out.println(rr.take().serialize());
		}
	}
}
