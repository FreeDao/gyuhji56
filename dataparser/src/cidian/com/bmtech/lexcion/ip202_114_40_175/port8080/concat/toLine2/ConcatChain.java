package com.bmtech.lexcion.ip202_114_40_175.port8080.concat.toLine2;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

import com.bmtech.lexcion.ip202_114_40_175.port8080.concat.toLine2.StringMerge.Line;

class ConcatChain{
	LinkedList<ConcatLine> left = new LinkedList<ConcatLine>();
	LinkedList<ConcatLine> right = new LinkedList<ConcatLine>();
	HashSet<Integer>set = new HashSet<Integer>();
	void addLeft(ConcatLine cl){
		set.add(cl.getArr()[1]);
		set.add(cl.getArr()[2]);
		left.add(cl);
	}
	void addRight(ConcatLine cl){
		set.add(cl.getArr()[1]);
		set.add(cl.getArr()[2]);
		right.add(cl);
	}
	public boolean hasId(int id){
		return set.contains(id);
	}
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(int x = left.size(); x > 0; x --){
			sb.append(left.get(x - 1));
			if(sb.length() > 0){
				sb.append("\t");
			}
		}
		sb.append("|");
		for(int x = 1; x < right.size(); x ++){
			sb.append(right.get(x));
		}
		
		return sb.toString();
	}
	public String buildString(HashMap<Integer, Line>lineMap) {
		String ret = concat(right.get(0), lineMap);
		for(int x = 1; x < right.size(); x ++){
			ConcatLine cline = right.get(x);
			Line l = lineMap.get(cline.getArr()[2]);
			String k = l.str.substring(cline.getArr()[3]);
			ret = ret + " " + k;
		}
		for(int x = 1; x < left.size(); x ++){
			ConcatLine cline = left.get(x);
			Line l = lineMap.get(cline.getArr()[1]);
			String k = l.str.substring(0, l.str.length() - cline.getArr()[3]).trim();
			ret = k + " " + ret;
		}

		return ret;
	}
	
	static String concat(ConcatLine line, Map<Integer, Line>map){
		String s1 = map.get(line.getArr()[1]).str;
		String s2 = map.get(line.getArr()[2]).str;
		int len = line.getArr()[3];
		String s1x = s1.substring(0, s1.length() - len).trim();
		String s2x = s2.substring(len).trim();
		return s1x + " "+ s2x;
	}
}