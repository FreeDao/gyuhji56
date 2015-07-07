package com.bmtech.nale.script.engine.jmo.lang.source;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;

public class CombinReader extends Reader {
	ArrayList<SourceNamedReader> readers = new ArrayList<SourceNamedReader>();
	ArrayList<Integer> readedLen = new ArrayList<Integer>();
	int currentLen = 0;
	int currentReaderIndex = 0;

	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		if (currentReaderIndex >= readers.size()) {
			return -1;
		}
		Reader r = this.readers.get(currentReaderIndex);
		int ret = r.read(cbuf, off, len);
		if (ret == -1) {
			readedLen.add(currentLen);
			currentLen = 0;
			r.close();
			this.currentReaderIndex++;
			return read(cbuf, off, len);
		} else {
			for (int x = 0; x < ret; x++) {
				if (cbuf[x + off] == '\n') {
					currentLen++;
				}
			}
		}
		return ret;
	}

	@Override
	public void close() throws IOException {
		for (Reader r : readers) {
			r.close();
		}
	}

	public void addSource(SourceNamedReader r) {
		this.readers.add(r);
	}

	public void addSourceWithNewLine(SourceNamedReader r) {
		this.readers.add(r);
		this.readers.add(newEmptySourceNamedReader());
	}

	private SourceNamedReader newEmptySourceNamedReader() {
		return new SourceNamedReader() {
			Reader r = new StringReader("\n");

			@Override
			public String getSourceName() {
				return "";
			}

			@Override
			public int read(char[] cbuf, int off, int len) throws IOException {
				return r.read(cbuf, off, len);
			}

			@Override
			public void close() throws IOException {
				r.close();
			}

		};
	}

	public void addSource(File file) throws IOException {
		this.readers.add(new FileSourceReader(file));
	}

	public SourcePosition getRelativeSourcePosition(int lineNo, int pos) {
		int no = lineNo;
		int readSource = readedLen.size();
		int sourceIndex = 0;
		// System.out.print(lineNo);
		for (; sourceIndex < readSource; sourceIndex++) {
			int sourceLen = this.readedLen.get(sourceIndex);
			if (sourceLen >= no) {
				break;
			} else {
				no -= sourceLen;
			}
		}
		// System.out.println(" " + no + this.readedLen);
		return new SourcePosition(
				this.readers.get(sourceIndex).getSourceName(), no, pos);
	}
}
