package com.funtv.webkit;

import java.net.URL;
import java.util.HashMap;
import java.util.Stack;

import android.text.TextUtils;

import com.funtv.utils.LogUtil;

public class NaviHistory {
	public class HistoryStackInfo{
		public final String url;
		boolean jsManageGoBack = false;
		private String envParas = "";
		private boolean loadFinished = false;
		private HashMap<String, String>pageProperty = new HashMap<String, String>();
		HistoryStackInfo (String urlStr) throws Exception{
			urlStr = urlStr.trim();
			URL url = new URL(urlStr);//XXX besure urlStr is valid url
			if(!url.getProtocol().startsWith("http")){
				throw new Exception("invalid protocol for NaviHistory for Url '" + urlStr + "'");
			}
			this.url = urlStr;
		}

		public String toString(){
			return url + ", loadFinished = " + this.isLoadFinished() + ", jsManageGoBack = " + jsManageGoBack + ", paras = " + this.envParas;
		}

		public boolean isLoadFinished() {
			return loadFinished;
		}

		public String getEnvParas() {
			return envParas;
		}

		public void setEnvParas(String envParas) {
			this.envParas = envParas;
		}
	}

	private class MyStack{
		private HistoryStackInfo current = null;
		private Stack<HistoryStackInfo>historyStack = new Stack<HistoryStackInfo>();
		private synchronized void push(HistoryStackInfo item){
			this.current = item;
			this.historyStack.push(this.current);
		}

		public synchronized HistoryStackInfo pop(){
			if(this.historyStack.isEmpty()){
				return null;
			}else{
				this.historyStack.pop();
				if(this.historyStack.isEmpty()){
					this.current = null;
				}else{
					this.current = this.historyStack.peek();
				}
				return this.current;
			}
		}

		public boolean equalsStackTop(String url){
			if(url == null || this.current == null){
				return false;
			}
			url = url.trim();
			return this.current.url.equals(url);
		}
	}

	private MyStack stack = new MyStack();
	
	public synchronized void addVisitHistoryUrl(String urlStr){
		if(urlStr == null){
			LogUtil.e("add null to addVisitHistoryUrl");
			return;
		}

		try {
			HistoryStackInfo newItem = new HistoryStackInfo(urlStr);
			if(stack.equalsStackTop(newItem.url)){
				LogUtil.i("deny push history Url becouse its sames as old one:" + urlStr);
				return;
			}

			stack.push(newItem);
		} catch (Exception e) {
			LogUtil.e("when input history:" + e);
		}
	}
	public synchronized void markUrlLoadFinished(String urlStr){
		try {
			HistoryStackInfo newItem = new HistoryStackInfo(urlStr);
			if(stack.equalsStackTop(newItem.url)){
				stack.current.loadFinished = true;
			}else{
				LogUtil.w("try mark load finished ERROR, current = " + this.stack.current + ", but the aim is " + urlStr );
			}

		} catch (Exception e) {
			LogUtil.e("when input history:" + e);
		}
	}

	public synchronized HistoryStackInfo getCurrent(){
		return this.stack.current;
	}

	public synchronized String toString(){
		return this.stack.historyStack.toString();
	}

	public synchronized HistoryStackInfo goBackHistory(){
		LogUtil.i("history goBack from " + stack.current);

		while(true){
			this.stack.pop();
			if(this.stack.current == null){
				LogUtil.e("history got NULL url when goBack");
				return null;
			}
			
			if(this.stack.current.loadFinished){
				HistoryStackInfo ret = this.stack.current;
				ret.loadFinished = false;//re-init
				ret.jsManageGoBack = false;
				LogUtil.i("history got finished url when goBack : " + this.stack.current);
				return ret;
			}else{
				LogUtil.i("history got NOT finished url when goBack : " + this.stack.current);
			}
		}
	}
	public synchronized boolean canGoBack(){
		if(stack.current != null){
			if(stack.current.jsManageGoBack){
				return true;
			}
		}
		int sizeOf = this.stack.historyStack.size();
		boolean ret = false;
		for(int index = sizeOf - 2; index >= 0; index --){
			HistoryStackInfo tmp = this.stack.historyStack.elementAt(index);
			if(tmp.isLoadFinished()){
				LogUtil.i("history tell can goBack by " + tmp);
				ret = true;
				break;
			}
		}
		LogUtil.i("history tell can NOT goBack");
		return ret;
	}
	
	public synchronized String getRefer(){
		String refer = "";
		int sizeOf = this.stack.historyStack.size();
		for(int index = sizeOf - 2; index >= 0; index --){
			HistoryStackInfo tmp = this.stack.historyStack.elementAt(index);
			if(tmp.isLoadFinished()){
				refer = tmp.url;
				break;
			}
		}
		if(TextUtils.isEmpty(refer)){
			refer = "";
		}
		LogUtil.i("current refer is: "+ refer);
		return refer;
	}
	public String setPageProperty(String key, String value){
		String ret = this.getCurrent().pageProperty.put(key, value);
		if(ret == null){
			ret = "";
		}
		LogUtil.i("setPageProperty key = " + key  + ", value = " + value + " for url " + getCurrent());
		return ret;
	}
	public String getPageProperty(String key){
		String ret = this.getCurrent().pageProperty.get(key);
		if(ret == null){
			ret = "";
		}
		LogUtil.i("getPageProperty key = " + key  + ", value = " + ret + " for url " + getCurrent());
		return ret;
	}
	public String removePageProperty(String key){
		String ret = this.getCurrent().pageProperty.remove(key);
		if(ret == null){
			ret = "";
		}
		LogUtil.i("removePageProperty key = " + key  + ", value = " + ret + " for url " + getCurrent());
		return ret;
	}
}
