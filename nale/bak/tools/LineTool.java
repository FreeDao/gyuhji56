package textMatcher.Pattern.tools;

import java.util.HashMap;

public class LineTool {
	static int Full_0_int = 65296;
	static int Full_9_int = 65305;
	private static final HashMap<Character, Character> map = new HashMap<Character, Character>();
	static{
		for(int x = Full_0_int; x <= Full_9_int; x ++){
			map.put((char) x, (char)('0' + x - Full_0_int));
		}
		map.put('　', ' ');
		map.put('\t', ' ');
		map.put('（', '(');
		map.put('）', ')');
		map.put('：', ':');
	}
	public static char SBCCaseFilter(char ch){
		char chnew;
		if (ch >= 65281 && ch <= 65374) {   
			chnew = ((char)(ch - 65248)); 
		}else if (ch == 12288){   
			chnew = (' ');   
		}else if(ch == 65377){   
			chnew = ('。');   
		}else if(ch == 12539){   
			chnew = ('·');   
		}else if(ch ==8226){ 
			chnew = ('·');   
		}else if(ch == '　'){ 
			chnew = (' ');   
		}else if(ch == '—'){ 
			chnew = ('-');   
		}else if(ch == '【'){ 
			chnew = ('[');   
		}else if(ch == '】'){ 
			chnew = (']');   
		}else{   
			chnew = (ch);   
		}

		return Character.toLowerCase(chnew);
	}
	
	public static String lineFormat3(String line){

		StringBuilder sb = new StringBuilder();
		char cs[] = line.toCharArray();
		for(char c : cs){
			c = SBCCaseFilter(c);
			if(Character.isLetterOrDigit(c)){
				sb.append(c);
			}else{
				sb.append(' ');
			}
		}
		line = sb.toString();
		line = line.replaceAll("\\s{2,}", " ").trim();
		return line;
	}
	public static String lineFormat2(String line){

		StringBuilder sb = new StringBuilder();
		char cs[] = line.toCharArray();
		for(char c : cs){
			c = SBCCaseFilter(c);
			sb.append(c);
		}
		line = sb.toString();
		line = line.replace('　', ' ').replaceAll("\\s{2,}", " ").trim();
		return line;
	}

	public static String lineFormat(String line){

		StringBuilder sb = new StringBuilder();
		char cs[] = line.toCharArray();
		for(char c : cs){
			Character rep = map.get(c);
			if(rep == null){
				sb.append(c);
			}else{
				sb.append(rep);
			}
		}
		line = sb.toString();
		line = line.replace('　', ' ').replaceAll("\\s{2,}", " ").trim();
		return line;
	}
}
