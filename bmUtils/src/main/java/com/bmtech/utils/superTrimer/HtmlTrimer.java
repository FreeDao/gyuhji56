package com.bmtech.utils.superTrimer;

import java.util.ArrayList;

import com.bmtech.utils.HtmlRemover;
/**
 * TC trim function:
 * @paras 
 * <br>0 trim suffix and prefix
 * <br>1 formatted trim, all white space will be trimed as one
 * <br>2 remove all white space
 * <br>3 remove \r \n
 * <br>
 * <br>
 * 
 * @author Fisher@Beiming
 *
 */
public class HtmlTrimer extends SuperTrimer{
	public static final String NAME = "_HtmlRmv_";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void setArgs(ArrayList<String> paras) throws Exception {

		if(paras.size() != 0){
			throw new Exception(
					String.format("para number error, only need 0 for %s, but '%s'",
							NAME, paras));
		}
	}


	@Override
	public String toDefineStr() {
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		sb.append(toDefineString(this.getName()));
		sb.append(']');
		return sb.toString();
	}

	@Override
	public String trim(String input) {
		if(input == null)
			return "";
		input = HtmlRemover.htmlToLineFormat(input).lines;
		return input;
	}
	public String toString(){
		return String.format("[%s]", NAME);
	}

}
