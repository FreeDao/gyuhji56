package frameParser.titleExt;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CNNum_a2 extends SerialNumber{
	String[]nums = new String[]{
			"⒈","⒉","⒊","⒋","⒌","⒍","⒎","⒏","⒐","⒑","⒒","⒓","⒔","⒕","⒖","⒗","⒘","⒙","⒚","⒛"
	};
	final int realIntId;
	public CNNum_a2(String number, String prefix, String suffix) {
		super(number, prefix, suffix);
		realIntId = strToNum(number);
	}
	protected int strToNum(String str){
		int isNum = -1;
		for(int x = 0; x < nums.length; x ++){
			if(number.equals(nums[x])){
				isNum = x + 1 ;
				break;
			}
		}
		if(isNum < 1){
			return isNum;
		}
		return isNum;
	}
	@Override
	public SerialNumber next() {
		if(this.realIntId >= nums.length){
			return this;
		}
		return new CNNum_a2(nums[realIntId], this.prefix, this.suffix);
	}
	public static CNNum_a2 match(String line){

		line = trimLine(line);
		if(line.length() == 0)
			return null;
		String regEx1 = "^(\\D{0,1})([⒈⒉⒊⒋⒌⒍⒎⒏⒐⒑⒒⒓⒔⒕⒖⒗⒘⒙⒚⒛])(\\D{0,1})";
		Pattern p = Pattern.compile(regEx1);
		Matcher m = p.matcher(line);
		if(m.find()){
			final String prefix = m.group(1);
			final String sNumber = m.group(2);
			final String suffix = m.group(3);
			final String[]fix = rewriteFix(prefix, suffix);

			return new CNNum_a2(sNumber, fix[0], fix[1]);
		}
		return null;
	}

	public static void main(String[]a){
		System.out.println(match(" 　　３.投标人资格要求:"));
	}
	@Override
	public int intNum() {
		return realIntId;
	}
	@Override
	public String type() {
		return "CNNum_a2";
	}
}
