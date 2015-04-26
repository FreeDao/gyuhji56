package com.bmtech.spider.core;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.bmtech.spider.core.scan.HostScanSiteOutIdv;
import com.bmtech.spider.core.scan.HostScanSiteOutItf;
import com.bmtech.utils.Misc;
import com.bmtech.utils.io.ConfigReader;
import com.bmtech.utils.log.BmtLogHelper;

public class ScanConfig {
	public static final ScanConfig instance = new ScanConfig();

	final BmtLogHelper log = new BmtLogHelper("log");

	public final String readyToCrawl = "ReadyToCrawl.txt";
	public final String hasFailedFileName = "failedToCrawl.txt";
	public final String urlDataBase = "urlDB.txt";
	public final String crawledUrlDB = "urlDB-done.txt";
	public final String crawledUrlTmp = "urlDB-done-tmp.txt";

	public final String newUrlDir = "newUrlDir";
	public final String iUrl = "URLDIR";
	public final File tmpCrawlDir;

	public Class<?> hostFilterCls;
	public final Class<?> foreignUrlCheckClass;
	private final Class<?> hostScanSiteOutClass;
	public final Class<?> scorerCls;

	private final File crawlDir;
	private final File saveDir;
	public final long urlCrawlItvSecond;
	public final long maxFileLen;
	public final long controllerCheckItv;
	public final int injectedUrlValue;
	public final int maxErrorForEveryPage;
	public final int hostPoolSize;
	public final int hostPoolNeedFillMargin;
	public final boolean allowDownload;
	public final boolean allowSaveOKFile;
	public final boolean allowSaveTmpFile;
	public final int hostInitThreads;
	public final int scoreInjected;
	public final int scoreAllwaysAllow;
	// public final int scoreDefault;
	public final int scoreFirstPage;
	public final boolean injectHomePagePerHost;
	public final int crawlConnectTimeout;
	public final int crawlReadTimeout;
	public final boolean useMDir;
	public final int maxCrawlPagesPerRound;
	public int parseThread;
	public final int parseQueueMaxSize;

	public final int mergeFactor;
	public final int sortFactor;

	private int maxPagePerHost;

	public final boolean useMFileGzip;

	private ScanConfig() {
		ConfigReader cr = null;
		cr = new ConfigReader("config/ws/sc.conf", "hostScan");

		urlCrawlItvSecond = cr.getInt("urlCrawlItvSecond", 5);

		String hostFltCls = cr.getValue("hostFilterCls");
		hostFilterCls = getClass(hostFltCls, HostFilterAllAccept.class);

		String foreignUrlCheckCls = cr.getValue("foreignUrlCheckClass");
		foreignUrlCheckClass = getClass(foreignUrlCheckCls,
				ForeignUrlAutoDrop.class);

		String hostScanSiteOutClassStr = cr.getValue("hostScanSiteOutClass");
		hostScanSiteOutClass = getClass(hostScanSiteOutClassStr,
				HostScanSiteOutIdv.class);

		String scoreClass = cr.getValue("scorerCls");
		scorerCls = this.getClass(scoreClass, null);
		if (scorerCls == null) {
			throw new RuntimeException("scorerCls not Set!");
		}

		crawlDir = new File(cr.getValue("crawlDir"));
		if (!crawlDir.exists()) {
			crawlDir.mkdirs();
		}
		String saveStr = cr.getValue("saveDir");
		if (saveStr == null) {
			this.saveDir = new File(crawlDir, "okCrawled");
		} else {
			this.saveDir = new File(saveStr);
			if (!this.saveDir.exists()) {
				this.saveDir.mkdirs();
			} else {
				if (!this.saveDir.isDirectory()) {
					throw new RuntimeException("is not directory : "
							+ this.saveDir);
				}
			}
		}
		String tmpDir = cr.getValue("tmpCrawlDir");
		if (tmpDir == null) {
			this.tmpCrawlDir = new File(crawlDir,
					"tmp-"
							+ new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss")
									.format(new Date()));
		} else {
			this.tmpCrawlDir = new File(tmpDir);
			if (!this.tmpCrawlDir.exists()) {
				this.tmpCrawlDir.mkdirs();
			} else {
				if (!this.tmpCrawlDir.isDirectory()) {
					throw new RuntimeException("is not directory : "
							+ this.tmpCrawlDir);
				}
			}
		}
		injectedUrlValue = cr.getInt("injectedUrlValue", 100);
		scoreAllwaysAllow = cr.getInt("scoreAllwaysAllow", 90);
		scoreInjected = cr.getInt("scoreInjected", 80);
		scoreFirstPage = cr.getInt("scoreAllwaysAllow", 100);
		hostInitThreads = cr.getInt("hostInitThreads", 5);
		allowDownload = cr.getInt("allowDownload", 0) == 1;
		maxErrorForEveryPage = cr.getInt("maxErrorForEveryPage", 5);
		maxFileLen = cr.getInt("maxFileLenKB", 3000) * 1000L;
		controllerCheckItv = cr.getInt("controllerCheckItvSecond", 5) * 1000L;

		maxCrawlPagesPerRound = cr.getInt("maxCrawlPagesPerRound", 500);
		maxPagePerHost = cr.getInt("maxPagePerHost", -1);

		hostPoolSize = cr.getInt("hostPoolSize", 100);
		hostPoolNeedFillMargin = cr.getInt("hostPoolNeedFillMargin", 5);
		parseThread = cr.getInt("parseThread", 8);
		parseQueueMaxSize = cr.getInt("parseQueueMaxSize", 10);
		allowSaveOKFile = cr.getInt("allowSaveOKFile", 1) == 1;
		allowSaveTmpFile = cr.getInt("allowSaveTmpFile", 0) == 1;
		injectHomePagePerHost = cr.getInt("injectHomePagePerHost", 1) == 1;
		crawlConnectTimeout = cr.getInt("crawlConnectTimeout", 10 * 1000);
		crawlReadTimeout = cr.getInt("crawlReadTimeout", 30 * 1000);
		useMDir = cr.getInt("useMDir", 1) == 1;
		useMFileGzip = cr.getInt("useMFileGzip", 0) == 1;

		sortFactor = cr.getInt("sortFactor", 64 * 1024);
		mergeFactor = cr.getInt("mergeFactor", 64 * 1024);

	}

	private Class<?> getClass(String className, Class<?> dft) {
		try {
			if (className == null) {
				log.warn("use default %s", dft);
				return dft;
			} else {
				return Class.forName(className);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public int hostHash(String host) {
		return Math.abs(host.hashCode()) % 1024;
	}

	public File getHostBase(HostInfo info) {
		File f = new File(this.crawlDir, "hosts/"
				+ hostHash(info.getHostName()) + "/"
				+ Misc.formatFileName(info.getHostName(), '-'));

		if (!f.exists()) {
			f.mkdirs();
		}
		return f;
	}

	// public synchronized boolean saveOkUrlDir(HostScanCrawlOut out, long seq,
	// ScoredUrlRecord url, MDir dir) throws IOException {
	// if (seqLw == null) {
	// try {
	// seqLw = new LineWriter(new File(this.saveDir, "crawl.log"),
	// true);
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// if (seqLw != null) {
	// try {
	// seqLw.writeLine(sdf.format(System.currentTimeMillis()) + '['
	// + seq + ']' + '[' + url.getUrl().getHost() + ']' + url);
	// seqLw.flush();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// if (!allowSaveOKFile) {
	// return false;
	// }
	// if (!TchFileTool.put("config", "systemSeq", this.nextSeq() + "")) {
	// throw new RuntimeException("seq save fail!");
	// }
	// if (dir != null) {
	// InputStreamCombin cmbIpt = SynCombin.getCombin(url.getUrl(),
	// out.getInputStream());
	// dir.addFile(out.name, cmbIpt, ScanConfig.instance.useMFileGzip);
	// return true;
	// } else {
	// long par0 = seq / 1024 / 1024;
	// long par1 = seq / 1024 % 1024;
	// File tDir2 = new File(saveDir, "okCrawled/" + par0 + "/" + par1
	// + "/");
	// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	// tDir2 = new File(tDir2, sdf.format(System.currentTimeMillis()));
	// if (!tDir2.exists()) {
	// tDir2.mkdirs();
	// }
	//
	// if (out.useFile) {
	// File f = out.fileToSave;
	// File saveFileName = new File(tDir2, f.getName());
	// if (!f.renameTo(saveFileName)) {
	// byte[] bs = FileGet.getBytes(f);
	// FileOutputStream fos = new FileOutputStream(saveFileName);
	// try {
	// fos.write(bs);
	// } finally {
	// fos.close();
	// }
	// }
	// }
	// return true;
	// }
	//
	// }

	public File getSaveDir(HostInfo hi) {
		return new File(ScanConfig.instance.saveDir, "okCrawled/"
				+ hi.getMyHostSuffix().subSequence(0, 3) + ".h/"
				+ hi.getMyHostSuffix());
	}

	public File getOkFileSaveDirBase() {
		return this.saveDir;
	}

	public boolean isHostTotalPageReached(HostInfo hi) {
		if (this.maxPagePerHost < 0) {
			return false;
		}
		return this.maxPagePerHost < hi.getTotalCrawled();
	}

	public HostScanSiteOutItf newSiteOut(HostInfo hostInfo) {
		try {
			HostScanSiteOutItf itf = (HostScanSiteOutItf) hostScanSiteOutClass
					.getConstructor(hostInfo.getClass()).newInstance(hostInfo);
			return itf;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
}
