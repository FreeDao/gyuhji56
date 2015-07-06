package com.bmtech.lexcion.ip202_114_40_175.port8080.concat;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.bmtech.utils.Consoler;
import com.bmtech.utils.io.LineWriter;
import com.bmtech.utils.io.diskMerge.MRecord;
import com.bmtech.utils.io.diskMerge.RecordReader;

public class WordHashGen2WordHashLinkSortedConcatLeft {

	public static class MyRecord  extends MRecord{
		int ids[];
		public MyRecord(){
			ids = null;
		}

		@Override
		public void init(String str) throws Exception {
			String tokens[] = str.trim().split("\\+");
			ids = new int[2];
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
	AtomicInteger linkIndexSeqGen = new AtomicInteger();
	int del = 0;
	Map<Integer, List<MyRecord>>mapLeftRecords = new HashMap<Integer, List<MyRecord>>();
	Map<Integer, Link>leftMap = new HashMap<Integer, Link>();
	HashSet<Integer>fbd = new HashSet<Integer>();

	ArrayList<MyRecord>tmp = new ArrayList<MyRecord>();
	Map<Integer, Link>map = new HashMap<Integer, Link>();
	private void collect(RecordReader reader) throws Exception {
		MyRecord rec = (MyRecord) reader.take();
		int cur = rec.ids[0];
		tmp.clear();
		tmp.add(rec);
		while(true){
			rec = (MyRecord) reader.peek();
			if(rec == null || rec.ids[0] != cur){
				break;
			}
			if(0 == reader.getReadedRecordNumber() % 10000){
				System.out.println("load " + reader.getReadedRecordNumber());
			}
			reader.take();
			tmp.add(rec);
		}
		List<MyRecord>tmp = new ArrayList<MyRecord>(this.tmp.size());
		tmp.addAll(this.tmp);
		if(tmp.size() == 0){
			System.out.println(tmp);
		}
		mapLeftRecords.put(cur, tmp);
	}
	RecordReader getReader() throws Exception{
		File f = new File("/ulks/bkts.simGroup.concat.sort.txt");
		return new RecordReader(f, MyRecord.class);
	}
	void concat() throws Exception{
		RecordReader reader = getReader();
		MyRecord cur;
		while(true){
			cur = (MyRecord) reader.peek();
			if(cur == null){
				break;
			}
			collect(reader);
		}
		tmp.clear();

		reader = getReader();
		while((cur = (MyRecord) reader.take()) != null){
			if(0 == reader.getReadedRecordNumber() % 10000){
				System.out.println("linking " + reader.getReadedRecordNumber());
			}
			new Link(cur);
		}
		Iterator<Link> itr = map.values().iterator();
		File f = new File("/ulks/bkts.simGroup.concat.sent.txt");
		LineWriter lw = new LineWriter(f, false);
		while(itr.hasNext()){
			Link l = itr.next();
			lw.writeLine(l);
		}
		lw.close();
	}

	class Link{
		final int id;
		List<Integer>segs = new ArrayList<Integer>();

		Link(MyRecord mr){
			id = linkIndexSeqGen.getAndAdd(1);
			this.segs.add(mr.ids[0]);
			fbd.add(mr.ids[0]);
			searchRight(mr.ids[1]);
		}
		public String toString(){
			StringBuilder sb = new StringBuilder();
			sb.append(id);
			sb.append("\t");
			String xs = segs.toString();
			sb.append(xs.subSequence(1, xs.length() - 1));
			return sb.toString();
		}
		void searchRight(int right){
			boolean goodBranch = false;
			while(true){
				List<MyRecord> myr = mapLeftRecords.get(right);
				if(myr == null){
					goodBranch = true;
					break;

				}
				int selectId = -1;
				int index = 0;
				boolean hasFbd = false;;
				for(; index < myr.size(); index ++){
					MyRecord mr = myr.get(index);
					Link l = leftMap.get(mr.ids[2]);
					if(l != null){
						l.linkLeft(this);
						return;
					}
					if(fbd.contains(mr.ids[2])){
						hasFbd = true;
						continue;
					}else{
						selectId = index;
					}
				}

				if(!hasFbd){
					if(selectId == -1){
						break;
					}else{
						fbd.add(right);
						right = myr.get(selectId).ids[1];
					}
					this.segs.add(right);
				}else{
					break;
				}
			}
			if(goodBranch){
				map.put(this.id, this);
				leftMap.put(this.segs.get(0), this);
				System.out.println("add new " + this.id + ", now has " + map.size()) ;
			}
		}
		private void linkLeft(Link l) {
			map.remove(l.id);
			leftMap.remove(this.segs.get(0));
			fbd.add(this.segs.get(0));//中间节点禁止搜索

			l.segs.addAll(this.segs);
			this.segs = l.segs;
			leftMap.put(this.segs.get(0), this);


		}
	}

	public static void main(String[]args) throws Exception{
		assert 5 ==3;
		WordHashGen2WordHashLinkSortedConcatLeft h = new WordHashGen2WordHashLinkSortedConcatLeft();
		h.concat();
		Consoler.readString("~");
	}
}
