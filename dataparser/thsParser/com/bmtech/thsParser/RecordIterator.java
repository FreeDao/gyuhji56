package com.bmtech.thsParser;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.bmtech.utils.bmfs.util.ReadProtocol;

public class RecordIterator implements Closeable{
	D1BarRecord ret;
	final FileInputStream fis ;
	final ReadProtocol prot;
	int current = 0;
	final FileHeader header;
	public RecordIterator(File f) throws IOException{
		fis = new FileInputStream(f);
		prot = new ReadProtocol(fis, false);
		header = FileHeader.read(prot);
		for ( int dwI = 0; dwI < header.getFieldCount(); dwI++ ) {
			ColumnHeader header = ColumnHeader.Read (prot);
			header.nothing();
//			System.out.println(header);
		}
	}
	public boolean hasNext() throws IOException {
		if(current >= this.header.getRecordCount()){
			return false;
		}
		ret = D1BarRecord.readRecord(prot, this.header.getRecordLength());
		this.current ++;
		return true;
	}

	public D1BarRecord next() {
		return ret;
	}


	@Override
	public void close() throws IOException {
		this.fis.close();
	}
	
	public static void main(String[]args) throws IOException{
		String fstr = "D:\\program files\\同花顺软件\\同花顺\\history\\shase\\day\\122702.day";
		File f = new File(fstr);
		RecordIterator itr = new RecordIterator(f);
		while(itr.hasNext()){
			D1BarRecord rec = itr.next();
			System.out.println(rec);
		}
		itr.close();
	}
}