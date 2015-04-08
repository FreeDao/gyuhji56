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

public class SiteMDirReader {
	static long maxGizpSize = 1024 * 512;
	static long maxUnzipSize = 1024 * 1024 * 2;

	ScanConfig sc = ScanConfig.instance;
	MDir mdir;
	MFileReaderIterator itrator;
	LogHelper log;

	public SiteMDirReader(MDir mdir) throws Exception {
		this.mdir = mdir;
		itrator = mdir.openReader();
		log = new LogHelper(mdir.dataFile.getName());
		crt = this.nextRecord();

	}

	public void close() {
		itrator.close();
		MDir.closeMDir(mdir);
	}

	DecodeSynCombin crt;

	public boolean hasNext() {
		return crt != null;
	}

	public DecodeSynCombin next() throws Exception {
		DecodeSynCombin ret = crt;
		crt = this.nextRecord();
		return ret;
	}

	private DecodeSynCombin nextRecord() throws Exception {

		while (itrator.hasNext()) {
			MFileReader reader = itrator.next();
			MFile mf = reader.getMfile();

			byte[] bs;
			if (sc.useMFileGzip) {
				if (mf.getLength() > maxGizpSize) {
					log.warn("skip tooooo big ZIPED %.2fKB named %s",
							mf.getLength() / 1024.0, mf);
					itrator.skip();
					continue;
				}
				bs = reader.getBytesUnGZiped();
			} else {
				if (mf.getLength() > maxUnzipSize) {
					log.warn("skip tooooo big  %.2fKB named %s",
							mf.getLength() / 1024.0, mf);
					itrator.skip();
					continue;
				}
				bs = reader.getBytes();
			}
			Charset cs = Charsets.getCharset(bs, true);
			String htmlEnc = new String(bs, cs);
			DecodeSynCombin cmb = SynCombin.parse(htmlEnc);
			return cmb;
		}
		return null;
	}

}
