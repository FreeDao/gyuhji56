package frameParser.titleExt;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ALBNum_level_1 extends SerialNumber{
	final int num;

	public ALBNum_level_1(String number, String prefix, String suffix){
		super(number, prefix, suffix);
		this.num = Integer.parseInt(number.substring(number.lastIndexOf(".") + 1));
	}

	@Override
	public SerialNumber next() {
		return new ALBNum_level_1(""+(num + 1), prefix, suffix);
	}
	
	public static ALBNum_level_1 match(String line){

		line = trimLine(line);
		if(line.length() == 0)
			return null;
		String regEx1 = "^(\\D{0,1})(\\d{1,2}\\.\\d{1,2})(\\D)";
		Pattern p = Pattern.compile(regEx1);
		Matcher m = p.matcher(line);
		if(m.find()){
			final String prefix = m.group(1);
			final String sNumber = m.group(2);
			final String suffix = m.group(3);
			final String[]fix = rewriteFix("", suffix);
			if(fix == null){
				System.out.println("ALB_l: " + line);
				return null;
			}
			return new ALBNum_level_1(sNumber, prefix, fix[1]);
		}
		return null;
	}

	@Override
	public int intNum() {
		return num;
	}

	@Override
	public String type() {
		return "ALBNum_1";
	}
	
	public static void main(String[]a){
		System.out.println(match("2.2.1 第4标："));
	}
}
