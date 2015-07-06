package com.bmtech.lexcion.ip202_114_40_175.port8080.concat.toLine2;

import java.util.HashSet;
import java.util.Set;

import com.bmtech.utils.io.diskMerge.MRecord;

public class Group extends MRecord{
	Set<Integer>lineIds = new HashSet<Integer>();
	int segId;
	@Override
	public void init(String str) throws Exception {
		System.out.println("str.length():" + str.length());
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