package com.bmtech.utils.bmfs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;


public class MFileWriter {
	MFile mfile;
	ByteArrayOutputStream bosx = new ByteArrayOutputStream();
	OutputStream out;
	
	public void openMFile(MFile mfile, boolean useGzip) throws IOException{
		this.mfile = mfile;
		if(this.mfile.isMount()){//FIXME magic number
			throw new IOException("has mount mfile " + mfile);
		}
		bosx.reset();
		if(useGzip){
			out = new GZIPOutputStream(bosx);
		}else{
			out = bosx;
		}
	}
	public void write(byte b[]) throws IOException {
		write(b, 0, b.length);
	}


	public void write(byte b[], int off, int len) throws IOException {
		out.write(b, off, len);
	}
	public void write(int b) throws IOException {
		out.write(b);
	}
	public void write(InputStream ips) throws IOException{
		while(true){
			int x = ips.read();
			if(x == -1){
				break;
			}
			this.out.write(x);
		}
	}
	public void mountToDir() throws IOException {
		out.close();
		byte[] bts = bosx.toByteArray();
		mfile.dir.mount(mfile, new ByteArrayInputStream(bts));
	}

}