package com.bmtech.utils.superTrimer;

import java.util.ArrayList;

import com.bmtech.htmls.parser.Parser;
import com.bmtech.htmls.parser.util.NodeList;
import com.bmtech.htmls.parser.util.ParserException;

/**
 * By define the prefix and suffix, we FIND the String inside it.
 * 
 * <br>the define String is[find:length_of_prefix:prefix:length_of_suffix:suffix]
 * <br>
 * 
 * @author Fisher@Beiming
 *
 */
public class Formater extends SuperTrimer{
	public static final String NAME = "formater";
	boolean reverse = false;
	@Override
	public void setArgs(ArrayList<String> paras)throws Exception {
	}

	public String getName(){
		return NAME;
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
		Parser p;
		try {
			p = new Parser(input);
			NodeList nl = p.parse(null);
			return nl.toHtml();
		} catch (ParserException e) {
			e.printStackTrace();
		}
		return input;
	}

	public String toString(){
		return String.format("[formater]");
	}

}
