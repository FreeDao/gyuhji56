package com.bmtech.lexcion.ip202_114_40_175.port8080.concat;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import com.bmtech.utils.io.LineWriter;
import com.bmtech.utils.io.diskMerge.MRecord;
import com.bmtech.utils.io.diskMerge.RecordReader;

public class WordHashGen2WordHashLinkSortedConcat {

	public static class MyRecord  extends MRecord{
		int ids[];
		public MyRecord(){
			ids = null;
		}

		@Override
		public void init(String str) throws Exception {
			String tokens[] = str.trim().split("\\+");
			ids = new int[3];
			for(int x = 0; x < ids.length; x ++){
				ids[x] = Integer.parseInt(tokens[x]);
			}
		}

		@Override
		public String serialize() {
			StringBuilder sb = new StringBuilder();
			for(int x : ids){
				if(sb.length() > 0){
					sb.append("+");
				}
				sb.append(x);
			}
			return sb.toString();
		}
	}
	AtomicInteger itg = new AtomicInteger();
	int del = 0;
	private static final Object PRESENT = new Object();
	class Segment{
		public final int id = itg.addAndGet(1);
		int refer = -1;
		private HashMap<Integer, Object>includes = new HashMap<Integer, Object>(8, 1.0f);
		public Segment(MyRecord cur) {
			
			includes.put(cur.ids[0], PRESENT);
			includes.put(cur.ids[1], PRESENT);
			mapx.put(cur.ids[0], this);
			mapx.put(cur.ids[1], this);
			mapFromSegId.put(id, this);
		}
		void grow(MyRecord rec){
			
			includes.put(rec.ids[0], PRESENT);
			includes.put(rec.ids[1], PRESENT);
			mapx.put(rec.ids[0], this);
			mapx.put(rec.ids[1], this);
		}
	}
	HashMap<Integer, Segment>mapx = new HashMap<Integer, Segment>();
	HashMap<Integer, Segment>mapFromSegId = new HashMap<Integer, Segment>();

	int threshold(HashMap<Integer, Object> set) throws Exception{
		return (Integer)f.get(set);
	}
	int resize(int size){
		return (int) (8 + size * 1.5);
	}
	void merge(Segment f, Segment t) throws Exception{
		f.refer = t.id;
		int sum = f.includes.size() + t.includes.size();
		int thresh = threshold(t.includes);
		if(sum < thresh){
			t.includes.putAll(f.includes);
			f.includes.clear();
			f.includes = null;
			System.out.println("....................no need create new, sum =" + sum + ", thread=" + thresh);
		}else{
			HashMap<Integer, Object>includes = new HashMap<Integer, Object>(resize(f.includes.size() + t.includes.size()), 1.0f);
			includes.putAll(f.includes);
			includes.putAll(t.includes);
			t.includes.clear();
			f.includes.clear();
			t.includes = null;
			f.includes = null;
			t.includes = includes;
		}
		del ++;
	}
	Segment get(int x){
		Segment seg = mapx.get(x);
		if(seg == null){
			return null;
		}else{
			while(seg.refer != -1){
				Segment segx = this.mapFromSegId.get(seg.refer);
				if(segx == null){
					throw new RuntimeException("null seg " + seg.refer + ", from seg " + seg.id);
				}
				seg = segx;
			}
			return seg;
		}
	}

	void concat() throws Exception{
		File f = new File("/ulks/bkts.simGroup.concat.sort.txt");
		RecordReader reader = new RecordReader(f, MyRecord.class);
		MyRecord cur;
		int skip = 0;
		while((cur = (MyRecord) reader.take()) != null){
			if(cur.ids[2] < 7){
				skip ++;
				continue;
			}
			if(reader.getReadedRecordNumber() % 1000 == 0){
				System.out.println(skip + "--------------------------- " + reader.getReadedRecordNumber() + "  , segs:" + (this.itg.get() - del));
				
			}
			Segment left = get(cur.ids[0]);
			Segment right = get(cur.ids[1]);
			if(left != null && right != null){
				if(left == right){
					left.grow(cur);
					System.out.println("SAME SEGMENT... " + left.id + ' ' + right.id);
				}else{//merge list into one
					left.grow(cur);
					merge(right, left);
					System.out.println("same id " + left.id + ' ' + right.id);
				}
			}else{
				if(left == null && right == null){
					Segment seg = new Segment(cur);
					System.out.println("make new, current id " + seg.id + ", read line " + reader.getReadedRecordNumber() + ", segs:" + (this.itg.get() - del));
				}else{
					if(left != null){
						left.grow(cur);
					}else{
						right.grow(cur);
					}
				}
			}
		}

		Collection<Segment> cols = mapFromSegId.values();
		File fx = new File("/ulks/bkts.artSeg.txt");
		LineWriter lw = new LineWriter(fx, false);
		StringBuilder sb = new StringBuilder();
		for(Segment s : cols){
			if(s.refer != -1){
				continue;
			}
			sb.setLength(0);
			sb.append(s.id);
			sb.append('\t');
			for(int x : s.includes.keySet()){
				sb.append('\t');
				sb.append(x);
			}
			lw.writeLine(sb);
		}
		lw.close();
	}

	static Field f;
	public static void main(String[]args) throws Exception{
		f = HashMap.class.getDeclaredField("threshold");
		f.setAccessible(true);
		WordHashGen2WordHashLinkSortedConcat h = new WordHashGen2WordHashLinkSortedConcat();
		h.concat();
	}
}
