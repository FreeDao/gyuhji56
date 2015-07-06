package com.bmtech.lexcion.ip202_114_40_175.port8080.concat.toLine2;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.bmtech.utils.io.LineWriter;
import com.bmtech.utils.io.diskMerge.MRTool;
import com.bmtech.utils.io.diskMerge.MRecord;
import com.bmtech.utils.io.diskMerge.RecordReader;

public class SlowMerge2 {
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
			sb.append(group);
			sb.append("\t");
			sb.append(line);
			return sb.toString();
		}

	}


	public static void main(String[] args) throws Exception {
		File f = new File("E:\\00-beming\\merge44.txt");
		File f2 = new File("E:\\00-beming\\merge244.txt");
		File f3 = new File("E:\\00-beming\\merge344.txt");
	int maxMergeLen = 150000;
	int minMergeLen = 30;
	int checkLen = 8;
		RecordReader rr = new RecordReader(f, VVR.class);
		final int max = 1000 * 1000 * 40;
		HashMap<Integer, List<Integer>>map = new HashMap<Integer, List<Integer>>();
		while(true){
			VVR vr = (VVR) rr.take();
			if(vr == null){
				break;
			}
			if(rr.getReadedRecordNumber() % 1000 == 0){
				System.out.println("currnet line number " + rr.getReadedRecordNumber() + " hash size " + map.size());
				if(rr.getReadedRecordNumber() >= max){
					break;
				}
			}
			String tokens[]  = vr.line.split(" ");
			if(tokens.length > maxMergeLen || tokens.length < minMergeLen ){
				continue;
			}
			
			int end = tokens.length - checkLen;
			for(int x = 0; x < end; x ++){
				int hash = hash(tokens, x, checkLen);
				List<Integer>l = map.get(hash);
				if(l == null){
					l = new ArrayList<Integer>(2);
					map.put(hash, l);
				}
				l.add(vr.id);
			}
		}
		Iterator<List<Integer>> itr = map.values().iterator();
		HashMap<Integer, Integer>map2 = new HashMap<Integer, Integer>();
		int newGid = 0;
		while(itr.hasNext()){
			List<Integer>lst = itr.next();
			if(lst.size() > 1 && lst.size() < 3){
				int gid = ++newGid;
				for(int x : lst){
					map2.put(x, gid);
				}
			}
			lst.clear();
		}
		map.clear();
		System.out.println("regroup:" + newGid);
		rr = new RecordReader(f, VVR.class);
		LineWriter lw = new LineWriter(f2, false);
		while(true){
			VVR vr = (VVR) rr.take();
			if(vr == null){
				break;
			}
			Integer v = map2.get(vr.id);
			if(v == null){
				v = ++newGid;
			}
			vr.group = v;
			lw.writeLine(vr.serialize());
		}
		lw.close();
		
		MRTool.sortFile(f3, f2, VVR.class, new Comparator<MRecord>(){

			@Override
			public int compare(MRecord o1, MRecord o2) {
				return ((VVR)o1).group - ((VVR)o2).group;
			}
			
		}, 3000*1000);
	}


	private static int hash(String[] tokens, int x, int ii) {
		int ret = 0;
		for(int i = x; i < ii; i ++){
			ret = ret ^ tokens[i].hashCode();
		}
		return ret;
	}
}
