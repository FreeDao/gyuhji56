package com.bmtech.spider.core;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import com.bmtech.spider.core.Connectioner.URLToInject;
import com.bmtech.utils.Consoler;
import com.bmtech.utils.Misc;
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
	final File hostBase;
	final File allUrlFile, newURLDir, hasCrawled, notCrawled, crawledUrlDBTmp;
	final String randomFilePrefix = "radmonFile.";

	public HostInitorTool(String host, int sortFactor) throws Exception {
		this.host = host.toLowerCase().trim();
		log = new BmtLogHelper("hInit-" + host);
		this.hostFilter = conf.hostFilterCls.newInstance();
		this.sortFactor = sortFactor;
		if (!Connectioner.instance().checkHostValid(host)) {
			throw new Exception("host is forbidden : " + host);
		}
		if (hostFilter.checkAndForbidden(host)) {
			throw new Exception("forbidden host " + host + ", by " + hostFilter);

		}
		hostBase = conf.getHostBase(new HostInfo(host));
		besureDirExists(hostBase);

		allUrlFile = new File(hostBase, conf.urlDataBase);
		this.besureFileExists(allUrlFile);
		newURLDir = new File(hostBase, conf.newUrlDir);
		makeDir(newURLDir);

		hasCrawled = new File(hostBase, conf.crawledUrlDB);
		this.besureFileExists(hasCrawled);
		notCrawled = new File(hostBase, conf.readyToCrawl);
		crawledUrlDBTmp = new File(hostBase, conf.crawledUrlTmp);

		File[] fs = hostBase.listFiles();
		for (File f : fs) {
			if (f.isFile()) {
				if (f.getName().startsWith(randomFilePrefix)) {
					Misc.del(f);
				}
			}
		}
	}

	public void markAllUrlToCrawled() throws Exception {
		List<File> toMerge = new ArrayList<File>();
		for (File f : hostBase.listFiles()) {
			if (f.isDirectory()) {
				File[] xx = f.listFiles();
				for (File fx : xx) {
					if (fx.isDirectory()) {
						System.err.println("SKIP " + fx.getAbsolutePath());
					} else {
						toMerge.add(fx);
					}
				}
			} else {
				toMerge.add(f);
			}
		}
		File[] fsx = new File[toMerge.size()];
		toMerge.toArray(fsx);
		File tmp = this.createRandomFile(hostBase, "toMerge");
		MRTool.combin(tmp, ScoredUrlRecord.class, fsx);
		File tmp2 = this.createRandomFile(hostBase, "toMerge2");
		MRTool.sortFile(tmp2, tmp, ScoredUrlRecord.class,
				ScoredUrlRecord.urlHashCmp, sortFactor);
		File xxxx[] = hostBase.listFiles();
		for (File f : xxxx) {
			if (!f.equals(tmp2)) {
				System.out.println("deleting " + f);
				Misc.del(f);

			}
		}
		tmp2.renameTo(allUrlFile);

		Misc.copyFile(allUrlFile, hasCrawled);
		Consoler.confirm("reinit ok " + hostBase.getCanonicalPath());
	}

	public HostScan initHost() throws Exception {

		log.warn("initializing host %s/%s", ScanConfig.instance.hostHash(host),
				host);

		addInjectedUrlsIntoNewUrlDir();
		addFailedUrlsIntoNewUrlDir();
		mergeAllUrlFile();

		int okCrawledNum = mergeHasCrawled();

		generateNotCrawled();

		return new HostScan(new HostInfo(host, okCrawledNum));
	}

	private void generateNotCrawled() throws Exception {
		File notCrawledTmp = diffNotCrawledAndSortByScore();
		RecordReader rr = null;
		try {

			List<ScoredUrlRecord> injected = getInjectedUrls();

			MOut mo = new MOut(notCrawled);
			try {
				for (ScoredUrlRecord record : injected) {
					mo.offer(record);
				}
				rr = new RecordReader(notCrawledTmp, ScoredUrlRecord.class);
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
				if (rr != null) {
					rr.close();
				}
				mo.close();
			}

		} finally {
			this.besureDelete(notCrawledTmp);
		}
	}

	private List<ScoredUrlRecord> getInjectedUrls() throws Exception {

		ArrayList<String> lst = Connectioner.instance().getAlwaysAllow(host);
		List<ScoredUrlRecord> ret = new ArrayList<ScoredUrlRecord>(
				lst.size() + 1);
		if (conf.injectHomePagePerHost) {
			ret.add(new ScoredUrlRecord(new URL("http://" + host + "/"),
					ScanConfig.instance.scoreFirstPage));
		}
		for (String injectedUrl : lst) {
			ret.add(new ScoredUrlRecord(new URL(injectedUrl),
					ScanConfig.instance.scoreAllwaysAllow));
		}
		return ret;
	}

	private File diffNotCrawledAndSortByScore() throws Exception {
		File notCrawledTmp1 = createRandomFile(hostBase, "readyToCrawl.tmp1");
		File notCrawledTmp2 = createRandomFile(hostBase, "readyToCrawl.tmp2");
		try {
			MRTool.diff(notCrawledTmp1, allUrlFile, hasCrawled,
					ScoredUrlRecord.class, ScoredUrlRecord.urlHashCmp);

			MRTool.sortFile(notCrawledTmp2, notCrawledTmp1,
					ScoredUrlRecord.class, ScoredUrlRecord.scoreCmp, sortFactor);
		} finally {
			notCrawledTmp1.delete();
		}
		return notCrawledTmp2;
	}

	private int mergeHasCrawled() throws Exception {
		int okCrawledNum = 0;
		if (crawledUrlDBTmp.exists() && crawledUrlDBTmp.length() > 0) {
			File hasCrawledTmp1 = this.createRandomFile(hostBase,
					"crawledUrlDB.tmp1");
			File hasCrawledTmp2 = this.createRandomFile(hostBase,
					"crawledUrlDB.tmp2");
			try {
				MRTool.combin(hasCrawledTmp1, ScoredUrlRecord.class,
						new File[] { hasCrawled, crawledUrlDBTmp });

				MRTool.sortFile(hasCrawledTmp2, hasCrawledTmp1,
						ScoredUrlRecord.class, ScoredUrlRecord.urlHashCmp,
						sortFactor);

				okCrawledNum = MRTool.reduce(hasCrawled, hasCrawledTmp2,
						ScoredUrlRecord.class, ScoredUrlRecord.urlEqualor);
			} finally {
				this.besureDelete(hasCrawledTmp1);
				this.besureDelete(hasCrawledTmp2);
			}
			try {
				Connectioner.instance().setHasCrawled(host, okCrawledNum);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return okCrawledNum;
	}

	private void mergeAllUrlFile() throws Exception {
		File[] newURLsFile = newURLDir.listFiles();

		if (newURLsFile.length > 0) {

			File allUrlTmp1 = this.createRandomFile(hostBase, "all-tmp.1");
			File allUrlTmp2 = this.createRandomFile(hostBase, "all-tmp.2");
			try {
				File mergeUrlFile[] = new File[newURLsFile.length + 1];
				System.arraycopy(newURLsFile, 0, mergeUrlFile, 0,
						newURLsFile.length);
				mergeUrlFile[newURLsFile.length] = allUrlFile;

				MRTool.combin(allUrlTmp1, ScoredUrlRecord.class, mergeUrlFile);

				MRTool.sortFile(allUrlTmp2, allUrlTmp1, ScoredUrlRecord.class,
						ScoredUrlRecord.urlHashCmp, sortFactor);
				MRTool.reduce(allUrlFile, allUrlTmp2, ScoredUrlRecord.class,
						ScoredUrlRecord.urlEqualor);
				Misc.del(newURLsFile);
			} finally {
				besureDelete(allUrlTmp1);
				besureDelete(allUrlTmp2);

			}
		}

	}

	private void addFailedUrlsIntoNewUrlDir() throws Exception {
		File hasFailedFile = new File(hostBase, conf.hasFailedFileName);
		if (hasFailedFile.exists() && hasFailedFile.length() > 0) {
			File hasFailedFileTo = new File(newURLDir, conf.hasFailedFileName);
			this.besureDelete(hasFailedFileTo);
			hasFailedFile.renameTo(hasFailedFileTo);
		}
	}

	private void addInjectedUrlsIntoNewUrlDir() throws SQLException,
			IOException {
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

	private File createRandomFile(File base, String suffix) throws IOException {
		File f = new File(base, randomFilePrefix + System.currentTimeMillis()
				+ "." + (int) (1 + Math.random() * 100000) + "." + suffix);
		this.besureFileExists(f);
		return f;
	}

	private void besureFileExists(File f) throws IOException {
		if (!f.exists()) {
			this.createFile(f);
		}
	}

	private void besureDelete(File f) throws IOException {
		f.delete();
		if (f.exists()) {
			log.fatal("can not DELETE sorted %s", f);
			throw new IOException("can not DELETE sorted " + f);
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
