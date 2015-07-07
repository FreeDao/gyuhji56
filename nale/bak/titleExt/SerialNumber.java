package frameParser.titleExt;

import java.util.ArrayList;

public abstract class SerialNumber {
	//FIXME
	/**
	 * 1. **去掉汉字间空格 
	 * 2.*****特殊符号局部寻优  如⑴⑵⑶ 1.1  1.2 等
	 * 3. **对1长度的分支，放弃
	 * 4. *****对没有文字描述的数字标题项， 放弃
	 */
	String prefix = "";
	String suffix = "";
	final String number;
	public abstract String type();
	public SerialNumber(String number){
		this(number, "", "");
	}
	public SerialNumber(String number, String prefix, String suffix){
		this.number = number;
		this.prefix = prefix;
		this.suffix = suffix;
	}
	public boolean lessThan(SerialNumber tNum){
		return this.intNum() < tNum.intNum();
	}
	public abstract int intNum();
	
	public String strValue(){
		return prefix + this.next() + suffix;
	}
	public String toString(){
		return prefix + "<" + number + ">" + suffix;
	}
	
	public abstract SerialNumber next();

	public boolean equals(SerialNumber num){
		if(this.prefix.equalsIgnoreCase(num.prefix) && this.number.equalsIgnoreCase(num.number)
				&& this.suffix.equalsIgnoreCase(num.suffix)){
			return true;
		}else{
			return false;
		}
	}
	public static final String trimLine(String line){
		line = line.replaceAll("二00([一二三四五六七八九])", "二OO$1");
		line = line.replaceAll("二0一0", "二O一O");
		line = line.replaceAll("二0([一二])([一二三四五六七八九])", "二O$1$2");
		return line.replace('　', ' ').replaceAll("\\s", " ");
	}

	public static ArrayList<SerialNumber> parseNum(String line){
		ArrayList<SerialNumber>lst = new ArrayList<SerialNumber>();
		SerialNumber tn;
		tn = ALBNum_level_2.match(line);
		if(tn != null){
			lst.add(tn);
		}
		
		tn = ALBNum_level_1.match(line);
		if(tn != null){
			lst.add(tn);
		}
		
		tn = CNNum_a1.match(line);
		if(tn != null){
			lst.add(tn);
		}
		tn = CNNum_a2.match(line);
		if(tn != null){
			lst.add(tn);
		}
		tn = CNNum_a3.match(line);
		if(tn != null){
			lst.add(tn);
		}
		tn = CNNum_a4.match(line);
		if(tn != null){
			lst.add(tn);
		}
		
		tn = ALBNum.match(line);
		if(tn != null)
			lst.add(tn);
		
		tn = CNNum.match(line);
		if(tn != null){
			lst.add(tn);
		}
		return lst;
	}
	

	static final String []GOOD_PREFIX = new String[]{
		"", "（", "("
	};
	static final String []GOOD_SUFFIX_1 = new String[]{
		"、", ".", "．",
	};
	static final String []GOOD_SUFFIX_2 = new String[]{
		")", "）",  
	};

	public static boolean goodPrefix(String prefix){

		for(String x : GOOD_PREFIX){
			if(prefix.equals(x)){
				return true;
			}
		}
		if(!Character.isLetterOrDigit(prefix.charAt(0))){
			return true;
		}
		return false;
	}

	public static boolean goodSuffix(String suffix){
		for(String x : GOOD_SUFFIX_1){
			if(suffix.equals(x)){
				return true;
			}
		}
		
		for(String x : GOOD_SUFFIX_2){
			if(suffix.equals(x)){
				return true;
			}
		}

		return false;
	}
	public static String[] rewriteFix(final String prefix, final String suffix){
		if(!goodPrefix(prefix)){
			return null;
		}
//		String goodSuffix = goodSuffix(suffix);
		if(goodSuffix(suffix)){
			return new String[]{
					prefix, suffix	
			};
		}else{
			return new String[]{
					prefix, ""	
			};
		}
	}
	public String pattern() {
		return prefix + this.type() + suffix;
	}
}
