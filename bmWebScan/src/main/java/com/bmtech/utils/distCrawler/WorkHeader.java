package com.bmtech.utils.distCrawler;

import java.io.IOException;
import java.io.InputStream;

import com.bmtech.utils.c2j.Bytable;
import com.bmtech.utils.c2j.FromBytes;
import com.bmtech.utils.c2j.Struct;
import com.bmtech.utils.c2j.ToBytes;
import com.bmtech.utils.c2j.cTypes.U32;
import com.bmtech.utils.c2j.cTypes.U8;

public class WorkHeader extends Struct implements Bytable{
	public static final int CLIENT_PUT_RESULT = 1;
	public static final int CLIENT_GET_URLS = 2;
	public static final int SERVER_TELL_URLS = 3;
	public static final int SERVER_TELL_STATUS = 4;


	U8 version = new U8(1);
	U8 taskType = new U8();
	byte[]checkBlock = new byte[6];

	byte[]clientId = new byte[32];
	U32 bodySize = new U32();
	U32 reserved = new U32();

	byte[] body;
	public final int totalBytes = 1 + 1 + 6 + 32 + 4 + 4;

	public int getVer(){
		return version.intValue();
	}
	public int taskType(){
		return taskType.intValue();
	}
	@Override
	public byte[] toBytes() {
		ToBytes tb = new ToBytes(totalBytes);
		tb.next(version);
		tb.next(taskType);
		tb.next(checkBlock);
		tb.next(clientId);
		tb.next(bodySize);
		tb.next(reserved);
		return tb.next();
	}

	@Override
	public void fromBytes(byte[] bss) {
		FromBytes fb = new FromBytes(bss);
		fb.next(version);
		fb.next(taskType);
		fb.next(checkBlock);
		fb.next(clientId);
		fb.next(bodySize);
		fb.next(reserved);
	}

	@Override
	public int sizeOf() {
		return totalBytes;
	}

	@Override
	protected void init() {
	}

	public void initFromInputStream(InputStream ips, byte[] checker) throws IOException, BlockCheckFailException{
		byte[] bs = new byte[this.totalBytes];
		fillBuffer(bs, ips);
		this.fromBytes(bs);
		this.check(checker);
		readBody(ips);
	}
	private void readBody(InputStream ips) throws IOException {
		this.body = new byte[this.bodySize.intValue()];
		fillBuffer(body, ips);
	}

	private void fillBuffer(byte[] toFill, InputStream ips) throws IOException{
		int offset = 0, len = toFill.length;
		while(true){
			int readed = ips.read(toFill, offset, len - offset);
			if(readed < 0){
				continue;
			}else{
				offset += readed;
				if(offset < toFill.length)
					continue;
				else{
					break;
				}
			}
		}
		if(offset != toFill.length){
			throw new IOException("fail to fill buffer: offset != toFill.length " + offset + "!=" + toFill.length);
		}
	}

	private void check(byte[] checker) throws IOException, BlockCheckFailException {
		boolean checkOk = true;
		if(this.checkBlock.length == checker.length){
			for(int x = 0; x < checker.length; x ++){
				if(this.checkBlock[x] != checker[x]){
					checkOk = false;
					break;
				}
			}
		}else{
			checkOk = false;
		}

		if(!checkOk){
			throw new BlockCheckFailException(this.checkBlock, checker);
		}
	}

}
