package frameParser.titleExt;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ALBNum extends SerialNumber{
	final int num;

	public ALBNum(String number, String prefix, String suffix){
		super(number, prefix, suffix);
		this.num = Integer.parseInt(number);
	}

	@Override
	public SerialNumber next() {
		return new ALBNum(""+(num + 1), prefix, suffix);
	}
	static int Full_0_int = 65296;
	static int Full_9_int = 65305;
	public static String rewriteLine(String text){
		StringBuilder sb = new StringBuilder();
		char cs[] = text.toCharArray();
		for(char c : cs){
			if(c >= Full_0_int && c <= Full_9_int){
				sb.append((char)(c - Full_0_int + '0'));
			}else{
				sb.append(c);
			}
		}
		return sb.toString();
	}
	public static ALBNum match(String line){
		line = rewriteLine(line);
		line = trimLine(line);
		if(line.length() == 0)
			return null;
		String regEx1 = "^(\\D{0,1})([\\d]{1,2})(\\D)";
		Pattern p = Pattern.compile(regEx1);
		Matcher m = p.matcher(line);
		if(m.find()){
			final String prefix = m.group(1);
			final String sNumber = m.group(2);
			if(sNumber.trim().length() == 0)
				return null;
			final String suffix = m.group(3);
			final String[]fix = rewriteFix(prefix, suffix);
			if(fix == null){
//				System.out.println("ALB: " + line);
				return null;
			}
			return new ALBNum(sNumber, fix[0], fix[1]);
		}
		return null;
	}

	@Override
	public int intNum() {
		return num;
	}

	@Override
	public String type() {
		return "ALBNum";
	}
	public static void main(String[]a){
		System.out.println(match("附1：有效身份证件复印件"));
	}
	
}
