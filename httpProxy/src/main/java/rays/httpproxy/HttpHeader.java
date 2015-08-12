package rays.httpproxy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpHeader {
	public final Map<String, List<String>> respHead = new HashMap<String, List<String>>();
	public int httpCode;
	
	HttpHeader(Map<String, List<String>> respHead){
		this.respHead.putAll(respHead);
	}
	public void add(String key, String value){
		List<String>list = this.respHead.get(key);
		if(list == null){
			list = new ArrayList<String>(2);
			this.respHead.put(key, list);
		}
		list.add(value);
	}
	
	public void setHttpCode(int httpCode) {
		this.httpCode = httpCode;
		
	}
	public int getHttpCode() {
		return httpCode;
	}
}
