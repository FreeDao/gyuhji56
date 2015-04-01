package com.bmtech.utils.io.diskMerge;

import java.io.File;
import java.io.IOException;

import com.bmtech.utils.io.LineWriter;

public class MOut {
	private LineWriter lw;
	public final File outFile;
	public MOut(File file) throws IOException{
		File par = file.getParentFile();
		if(!par.exists()){
			if(!par.mkdirs()){
				throw new IOException("MOut can not create dir " + file + " for outfile " + file);
			}
		}
		outFile = file;
		if(!outFile.exists()){
			outFile.createNewFile();
		}

		lw = new LineWriter(outFile, false);
	}
	public synchronized void flush(MRecord mm) throws IOException{
		offer(mm);
		flush();
	}
	public synchronized void offer(MRecord mm) throws IOException{
		lw.writeLine(mm.serialize());
	}
	public synchronized void flush() throws IOException{
		lw.flush();
	}
	public synchronized void close(){
		if(lw != null){
			lw.close();
			lw = null;
		}
	}

	public synchronized void finalize(){
		this.close();
	}
}
