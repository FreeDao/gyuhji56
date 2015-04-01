package com.bmtech.spider.entrancefinder;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.bmtech.spider.core.ScoredUrlRecord;
import com.bmtech.utils.io.diskMerge.MOut;

public class UrlSavor {
	MOut linkOut;
	MOut linkTo;
	public UrlSavor(File linkOut, File linkTo) throws IOException{
		this.linkOut = new MOut(linkOut);
		this.linkTo = new MOut(linkTo);

		Thread shutdown = new Thread() {
			@Override
			public void run() {
				close();
			}

		};
		Runtime.getRuntime().addShutdownHook(shutdown);
	
	}
	public void addLinkOut(URL url, int score) throws IOException {
		ScoredUrlRecord su = new ScoredUrlRecord(url, score);
		linkOut.offer(su);
	}
	public void addLinkTo(URL url, HashMap<String, Integer> map) {
		Iterator<Entry<String, Integer>> itr = 
			map.entrySet().iterator();
		while(itr.hasNext()){
			Entry<String, Integer>  e = itr.next();
			URL url2;
			try {
				url2 = new URL(e.getKey());
				ScoredUrlRecord su = new ScoredUrlRecord(url2, e.getValue());
				this.linkTo.offer(su);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
	}
	public void close(){
		this.linkOut.close();
		this.linkTo.close();
	}


}
