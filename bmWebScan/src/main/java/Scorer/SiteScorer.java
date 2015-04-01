package Scorer;

import java.io.File;

import com.bmtech.utils.bmfs.MDir;
import com.bmtech.utils.io.TchFileTool;
import com.bmtech.utils.log.LogHelper;

public class SiteScorer implements Runnable {
	private LogHelper log;
	String hostName;
	File mdirPath;
	SiteScoreDao dao;
	File saveDir;
	final String parseFlag = "parsed.flag";

	public SiteScorer(File mdirPath, SiteScoreDao dao, File saveDir) {
		this.hostName = mdirPath.getName();
		this.mdirPath = mdirPath;
		this.dao = dao;
		this.saveDir = saveDir;
		log = new LogHelper(hostName);
	}

	@Override
	public void run() {
		log.info("parsing %s", hostName);
		try {
			String flag = TchFileTool.get(mdirPath, parseFlag);
			if (flag != null) {// FIXME should move to lastFile
				log.info("skip parsed %s", mdirPath);
			} else {
				MDir mdir = MDir.open(mdirPath);
				try {
					MDirScorer scorer = new MDirScorer(mdir, saveDir);
					scorer.score();
					dao.saveHostScore(hostName, scorer);
					TchFileTool.put(mdirPath, parseFlag, scorer.getLastName());
					log.info("parse total %s, levels %s:%s:%s",
							scorer.getScanNum(), scorer.getLevel1Num(),
							scorer.getLevel2Num(), scorer.getLevel3Num());
					if (saveDir.listFiles().length == 0) {
						saveDir.delete();
					}
				} finally {
					MDir.closeMDir(mdir);
				}
			}
		} catch (Exception e) {
			log.error(e, "when parse");
			e.printStackTrace();
		}

	}
}
