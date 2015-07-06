package com.bmtech.lexcion.ip202_114_40_175.port8080.concat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.bmtech.lexcion.ip202_114_40_175.port8080.concat.WordHashGen2WordHashLinkSortedConcatSortBySeg.ConcatLine;
import com.bmtech.utils.io.LineReader;
import com.bmtech.utils.io.LineWriter;
import com.bmtech.utils.io.diskMerge.MRecord;
import com.bmtech.utils.io.diskMerge.RecordReader;
import com.bmtech.utils.log.LogHelper;

public class StringMerge {
	class Group extends MRecord{
		Set<Integer>lineIds = new HashSet<Integer>();
		int segId;
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
	class Line{
		int id;
		String str;
	}
	final RecordReader groupReader;
	LineReader lr;
	StringMerge() throws Exception{
		lr = new LineReader(new File("/ulks/ulk-refine"));
		groupReader = new RecordReader(
				new File("/ulks/groups.txt"),
				Group.class
				);
	}

	HashMap<Integer, Line> loadSegment() throws Exception{
		HashMap<Integer, Line> ret = new HashMap<Integer, Line>();
		while(lr.hasNext()){
			String str = lr.next();
			Line l = new Line();
			int pos = str.indexOf('\t');
			l.id = Integer.parseInt(str.substring(0, pos));
			l.str = str.substring(pos + 1);
			ret.put(l.id, l);
		}
		return ret;
	}

	List<ConcatLine> loadConcatLines(int var) throws Exception{
		while(true){
			ConcatLine l = (ConcatLine) groupReader.peek();
			if(l == null)
				return null;
			if(l.getArr()[0] == var){
				break;
			}else{
				groupReader.take();
				System.out.println("SKIP " + l.serialize());
			}
		}
		List<ConcatLine>ret = new ArrayList<ConcatLine>();
		while(true){
			ConcatLine l = (ConcatLine) groupReader.peek();
			if(l == null)
				break;
			if(l.getArr()[0] != var){
				break;
			}
			ret.add(l);
			groupReader.take();
		}
		return ret;
	}

	static String concat(ConcatLine line, Map<Integer, Line>map){
		String s1 = map.get(line.getArr()[1]).str;
		String s2 = map.get(line.getArr()[2]).str;
		int len = line.getArr()[3];
		String s1x = s1.substring(0, len);
		String s2x = s2.substring(len);
		return s1x + s2x;
	}
	static HashMap<Integer, Line> lineMap;
	static class ConcatChain{
		LinkedList<ConcatLine> left = new LinkedList();
		LinkedList<ConcatLine> right = new LinkedList();
		public String buildString() {
			String ret = concat(right.get(0), lineMap);
			for(int x = 1; x < right.size(); x ++){
				ConcatLine cline = right.get(x);
				Line l = lineMap.get(cline.getArr()[2]);
				String k = l.str.substring(cline.getArr()[3]);
				ret += k;
			}
			for(int x = 1; x < left.size(); x ++){
				ConcatLine cline = left.get(x);
				Line l = lineMap.get(cline.getArr()[1]);
				String k = l.str.substring(0, cline.getArr()[3]);
				ret = k + ret;
			}

			return ret;
		}
	}
	static LineWriter lw;
	static{
		try {
			lw = new LineWriter("/ulks/lines", false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void tryGetLink(final List<Line> lines, final List<ConcatLine>concats, final Group g) throws IOException{
		LogHelper log = new LogHelper("g" + g.segId);
		final HashMap<Integer, List<ConcatLine>>from = new HashMap<Integer, List<ConcatLine>>();
		final HashMap<Integer, List<ConcatLine>>  to = new HashMap<Integer, List<ConcatLine>>();
		for(ConcatLine c : concats){
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
			for(int var = 0; var < lines.size(); var ++){
				Line x = lines.get(var);
				if(usedLines.contains(x.id)){
					continue;
				}else{
					checkLineId = var;
				}
			}
			if(checkLineId == null){
				break;
			}
			log.info("for new checkId %s, start checking ", checkLineId);

			usedLines.add(checkLineId);
			ConcatLine crtLine = concats.get(checkLineId);
			ConcatChain c = new ConcatChain();
			c.left.add(crtLine);
			c.right.add(crtLine);
			//linkleft
			linLeft(c, to, usedLines);
			log.info("left link ");
			//linkright
			linRight(c, from, usedLines);
			log.info("right link ");
			//check contains
			String str = c.buildString();

			//check not used
			for(int v : g.lineIds){
				if(usedLines.contains(v)){
					continue;
				}
				String tryConcat = lineMap.get(v).str;
				if(str.contains(tryConcat)){
					usedLines.add(v);
				}else{
					int ret = tryConcat(str, tryConcat);
					if(ret == -1){
						ret = tryConcat(tryConcat, str);
						if(ret != -1){
							str = tryConcat.substring(0, ret) + str;
							usedLines.add(v);
						}
					}else{
						str = str + tryConcat.substring(ret);
						usedLines.add(v);
					}
				}
				//check concat letf by str
			}

			lw.writeLine(g.segId + "\t" + str);
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
					if(s1.charAt(s1.length() - y) == s2.charAt(y)){
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
		int left = c.left.getLast().getArr()[1];
		List<ConcatLine> lst = to.get(left);
		if(lst == null){
			return;
		}
		int minIdx = 0, var = lst.get(0).getArr()[3];
		for(int x = 1; x < lst.size(); x ++){
			int v = lst.get(x).getArr()[3];
			if(var < v){
				var = v;
				minIdx = x;
			}
		}
		c.left.add(lst.get(minIdx));
		usedLines.add(minIdx);
		linLeft(c, to, usedLines);
	}
	static void linRight(ConcatChain c, final HashMap<Integer, List<ConcatLine>>  from, HashSet<Integer>usedLines){
		int right = c.right.getLast().getArr()[2];
		List<ConcatLine> lst = from.get(right);
		if(lst == null){
			return;
		}
		int minIdx = 0, var = lst.get(0).getArr()[3];
		for(int x = 1; x < lst.size(); x ++){
			int v = lst.get(x).getArr()[3];
			if(var < v){
				var = v;
				minIdx = x;
			}
		}
		c.right.add(lst.get(minIdx));
		usedLines.add(minIdx);
		linLeft(c, from, usedLines);
	}
}
