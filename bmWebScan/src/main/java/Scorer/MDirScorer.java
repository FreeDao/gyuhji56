package Scorer;

import java.io.File;
import java.nio.charset.Charset;

import Scorer.scorers.MultiScorer;

import com.bmtech.spider.core.ScanConfig;
import com.bmtech.utils.Charsets;
import com.bmtech.utils.HtmlToLines;
import com.bmtech.utils.Misc;
import com.bmtech.utils.bmfs.MDir;
import com.bmtech.utils.bmfs.MFile;
import com.bmtech.utils.bmfs.MFileReader;
import com.bmtech.utils.io.LineWriter;
import com.bmtech.utils.log.LogHelper;

public class MDirScorer {
	static long maxGizpSize = 1024 * 256;
	static long maxUnzipSize = 1024 * 1024;
	private final int level1 = 70;
	private final int leve2 = 100;
	private final int leve3 = 130;
	private int level1Num = 0;
	private int level2Num = 0;
	private int level3Num = 0;
	private int scanNum = 0;
	File scoreDir;
	MultiScorer scorer = MultiScorer.getInstance();
	ScanConfig sc = ScanConfig.instance;
	MDir mdir;
	LogHelper log;

	MDirScorer(MDir mdir, File scoreDir) throws Exception {
		this.scoreDir = scoreDir;
		this.mdir = mdir;
		log = new LogHelper(mdir.dataFile.getName());
	}

	private String lastParseFileName;

	void score() throws Exception {
		scoreDir.mkdirs();
		MFileReader reader = mdir.openReader();

		while (reader.hasNext()) {
			scanNum++;
			MFile mf = reader.next();
			lastParseFileName = mf.name;

			byte[] bs;
			if (sc.useMFileGzip) {
				if (mf.getLength() > maxGizpSize) {
					log.warn("skip tooooo big ZIPED %.2fKB named %s",
							mf.getLength() / 1024.0, mf);
					reader.skip();
					continue;
				}
				bs = reader.getBytesUnGZiped();
			} else {
				if (mf.getLength() > maxUnzipSize) {
					log.warn("skip tooooo big  %.2fKB named %s",
							mf.getLength() / 1024.0, mf);
					reader.skip();
					continue;
				}
				bs = reader.getBytes();
			}
			Charset cs = Charsets.getCharset(bs);
			String html = new String(bs, cs);
			String lines = HtmlToLines.htmlToLineFormat(html).lines;
			int score = scorer.score(lines);

			if (score < this.level1)
				continue;
			level1Num++;
			if (score >= this.leve2) {
				this.level2Num++;
			}
			if (score >= this.leve3) {
				this.level3Num++;
			}
			String name = String.format("%04d-%s.txt", score,
					Misc.formatFileName(mf.name));
			File saveFile = new File(scoreDir, name);
			LineWriter lw = new LineWriter(saveFile, false);
			lw.writeLine(lines);
			lw.close();
		}
	}

	public int getLevel1Num() {
		return level1Num;
	}

	public int getLevel2Num() {
		return level2Num;
	}

	public int getLevel3Num() {
		return level3Num;
	}

	public int getScanNum() {
		return scanNum;
	}

	public void setScanNum(int scanNum) {
		this.scanNum = scanNum;
	}

	public String getLastName() {
		return lastParseFileName;
	}

}
