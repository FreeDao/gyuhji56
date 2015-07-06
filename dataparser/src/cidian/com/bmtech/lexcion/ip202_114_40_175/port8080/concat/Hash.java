package com.bmtech.lexcion.ip202_114_40_175.port8080.concat;

import java.io.File;
import java.io.IOException;

import com.bmtech.utils.io.LineReader;
import com.bmtech.utils.io.LineWriter;

public class Hash {
	int hash(String tokens[], boolean fromHead, int len){
		
		if(fromHead){
			int ret = 0;
			for(int x = 0; x < len; x ++){
				ret = ret ^ tokens[x].hashCode();
			}
			return ret;
		}else{
			int ret = 0;
			for(int x = 0; x < len; x ++){
				ret = ret ^ tokens[tokens.length - x - 1].hashCode();
			}
			return ret;
		}
	}


	void makeBuckets() throws IOException{
		LineWriter []lws = new LineWriter[bucketSize];
		File base2 = new File(base, len + "-" + fromHead + "-" + bucketSize);
		base2.mkdirs();
		for(int x = 0; x < lws.length; x ++){
			lws[x] = new LineWriter(new File(base2, x + ".bkt"), false);
		}
		LineReader lr = new LineReader("/ulk");
		int cnt = 0;
		while(lr.hasNext()){
			String line = lr.next();
			String []tokens = line.trim().split(" ");
			cnt ++;
			if(tokens.length <= len){
				continue;
			}
			
			int hash = Math.abs(hash(tokens, fromHead, len)) ;
			int hashIndex = hash % bucketSize;
			lws[hashIndex].writeLine(hash + "\t" + line);
		}
		lr.close();
		for(LineWriter lw : lws){
			lw.close();
		}
	}
	final File base = new File("/buckets");
	int bucketSize = 1024;
	boolean fromHead = true;
	int len = 4;

	public static void main(String[]args) throws IOException{
		Hash h = new Hash();
		h.makeBuckets();
	}
}
