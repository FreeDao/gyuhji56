package com.bmtech.utils.distCrawler;

import com.bmtech.utils.c2j.Bytable;
import com.bmtech.utils.c2j.Struct;

public abstract class WorkTask extends Struct implements Bytable{
	
	public static final int NotSend = 0;
	public static final int sending = 1;
	public static final int sent = 9;
	public static final int writeError = -1;
	public static final int waitingResponse = 2;
	
	int sendStatus = 0;
	int type;
	
	public WorkTask(int type){
		this.type = type;
	}

	
}
