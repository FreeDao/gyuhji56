package com.bmtech.nale.script.engine.jmo.lang.source;

import java.io.IOException;
import java.io.Reader;

public class SourceReader extends SourceNamedReader {
	final String name;
	final Reader fr;

	public SourceReader(String name, Reader r) throws IOException {
		this.fr = r;
		this.name = name;
	}

	@Override
	public String getSourceName() {
		return name;
	}

	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		return fr.read(cbuf, off, len);
	}

	@Override
	public void close() throws IOException {
		fr.close();
	}

	@Override
	public void finalize() {
		try {
			if (fr != null) {
				fr.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
