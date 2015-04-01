package com.bmtech.utils.io.diskMerge;

import java.io.File;
import java.nio.charset.Charset;

import com.bmtech.utils.io.LineReader;
import com.bmtech.utils.log.LogHelper;

public class RecordReader implements PeekableQueue{

	private MRecord crt;
	private LineReader lr;
	private int readed = 0;
	Class<?> mOutClass;
	LogHelper log = new LogHelper("recordReader");
	public RecordReader(LineReader lr, Class<?>mOutClass) throws Exception{
		this.lr = lr;
		this.mOutClass = mOutClass;
		this.crt = getNextRecord();
	}

	public RecordReader(File file, Class<?>moutClass) throws Exception{
		this(new LineReader(file), moutClass);
	}
	public RecordReader(File file, Charset cs, Class<?>moutClass) throws Exception{
		this(new LineReader(file, cs), moutClass);
	}

	public synchronized MRecord peek(){
		return crt;
	}
	public synchronized MRecord take() throws Exception {
		MRecord ret = crt;
		this.crt = getNextRecord();
		return ret;
	}

	private MRecord getNextRecord(){
		MRecord crt = null;
		while(lr.hasNext()){
			String crtLine = lr.next();
			if(crtLine == null){
				return null;
			}
			readed ++;
			try {
				crt = (MRecord) mOutClass.newInstance();
				crt.init(crtLine);
				break;
			
			} catch (Exception e) {
				log.error(e, "when init %s from str '%s'", mOutClass, crtLine);
			}
		}
		return crt;
	}
	public synchronized void close(){
		lr.close();
	}
	public synchronized void finalize(){
		close();
	}

	public int getReadedRecordNumber() {
		return readed;
	}
}
