package com.bmtech.utils.bmfs;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;

import com.bmtech.utils.ReadAllInputStream;
import com.bmtech.utils.bmfs.util.MFileFormatErrorException;
import com.bmtech.utils.bmfs.util.ReadProtocol;
import com.bmtech.utils.bmfs.util.AssureInputStream.AssureFailException;
/**
 * like MFileWriter, * NOT thread-safe *
 * @author liying
 *
 */
public class MFileReader extends ReadProtocol implements Iterator<MFile>{
	private ByteArrayOutputStream bos = new ByteArrayOutputStream();
	private byte[] buf;
	final MDir mdir;
	final byte[]margin = new byte[MFile.marginLen];
	private MFile current;
	private boolean readed = false;
	MFileReader(MDir mdir) throws IOException{
		super(new FileInputStream(mdir.dataFile));
		this.mdir = mdir;
	}
	private int read() throws IOException{
		return trans_.read();
	}

	private void readUtilGet() throws Exception{
		while(true){
			try {
				margin[0] = (byte) read();
			} catch (AssureFailException e) {
				this.current = null;
				break;
			}
			readMargin();
			this.current = parseMargin();
			if(this.current != null){
				break;
			}
		}
		readed = true;
	}
	private void readMargin() throws IOException{
		super.read(margin, 1, margin.length - 1);
	}
	private MFile parseMargin() throws IOException, Exception{
		int fsId = super.readI32();

		if( margin[14] != (byte)(fsId & 0xff) ||
				margin[15] != (byte)(fsId >> 4)){
			throw new MFileFormatErrorException("MDir format Error, margin digest mismatch");
		}
		return mdir.getFileById(fsId);
	}

	@Override
	public boolean hasNext() {
		if(!this.readed){
			try {
				readUtilGet();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return this.current != null;
	}

	@Override
	public MFile next() {
		MFile ret = this.current;
		this.readed = false;
		return ret;
	}
	public ReadAllInputStream getInputStream() throws IOException{
		return getInputStream(false);
	}
	public ReadAllInputStream getInputStreamUnGZiped() throws IOException{
		return getInputStream(true);
	}
	public byte[] getBytes() throws IOException{
		ReadAllInputStream ips = getInputStream();
		return ips.readAll();
	}
	public byte[] getBytesUnGZiped() throws IOException{
		ReadAllInputStream ips = getInputStreamUnGZiped();
		return ips.readAll();
	}
	private class MFileInputStream extends ReadAllInputStream{
		int readed = 0;
		final long fileLen;
		MFileInputStream(long fileLen){
			this.fileLen = fileLen;
		}

		@Override
		public int read() throws IOException {
			if(readed >= fileLen){
				return -1;
			}
			int ret =  trans_.readByte();
			if(ret == -1){
				throw new MFileFormatErrorException("corrupt mdir " + mdir);
			}
			readed ++;
			return ret;
		}
		public int read(byte[]buf) throws IOException{
			return read(buf, 0, buf.length);
		}
		public int read(byte[]buf, int offset, int len) throws IOException{
			long left = fileLen - readed;
			if(left < 1){
				return -1;
			}
			int canRead = buf.length - offset;
			if(canRead > left){
				len = (int) left;
			}

			int ret = trans_.readToBuf(buf, offset, len);
			if(ret == -1){
				throw new MFileFormatErrorException("premature stream! should left " + left + ", require " + len);
			}
			readed += ret;
			return ret;
		}

		public byte[] readAll() throws IOException{
			if(buf == null){
				buf = new byte[1024 * 8];
			}
			bos.reset();

			return this.readAll(bos, buf);
		}
		
		public void close(){
			
		}
	}
	class GZipReadAllInputStream extends ReadAllInputStream{
		final GZIPInputStream ips;
		GZipReadAllInputStream(InputStream ips) throws IOException{
			GZIPInputStream ret = new GZIPInputStream(ips, 4096);
			this.ips = ret;
		}
		@Override
		public byte[] readAll() throws IOException {
			if(buf == null){
				buf = new byte[1024 * 8];
			}
			bos.reset();
			return this.readAll(bos, buf);
		}

		@Override
		public int read() throws IOException {
			return ips.read();
		}
		public void close() throws IOException{
			this.ips.close();
		}

	}
	private ReadAllInputStream getInputStream(boolean unGzip) throws IOException{

		final long fileLen = current.getLength();
		ReadAllInputStream ips = new MFileInputStream(fileLen);
		if(unGzip){
			GZipReadAllInputStream ret = new GZipReadAllInputStream(ips);
			return ret;
		}else{
			return ips;
		}
	}
	@Override
	public void remove() {
		throw new RuntimeException("not support remove()");		
	}
	/**
	 * 
	 * @throws IOException
	 */
	public void skip() throws IOException {
		final long fileLen = current.getLength();
		InputStream ips = new MFileInputStream(fileLen);
		ips.skip(fileLen);
		ips.close();
	}
 }
