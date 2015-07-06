package com.bmtech.thsParser;

import java.io.IOException;

import com.bmtech.utils.bmfs.util.ReadProtocol;

public class FileHeader {
	/// <summary>
	/// 同花顺文件头。
	/// </summary>
	/// <summary>
	/// 文件标记，固定为“hd1.”
	/// </summary>
	//		[FieldOffset ( 0 )]
	private int magicNumber;
	/// <summary>
	/// 填充空白，固定为“0x0030”
	/// </summary>
	//		[FieldOffset ( 4 )]
	public short W1;
	/// <summary>
	/// 记录数量与文件类型，其中最高 8 位为文件类型，0x00 表示简单类型文件格式，0x03 表示复合索引类型文件格式。
	/// </summary>
	//		[FieldOffset ( 6 )]
	private int RecordCount;
	/// <summary>
	/// 文件头长度。
	/// </summary>
	//		[FieldOffset ( 0x0A )]
	private short HeaderLength;
	/// <summary>
	/// 记录的字节长度。
	/// </summary>
	//		[FieldOffset ( 0x0C )]
	private short RecordLength;
	/// <summary>
	/// 记录的字段数量。
	/// </summary>
	//		[FieldOffset ( 0x0E )]
	private short FieldCount;
	/// <summary>
	/// 文件头的字节长度。
	/// </summary>
	private int LENGTH = 0x10;
	/// <summary>
	/// 
	/// </summary>
	/// <param name="header"></param>
	/// <param name="stream"></param>
	/// <returns></returns>
	public static FileHeader read (ReadProtocol stream ) throws IOException {
		FileHeader header = new FileHeader();

		header.magicNumber = stream.readI32();
		header.W1 = stream.readI16();
		header.RecordCount = stream.readI32();
		header.HeaderLength = stream.readI16();
		header.RecordLength = stream.readI16();
		header.FieldCount = stream.readI16();

		return header;
	}
	public int getMagicNumber() {
		return magicNumber;
	}
	public int getRecordCount() {
		return RecordCount;
	}
	public short getHeaderLength() {
		return HeaderLength;
	}
	public short getRecordLength() {
		return RecordLength;
	}
	public short getFieldCount() {
		return FieldCount;
	}
	public int getLENGTH() {
		return LENGTH;
	}
}
