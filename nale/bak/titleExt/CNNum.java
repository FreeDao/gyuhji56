package frameParser.titleExt;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CNNum extends SerialNumber{
	String[]nums = new String[]{"一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二", "十三", "十四", "十五", "十六", "十七", "十八", "十九", "二十", "二十一", "二十二", "二十三", "二十四", "二十五", "二十六", "二十七", "二十八", "二十九", "三十", "三十一", "三十二", "三十三", "三十四", "三十五", "三十六", "三十七", "三十八", "三十九", "四十", "四十一", "四十二", "四十三", "四十四", "四十五", "四十六", "四十七", "四十八", "四十九", "五十", "五十一", "五十二", "五十三", "五十四", "五十五", "五十六", "五十七", "五十八", "五十九", "六十", "六十一", "六十二", "六十三", "六十四", "六十五", "六十六", "六十七", "六十八", "六十九", "七十", "七十一", "七十二", "七十三", "七十四", "七十五", "七十六", "七十七", "七十八", "七十九", "八十", "八十一", "八十二", "八十三", "八十四", "八十五", "八十六", "八十七", "八十八", "八十九", "九十", "九十一", "九十二", "九十三", "九十四", "九十五", "九十六", "九十七", "九十八", "九十九", };
	final int realIntId;
	public CNNum(String number, String prefix, String suffix) {
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
		if(this.realIntId >= 100){
			return this;
		}
		return new CNNum(nums[realIntId], this.prefix, this.suffix);
	}
	public static CNNum match(String line){

		line = trimLine(line);
		if(line.length() == 0)
			return null;
		String regEx1 = "^([^一二三四五六七八九十]{0,1})([一二三四五六七八九十]{1,3})([^一二三四五六七八九十〇])";
		Pattern p = Pattern.compile(regEx1);
		Matcher m = p.matcher(line);
		if(m.find()){
			final String prefix = m.group(1);
			final String sNumber = m.group(2);
			final String suffix = m.group(3);
			final String[]fix = rewriteFix(prefix, suffix);
			if(fix == null){
				return null;
			}
			if(suffix.startsWith("○")){
				return null;
			}
			if(suffix.startsWith("〇")){
				return null;
			}
			if(suffix.startsWith("?")){
				return null;
			}
			return new CNNum(sNumber, fix[0], fix[1]);
		}
		return null;
	}

	
	@Override
	public int intNum() {
		return realIntId;
	}
	@Override
	public String type() {
		return "CNNum";
	}
	
	public static void main(String[]a){
		CNNum cn = match("（七一）结合战略性新兴产业发展，培育壮大新一代信息技术产业");
		System.out.println(cn.prefix + " " + cn.number + " " + cn.suffix);
	}
}
