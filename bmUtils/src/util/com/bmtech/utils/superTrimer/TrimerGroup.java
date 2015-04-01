package com.bmtech.utils.superTrimer;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import com.bmtech.utils.log.BmtLogger;
import com.bmtech.utils.log.LogLevel;

public class TrimerGroup {
	protected Vector<SuperTrimer>vector = new Vector<SuperTrimer>();

	@SuppressWarnings("rawtypes")
	static final Hashtable<String,Class>table = new Hashtable<String,Class>();

	static{
		forceRegTrimer(Finder.NAME.toLowerCase(), Finder.class);
		forceRegTrimer(Findor.NAME.toLowerCase(), Findor.class);
		forceRegTrimer(RegFindor.NAME.toLowerCase(), RegFindor.class);
		forceRegTrimer(Replacer.NAME.toLowerCase(), Replacer.class);
		forceRegTrimer(WhiteTrimer.NAME.toLowerCase(), WhiteTrimer.class);
		forceRegTrimer(Wrappper.NAME.toLowerCase(), Wrappper.class);
		forceRegTrimer(Formater.NAME.toLowerCase(), Formater.class);
		forceRegTrimer(HtmlTrimer.NAME.toLowerCase(), HtmlTrimer.class);
		forceRegTrimer(EncodeTrimer.NAME.toLowerCase(), EncodeTrimer.class);
		forceRegTrimer(Rewind.NAME.toLowerCase(), Rewind.class);
		forceRegTrimer(RewriteTrimer.NAME.toLowerCase(), RewriteTrimer.class);
		forceRegTrimer("lnkext".toLowerCase(), LinkExtractor.class);
	}
	public boolean tryRegTrimer(String name, Class<?> trimerClass) {
		if(hasReged(name)) {
			return false;
		}
		forceRegTrimer(name, trimerClass);
		return true;
	}
	public static boolean hasReged(String name) {
		name = name.toLowerCase().trim();
		if(table.contains(name)) {
			return true;
		}
		return false;
	}
	public static void forceRegTrimer(String name, Class<?> trimerClass) {
		name = name.toLowerCase().trim();
		if(table.contains(name)) {
			BmtLogger.instance().log(LogLevel.Warning, "old copy of %s:%s is replaced with %s",
					name, table.get(name), trimerClass);
		}
		table.put(name, trimerClass);
	}

	private static class TrimerFormatParser {
		//		static final int NumLen = 8;
		private int pos = 0;
		private String def;
		TrimerFormatParser(String defineString, int pos){
			this.def = defineString;
			this.pos = pos;
		}

		public TrimerFormatParser(String defineString){
			this(defineString,0);
		}

		class Paras{
			String name;
			ArrayList<String> paras = new ArrayList<String>();
			void parse() throws Exception{
				if('[' !=  def.charAt(pos)){
					throw new RuntimeException(
							String.format("TrimerFormat can not parse String %s, position is %d. '[' is expected",
									def, pos));
				}
				pos ++;

				//find paras
				while(true){
					String sLen,sValue;
					sLen = def.substring(pos, pos + 8);
					int len;
					pos += 8;

					if(def.charAt(pos) != ':'){
						throw new Exception("format error, for : %s");
					}
					len = Integer.parseInt(sLen);
					pos ++;
					sValue = def.substring(pos, pos + len);
					if(this.name == null){
						this.name = sValue;
					}else{
						paras.add(sValue);
					}
					pos = pos + len;
					if(def.charAt(pos) == ']'){
						pos ++;
						break;
					}else{
						if(def.charAt(pos) == ':'){
							pos++;
							continue;
						}else{
							throw new Exception("format error, for : " + def);
						}
					}
				}		
			}
		}

		protected SuperTrimer getTrimer(String name){
			@SuppressWarnings("rawtypes")
			Class cls = table.get(name.toLowerCase());
			if(cls == null){
				return null;
			}
			try {
				return (SuperTrimer) cls.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		public Vector<SuperTrimer> parse() throws Exception{
			ArrayList<Paras>lst = new ArrayList<Paras>();
			while(pos < def.length()-1){
				if('[' == def.charAt(pos)){
					Paras pone = new Paras();
					pone.parse();
					lst.add(pone);
				}else {
					throw new RuntimeException("trimer define error : " + def);
				}
			}

			Vector<SuperTrimer>ret = new Vector<SuperTrimer>();
			for(Paras p : lst){
				SuperTrimer t = this.getTrimer(p.name);
				if(t == null){
					BmtLogger.instance().log(LogLevel.Error, "can not get SuperTrimer name = %s",
							p.name);
					continue;
				}
				t.setArgs(p.paras);
				ret.add(t);
			}
			return ret;
		}
	}
	public static Vector<SuperTrimer>parse(String def) throws Exception{
		TrimerFormatParser tfd = new TrimerFormatParser(def);
		return tfd.parse();
	}
	private String toDef() {
		StringBuilder sb = new StringBuilder();
		for(SuperTrimer st : this.vector) {
			sb.append(st.toDefineStr());
		}
		return sb.toString();
	}
	public String toString() {
		return toDef();
	}
	public TrimerGroup(Vector<SuperTrimer>vec) {
		this.vector.addAll(vec);
	}
	public TrimerGroup(String def) throws Exception{
		TrimerFormatParser tfd = new TrimerFormatParser(def);
		Vector<SuperTrimer> vec = tfd.parse();
		if(vec == null) {
			throw new Exception("parse error : " + def);
		}
		this.vector.addAll(vec);
	}
	public TrimerGroup clone() {
		TrimerGroup tg = new TrimerGroup();
		tg.vector.addAll(this.vector);
		return tg;
	}
	public TrimerGroup() {
	}
	public boolean isEmpty() {
		return this.vector.size() == 0;
	}
	public String trim(String input) {
		if(input == null) {
			return "";
		}
		for(SuperTrimer trimer : this.vector) {
			input = trimer.trim(input);
		}
		return input;
	}

	public Vector<SuperTrimer> getVector() {
		return this.vector;
	}
	public void insertElementAt(SuperTrimer trimer, int index) {
		this.vector.insertElementAt(trimer, index);
	}
	public void remove(int idx) {
		this.vector.remove(idx);
	}
	public void set(int index, SuperTrimer trimer) {
		this.vector.set(index, trimer);

	}
	public void add(SuperTrimer trimer) {
		this.vector.add(trimer);
	}

	public void clone(TrimerGroup tg) {
		synchronized(vector) {
			this.vector.clear();
			this.vector.addAll(tg.vector);
		}
	}
	public String toDefineStr() {
		return toDef();
	}

}
