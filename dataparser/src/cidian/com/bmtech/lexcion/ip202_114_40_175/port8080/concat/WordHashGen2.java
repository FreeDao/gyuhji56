package com.bmtech.lexcion.ip202_114_40_175.port8080.concat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import com.bmtech.utils.bmfs.util.WriteProtocol;
import com.bmtech.utils.io.LineReader;
import com.bmtech.utils.io.LineWriter;

public class WordHashGen2 {
	final int writerNumber =  1024;
	WriteProtocol lws []= new WriteProtocol[writerNumber];
	final int stickSize = 4;
	void write(int id, String[] tokens, StringBuilder sb) throws IOException{
		int end = tokens.length - stickSize;
		for(int x = 0; x < end; x ++){
			int hash = 0;
			sb.setLength(0);
			for(int y = 0; y < stickSize; y ++){
				sb.append(tokens[y + x]);
			}
			hash = Math.abs(sb.toString().hashCode());
			int hashV = hash % writerNumber;
			synchronized(lws){
				lws[hashV].writeI32(id);
				lws[hashV].writeI32(hash);
			}
		}
	}
	void makeBuckets() throws IOException{
		int size = lws.length;
		File base = new File("/ulks/bkts");
		base.mkdirs();
		for(int x = 0; x < size; x ++){
			FileOutputStream fos = new FileOutputStream(
					new File(base, x +".bkt"));
			lws[x] = new WriteProtocol(fos);
		}
		final LineReader lr = new LineReader("/ulks/ulk-refine");
		for(int x = 0; x < 4; x ++){
			new Thread(){
				StringBuilder sb = new StringBuilder();
				public void run(){
					while(true){
						String line;
						int id;
						synchronized(lr){
							if(!lr.hasNext()){
								break;
							}
							id = lr.currentLineNumber();
							line = lr.next();
						}
						if(0 == id % 10000){
							System.out.println(id);
						}
						String []tokens = line.trim().split(" ");

						try {
							write( id, tokens, sb);
						} catch (IOException e) {
							throw new RuntimeException(e);
						}
					}
				}
			}.start();
		}
		Runtime.getRuntime().addShutdownHook(new Thread(){
			public void run(){
				lr.close();
				for(WriteProtocol wp : lws){
					try {
						wp.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
	
	}

	public static void main(String[]args) throws IOException{
		WordHashGen2 h = new WordHashGen2();
		h.makeBuckets();
	}
}
