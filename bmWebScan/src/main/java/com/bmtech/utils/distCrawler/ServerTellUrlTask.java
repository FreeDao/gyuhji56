package com.bmtech.utils.distCrawler;

import java.util.ArrayList;

import com.bmtech.utils.c2j.Bytable;
import com.bmtech.utils.c2j.FromBytes;
import com.bmtech.utils.c2j.Struct;
import com.bmtech.utils.c2j.ToBytes;
import com.bmtech.utils.c2j.cTypes.U32;
import com.bmtech.utils.c2j.cTypes.U64;

public class ServerTellUrlTask extends Struct implements Bytable{
//	public ServerTellUrlTask() {
//		super();
//	}

	ArrayList<URLEntry> urls = new ArrayList<URLEntry>();
	@Override
	public byte[] toBytes() {
		ArrayList<byte[]> arr = new ArrayList<byte[]>();
		int ub = 4 + urls.size() * 8 + + urls.size() * 4  ;
		for(URLEntry ue : urls){
			byte[] bs = ue.url.getBytes();
			ub += bs.length;
			arr.add(bs);
		}
		ToBytes tb = new ToBytes(ub);
		tb.next(new U32(ub));
		for(int x = 0; x < arr.size(); x ++){
			URLEntry ue = urls.get(x);
			tb.next(new U64(ue.id));
			tb.next(new U32(arr.get(x).length));
			tb.next(arr.get(x));
		}
		return tb.next();
	}

	@Override
	public void fromBytes(byte[] bss) {
		FromBytes fb = new FromBytes(bss);
		U32 records = new U32();
		fb.next(records);
		
		int recordNum = records.intValue();
		for(int x = 0; x < recordNum; x ++){
			U64 uid = new U64();
			fb.next(uid);
			U32 sLen = new U32();
			fb.next(sLen);
			byte[] buffer = new byte[sLen.intValue()];
			fb.next(buffer);
			URLEntry ue = new URLEntry(uid.getValue(), new String(buffer));
			urls.add(ue);
		}
	}

	@Override
	public int sizeOf() {
		throw new RuntimeException(this.getClass() + " can not access #sizeOf()");
	}

	@Override
	protected void init() {
		
	}

}
