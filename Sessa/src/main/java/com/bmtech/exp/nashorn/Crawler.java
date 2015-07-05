package com.bmtech.exp.nashorn;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

import com.bmtech.utils.Charsets;
import com.bmtech.utils.http.HttpCrawler;
import com.bmtech.utils.http.HttpHandler;

public class Crawler {

	public String get(String url, HttpHandler hdl) throws IOException {
		HttpCrawler crl = new HttpCrawler(new URL(url), hdl);
		crl.connect();
		byte[] bs = crl.getBytes();
		Charset cs = Charsets.getCharset(bs, true);
		return new String(bs, cs);
	}

	public String get(String url) throws IOException {
		return get(url, HttpHandler.getCrawlHandler());
	}

	public void dumpToFile(String url, String filePath, boolean raw)
			throws IOException {
		File file = new File(filePath);
		HttpCrawler crl = new HttpCrawler(new URL(url),
				HttpHandler.getCrawlHandler());
		crl.connect();
		if (raw) {
			FileOutputStream fos = new FileOutputStream(file);
			try {
				crl.rawDataDumpTo(fos);
			} finally {
				fos.close();
			}
		} else {
			crl.dumpTo(file);
		}
	}

	public void rawDumpToFile(String url, String filePath) throws IOException {
		dumpToFile(url, filePath, true);
	}

	public void dumpToFile(String url, String filePath) throws IOException {
		dumpToFile(url, filePath, false);
	}
}
