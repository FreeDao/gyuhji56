package com.bmtech.spider.core.scan;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.bmtech.spider.core.ScanConfig;

public class HostScanOutputStream extends OutputStream {
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	int size = 0;

	@Override
	public void write(int b) throws IOException {
		size++;
		if (size < ScanConfig.instance.maxFileLen) {
			out.write(b);
		}
	}

	public byte[] toByteArray() {
		return out.toByteArray();
	}
}