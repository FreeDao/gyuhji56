package com.bmtech.utils.io.diskMerge;

public interface PeekableQueue{
	public MRecord peek()throws Exception;
	public MRecord take()throws Exception;
	public void close();
	public int getReadedRecordNumber();
	
}
