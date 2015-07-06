package dfcf.parseText;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.bmtech.utils.counter.Counter;
import com.bmtech.utils.counter.NumCount;
import com.bmtech.utils.io.LineReader;
import com.bmtech.utils.io.LineWriter;
import com.bmtech.utils.log.LogHelper;
import com.bmtech.utils.segment.CnSegment;
import com.bmtech.utils.segment.TokenHandler;

import dfcf.Env;

public class YYAnalysis {
	LogHelper log = new LogHelper("yya");
	Map<Character, List<Integer>>map = new HashMap<Character, List<Integer>>();
	
	HashSet<String>fbd = new HashSet<String>();
	YYAnalysis(){
		fbd.add("的");
		fbd.add("它们");
		fbd.add("");
		fbd.add("");
		fbd.add("");
		fbd.add("");
		fbd.add("");
	}

	HashMap<String, Integer>alias = new HashMap<String, Integer>();
	CnSegment seg = CnSegment.instance(true);

	Counter<String>c = new Counter<String>();
	private List<String> mayList(String str){
		TokenHandler h = seg.segment(str);
		ArrayList<String>tokens = new ArrayList<String>();
		if(tokens.size() > 20){
			return null;
		}
		while(h.hasNext()){
			tokens.add(h.next());
		}
		//		System.out.println("::" + var);
		//		System.out.println(tokens);

		ArrayList<String>segs = new ArrayList<String>();
		int end = tokens.size() - 2;

		for(int x = 0; x < end; x ++){
			String sx = tokens.get(x);
			if(!allow(sx)){
				continue;
			}
			for(int y = x + 2; y < tokens.size(); y++){

				String sy = tokens.get(y);
				if(!allow(sy)){
					continue;
				}
				String var = sx + "..." + sy;
				var = var.toLowerCase();
				segs.add(var);
			}
		}
		return segs;
	}

	private boolean allow(String sx) {
		return !fbd.contains(sx);
	}
	private void ana(File base) throws IOException {
		LineReader lr = new LineReader(base);
		Set<String>allow = new HashSet<String>();
		while(lr.hasNext()){
			String var = lr.next();
			if(var.length() > 25){
				continue;
			}
			if(allow.contains(var)){
				continue;
			}
			
			allow.add(var);
			List<String> lst = mayList(var);
			if(lst == null){
				continue;
			}
			for(String x : lst){
				c.count(x);
			}
			if(lr.currentLineNumber() % 1000 == 0){
				log.info("current line %s", lr.currentLineNumber());
			}
		}
		lr.close();
		List<Entry<String, NumCount>> top = c.topEntry(1000);
		Map<String, Set<String>>map = new HashMap<String, Set<String>>();
		for(Entry<String, NumCount> e : top){
			System.out.println(e);
			map.put(e.getKey(), new HashSet<String>());
		}
		lr = new LineReader(base);
		allow.clear();
		while(lr.hasNext()){
			String var = lr.next();
			if(var.length() > 25){
				continue;
			}
			if(allow.contains(var)){
				continue;
			}
			
			allow.add(var);
			List<String> lst = mayList(var);
			if(lst == null){
				continue;
			}
			for(String x : lst){
				Set<String> set = map.get(x);
				if(set != null){
					set.add(var);
				}
			}
			if(lr.currentLineNumber() % 1000 == 0){
				log.info("current line %s", lr.currentLineNumber());
			}
		}
		lr.close();
		Iterator<Entry<String, Set<String>>> itr = map.entrySet().iterator();
		LineWriter lw = new LineWriter(
				new File(Env.tmpFile, "dfcf/lines-parsed.txt"), false);
		while(itr.hasNext()){
			Entry<String, Set<String>> e = itr.next();
			lw.writeLine(e.getKey().trim());
			for(String x : e.getValue()){
				lw.writeLine("\t" + x.trim());
			}
		}
		lw.close();
	}
	
	public static void main(String[]args) throws IOException, Exception{
		File base = new File(Env.tmpFile, "dfcf/lines.txt");
		YYAnalysis a = new YYAnalysis();
		a.ana(base);
	}
}
