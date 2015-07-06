package com.bmtech.thsParser;

import java.io.File;
import java.io.IOException;

import com.bmtech.utils.bmfs.util.ReadProtocol;

public class D1BarRecord {
	private int m_Date;
	private double m_Open;
	private double m_High;
	private double m_Low ;
	private double m_Close;
	private double m_Amount;
	private long m_Volume;
	public D1BarRecord(){

	}
	public D1BarRecord(int m_Date,
			double m_Open,
			double m_High,
			double m_Low,
			double m_Close,
			double m_Amount,
			long m_Volume){
		this.m_Date = m_Date;
		this.m_Open = m_Open;
		this.m_High = m_High;
		this.m_Low = m_Low;
		this.m_Close = m_Close;
		this.m_Amount = m_Amount;
		this.m_Volume = m_Volume;
	}
	public static double GetValue ( int value ) {

		double FValue = ( value & 0x0FFFFFFF );
		byte FSign = ( byte ) ( value >> 28 );

		if ( ( FSign & 0x07 ) != 0 ) {
			double FFactor = Math.pow ( 10, FSign & 0x07 );
			if ( ( FSign & 0x08 ) != 0 ) {
				FValue /= FFactor;
			}
			else {
				FValue *= FFactor;
			}
		}
		return FValue;
	}
	/// <summary>
	/// 
	/// </summary>
	public String toString(){

		return String.format("%s\t%.3f\t%.3f\t%.3f\t%.3f\t%.3f\t%d\t%.3f", m_Date, m_Open, m_High, m_Low, m_Close, m_Amount, m_Volume,
				this.averagePrice());
	}
	public static RecordIterator openReadIterator(File dayFile) throws IOException{
		return new RecordIterator(dayFile);
	}
	static final int recordSize = 28;
	public static D1BarRecord readRecord(ReadProtocol stream, int read ) throws IOException {
		D1BarRecord record = new D1BarRecord();
		int FValue;
		FValue = stream.readI32();
		record.m_Date = FValue;

		record.m_Open = GetValue (stream.readI32());
		record.m_High = GetValue (stream.readI32());
		record.m_Low = GetValue (stream.readI32());
		record.m_Close = GetValue (stream.readI32());
		record.m_Amount = GetValue (stream.readI32());
		record.m_Volume = (long) GetValue (stream.readI32());
		int left = read - recordSize;
		if(left > 0){
			byte[] buf = new byte[left];
			stream.read(buf);
		}
		return record;
	}

	public int getDate() {
		return m_Date;
	}
	public double getOpen() {
		return m_Open;
	}
	public double getHigh() {
		return m_High;
	}
	public double getLow() {
		return m_Low;
	}
	public double getClose() {
		return m_Close;
	}
	public double getAmount() {
		return m_Amount;
	}
	public long getVolume() {
		return m_Volume;
	}
	
	public double averagePrice(){
		return this.getAmount() / this.getVolume();
	}
	public double averagePriceChange(double base){
		return (this.averagePrice() - base) / base;
	}
	public double averagePriceChange(D1BarRecord recBase){
		double base = recBase.averagePrice();
		return (this.averagePrice() - base) / base;
	}
	public double volumeChangeAverage(long totalVolume){
		return this.getAmount() / totalVolume;
	}
	
	public double priceChangeRatio(double by){
		return (this.getClose() - by) / by;
	}
	public double priceAverageChangeRatio(double by){
		return (this.averagePrice() - by) / by;
	}
}
