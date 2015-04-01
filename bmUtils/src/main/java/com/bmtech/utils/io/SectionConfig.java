package com.bmtech.utils.io;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.bmtech.utils.KeyValuePair;

/**
 * TC is a basic tool to read config file the configure file should constructed
 * as #---- notice,lines starting with '#' will be ingnored key=value
 * <b>key-value pair</b>
 * 
 * NOTICE:all of the line will be trimed at first NOTICE:the line contains no
 * '='symbol will be ingored NOTICE:if no appender after '=' the value will be
 * viewed as 0-length String NOTICE:if no prifix before '=', the line will be
 * viewed as illegal and ignored
 * 
 * @author beiming
 *
 */
public class SectionConfig {

	protected final Map<String, String> map;
	public final String sectionName;

	SectionConfig(String sectionName, Map<String, String> map) {
		this.sectionName = sectionName;
		this.map = map;
	}

	/**
	 * read all config into memory.
	 * 
	 * @return
	 */
	public Map<String, String> read2Map() {
		return map;
	}

	/**
	 * this method get the value by walking throgh the list. when finding one of
	 * the matched pair,this method will return, no matter how many pairs will
	 * match becouse efficiency is low,and TC may not give all of the good
	 * results
	 * 
	 * @param key
	 *            ,get the specificated key's value pair
	 * @return
	 */
	public String getValue(String key) {
		return getValue(key, null);
	}

	public String getValue(String key, String defaultValue) {
		if (key == null)
			return defaultValue;
		String value = map.get(key.trim());
		return value == null ? defaultValue : value;
	}

	/**
	 * if null or not good format return 0
	 * 
	 * @param key
	 * @return
	 */
	public int getInt(String key) {
		return getInt(key, 0);
	}

	public int getInt(String key, int defalutValue) {
		String v = this.getValue(key);
		if (v == null)
			return defalutValue;
		try {
			return Integer.parseInt(v);
		} catch (Exception e) {
			return defalutValue;
		}
	}

	public ArrayList<KeyValuePair<String, String>> getAllConfig() {
		ArrayList<KeyValuePair<String, String>> list = new ArrayList<KeyValuePair<String, String>>(
				this.map.size());
		Iterator<Entry<String, String>> itr = map.entrySet().iterator();

		while (itr.hasNext()) {
			Entry<String, String> e = itr.next();
			list.add(new KeyValuePair<String, String>(e.getKey(), e.getValue()));
		}
		return list;
	}
}
