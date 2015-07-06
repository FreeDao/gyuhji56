package com.bmtech.lexcion.ip202_114_40_175.port8080.concat;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.bmtech.utils.bmfs.util.ReadProtocol;
import com.bmtech.utils.bmfs.util.WriteProtocol;
import com.bmtech.utils.io.LineWriter;

public class WordHashGen2Reader {
	int writerNumber =  1024;
	WriteProtocol lws []= new WriteProtocol[writerNumber];
	int stickSize = 5;
	StringBuilder sb = new StringBuilder();
	Comparator<int[]> cmp = new Comparator<int[]>(){

		@Override
		public int compare(int[] o1, int[] o2) {
			return o1[1] - o2[1];
		}
		
	};
	void read(int size, ReadProtocol rp) throws IOException{
		List<int[]>lst = new ArrayList<int[]>(size);
		for(int x = 0; x < size;  x ++){
			int arr[] = new int[]{
					rp.readI32(),
					rp.readI32()
			};
			assert arr[0] < 19000000;
			assert arr[0]  >=0;
			lst.add(arr);
		}
		System.out.println("collected, expect  " + size + " got " + lst.size());
		Collections.sort(lst, cmp);
		int start = 0;
		StringBuilder sb = new StringBuilder();
		while(start < lst.size()){
			int end = findRange(lst, start);
			if(end - start < 2){
				start = end;
				continue;
			}
			sb.setLength(0);
			for(int x = start; x < end; x ++){
				sb.append(lst.get(x)[0]);
				sb.append(' ');
			}
			lw.writeLine(sb);
			start = end;
		}
	}//403 - 318 = 85 * 2 = 170
	private int findRange(List<int[]> lst, int start) {
		int size = lst.size();
		int value = lst.get(start)[1];
		for(start ++;start < size;start ++){
			if(lst.get(start) [1]!= value){
				break;
			}
		}
		return start;
	}
	LineWriter lw;
	void makeBuckets() throws IOException{
		 lw = new LineWriter("/ulks//bkts.simGroup", false);
		File base = new File("/ulks//bkts");
		File fs[] = base.listFiles();
		for(File fss : fs){
			System.out.println("doing " + fss);
			int tokens = (int) (fss.length() / 4 / 2);
			
			FileInputStream fis = new FileInputStream(fss);
			ReadProtocol rp = new ReadProtocol(fis);
			read(tokens, rp);
		}
		lw.close();
	}

	public static void main(String[]args) throws IOException{
		WordHashGen2Reader h = new WordHashGen2Reader();
		h.makeBuckets();
	}
}
