package com.bmtech.lexcion.ip202_114_40_175.port8080.concat.toLine2;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.bmtech.utils.io.LineReader;
import com.bmtech.utils.io.LineWriter;
import com.bmtech.utils.io.diskMerge.RecordReader;
import com.bmtech.utils.log.LogHelper;

public class StringMerge {
	class Line{
		int id;
		String str;
	}
	final RecordReader groupReader, concatReader;
	LineReader lr;
	HashMap<Integer, Line> lineMap;

	static LineWriter lw;
	static{
		try {
			lw = new LineWriter("/ulks/corpus.lines.txt", false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	StringMerge() throws Exception{
		lr = new LineReader(new File("/ulks/ulk-refine"));
		groupReader = new RecordReader(
				new File("/ulks/groups.txt"),
				Group.class
				);
		concatReader = new RecordReader(
				new File("/ulks/bkts.simGroup.concat.used.sort.txt"),
				ConcatLine.class
				);
		System.out.println("loading lineMap");
		this.lineMap = this.loadLines();
		System.out.println("loading done");
	}
	public void run() throws Exception{
		while(groupReader.peek() != null){
			Group g = (Group) groupReader.take();
			List<ConcatLine> concats = loadConcatLines(g.segId);
			System.out.println("concating " + g.segId);
			tryGetLink(concats, g);
		}
	}
	HashMap<Integer, Line> loadLines() throws Exception{
		HashMap<Integer, Line> ret = new HashMap<Integer, Line>();
		while(lr.hasNext()){
			String str = lr.next();
			Line l = new Line();
			l.id = lr.currentLineNumber();
			l.str = str;
			ret.put(l.id, l);
		}
		return ret;
	}

	List<ConcatLine> loadConcatLines(int var) throws Exception{
		while(true){
			ConcatLine l = (ConcatLine) concatReader.peek();
			if(l == null)
				return null;
			if(l.getArr()[0] == var){
				break;
			}else{
				concatReader.take();
				System.out.println("SKIP " + l.serialize());
			}
		}
		List<ConcatLine>ret = new ArrayList<ConcatLine>();
		while(true){
			ConcatLine l = (ConcatLine) concatReader.peek();
			if(l == null)
				break;
			if(l.getArr()[0] != var){
				break;
			}
			ret.add(l);
			concatReader.take();
		}
		return ret;
	}


	public void tryGetLink(final List<ConcatLine>concats, 
			final Group g) throws IOException{
		LogHelper log = new LogHelper("g" + g.segId);
		log.info("-------------(%s)--------------", g.segId);
		final HashMap<Integer, List<ConcatLine>>from = new HashMap<Integer, List<ConcatLine>>();
		final HashMap<Integer, List<ConcatLine>>  to = new HashMap<Integer, List<ConcatLine>>();
		for(int x = 0; x < concats.size(); x ++){
			ConcatLine c = concats.get(x);
			c.index = x;
			List<ConcatLine> l = from.get(c.getArr()[1]);
			if(l == null){
				l = new ArrayList<ConcatLine>(4);
				from.put(c.getArr()[1], l);
			}
			l.add(c);

			l = to.get(c.getArr()[2]);
			if(l == null){
				l = new ArrayList<ConcatLine>(4);
				to.put(c.getArr()[2], l);
			}
			l.add(c);
		}
		log.info("load concat map ok");

		HashSet<Integer>usedLines = new HashSet<Integer>();
		while(true){
			Integer checkLineId = null;
			for(int var = 0; var < concats.size(); var ++){
				if(usedLines.contains(var)){
					continue;
				}else{
					checkLineId = var;
					break;
				}
			}
			if(checkLineId == null){
				break;
			}
			log.info("for new checkId %s, start checking ", checkLineId);
			usedLines.add(checkLineId);
			ConcatLine crtLine = concats.get(checkLineId);
			ConcatChain c = new ConcatChain();
			c.addLeft(crtLine);
			c.addRight(crtLine);
			//linkleft
			linLeft(c, to, usedLines);
			log.info("left link ");
			//linkright
			linRight(c, from, usedLines);
			log.info("right link ");
			//check contains
			String str = c.buildString(lineMap);
			for(int var = 0; var < concats.size(); var ++){
				if(usedLines.contains(var)){
					continue;
				}else{
					ConcatLine cc = concats.get(var);
					int[]arr = cc.getArr();
					if(c.hasId(arr[1])){
						if(c.hasId(arr[2])){
							continue;
						}else{
							String newstr = lineMap.get(arr[2]).str;
							int ret = tryConcat(str, newstr);
							str = str.substring(0, ret).trim() + newstr.substring(ret).trim();
							System.out.println(str);
							//should link to right
						}
					}else{
						if(c.hasId(arr[2])){
							//should link to left
							String newstr = lineMap.get(arr[1]).str;
							int ret = tryConcat(newstr, str);
							str = newstr.substring(0, ret).trim() + str.substring(ret).trim();
							System.out.println(str);
							
						}else{
							continue;
						}
					}
				}	
			}
			

			lw.writeLine(g.segId +"\t"+ c.toString()  + "\t=" + str);
		}
		log.info("finished");
	}
	static int tryConcat(String s1, String s2){
		if(s1.length() > 3 && s2.length() > 3){

			int checkStart = 3;
			int checkEnd = 9;

			if(checkEnd >= s2.length()){
				checkEnd = s2.length() - 1;
			}

			if(checkEnd >= s1.length()){
				checkEnd = s1.length() - 1;
			}
			if(checkEnd < checkStart){
				return -1;
			}
			for(int x = checkStart; x < checkEnd; x ++){
				boolean match = true;
				int y = 0;
				for(; y < x; y ++){
					if(s1.charAt(s1.length() - x  + y) == s2.charAt(y)){
						continue;
					}else{
						match = false;
						break;
					}
				}
				if(match){
					return x;
				}
			}
		}
		return -1;
	}
	static void linLeft(ConcatChain c, final HashMap<Integer, List<ConcatLine>>  to, HashSet<Integer>usedLines){
		ConcatLine cl = c.right.getLast();
		int left = cl.getArr()[1];
		List<ConcatLine> lst = to.get(left);
		if(lst == null){
			return;
		}
		int minIdx = -1, var = 0;
		for(int x = 0; x < lst.size(); x ++){
			if(usedLines.contains(lst.get(x).index)){
				continue;
			}
			int v = lst.get(x).getArr()[3];
			if(minIdx == -1){
				minIdx = x;
				var = v;
			}else{
				if(var < v){
					var = v;
					minIdx = x;
				}
			}
		}
		if(minIdx == -1){
			return;
		}
		c.addLeft(lst.get(minIdx));
		usedLines.add(lst.get(minIdx).index);
		linLeft(c, to, usedLines);
	}
	static void linRight(ConcatChain c, final HashMap<Integer, List<ConcatLine>>  from, 
			HashSet<Integer>usedLines){
		ConcatLine cl = c.right.getLast();
		System.out.println(cl);
		int right = cl.getArr()[2];
		List<ConcatLine> lst = from.get(right);
		if(lst == null){
			return;
		}
		int minIdx = -1, var = 0;
		for(int x = 0; x < lst.size(); x ++){
			if(usedLines.contains(lst.get(x).index)){
				continue;
			}
			int v = lst.get(x).getArr()[3];
			if(minIdx == -1){
				minIdx = x;
				var = v;
			}else{
				if(var < v){
					var = v;
					minIdx = x;
				}
			}
		}
		if(minIdx == -1){
			return;
		}
		c.addRight(lst.get(minIdx));
		usedLines.add(lst.get(minIdx).index);
		linRight(c, from, usedLines);
	}
	public static void main(String[] args) throws Exception {
		StringMerge sm = new StringMerge();
		sm.run();
	}
}
