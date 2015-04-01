package com.bmtech.utils.distCrawler;

import java.io.File;

import com.bmtech.utils.c2j.Bytable;
import com.bmtech.utils.c2j.Struct;
import com.bmtech.utils.c2j.cTypes.U32;
import com.bmtech.utils.c2j.cTypes.U64;

public class ClientTellResultTask extends Struct implements Bytable{

	U64 uid = new U64();
	String url;
	U32 httpCode = new U32();
	String error = "";
	File file;
	
	@Override
	public byte[] toBytes() {
		return null;
	}

	@Override
	public void fromBytes(byte[] bss) {
		
	}

	@Override
	public int sizeOf() {
		return 0;
	}

	@Override
	protected void init() {
		
	}

}
