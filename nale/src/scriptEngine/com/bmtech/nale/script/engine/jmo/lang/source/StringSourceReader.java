package com.bmtech.nale.script.engine.jmo.lang.source;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class StringSourceReader extends SourceNamedReader {
	final String name;
	final Reader fr;

	public StringSourceReader(String name, String script) throws IOException {
		this.name = name;
		this.fr = new StringReader(script);
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
