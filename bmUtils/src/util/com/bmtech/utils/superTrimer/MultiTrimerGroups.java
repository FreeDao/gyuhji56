package com.bmtech.utils.superTrimer;

import java.util.Vector;

public class MultiTrimerGroups {
	private Vector<TrimerGroup>groups = new Vector<TrimerGroup>();
	public MultiTrimerGroups(Vector<TrimerGroup>groups){
		this.groups = groups;
	}
	public MultiTrimerGroups(String def) throws Exception{
		this.fromDefineStr(def);
	}
	public String toDefineStr() {
		if(this.isEmpty()) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for(TrimerGroup tg : groups) {
			if(tg.isEmpty()) {
				continue;
			}
			sb.append(new Rewind().toDefineStr());
			sb.append(tg.toDefineStr());
		}
		return sb.toString();
	}
	protected void fromDefineStr(String def) throws Exception {
		if(def == null)
			return;
		def = def.trim();
		if(def.length() == 0)
			return;
		Vector<SuperTrimer> vec = TrimerGroup.parse(def);
		Vector<SuperTrimer> crt  = new Vector<SuperTrimer>();
		for(SuperTrimer st : vec) {
			if(st instanceof Rewind) {
				if(!crt.isEmpty()) {
					this.groups.add(new TrimerGroup(crt));
				}
				crt = new Vector<SuperTrimer>();//prepared for next group
			}else {
				crt.add(st);
			}
		}
		if(!crt.isEmpty()) {
			this.groups.add(new TrimerGroup(crt));
		}
	}
	public boolean isEmpty() {
		if(groups.size() == 0)
			return true;
		int ruleCount = 0;
		for(TrimerGroup tg : groups) {
			ruleCount += tg.vector.size();
		}
		return ruleCount == 0;
	}
	public String trim(String input) {
		StringBuilder sb = new StringBuilder();
		for(TrimerGroup tg : groups) {
			if(tg.isEmpty()) {
				continue;
			}
			String tmp = tg.trim(input);
			sb.append(tmp);
		}
		return sb.toString();
	}
	public Vector<TrimerGroup> getGroups() {
		return this.groups;
	}
	public String toString() {
		return this.toDefineStr();
	}
}
