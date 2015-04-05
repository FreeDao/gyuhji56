package Scorer;

import java.nio.charset.Charset;

import Scorer.scorers.MultiScorer;

import com.bmtech.htmls.parser.Parser;
import com.bmtech.htmls.parser.util.NodeList;
import com.bmtech.spider.core.CoreUtil;
import com.bmtech.spider.core.ScanConfig;
import com.bmtech.spider.core.util.SynCombin;
import com.bmtech.spider.core.util.SynCombin.DecodeSynCombin;
import com.bmtech.utils.Charsets;
import com.bmtech.utils.bmfs.MDir;
import com.bmtech.utils.bmfs.MFile;
import com.bmtech.utils.bmfs.MFileReader;
import com.bmtech.utils.log.LogHelper;

public class MDirScorer {
	static long maxGizpSize = 1024 * 512;
	static long maxUnzipSize = 1024 * 1024 * 2;
	public static final int level1 = 70;
	public static final int level2 = 100;
	public static final int level3 = 130;
	private int level1Num = 0;
	private int level2Num = 0;
	private int level3Num = 0;
	private int scanNum = 0;
	MultiScorer scorer = MultiScorer.getInstance();
	ScanConfig sc = ScanConfig.instance;
	MDir mdir;
	LogHelper log;
	final ScoreSaver savor;

	MDirScorer(MDir mdir, ScoreSaver savor) throws Exception {
		this.savor = savor;
		this.mdir = mdir;
		log = new LogHelper(mdir.dataFile.getName());
	}

	private String lastParseFileName;

	void score() throws Exception {
		MFileReader reader = mdir.openReader();

		while (reader.hasNext()) {
			scanNum++;
			MFile mf = reader.next();
			lastParseFileName = mf.getName();

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
			Charset cs = Charsets.getCharset(bs, true);
			String htmlEnc = new String(bs, cs);
			DecodeSynCombin cmb = SynCombin.parse(htmlEnc);

			Parser p = new Parser(cmb.html);
			NodeList nl = p.parse(null);
			String lines = nl.asString();

			String title = CoreUtil.getHtmlTitle(nl);

			// String lines = HtmlToLines.htmlToLineFormat(cmb.html).lines;
			int score = scorer.score(lines);

			if (score < level1)
				continue;
			level1Num++;
			if (score >= level2) {
				this.level2Num++;
			}
			if (score >= level3) {
				this.level3Num++;
			}
			// System.out.println(cmb.html);
			// System.out.println(cmb.url);
			// System.out.println(title);
			//
			// Consoler.readString("~:");
			savor.save(title, lines, cmb.url, score);

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

	public int[] scoreArray() {
		return new int[] { this.scanNum, this.level1Num, this.level2Num,
				this.level3Num };
	}

	public void saveHostScore(String hostName) throws Exception {
		savor.saveHostScore(hostName, this.scoreArray());
	}
}
