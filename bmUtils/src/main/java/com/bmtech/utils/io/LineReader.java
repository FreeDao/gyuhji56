package com.bmtech.utils.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Iterator;

import com.bmtech.utils.Charsets;

public class LineReader extends BufferedReader implements Iterator<String> {
	private File file;
	Charset cs = Charset.defaultCharset();
	private int pos = -1;

	String line;
	boolean first = true;

	public LineReader(String file) throws IOException {
		super(new FileReader(new File(file)));
		this.file = new File(file);
	}

	public LineReader(File file) throws IOException {
		super(new FileReader(file));
		this.file = file;
	}

	public LineReader(String file, String encoder) throws IOException {
		super(new InputStreamReader(new FileInputStream(file), encoder));
		this.file = new File(file);
		this.cs = Charset.forName(encoder);
	}

	public LineReader(String file, Charset encoder) throws IOException {
		super(new InputStreamReader(new FileInputStream(file), encoder));
		this.file = new File(file);
		this.cs = encoder;
	}

	public LineReader(File file, Charset encoder) throws IOException {
		super(new InputStreamReader(new FileInputStream(file), encoder));
		this.file = file;
		this.cs = encoder;
	}

	public LineReader(File file, String encoder) throws IOException {
		super(new InputStreamReader(new FileInputStream(file), encoder));
		this.file = file;
		this.cs = Charset.forName(encoder);
	}

	/**
	 * get the absolute path of the file
	 * 
	 * @return
	 */

	public File getFile() {
		return this.file;
	}

	@Override
	public synchronized String readLine() throws IOException {
		String s = super.readLine();
		if (first) {
			if (s != null) {
				if (this.cs.equals(Charsets.UTF8_CS)) {
					if (s.length() > 0) {
						int c = s.charAt(0);
						if (c == 0xfeff) {
							s = s.substring(1);
						}
					}
				}
			}
			first = false;
		}
		return s;
	}

	@Override
	public boolean hasNext() {
		try {
			line = this.readLine();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		pos++;
		if (line == null)
			return false;
		return true;
	}

	public String peer() {
		return line;
	}

	@Override
	public String next() {
		return line;
	}

	@Override
	public void remove() {
		this.line = null;
	}

	/**
	 * test if this line start with a invalidate prefix
	 * 
	 * @param line
	 * @return true if this line is not forbiddened
	 */

	@Override
	public void finalize() {
		this.close();
	}

	@Override
	public void close() {
		try {
			super.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int currentLineNumber() {
		return pos;
	}

	@Override
	public String toString() {
		return "LineReader:" + this.file;
	}
}
