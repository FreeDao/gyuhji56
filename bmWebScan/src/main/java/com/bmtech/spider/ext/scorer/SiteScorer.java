package com.bmtech.spider.ext.scorer;

import java.io.File;

import com.bmtech.utils.bmfs.MDir;
import com.bmtech.utils.io.TchFileTool;
import com.bmtech.utils.log.LogHelper;

public class SiteScorer implements Runnable {
	private LogHelper log;
	String hostName;
	File mdirPath;
	ScoreSaver saver;
	final String parseFlag = "parsed2.flag";

	public SiteScorer(File mdirPath, ScoreSaver saver) {
		this.hostName = mdirPath.getName();
		this.mdirPath = mdirPath;
		this.saver = saver;
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
					MDirScorer scorer = new MDirScorer(mdir, saver);
					scorer.score();
					scorer.saveHostScore(hostName);
					TchFileTool.put(mdirPath, parseFlag, scorer.getLastName());
					log.info("parse total %s, levels %s:%s:%s",
							scorer.getScanNum(), scorer.getLevel1Num(),
							scorer.getLevel2Num(), scorer.getLevel3Num());
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
