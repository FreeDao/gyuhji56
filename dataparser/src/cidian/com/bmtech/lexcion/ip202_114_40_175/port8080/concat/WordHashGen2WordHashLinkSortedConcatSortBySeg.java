package com.bmtech.lexcion.ip202_114_40_175.port8080.concat;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.bmtech.utils.io.LineReader;
import com.bmtech.utils.io.LineWriter;
import com.bmtech.utils.io.diskMerge.MRTool;
import com.bmtech.utils.io.diskMerge.MRecord;
import com.bmtech.utils.io.diskMerge.RecordReader;

public class WordHashGen2WordHashLinkSortedConcatSortBySeg {

	static class Sim{
		Sim(String line){
			String[] tokens = line.split("+");
			from = Integer.parseInt(tokens[0]);
			to = Integer.parseInt(tokens[1]);
			len = Integer.parseInt(tokens[2]);
		}
		int from, to, len;
		
		public String toString(){
			return from + "+" + to + "\t" + len;
		}
	}
	static final File from = new File("/ulks//bkts.simGroup.concat.txt");
	static final File from_used = new File("/ulks//bkts.simGroup.concat.used.txt");
	static final File from_used_sort = new File("/ulks//bkts.simGroup.concat.used.sort.txt");
	static final File from_not_used = new File("/ulks//bkts.simGroup.concat.not.used.txt");
	public static void main(String[]args) throws Exception{
		WordHashGen2WordHashLinkSortedConcatSortBySeg_regroup h = new WordHashGen2WordHashLinkSortedConcatSortBySeg_regroup();
		h.load(h.fileGroup);

		System.out.println("make used record to file " + from_used);
		final LineWriter lwno = new LineWriter(from_not_used, false);
		final LineWriter lwuse = new LineWriter(from_used, false);
		LineReader lrFrom = new LineReader(from);
		RecordReader rr = new RecordReader(lrFrom, ConcatLineRecord.class);
		while(rr.peek() != null){
			ConcatLineRecord r1 = (ConcatLineRecord) rr.take();
			Integer seg1 = h.map.get(r1.ids[0]);
			Integer seg2 = h.map.get(r1.ids[1]);
			if(seg1 == null || seg2 == null || ((int)seg1 != seg2)){
				lwno.writeLine("0+" + r1.serialize());
			}else{
				lwuse.writeLine(seg1 + "+" + r1.serialize());
			}
		}
		lwuse.close();
		lwno.close();
		System.out.println("sorting");
		MRTool.sortFile(from_used_sort, from_used, ConcatLine.class, new Comparator<MRecord>(){

			@Override
			public int compare(MRecord o1, MRecord o2) {
				return ((ConcatLine)o1).getArr()[0] - ((ConcatLine)o2).getArr()[0] ;
			}
		}, 20 * 1024 * 1024);
	}
	public static class ConcatLine extends MRecord{
		private int[]arr;
		public int index;
		@Override
		protected void init(String str) throws Exception {
			String strs[] = str.split("\\+");
			setArr(new int[strs.length]);
			for(int x = 0; x < strs.length; x++){
				getArr()[x] = Integer.parseInt(strs[x]);
			}
		}

		public String toString(){
			return String.format("%s %s %s %s", arr[0], arr[1], arr[2], arr[3]);
		}
		@Override
		public String serialize() {
			StringBuilder sb = new StringBuilder();
			for(int x : getArr()){
				if(sb.length() > 0){
					sb.append("+");
				}
				sb.append(x);
			}
			return sb.toString();
		}

		public int[] getArr() {
			return arr;
		}

		public void setArr(int[] arr) {
			this.arr = arr;
		}
		
	}


}
