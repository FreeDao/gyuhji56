package com.bmtech.utils.io.diskMerge;

public abstract class MRecord{
	/**
	 * construct the instance using a String
	 * @param str
	 * @throws Exception
	 */
	public MRecord(String str) throws Exception{
		this.init(str);
	}
	public MRecord() {
	}
	/**
	 * init the object using a string 
	 * @param str
	 * @throws Exception
	 */
	protected abstract void init(String str)throws Exception;
	/**
	 * convert to string
	 * @return
	 */
	public abstract String serialize();
}
