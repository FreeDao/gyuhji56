package com.bmtech.nale.script.engine.jmo.lang.source;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class ResourceReader extends SourceNamedReader {
	final String file;
	final Reader fr;

	public ResourceReader(String resource) throws IOException {
		this.file = resource;
		this.fr = new BufferedReader(new InputStreamReader(this.getClass()
				.getClassLoader().getResourceAsStream(resource)));
	}

	@Override
	public String getSourceName() {
		return file;
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
