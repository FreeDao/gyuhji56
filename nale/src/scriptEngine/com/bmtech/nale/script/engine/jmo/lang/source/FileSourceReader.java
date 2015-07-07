package com.bmtech.nale.script.engine.jmo.lang.source;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileSourceReader extends SourceReader {

	public FileSourceReader(File file) throws IOException {
		super(file.getPath(), new FileReader(file));
	}
}
