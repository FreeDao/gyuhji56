package com.bmtech.utils.segment;

import java.util.HashMap;
public class CnSegment extends Segment{
	private static CnSegment reversed ;
	private static CnSegment noReversed;
	HashMap<String, Short> map;

	private  boolean reverse;
	private CnSegment(boolean reverse) {
		this.reverse = reverse;
		map = new HashMap<String, Short>();
		load("config/segment/lexicon", map, reverse);
	}
	/**
	 * get a instance of of this class
	 * @return
	 */
	public synchronized static  CnSegment instance(boolean reverse) {
		if(reverse) {
			if(reversed ==null) {
				reversed = new CnSegment(true);
			}
			return reversed;
		}else {
			if(noReversed ==null) {
				noReversed = new CnSegment(false);
			}
			return  noReversed;
		}
	}

	public TokenHandler segment(String source){
		return Segment.segment(source, map, reverse);
	}

}