package com.bmtech.spider.core;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import com.bmtech.spider.core.Connectioner.URLToInject;
import com.bmtech.utils.io.diskMerge.MOut;
import com.bmtech.utils.io.diskMerge.MRTool;
import com.bmtech.utils.io.diskMerge.RecordReader;
import com.bmtech.utils.log.BmtLogHelper;

public class HostInitorTool {

	final BmtLogHelper log;
	private final String host;
	final HostFilter hostFilter;
	final int sortFactor;
	ScanConfig conf = ScanConfig.instance;

	HostInitorTool(String host, HostFilter hostFilter, int sortFactor)
			throws Exception {
		this.host = host.toLowerCase().trim();
		log = new BmtLogHelper("hInit-" + host);
		this.hostFilter = hostFilter;
		this.sortFactor = sortFactor;
		if (!Connectioner.instance().checkHostValid(host)) {
			// Connectioner.instance().getConnected(true);
			throw new Exception("host is forbidden : " + host);
		}
		if (true) {
			if (hostFilter.checkAndForbidden(host)) {
				throw new Exception("forbidden host " + host + ", by "
						+ hostFilter);

			}
		}
	}

	public HostScan initHost() throws Exception {

		log.warn("initializing host %s/%s", ScanConfig.instance.hostHash(host),
				host);

		// merge new finded url
		final File hostBase = conf.getHostBase(new HostInfo(host));
		besureDirExists(hostBase);

		final File allUrlFile = new File(hostBase, conf.urlDataBase);
		this.besureFileExists(allUrlFile);
		File newURLDir = new File(hostBase, conf.newUrlDir);
		makeDir(newURLDir);

		ArrayList<URLToInject> toInject = Connectioner.instance()
				.getUnInjected(host);
		if (toInject.size() > 0) {
			ArrayList<ScoredUrlRecord> rec = new ArrayList<ScoredUrlRecord>();
			HashSet<String> set = new HashSet<String>();
			for (URLToInject ut : toInject) {
				try {
					URL u = new URL(ut.url);
					String us = u.toString();
					if (set.contains(us)) {
						continue;
					}
					set.add(us);
					ScoredUrlRecord su = new ScoredUrlRecord(u,
							ScanConfig.instance.scoreInjected);
					rec.add(su);
				} catch (Exception e) {
					log.fatal(e, "when inject %s", ut);
					e.printStackTrace();
				}
			}
			// get InitUrls

			Collections.sort(rec, ScoredUrlRecord.urlHashCmp);
			File toInjectFile = new File(newURLDir, System.currentTimeMillis()
					+ "." + (int) (1 + Math.random() * 100000) + ".ninj");
			MOut out = new MOut(toInjectFile);
			for (ScoredUrlRecord su : rec) {
				log.info("add injected url %s", su);
				out.offer(su);
			}
			out.close();

			for (URLToInject ut : toInject) {
				Connectioner.instance().setToInjected(ut.id);
			}
		}
		// sort failed urls
		{
			File hasFailedFile = new File(hostBase, conf.hasFailedFileName);
			if (hasFailedFile.exists() && hasFailedFile.length() > 0) {
				File hasFailedFileTo = new File(newURLDir,
						conf.hasFailedFileName);
				MRTool.sortFile(hasFailedFileTo, hasFailedFile,
						ScoredUrlRecord.class, ScoredUrlRecord.urlHashCmp,
						sortFactor);
			}

		}
		// merge allUrlFile and newURLs to allUrlTmp
		File[] newURLsFile = newURLDir.listFiles();

		if (newURLsFile.length > 0) {
			for (File f : newURLsFile) {
				File fNewTmp = new File(f.getParentFile(), f.getName()
						+ ".ntmp-" + System.currentTimeMillis());
				// System.out.println("fNewTmp:" + fNewTmp);
				// System.out.println("f:" + f);
				MRTool.sortFile(fNewTmp, f, ScoredUrlRecord.class,
						ScoredUrlRecord.urlHashCmp, sortFactor);
				besureDelete(f);
				fNewTmp.renameTo(f);
				checkFileExists(f);
			}

			File allUrlTmp1 = new File(hostBase,
					"all-tmp.merge-from-new-urls.tmp1");

			File mergeUrlFile[] = new File[newURLsFile.length + 1];
			System.arraycopy(newURLsFile, 0, mergeUrlFile, 0,
					newURLsFile.length);

			mergeUrlFile[newURLsFile.length] = allUrlFile;

			MRTool.mergeTo(allUrlTmp1, ScoredUrlRecord.class,
					ScoredUrlRecord.urlHashCmp,
					ScanConfig.instance.mergeFactor, mergeUrlFile);

			// reduce and remove the same records
			File allUrlTmp2 = new File(hostBase, conf.urlDataBase + ".tmp2");
			if (!allUrlTmp2.exists()) {
				allUrlTmp2.createNewFile();
			}
			MRTool.reduce(allUrlTmp2, allUrlTmp1, ScoredUrlRecord.class,
					ScoredUrlRecord.urlEqualor);

			allUrlFile.delete();

			if (allUrlTmp2.renameTo(allUrlFile)) {
				allUrlTmp1.delete();
			} else {
				log.error("rename fail! from %s to %s", allUrlTmp2, allUrlFile);
			}
			for (File f : newURLsFile) {
				f.delete();
			}
			if (true) {
				MRTool.sortFile(allUrlTmp1, allUrlFile, ScoredUrlRecord.class,
						ScoredUrlRecord.urlHashCmp, sortFactor);
				MRTool.reduce(allUrlFile, allUrlTmp1, ScoredUrlRecord.class,
						ScoredUrlRecord.urlEqualor);
				allUrlTmp1.delete();
			}
		}

		/*** all urls ok! **/

		// merge has crawled

		File hasCrawled = new File(hostBase, conf.crawledUrlDB);
		if (!hasCrawled.exists()) {
			hasCrawled.createNewFile();
		}
		File crawledUrlDBTmp = new File(hostBase, conf.crawledUrlTmp);
		int okCrawledNum = 0;
		if (crawledUrlDBTmp.exists() && crawledUrlDBTmp.length() > 0) {
			File hasCrawledTmp1 = new File(hostBase, conf.crawledUrlDB + ".tmp");
			File hasCrawledTmp2 = new File(hostBase, conf.crawledUrlDB
					+ ".tmp2");
			File hasCrawledTmp3 = new File(hostBase, conf.crawledUrlDB
					+ ".tmp3");

			MRTool.sortFile(hasCrawledTmp1, crawledUrlDBTmp,
					ScoredUrlRecord.class, ScoredUrlRecord.urlHashCmp,
					sortFactor);

			MRTool.mergeTo(hasCrawledTmp2, ScoredUrlRecord.class,
					ScoredUrlRecord.urlHashCmp,
					ScanConfig.instance.mergeFactor, new File[] { hasCrawled,
							hasCrawledTmp1 });
			hasCrawledTmp1.delete();

			okCrawledNum = MRTool.reduce(hasCrawledTmp3, hasCrawledTmp2,
					ScoredUrlRecord.class, ScoredUrlRecord.urlEqualor);

			hasCrawledTmp2.delete();

			hasCrawled.delete();
			crawledUrlDBTmp.delete();
			hasCrawledTmp3.renameTo(hasCrawled);

			try {
				Connectioner.instance().setHasCrawled(host, okCrawledNum);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		File notCrawledTmp1 = new File(hostBase, conf.readyToCrawl + ".tmp1");
		MRTool.diff(notCrawledTmp1, allUrlFile, hasCrawled,
				ScoredUrlRecord.class, ScoredUrlRecord.urlHashCmp);

		File notCrawledTmp2 = new File(hostBase, conf.readyToCrawl + ".tmp2");
		MRTool.sortFile(notCrawledTmp2, notCrawledTmp1, ScoredUrlRecord.class,
				ScoredUrlRecord.scoreCmp, sortFactor);
		notCrawledTmp1.delete();

		RecordReader rr = new RecordReader(notCrawledTmp2,
				ScoredUrlRecord.class);
		File notCrawledTmp3 = new File(hostBase, conf.readyToCrawl + ".tmp3");
		MOut mo = new MOut(notCrawledTmp3);
		try {
			if (conf.injectHomePagePerHost) {
				mo.offer(new ScoredUrlRecord(new URL("http://" + host + "/"),
						ScanConfig.instance.scoreFirstPage));
			}
			ArrayList<String> lst = Connectioner.instance()
					.getAlwaysAllow(host);
			for (String xv : lst) {
				try {
					mo.offer(new ScoredUrlRecord(new URL(xv),
							ScanConfig.instance.scoreAllwaysAllow));
				} catch (Exception e) {
					log.fatal("when offer %s", xv);
					e.printStackTrace();
				}
			}
			int readed = 0;
			while (readed < conf.maxCrawlPagesPerRound) {
				ScoredUrlRecord su = (ScoredUrlRecord) rr.take();
				if (su == null) {
					break;
				}
				mo.offer(su);
				readed++;
			}
		} finally {
			rr.close();
			mo.close();
		}
		notCrawledTmp2.delete();
		File notCrawled = new File(hostBase, conf.readyToCrawl);
		notCrawled.delete();
		notCrawledTmp3.renameTo(notCrawled);

		return new HostScan(new HostInfo(host, okCrawledNum));
	}

	private void checkFileExists(File f) {
		if (!f.exists()) {
			log.fatal("can not find sorted %s", f);
			throw new RuntimeException("can not FIND sorted " + f);
		}
	}

	private void besureDirExists(File dir) throws IOException {
		if (!dir.exists()) {
			makeDir(dir);
		} else {
			if (!dir.isDirectory()) {
				throw new IOException("IS NOT Dir " + dir);

			}
		}

	}

	private void createFile(File file) throws IOException {
		if (!file.createNewFile()) {
			throw new IOException("can not create file " + file);
		}
	}

	private void beusureFileExists(File file) throws IOException {
		if (!file.exists()) {
			this.createFile(file);
		}
	}

	private File createRandomFile(File base, String suffix) {
		return new File(base, System.currentTimeMillis() + "."
				+ (int) (1 + Math.random() * 100000) + suffix);
	}

	private void besureFileExists(File f) throws IOException {

		if (!f.exists()) {
			if (!f.createNewFile()) {
				throw new IOException("can not create file " + f);
			}
		}
	}

	private void besureDelete(File f) {
		f.delete();
		if (f.exists()) {
			log.fatal("can not DELETE sorted %s", f);
			throw new RuntimeException("can not DELETE sorted " + f);
		}

	}

	private void makeDir(File dir) throws IOException {
		if (!dir.exists()) {
			if (!dir.mkdir()) {
				throw new IOException("can not create dir " + dir);
			}
		}

	}
}
