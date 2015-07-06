package dfcf;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import textMatcher.Pattern.def.reg.REnum;

import com.bmtech.utils.counter.Counter;
import com.bmtech.utils.counter.NumCount;
import com.bmtech.utils.io.LineReader;
import com.bmtech.utils.segment.CnSegment;
import com.bmtech.utils.segment.TokenHandler;

public class Ana {
	HashSet<String>words = new HashSet<String>();
	ArrayList<String>lst = new ArrayList<String>();
	Map<Character, List<Integer>>map = new HashMap<Character, List<Integer>>();
	HashSet<String>fbd = new HashSet<String>();
	Ana(){
		fbd.add("的");
		fbd.add("它们");
		fbd.add("");
		fbd.add("");
		fbd.add("");
		fbd.add("");
		fbd.add("");
	}
	void putLine(String line){
		line = line.trim();
		if(line.length() < 2)
			return;
		if(words.contains(line)){
			return;
		}
		int newPos = lst.size();
		System.out.println(newPos + ":" + line);

		for(int x = 0; x < line.length(); x ++){
			char c = line.charAt(x);
			if(Character.isLetterOrDigit(c)){
				List<Integer> lst = map.get(c);
				if(lst == null){
					lst = new ArrayList<Integer>();
					map.put(c, lst);
				}
				lst.add(newPos);
			}
			lst.add(line);
			words.add(line);
		}
	}

	public static void main(String[]args) throws IOException, Exception{
		File base = new File(Env.tmpFile, "dfcf/lines.txt");
		LineReader lr = new LineReader(base);
		Ana a = new Ana();
		while(lr.hasNext()){
			String line = lr.next();
			a.putTxt(line);
		}
		a.ana();
	}
	HashMap<String, Integer>alias = new HashMap<String, Integer>();
	CnSegment seg = CnSegment.instance(true);

	Counter<String>c = new Counter<String>();
	private List<String> mayList(String str){
		TokenHandler h = seg.segment(str);
		ArrayList<String>tokens = new ArrayList<String>();
		if(tokens.size() > 20){
			return tokens;
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
			for(int y = x + 1; y < tokens.size(); y++){
				
				String sy = tokens.get(y);
				if(!allow(sy)){
					continue;
				}
				String var = sx + "..." + sy;
				var = var.toLowerCase();
				segs.add(var);
				c.count(var);
			}
		}
		return segs;
	}
	
	private boolean allow(String sx) {
		return !fbd.contains(sx);
	}

	private List<String> mayList2(String str){
		ArrayList<String>pattern = new ArrayList<String>();
		for(int x = 0; x < str.length(); x ++){
			int left = str.length() - x ;
			left = left > 4 ? 4 : left;
			for(int y = 1; y < left; y ++){
				String prefix = str.substring(x, x + y);
				for(int z = x + y + 1; z< str.length(); z ++){
					for(int k = z + 1; k < str.length() &&(k -z <= 2); k ++){
						String has = prefix + "." + str.substring(z, k) ;
						if(this.alias.containsKey(has)){
							continue;
						}
						System.out.println(has);
						if(pattern.contains(has)){
							continue;
						}
						pattern.add(has);
					}
				}
			}
		}
		return pattern;
	}
	private void ana() {
		for(int x = 0; x < lst.size(); x ++){
			String var = lst.get(x);
			
			mayList(var);
		}
		List<Entry<String, NumCount>> top = c.topEntry(1000);
		for(Entry<String, NumCount> e : top){
			System.out.println(e);
		}

	}
	REnum re = new REnum(
			new char[]{'?', '!', '.', ',', ':','：',
					'？', '！', '。', '，',  '\n', '\r'} );
	private void putTxt(String line) {
		if(line.startsWith("-----------")){
			return;
		}
		int start = 0;
		int x = 0;
		for(; x < line.length(); x ++){
			char c = line.charAt(x);
			if(re.accept(c)){
				putLine(line.substring(start, x));
				start = x + 1;
			}
		}
		if( x > start){
			putLine(line.substring(start, x));
		}
	}
}
