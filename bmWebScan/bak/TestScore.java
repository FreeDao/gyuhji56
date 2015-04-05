package Scorer;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import Scorer.scorers.MultiScorer;

import com.bmtech.spider.core.ScanConfig;
import com.bmtech.utils.Charsets;
import com.bmtech.utils.Consoler;
import com.bmtech.utils.HtmlToLines;
import com.bmtech.utils.bmfs.MDir;
import com.bmtech.utils.bmfs.MFile;
import com.bmtech.utils.bmfs.MFileReader;
import com.bmtech.utils.io.LineWriter;

public class TestScore {
	static long maxGizpSize = 1024 * 256;
	static long maxUnzipSize = 1024 * 1024;

	File[] fs;
	File scoreDir;
	MultiScorer scorer;
	File baseRoot;
	final int minScore;
	ExecutorService exe = Executors.newFixedThreadPool(4);
	final AtomicInteger filesNum = new AtomicInteger(0);
	final AtomicInteger dirNum = new AtomicInteger(0);
	static ScanConfig sc = ScanConfig.instance;

	TestScore(File baseRoot, int minScore) throws Exception {
		this.baseRoot = baseRoot;
		this.minScore = minScore;
		fs = baseRoot.listFiles();
		scorer = MultiScorer.getInstance();
		scoreDir = new File(baseRoot, "../scored/");
		scoreDir.mkdirs();
		new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("fileNum " + filesNum + "\t dirNum "
							+ dirNum);
				}
			}
		}.start();

	}

	MDir[] list(File file) throws IOException {
		File fs[] = file.listFiles();
		MDir[] dirs = new MDir[fs.length];
		int x = 0;
		for (File f : fs) {
			MDir dir = MDir.open(f);
			dirs[x] = dir;
			x++;
		}
		return dirs;
	}

	public void score() throws Exception {
		int num0 = 0, num1 = 0;
		for (File f : fs) {
			File[] dirs = f.listFiles();
			num0++;
			for (final File dir : dirs) {
				num1++;
				System.out.println("adding " + dir.getName() + ", " + num0
						+ ":" + num1);
				exe.execute(new Runnable() {

					@Override
					public void run() {

						dirNum.addAndGet(1);
						try {
							File f = new File(dir, "parseOk");

							if (f.exists()) {
								System.out.println("skip " + f);
								return;
							}
							MDir mdir = MDir.open(dir);

							score(mdir);
							f.createNewFile();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

				});

			}

		}
	}

	void score(MDir mdir) throws Exception {
		MFileReader reader = mdir.openReader();

		while (reader.hasNext()) {
			filesNum.addAndGet(1);
			MFile mf = reader.next();
			byte[] bs;
			if (sc.useMFileGzip) {
				if (mf.getLength() > maxGizpSize) {
					System.out.println("skip tooooo big ZIPED "
							+ mf.getLength() + " named " + mf);
					reader.skip();
					continue;
				}
				bs = reader.getBytesUnGZiped();
			} else {
				if (mf.getLength() > maxUnzipSize) {
					System.out.println("skip toooo big " + mf.getLength()
							+ " named " + mf);
					reader.skip();
					continue;
				}
				bs = reader.getBytes();
			}
			Charset cs = Charsets.getCharset(bs);
			String html = new String(bs, cs);
			// System.out.println(html);
			String lines = HtmlToLines.htmlToLineFormat(html).lines;
			// String lt[] = lines.split("\n");
			// for (String linex : lt) {
			// linex = linex.trim();
			// if (linex.length() == 0)
			// continue;
			// int score = scorer.score(linex);
			// System.out.println(linex);
			// System.out.println(score);
			//
			// }
			int score = scorer.score(lines);
			// System.out.println(lines);
			if (filesNum.intValue() % 100 == 0)
				System.out.println(score + " for " + mf.getName());
			if (score < minScore)
				continue;
			File f;
			for (int x = 0;; x++) {
				String name = String.format("%04d-%05d-%s.txt", score, x, mdir
						.getLocalDir().getName());
				f = new File(scoreDir, name);
				if (!f.exists()) {
					break;
				}
			}
			LineWriter lw = new LineWriter(f, false);
			lw.writeLine(lines);
			lw.close();
			// Consoler.readString("");
		}
	}

	public static void main(String[] args) throws Exception {
		String root = Consoler.readString("baseRoot:");

		System.out.println("sc.useMFileGzip:" + sc.useMFileGzip + " sc=" + sc);
		if (root.length() == 0) {
			root = sc.getOkFileSaveDirBase().getCanonicalPath() + "/okCrawled";
		}
		System.out.println("parse " + root);
		int minScore = Consoler.readInt("minScore:", 70);
		File baseRoot = new File(root);
		TestScore ts = new TestScore(baseRoot, minScore);
		ts.score();

	}
}
