package com.bmtech.thsParser;

import java.io.IOException;

import com.bmtech.utils.bmfs.util.ReadProtocol;

public class ColumnHeader {
	//		[FieldOffset ( 0 )]
	public byte W1;
	//		[FieldOffset ( 1 )]
	public byte Type;
	//		[FieldOffset ( 2 )]
	public byte W2;
	//		[FieldOffset ( 3 )]
	public byte Size;
	/// <summary>
	/// 列定义的字节长度。
	/// </summary>
	public static int LENGTH = 0x04;
	/// <summary>
	/// 
	/// </summary>
	/// <param name="header"></param>
	/// <param name="stream"></param>
	/// <returns></returns>
	public static ColumnHeader Read (ReadProtocol stream ) throws IOException {
		ColumnHeader header = new ColumnHeader();

		header.W1 = stream.readByte();
		header.Type = stream.readByte();
		header.W2 = stream.readByte();
		header.Size = stream.readByte();
		return header;
	}
	public String toString(){
		return "type: " + this.Type + ", size = " + this.Size;
	}
	void nothing(){
		
	}
}
