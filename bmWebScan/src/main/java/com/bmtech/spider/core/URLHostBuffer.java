package com.bmtech.spider.core;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.bmtech.utils.io.diskMerge.MRTool;
import com.bmtech.utils.io.diskMerge.MRecord;
import com.bmtech.utils.io.diskMerge.RecordReader;
import com.bmtech.utils.log.BmtLogHelper;

public class URLHostBuffer {
	public final File toRead;
	private final RecordReader mr;

	public URLHostBuffer(File f) throws Exception {
		toRead = f;
		File tmpFile = new File("./tmp/ub." + System.currentTimeMillis()
				+ (int) (Math.random() * 10000) + ".tmp");
		tmpFile.deleteOnExit();
		if (!tmpFile.getParentFile().exists()) {
			if (!tmpFile.getParentFile().mkdirs()) {
				throw new IOException("can not create file "
						+ tmpFile.getAbsolutePath());
			}
		}
		log.info("sorting urls file %s", toRead);
		MRTool.sortFile(tmpFile, toRead, URLRecord.class,
				new Comparator<MRecord>() {
					@Override
					public int compare(MRecord o1, MRecord o2) {
						URLRecord u1 = (URLRecord) o1;
						URLRecord u2 = (URLRecord) o2;
						return u2.hash - u1.hash;
					}
				}, ScanConfig.instance.sortFactor);
		log.info("sort OK!");
		mr = new RecordReader(tmpFile, URLRecord.class);
	}

	BmtLogHelper log = new BmtLogHelper("uhb");

	@Override
	public void finalize() {
		close();
	}

	public List<URL> next() throws Exception {
		List<URL> urlBuffer = nextInner();
		if (urlBuffer.size() > 0) {
			return urlBuffer;
		} else {
			return null;
		}
	}

	private List<URL> nextInner() throws Exception {

		String crtHost = null;
		List<URL> urlBuffer = new ArrayList<URL>();

		while (true) {
			URLRecord ur = (URLRecord) mr.peek();
			if (ur == null) {
				mr.close();
				break;
			}

			if (crtHost == null) {
				urlBuffer.add(ur.url);
				crtHost = CoreUtil.urlHost(ur.url);
			} else {
				if (CoreUtil.urlHost(ur.url).equalsIgnoreCase(crtHost)) {
					urlBuffer.add(ur.url);
					mr.take();
					break;
				} else {
					return urlBuffer;
				}
			}
		}
		return urlBuffer;
	}

	public void close() {
		synchronized (this) {
			mr.close();
		}
	}

}
