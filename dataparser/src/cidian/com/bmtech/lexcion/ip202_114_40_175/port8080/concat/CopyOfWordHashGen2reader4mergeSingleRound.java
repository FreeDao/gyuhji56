package com.bmtech.lexcion.ip202_114_40_175.port8080.concat;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.bmtech.utils.counter.Counter;
import com.bmtech.utils.io.LineReader;
import com.bmtech.utils.io.diskMerge.MOut;
import com.bmtech.utils.io.diskMerge.MRecord;
import com.bmtech.utils.io.diskMerge.RecordReader;

public class CopyOfWordHashGen2reader4mergeSingleRound {

	public static class MyRecord  extends MRecord{
		int ids[];
		public MyRecord(){
			ids = null;
		}
		
		@Override
		public void init(String str) throws Exception {
			String tokens[] = str.trim().split(" ");
			ids = new int[tokens.length];
			for(int x = 0; x < ids.length; x ++){
				ids[x] = Integer.parseInt(tokens[x]);
			}
		}

		@Override
		public String serialize() {
			StringBuilder sb = new StringBuilder();
			for(int x : ids){
				if(sb.length() > 0){
					sb.append(" ");
				}
				sb.append(x);
			}
			return sb.toString();
		}
	}

	public static void main(String[]args) throws Exception{
		CopyOfWordHashGen2reader4mergeSingleRound h = new CopyOfWordHashGen2reader4mergeSingleRound();
		h.mapOut();
	}

	private void mapOut() throws Exception {
		LineReader lr = new LineReader("R:/bkts.simGroup");
		RecordReader reader = new RecordReader(lr, MyRecord.class);
		int index = 0;
		Set<Integer>cnt = new HashSet(20000000);
		while(true){
			MyRecord mr = (MyRecord) reader.take();
			if(mr == null)
				break;
			index ++;
			if(index % 10000 == 0){
				System.out.println(index);
			}
			for(int x : mr.ids){
				cnt.add(x);
			}
			
		}
		lr.close();
		System.out.println("total" + cnt.size());
	}

}
