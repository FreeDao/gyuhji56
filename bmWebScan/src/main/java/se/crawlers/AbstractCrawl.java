package se.crawlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.bmtech.utils.bmfs.MDir;
import com.bmtech.utils.http.HttpCrawler;
import com.bmtech.utils.log.LogHelper;
import com.bmtech.utils.rds.RDS;

public abstract class AbstractCrawl extends Thread {
	class WordCount {
		String word;
		int crawl;
		int okCount;

		public WordCount(String word, int crawl, int okCount) {
			this.word = word;
			this.crawl = crawl;
			this.okCount = okCount;
		}

		@Override
		public String toString() {
			return "WordCount [word=" + word + ", crawl=" + crawl
					+ ", okCount=" + okCount + "]";
		}

	}

	int index;

	LogHelper log;
	private int crawlNum, okNum;

	MDir dir;
	File tmpFile;
	SeCrawlConfig conf;

	public AbstractCrawl(int index) throws IOException {
		this.index = index;
		conf = new SeCrawlConfig(index);
		log = new LogHelper(SeCrawlConfig.names[index]);

		dir = MDir.makeMDir(conf.saveDir, true);
		tmpFile = new File(conf.saveDir, "tmp.dir.html");
	}

	private WordCount word() throws SQLException {
		RDS rds = null;
		try {
			rds = RDS
					.getRDSByDefine(
							"seCrawl",
							"select word,crawl,okcount from secrawl where site=? order by crawl, id limit 1");
			rds.setInt(1, index);
			ResultSet rs = rds.executeQuery();
			rs.next();
			String word = rs.getString(1);
			int crawl = rs.getInt(2);
			int okCount = rs.getInt(3);
			rs.close();
			return new WordCount(word, crawl, okCount);
		} finally {
			if (rds != null) {
				rds.close();
			}
		}

	}

	abstract URL getUrl(WordCount word) throws Exception;

	@Override
	public void run() {

		while (true) {

			try {
				log.info("start crawl, now %s:%s", this.okNum, this.crawlNum);
				round();
				Thread.sleep(conf.sleepItvSec);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void round() {
		boolean okCrawl = false;
		WordCount word = null;
		try {
			word = this.word();
			this.crawlNum++;
			log.info("crawling %s, ", word);
			crawl(word);
			this.okNum++;
			okCrawl = true;
		} catch (Exception e) {
			log.error(e, "when crawl");
			e.printStackTrace();
		} finally {
			if (word != null) {
				try {
					saveStatus(word, okCrawl);
				} catch (SQLException e) {
					log.error(e, "when saveStatus");
					e.printStackTrace();
				}
			}

		}
	}

	void crawl(WordCount word) throws Exception {
		URL url = this.getUrl(word);
		log.info("crawling %s", url);
		HttpCrawler crl = new HttpCrawler(url);
		crl.connect();
		crl.dumpTo(tmpFile);
		crl.close();
		log.info("file size %.1fk for %s:%s", tmpFile.length() / 1024.0, word,
				url);
		FileInputStream fis = new FileInputStream(tmpFile);
		dir.addFile(
				word.word + "." + word.crawl + ".okCount."
						+ System.currentTimeMillis(), fis, true);
	}

	private void saveStatus(WordCount word, boolean okCrawl)
			throws SQLException {
		RDS rds = RDS
				.getRDSByDefine("seCrawl",
						"update secrawl  set crawl = ?, okcount=? where site=?  and word=?");
		try {
			rds.setInt(1, word.crawl + 1);
			rds.setInt(2, word.okCount + (okCrawl ? 1 : 0));
			rds.setInt(3, index);
			rds.setString(4, word.word);
			rds.execute();
		} finally {
			rds.close();
		}
	}
}
