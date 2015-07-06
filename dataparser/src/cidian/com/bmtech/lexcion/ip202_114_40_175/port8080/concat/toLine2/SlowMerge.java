package com.bmtech.lexcion.ip202_114_40_175.port8080.concat.toLine2;

import java.io.File;
import java.util.ArrayList;

import com.bmtech.utils.io.LineWriter;
import com.bmtech.utils.io.diskMerge.MRecord;
import com.bmtech.utils.io.diskMerge.RecordReader;

public class SlowMerge {
	public static int minMatch = 6;
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
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	int merge(String token[], String toAppend[]){
		for(int x = 0; x < token.length; x ++){
			int k = x;
			int y = 0;
			boolean match = true;
			for(; y < toAppend.length; ){
				if(token[k].equals(toAppend[y])){
					k ++;
					y ++;
					if(k < token.length){
						continue;
					}else{
						break;
					}
				}else{
					match = false;
					break;
				}
			}
			if(match){
				if(y == toAppend.length){
					return -2;//include
				}else if(y < toAppend.length){
					if(y < minMatch){
						return -1;//to small match
					}
					return y;
				}
			}
		}
		return -3;
	}


	String[]newx(String[]x, String[]y, int ret){
		
		int len = x.length + y.length - ret;
		String[]newx = new String[len];
		System.arraycopy(x, 0, newx, 0, x.length);
		System.arraycopy(y, ret, newx, x.length, y.length - ret);
		return newx;

	}
	ArrayList<String[]> mergeAll(String[][]lines){
		int len = lines.length;
		while(true){
			int before = lines.length;
			
			ArrayList<String[]> ret = merge(lines);
//			System.out.println(before + "--->" + ret.size());
			if(before == ret.size()){
				return ret;
			}
			if(before < ret.size()){
				throw new RuntimeException(before + "--->" + ret.size());
			}
			lines = new String[ret.size()][];
			for(int x = 0; x < ret.size(); x ++){
				lines[x] = ret.get(x);
			}
		}
	}
	ArrayList<String[]> merge(String[][]lines){
		ArrayList<String[]>lst = new ArrayList<String[]>();
		for(int x = 0; x < lines.length; x ++){
			if(lines[x] == null){
				continue;
			}
			boolean m = false;
			for(int y = x + 1; y < lines.length; y ++){
				if(lines[y] == null){
					continue;
				}
				{
					int ret = merge(lines[x], lines[y]);
					if(ret > 0){
						String[]newx = newx(lines[x], lines[y], ret);
						lines[x] = null;
						lines[y] = null;
						m = true;
						lst.add(newx);
						break;
					}else{
						if(ret == -2){
							m = true;
							lst.add(lines[x]);
							lines[x] = null;
							lines[y] = null;
							break;
						}
					}
				}{

					int ret = merge(lines[y], lines[x]);
					if(ret > 0){
						String[]newx = newx(lines[y], lines[x], ret);
						lines[x] = null;
						lines[y] = null;
						m = true;
						lst.add(newx);
						break;
					}else{
						if(ret == -2){
							m = true;
							lst.add(lines[y]);
							lines[x] = null;
							lines[y] = null;
							break;
						}
					}
				}
			}
			if(!m){
				lst.add(lines[x]);
			}
		}
		return lst;
	}
	int nextGid(RecordReader rr){
		VVR vr = (VVR) rr.peek();
		if(vr == null){
			return -1;
		}
		return vr.group;
	}
	String[][] load(RecordReader rr,int gid) throws Exception{
		
		System.out.println("loading " + gid);
		VVR vr;
		ArrayList<VVR>lst = new ArrayList<VVR>();
		while(true){
			vr = (VVR) rr.peek();
			if(vr == null || vr.group != gid){
				break;
			}
			lst.add(vr);
			rr.take();
			if(lst.size() % 1000 == 0){
				System.out.println(lst.size());
				if(lst.size() == 1000){
					break;
				}
			}
		}
		String[][]ret = new String[lst.size()][];
		for(int x = 0; x < lst.size(); x ++){
			ret[x] = lst.get(x).line.split(" ");
		}
		return ret;
	}
	public static void main(String[] args) throws Exception {
		File f = new File("E:\\00-beming\\merge344.txt");
		LineWriter lw = new LineWriter(
				new File(f.getParent(), "merge444.txt"), false);
		RecordReader rr = new RecordReader(f, VVR.class);
		SlowMerge sm = new SlowMerge();
		int index = 0;
		StringBuilder sb = new StringBuilder();
		while(true){
			int gid = sm.nextGid(rr);
			if(gid == -1){
				break;
			}
			String[][] ret = sm.load(rr, gid);
			if(ret == null){
				break;
			}
			ArrayList<String[]> lst = sm.mergeAll(ret);
			
			for(String[] x : lst){
				sb.setLength(0);
				sb.append(index ++);
				sb.append("\t");
				sb.append(gid);
				sb.append("\t");
				for(String k : x){
					sb.append(k);
					sb.append(" ");
				}
				lw.writeLine(sb.toString().trim());
			}
		}
	}
	public static void mainx(String[] args) {
		SlowMerge sm = new SlowMerge();
		String ss1[] = new String[]{
				"1", "2", "3", "4", "5", "6", "7"	
		};
		String ss2[] = new String[]{
				"4", "5", "6", "7", "8", "9"
		};
		int x = sm.merge(ss1, ss2);
		System.out.println(x);
	}
}
