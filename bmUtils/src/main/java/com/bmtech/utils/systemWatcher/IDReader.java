package com.bmtech.utils.systemWatcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.bmtech.utils.ZipUnzip;
import com.bmtech.utils.c2j.cTypes.U64;
import com.bmtech.utils.security.BmAes;

public class IDReader {
	private final byte[] enc;
	private final FileInputStream fis;
	private int nextLen;
	public IDReader(File file){
		this(file, null);
	}
	IDReader(File file, String enc){
		if(enc == null) {
			enc = IDDump.class.getName();
			if(enc.length() > 16) {
				enc = enc.substring(enc.length() - 16, enc.length());
			}
			this.enc = enc.getBytes();
		}else {
			this.enc = enc.getBytes();
		}
		FileInputStream fos;
		try {
			fos = new FileInputStream(file);
		}catch(Exception e) {
			fos = null;
		}
		this.fis = fos;
	}
	public boolean hasNext() throws IOException {
		nextLen = nextLen();
		if(nextLen > 0) {
			return true;
		}
		return false;
	}
	public byte[] next() throws Exception {
		if(nextLen < 0) {
			throw new Exception("IDD ERROR X");
		}
		byte [] bs = new byte[nextLen];
		int readed = fis.read(bs);
		if(nextLen != readed) {
			throw new Exception("not match:" + nextLen + "!="+ readed);
		}
		bs = BmAes.decrypt(enc, bs);
		bs = ZipUnzip.unGzip(bs);
		return bs;
	}
	private int nextLen() throws IOException {
		if(nextLen < 0) {//if finished! set to finished
			return -1;
		}
		byte [] blen = new byte[U64.size()];
		int lenReaded = fis.read(blen);
		if(lenReaded != blen.length) {
			if(lenReaded == -1) {
				return -1;
			}else {
				throw new IOException("can not read " + lenReaded);
			}
		}
		U64 u64 = new U64(0);
		u64.fromBytes(blen);
		return (int) u64.getValue();
	}
	public static void main(String[]a) throws Exception {
		IDReader idd = new IDReader(new File("D:\\workspace\\CrawlAPI\\config\\tmp\\2011-02-28_17_03_43_896.dmp"));
		while(idd.hasNext()) {
			byte[] bs = idd.next();
			String str = new String(bs);
			System.out.println(str);
		}
	}

}
