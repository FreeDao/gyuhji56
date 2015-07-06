package com.bmtech.lexcion.ip202_114_40_175.port8080.concat.toLine2;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.bmtech.utils.io.LineReader;
import com.bmtech.utils.io.LineWriter;
import com.bmtech.utils.io.diskMerge.MRTool;
import com.bmtech.utils.io.diskMerge.MRecord;
import com.bmtech.utils.io.diskMerge.RecordReader;

public class SlowMerge3 {
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
		{
			File f = new File("E:\\00-beming\\merge444.txt");
			File f2 = new File("E:\\00-beming\\merge244.txt");
			File f3 = new File("E:\\00-beming\\merge344.txt");
			int maxMergeLen = 15;
			int minMergeLen = 3;
			int checkLen = 6;
			int skip = 7 * 1000 * 1000;
			final int max = 11 *1000 * 1000;
			RecordReader rr = new RecordReader(f, VVR.class);

			HashMap<Integer, List<Integer>>map = new HashMap<Integer, List<Integer>>();
			while(true){

				VVR vr = (VVR) rr.take();
				if(vr == null){
					break;
				}
				if(skip > 0){
					skip --;
					continue;
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

				LinkedList<Integer>lst = new LinkedList<Integer>();
				for(int x = 0 ; x < tokens.length; x ++){
					if(tokens.length - x < checkLen){
						break;
					}
					int hash = hash(tokens, x, checkLen);
					lst.add(hash);
				}
				while(true){
					if(lst.size() > 4){
						lst.remove(2);
					}else{
						break;
					}
				}
				for(int hash : lst){
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
			LineWriter lws = new LineWriter("/alias.txt", false);
			while(itr.hasNext()){
				List<Integer>lst = itr.next();
				if(lst.size() > 1){
					int gid = ++newGid;
					for(int x : lst){
						Integer itg = map2.put(x, gid);
						if(itg != null && gid != itg){
							lws.writeLine(gid + " " + itg);
						}
					}
				}
				lst.clear();
			}
			lws.close();
			LineReader lr = new LineReader("/alias.txt");
			Map<Integer, Integer>aliaMap = new HashMap<Integer, Integer>();
			int inv = -1;
			while(lr.hasNext()){
				inv --;
				String sttr = lr.next();
				String[]ts = sttr.split(" ");
				int v1 = Integer.parseInt(ts[0]);
				int v2 = Integer.parseInt(ts[1]);
				Integer v1v = aliaMap.get(v1);
				Integer v2v = aliaMap.get(v2);
				int ret = inv;
				if(v1v != null && v2v != null && !v1v.equals(v2v)){
					aliaMap.put(v1, ret);
					aliaMap.put(v2, ret);
				}else{
					if(v1v != null){
						ret = v1v;
					}else if(v2v != null){
						ret = v2v;
					}
					aliaMap.put(v1, ret);
					aliaMap.put(v2, ret);
				}

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
				}else{
					Integer v2 = aliaMap.get(v);
					if(v2 != null){
						v = v2;
					}
				}
				vr.group = v;
				lw.writeLine(vr.serialize());
			}
			lw.close();
			System.out.println("try sort");
			MRTool.sortFile(f3, f2, VVR.class, new Comparator<MRecord>(){

				@Override
				public int compare(MRecord o1, MRecord o2) {
					return ((VVR)o1).group - ((VVR)o2).group;
				}

			}, 3*1000*1000);
		}
		System.gc();
		{
			Thread.sleep(5000);
			System.gc();
			SlowMerge.main(null);
		}
	}

	private static int hash(String[] tokens, int x, int ii) {
		int ret = 0;
		for(int i = x; i < ii; i ++){
			ret = ret ^ tokens[i].hashCode();
		}
		return ret;
	}
}
