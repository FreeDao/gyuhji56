package com.bmtech.spider.core.util;

import java.nio.charset.Charset;

import com.bmtech.spider.core.ScanConfig;
import com.bmtech.spider.core.util.SynCombin.DecodeSynCombin;
import com.bmtech.utils.Charsets;
import com.bmtech.utils.bmfs.MDir;
import com.bmtech.utils.bmfs.MFile;
import com.bmtech.utils.bmfs.MFileReader;
import com.bmtech.utils.log.LogHelper;

public class SiteMDirReader {
	static long maxGizpSize = 1024 * 512;
	static long maxUnzipSize = 1024 * 1024 * 2;

	ScanConfig sc = ScanConfig.instance;
	MDir mdir;
	LogHelper log;

	public SiteMDirReader(MDir mdir) throws Exception {
		this.mdir = mdir;
		log = new LogHelper(mdir.getLocalDir().getName());

	}

	public DecodeSynCombin getFile(String fileName) throws Exception {
		MFile mfile = mdir.getMFileByName(fileName);
		log.info("get file %s from mdir %s, mfile is %s", fileName, this, mfile);

		MFileReader reader = mfile.openReader();
		try {
			return getFile(reader);
		} finally {
			reader.close();
		}

	}

	public DecodeSynCombin getFile(MFileReader reader) throws Exception {

		byte[] bs = reader.getBytes(sc.useMFileGzip);
		Charset cs = Charsets.getCharset(bs, true);
		String htmlEnc = new String(bs, cs);
		DecodeSynCombin cmb = SynCombin.parse(htmlEnc);
		return cmb;

	}

}
