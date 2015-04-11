package com.bmtech.spider.ext.scorer;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.bmtech.spider.core.ScanConfig;
import com.bmtech.utils.Consoler;
import com.bmtech.utils.Misc;
import com.bmtech.utils.bmfs.MDir;
import com.bmtech.utils.log.LogHelper;

public class ScoreExecutor {
	static long maxGizpSize = 1024 * 256;
	static long maxUnzipSize = 1024 * 1024;

	File scoreDir;
	File baseRoot;
	final int exeNum;
	BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();
	ThreadPoolExecutor exe;
	static ScanConfig sc = ScanConfig.instance;
	SiteScoreDao dao;
	LogHelper log = new LogHelper("scoreExe");

	ScoreExecutor(File baseRoot, int exeNum) throws Exception {
		this.exeNum = exeNum;
		this.baseRoot = baseRoot;
		exe = new ThreadPoolExecutor(exeNum, exeNum, exeNum, TimeUnit.SECONDS,
				queue);
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd.HH.mm.ss");
		scoreDir = new File(baseRoot, "../scored@"
				+ sdf.format(System.currentTimeMillis()));
		scoreDir.mkdirs();
		MDir dir = MDir.makeMDir(this.scoreDir, true);
		dao = new SiteScoreDao(dir);
		new Thread() {
			@Override
			public void run() {
				while (true) {
					Misc.sleep(10000);
					log.info("executing %s, executed %s, waiting %s",
							exe.getActiveCount(), exe.getCompletedTaskCount(),
							queue.size());
				}
			}
		}.start();

	}

	public void score() throws Exception {
		File fs[] = baseRoot.listFiles();
		for (File f : fs) {
			File[] dirs = f.listFiles();
			for (final File dir : dirs) {

				SiteScorer ss = new SiteScorer(dir, dao);
				exe.execute(ss);

			}

		}
	}

	public static void main(String[] args) throws Exception {
		String root = Consoler.readString("baseRoot:");
		File baseRoot;
		System.out.println("sc.useMFileGzip:" + sc.useMFileGzip + " sc=" + sc);
		if (root.length() == 0) {
			baseRoot = new File(sc.getOkFileSaveDirBase(), "okCrawled");
		} else {
			baseRoot = new File(root);
		}
		System.out.println("parse " + root);
		int exeNum = Consoler.readInt("exeNum", 4);
		ScoreExecutor ts = new ScoreExecutor(baseRoot, exeNum);
		ts.score();

	}
}
