package com.bmtech.utils.distCrawler;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.bmtech.utils.Misc;
import com.bmtech.utils.io.ConfigReader;

public class ClientConfig {
	public final int crawlPoolSizeCore;
	public final int crawlPoolSizeMax;
	public final File baseDir, tmpDir;
	public boolean allowDownload = true;

	private ClientConfig() {
		ConfigReader cr = new ConfigReader("config/ws/sc.conf", "hostScan");

		File baseDir0 = new File(cr.getValue("baseDir"));
		if (!baseDir0.exists()) {
			baseDir0.mkdirs();
		}

		baseDir = new File(baseDir0, "hosts");
		if (!baseDir.exists()) {
			baseDir.mkdirs();
		}
		crawlPoolSizeCore = cr.getInt("crawlPoolSizeCore", 100);
		crawlPoolSizeMax = cr.getInt("crawlPoolSizeMax", 100);

		String tmpDir = cr.getValue("tmpDir");
		if (tmpDir == null) {
			this.tmpDir = new File(baseDir0,
					"tmp-"
							+ new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss")
									.format(new Date()));
		} else {
			this.tmpDir = new File(tmpDir);
			if (!this.tmpDir.exists()) {
				this.tmpDir.mkdirs();
			} else {
				if (!this.tmpDir.isDirectory()) {
					throw new RuntimeException("is not directory : "
							+ this.tmpDir);
				}
			}
		}
	}

	public static final ClientConfig instance = new ClientConfig();

	public int hostHash(String host) {
		return Math.abs(host.hashCode()) % 1024;
	}

	public File getHostBase(String host) {
		host = host.toLowerCase().trim();
		File f = new File(this.baseDir, hostHash(host) + "/"
				+ Misc.formatFileName(host, '-'));
		if (!f.exists()) {
			f.mkdirs();
		}
		return f;
	}

	public static String urlHost(URL url) {
		return url.getHost().toLowerCase().trim();
	}

}
