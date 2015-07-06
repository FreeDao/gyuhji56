package com.bmtech.lexcion.ip202_114_40_175.port8080.concat;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.bmtech.utils.io.LineReader;
import com.bmtech.utils.io.LineWriter;
import com.bmtech.utils.io.diskMerge.MRTool;
import com.bmtech.utils.io.diskMerge.MRecord;
import com.bmtech.utils.io.diskMerge.RecordReader;

public class WordHashGen2WordHashLinkSortedConcatSortBySeg_regroup {

	public static class SegRecord  extends MRecord{
		Set<Integer>lineIds = new HashSet<Integer>();
		SegRecord refer = null;
		int segId;

		public SegRecord(){
		}

		@Override
		public void init(String str) throws Exception {
			String tokens[] = str.trim().split("\t");
			if(tokens[1].length() != 0){
				throw new Exception("ERROR!");
			}
			int size = tokens.length - 2;
			for(int x = 0; x < size; x ++){
				lineIds.add(Integer.parseInt(tokens[x + 2]));
			}
			this.segId = Integer.parseInt(tokens[0]);
		}

		@Override
		public String serialize() {
			StringBuilder sb = new StringBuilder();
			sb.append('\t');
			for(int x : lineIds){
				if(sb.length() > 0){
					sb.append('\t');
				}
				sb.append(x);
			}
			return sb.toString();
		}
	}
	static final Map<Integer, Integer>map = new HashMap<Integer, Integer>();
	static final Map<Integer, SegRecord>segs = new HashMap<Integer, SegRecord>();

	void load(File file) throws Exception{
		RecordReader rr = new RecordReader(file, SegRecord.class);
		while(rr.peek() != null){
			if(rr.getReadedRecordNumber() % 1000 == 0){
				System.out.println("segload " + rr.getReadedRecordNumber());
			}
			SegRecord mr = (SegRecord) rr.take();
			segs.put(mr.segId, mr);
			for(int x : mr.lineIds){
				Integer v = map.put(x, mr.segId);
//				if(v != null && v != mr.segId){
//					throw new RuntimeException("fail segment " + mr.segId + " for line " + x + ", old segment " + v);
//				}
			}
		}
		System.out.println("segLoad ok");
	}
	void tryLink() throws Exception{

		File f = new File("/ulks//bkts.simGroup.concat.txt");
		RecordReader rr = new RecordReader(f, ConcatLineRecord.class);
		LineWriter lw = new LineWriter("/ulks/bkts.simGroup.concat.notuse.txt", false);
		int notused = 0;
		int del = 0;
		while(rr.peek() != null){
			if(rr.getReadedRecordNumber() % 10000 == 0){
				System.out.println("current ------------- " + rr.getReadedRecordNumber());
			}
			ConcatLineRecord crr = (ConcatLineRecord) rr.take();
			if( crr.ids[2] < matchLen){
				continue;
			}
			Integer seg1 = map.get(crr.ids[0]);
			Integer seg2 = map.get(crr.ids[1]);
			if(seg1 == null && seg2 == null){
				lw.writeLine(crr.serialize());
				notused ++;
			}else if(seg1 != null && seg2 != null){
				if(seg1.intValue() != seg2.intValue()){
					SegRecord sx = get(segs, seg1);
					SegRecord sy = get(segs, seg2);
					if(sx != sy){
						sy.refer = sx;
						//							sx.lineIds.addAll(sy.lineIds);
						//							sy.lineIds = null;
						del ++;
					}
					//						System.out.println("need merge " + seg1 + " " + seg2);
				}
			}else{
				if(seg1 != null){
					get(segs, seg1).lineIds.add(crr.ids[1]);
				}else{
					get(segs, seg2).lineIds.add(crr.ids[0]);
				}
			}
		}
		lw.close();
		System.out.println("not used " + notused + ", delSeg = " + del);
		HashMap<Integer, List<Integer>>lst = new HashMap<Integer, List<Integer>>();
		Iterator<SegRecord> itr = segs.values().iterator();
		while(itr.hasNext()){
			SegRecord sr = itr.next();
			int id = sr.segId;
			int gid;
			while(true){
				if(sr.refer == null){
					gid = sr.segId;
					break;
				}else{
					sr = sr.refer;
				}
			}
			List<Integer> l = lst.get(gid);
			if(l == null){
				l = new ArrayList<Integer>();
				lst.put(gid, l);
			}
			l.add(id);
		}
		LineWriter lwx = new LineWriter("/ulks//bkts.simGroup.concat." + matchLen + ".txt", false);
		
		Iterator<List<Integer>> v = lst.values().iterator();
		int x = 0;
		while(v.hasNext()){
			List<Integer>vx = v.next();
			x += vx.size();
			lwx.writeLine(vx);
		}
		lwx.close();
		System.out.println("total use segments " + x);

		v = lst.values().iterator();
		x = 0;
		HashMap<Integer, Integer>itgMap = new HashMap<Integer, Integer>();
		map.clear();
		while(v.hasNext()){
			x --;
			List<Integer>vx = v.next();
			for(int var : vx){
				itgMap.put(var, x);
			}

		}
		System.out.println("regen itgMap " + itgMap.size());
		Iterator<Entry<Integer, SegRecord>>itrx = segs.entrySet().iterator();
		HashMap<Integer, Set<Integer>>newG = new HashMap<Integer, Set<Integer>>();
		int idex = 0;
		while(itrx.hasNext()){
			idex ++;
			if(idex % 100 == 0){
				System.out.println("set.size(): " + newG.size() + ", idex = " + idex);
			}
			Entry<Integer, SegRecord> e =  itrx.next();
			Integer itg = itgMap.get(e.getKey());
			if(itg == null){
				continue;
			}
			SegRecord var = e.getValue();
			Set<Integer>set = newG.get(itg);
			if(set == null){
				set = new HashSet<Integer>();
				newG.put(itg, set);
			}
			set.addAll(var.lineIds);
		}
		LineWriter lwxxx = new LineWriter(fileGroup, false);
		StringBuilder sb = new StringBuilder();
		List<Integer>iiilst = new ArrayList<Integer>();
		iiilst.addAll(newG.keySet());
		Collections.sort(iiilst, new Comparator<Integer>(){

			@Override
			public int compare(Integer o1, Integer o2) {
				return o1 - o2;
			}
			
		});
		for(int vvvxxx : iiilst){
			sb.setLength(0);
			Set<Integer> e = newG.get(vvvxxx);

			sb.append(vvvxxx);
			sb.append('\t');
			for(int xr : e){
				sb.append('\t');
				sb.append(xr);
			}
			lwxxx.writeLine(sb);
		}
		lwxxx.close();
		

	}
	void sortRefine() throws Exception{
		MRTool.sortFile(new File("/ulks/ulk-refine.sort.by.segs.txt"), new File("/ulks/ulk-refine.txt"), 
				LineRecord.class, new Comparator<MRecord>(){

			@Override
			public int compare(MRecord o1, MRecord o2) {
				LineRecord r1 = (LineRecord) o1;
				LineRecord r2 = (LineRecord) o2;

				return r1.segId - r2.segId;
			}
		},
		1024 * 1024);
	}
	void sortSegIds() throws Exception{
		MRTool.sortFile(new File("/ulks/ulk-refine.sort.by.segs.txt"), new File("/ulks/ulk-refine.txt"), 
				LineRecord.class, new Comparator<MRecord>(){

			@Override
			public int compare(MRecord o1, MRecord o2) {
				LineRecord r1 = (LineRecord) o1;
				LineRecord r2 = (LineRecord) o2;

				return r1.segId - r2.segId;
			}
		},
		1024 * 1024);
	}

	void regroup() throws Exception{
		final LineWriter lwno = new LineWriter(from_not_used, false);
		final LineWriter lwuse = new LineWriter(from_used, false);
		LineReader lrFrom = new LineReader(from);
		RecordReader rr = new RecordReader(lrFrom, ConcatLineRecord.class);
		while(rr.peek() != null){
			ConcatLineRecord r1 = (ConcatLineRecord) rr.take();
			Integer seg1 = map.get(r1.ids[0]);
			Integer seg2 = map.get(r1.ids[1]);
			if(seg1 == null || seg2 == null || (seg1 != seg2)){
				lwno.writeLine(r1.serialize());
			}else{
				lwuse.writeLine(r1.serialize());
			}
		}
		lwuse.close();
		lwno.close();
	}
	static int matchLen = 6;
	static File from = new File("/ulks//bkts.simGroup.concat.txt");
	static File from_used = new File("/ulks//bkts.simGroup.concat.used.txt");
	static File from_not_used = new File("/ulks//bkts.simGroup.concat.not.used.txt");
	static File fileGroup = new File("/ulks/groups.txt");
	static File fx = new File("/ulks/bkts.artSeg.txt");
	static void toGroup() throws Exception{
		WordHashGen2WordHashLinkSortedConcatSortBySeg_regroup h = new WordHashGen2WordHashLinkSortedConcatSortBySeg_regroup();
		h.load(fx);
		h.tryLink();
	}
	static void sortRefineTo() throws Exception{
		WordHashGen2WordHashLinkSortedConcatSortBySeg_regroup h = new WordHashGen2WordHashLinkSortedConcatSortBySeg_regroup();
		h.load(fileGroup);
		h.sortRefine();
	}
	public static void main(String[]args) throws Exception{
		toGroup();
//		sortRefineTo();
	}

	private static SegRecord get(Map<Integer, SegRecord> segs2, int x) {
		SegRecord ret = segs2.get(x);
		SegRecord rx = ret;
		while(true){
			if(ret.refer == null){
				return ret;
			}else{
				ret = ret.refer;
			}
			if(ret == rx){
				throw new RuntimeException("sdadfsafd");
			}
		}
	}

	public static class LineRecord extends MRecord{
		String str;
		private int id;
		protected int segId = 0;
		@Override
		protected void init(String str) throws Exception {
			int pos = str.indexOf("\t");

			this.id = Integer.parseInt(str.substring(0, pos));
			int pos0 = pos;
			pos = str.indexOf("\t", pos0 + 1);
			Integer segId = map.get(this.id);
			if(segId == null){
				this.segId = 0;
			}else{
				this.segId = segId;
			}
			this.str = str.substring(pos + 1);
		}

		@Override
		public String serialize() {
			return id + "\t" + segId + "\t"+ str;
		}
	}
}
