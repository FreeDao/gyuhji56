package com.bmtech.spider.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import com.bmtech.spider.core.scan.HostScanCrawlOut;
import com.bmtech.spider.core.util.SynCombin;
import com.bmtech.utils.Misc;
import com.bmtech.utils.bmfs.MDir;
import com.bmtech.utils.io.ConfigReader;
import com.bmtech.utils.io.FileGet;
import com.bmtech.utils.io.InputStreamCombin;
import com.bmtech.utils.io.LineWriter;
import com.bmtech.utils.io.TchFileTool;
import com.bmtech.utils.log.BmtLogHelper;

public class ScanConfig {
	public static final ScanConfig instance = new ScanConfig();

	final BmtLogHelper log = new BmtLogHelper("log");
	private LineWriter seqLw;
	private SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss.SSS");

	public final String readyToCrawl = "ReadyToCrawl.txt";
	public final String hasFailedFileName = "failedToCrawl.txt";
	public final String urlDataBase = "urlDB.txt";
	public final String crawledUrlDB = "urlDB-done.txt";
	public final String crawledUrlTmp = "urlDB-done-tmp.txt";
	// public final String tmpHtmlsDir = "tmpHtmls";

	public final String newUrlDir = "newUrlDir";
	public final String iUrl = "URLDIR";
	public final File tmpCrawlDir;

	public Class<HostFilter> hostFilterCls;
	public final Class<ForeignUrlChecker> foreignUrlCheckClass;

	public final Class<HtmlScore> scorerCls;
	private final File crawlDir;
	private final File saveDir;
	public final AtomicLong systemSeq;
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
	public final int scoreDefault;
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

	public int maxPagePerHost;

	public final boolean useMFileGzip;

	@SuppressWarnings("unchecked")
	private ScanConfig() {
		ConfigReader cr = null;
		try {
			cr = new ConfigReader("config/ws/sc.conf", "hostScan");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		urlCrawlItvSecond = cr.getInt("urlCrawlItvSecond", 5) * 1000;
		try {
			String scoreClass = cr.getValue("scorerCls");
			scorerCls = (Class<HtmlScore>) Class.forName(scoreClass);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}

		try {
			String hostFltCls = cr.getValue("hostFilterCls");
			if (hostFltCls == null || hostFltCls.length() == 0) {
				hostFltCls = "com.bmtech.spider.core.HostFilterAllAccept";
				log.warn("hostFltCls  not set! use default %s", hostFltCls);
			}
			hostFilterCls = (Class<HostFilter>) Class.forName(hostFltCls);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		try {
			String foreignUrlCheckCls = cr.getValue("foreignUrlCheckClass");
			if (foreignUrlCheckCls == null) {
				foreignUrlCheckCls = "com.bmtech.spider.core.ForeignUrlAutoDrop";
				log.warn("foreinUrlCheckClass  not set! use default %s",
						foreignUrlCheckCls);
			}
			foreignUrlCheckClass = (Class<ForeignUrlChecker>) Class
					.forName(foreignUrlCheckCls);

		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}

		systemSeq = new AtomicLong(TchFileTool.getInt("config", "systemSeq", 0));

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
		scoreDefault = cr.getInt("scoreInjected", 70);
		scoreFirstPage = cr.getInt("scoreAllwaysAllow", 100);
		hostInitThreads = cr.getInt("hostInitThreads", 5);
		allowDownload = cr.getInt("allowDownload", 0) == 1;
		maxErrorForEveryPage = cr.getInt("maxErrorForEveryPage", 5);
		maxFileLen = cr.getInt("maxFileLenKB", 3000) * 1000L;
		controllerCheckItv = cr.getInt("controllerCheckItvSecond", 5) * 1000L;

		maxCrawlPagesPerRound = cr.getInt("maxPagePerHost", 500);
		maxPagePerHost = cr.getInt("maxPagePerHost", Integer.MAX_VALUE);

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

	public long nextSeq() {
		long next = systemSeq.addAndGet(1);
		return next;
	}

	public int hostHash(String host) {
		return Math.abs(host.hashCode()) % 1024;
	}

	public File getHostBase(String host) {
		host = host.toLowerCase().trim();
		File f = new File(this.crawlDir, "hosts/" + hostHash(host) + "/"
				+ Misc.formatFileName(host, '-'));

		if (!f.exists()) {
			f.mkdirs();
		}
		return f;
	}

	public synchronized boolean saveOkUrlDir(HostScanCrawlOut out, long seq,
			ScoredUrlRecord url, MDir dir) throws IOException {
		if (seqLw == null) {
			try {
				seqLw = new LineWriter(new File(this.saveDir, "crawl.log"),
						true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (seqLw != null) {
			try {
				seqLw.writeLine(sdf.format(System.currentTimeMillis()) + '['
						+ seq + ']' + '[' + url.getUrl().getHost() + ']' + url);
				seqLw.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (!allowSaveOKFile) {
			return false;
		}
		if (!TchFileTool.put("config", "systemSeq", this.nextSeq() + "")) {
			throw new RuntimeException("seq save fail!");
		}
		if (dir != null) {
			InputStreamCombin cmbIpt = SynCombin.getCombin(url.getUrl(),
					out.getInputStream());
			dir.addFile(out.name, cmbIpt, ScanConfig.instance.useMFileGzip);
			return true;
		} else {
			long par0 = seq / 1024 / 1024;
			long par1 = seq / 1024 % 1024;
			File tDir2 = new File(saveDir, "okCrawled/" + par0 + "/" + par1
					+ "/");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			tDir2 = new File(tDir2, sdf.format(System.currentTimeMillis()));
			if (!tDir2.exists()) {
				tDir2.mkdirs();
			}

			if (out.useFile) {
				File f = out.fileToSave;
				File saveFileName = new File(tDir2, f.getName());
				if (!f.renameTo(saveFileName)) {
					byte[] bs = FileGet.getBytes(f);
					FileOutputStream fos = new FileOutputStream(saveFileName);
					try {
						fos.write(bs);
					} finally {
						fos.close();
					}
				}
			}
			return true;
		}

	}

	public File getSaveDir(String myHostSuffix) {
		return new File(ScanConfig.instance.saveDir, "okCrawled/"
				+ myHostSuffix.subSequence(0, 3) + ".h/" + myHostSuffix);
	}

	public File getOkFileSaveDirBase() {
		return this.saveDir;
	}

	@Override
	public String toString() {
		return "ScanConfig [log=" + log + ", seqLw=" + seqLw + ", sdf=" + sdf
				+ ", readyToCrawl=" + readyToCrawl + ", hasFailedFileName="
				+ hasFailedFileName + ", urlDataBase=" + urlDataBase
				+ ", crawledUrlDB=" + crawledUrlDB + ", crawledUrlTmp="
				+ crawledUrlTmp + ", newUrlDir=" + newUrlDir + ", iUrl=" + iUrl
				+ ", tmpCrawlDir=" + tmpCrawlDir + ", hostFilterCls="
				+ hostFilterCls + ", foreignUrlCheckClass="
				+ foreignUrlCheckClass + ", scorerCls=" + scorerCls
				+ ", crawlDir=" + crawlDir + ", saveDir=" + saveDir
				+ ", systemSeq=" + systemSeq + ", urlCrawlItvSecond="
				+ urlCrawlItvSecond + ", maxFileLen=" + maxFileLen
				+ ", controllerCheckItv=" + controllerCheckItv
				+ ", injectedUrlValue=" + injectedUrlValue
				+ ", maxErrorForEveryPage=" + maxErrorForEveryPage
				+ ", hostPoolSize=" + hostPoolSize
				+ ", hostPoolNeedFillMargin=" + hostPoolNeedFillMargin
				+ ", allowDownload=" + allowDownload + ", allowSaveOKFile="
				+ allowSaveOKFile + ", allowSaveTmpFile=" + allowSaveTmpFile
				+ ", hostInitThreads=" + hostInitThreads + ", scoreInjected="
				+ scoreInjected + ", scoreAllwaysAllow=" + scoreAllwaysAllow
				+ ", scoreDefault=" + scoreDefault + ", scoreFirstPage="
				+ scoreFirstPage + ", injectHomePagePerHost="
				+ injectHomePagePerHost + ", crawlConnectTimeout="
				+ crawlConnectTimeout + ", crawlReadTimeout="
				+ crawlReadTimeout + ", useMDir=" + useMDir
				+ ", maxCrawlPagesPerRound=" + maxCrawlPagesPerRound
				+ ", parseThread=" + parseThread + ", parseQueueMaxSize="
				+ parseQueueMaxSize + ", mergeFactor=" + mergeFactor
				+ ", sortFactor=" + sortFactor + ", maxPagePerHost="
				+ maxPagePerHost + ", useMFileGzip=" + useMFileGzip + "]";
	}

}
