package Scorer.salon;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;

import Scorer.SiteScoreDao;

import com.bmtech.spider.core.ScanConfig;
import com.bmtech.utils.bmfs.MDir;

public class OnlineSavor {
	private static final OnlineSavor instance = new OnlineSavor();
	SiteScoreDao dao;
	final MDir mdir;

	private OnlineSavor() {
		try {
			File baseRoot = ScanConfig.instance.getOkFileSaveDirBase()
					.getParentFile();
			File scoreDir = new File(baseRoot, "HtmlScored@"
					+ new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(System
							.currentTimeMillis()));
			scoreDir.mkdirs();
			mdir = MDir.makeMDir(scoreDir, true);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static OnlineSavor getInstance() {
		return instance;
	}

	public void close() {
		MDir.closeMDir(mdir);
	}

	public synchronized void save(String title, String lines, URL url, int score) {
		try {
			if (dao == null) {
				dao = new SiteScoreDao(mdir);
			}
			dao.save(title, lines, url, score);
		} catch (Exception e) {
			e.printStackTrace();
			dao.close();
			dao = null;

		}

	}

}
