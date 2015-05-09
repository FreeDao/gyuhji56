package com.bmtech.spider.core.util;

import java.nio.charset.Charset;

import com.bmtech.spider.core.ScanConfig;
import com.bmtech.spider.core.util.SynCombin.DecodeSynCombin;
import com.bmtech.utils.Charsets;
import com.bmtech.utils.bmfs.MDir;
import com.bmtech.utils.bmfs.MFile;
import com.bmtech.utils.bmfs.MFileReader;
import com.bmtech.utils.bmfs.MFileReaderIterator;
import com.bmtech.utils.log.LogHelper;

public class SiteMDirReaderIterator {
	static long maxGizpSize = 1024 * 512;
	static long maxUnzipSize = 1024 * 1024 * 2;

	ScanConfig sc = ScanConfig.instance;
	MDir mdir;
	MFileReaderIterator iterator;
	LogHelper log;

	public SiteMDirReaderIterator(MDir mdir) throws Exception {
		this.mdir = mdir;
		iterator = mdir.openReader();
		log = new LogHelper(mdir.getLocalDir().getName());
		loadNextRecord();
	}

	public void close() {
		iterator.close();
		MDir.closeMDir(mdir);
	}

	DecodeSynCombin crt;

	public boolean hasNext() {
		return crt != null;
	}

	public DecodeSynCombin next() throws Exception {
		DecodeSynCombin ret = crt;
		crt = this.loadNextRecord();
		return ret;
	}

	private DecodeSynCombin loadNextRecord() throws Exception {
		while (iterator.hasNext()) {
			MFileReader reader = iterator.next();
			try {
				MFile mf = reader.getMfile();

				byte[] bs;
				if (sc.useMFileGzip) {
					if (mf.getLength() > maxGizpSize) {
						log.warn("skip tooooo big ZIPED %.2fKB named %s",
								mf.getLength() / 1024.0, mf);
						iterator.skip();
						continue;
					}
					bs = reader.getBytesUnGZiped();
				} else {
					if (mf.getLength() > maxUnzipSize) {
						log.warn("skip tooooo big  %.2fKB named %s",
								mf.getLength() / 1024.0, mf);
						iterator.skip();
						continue;
					}
					bs = reader.getBytes();
				}
				Charset cs = Charsets.getCharset(bs, true);
				String htmlEnc = new String(bs, cs);
				crt = SynCombin.parse(htmlEnc);
				return crt;
			} finally {
				reader.close();
			}
		}
		return null;
	}

}
