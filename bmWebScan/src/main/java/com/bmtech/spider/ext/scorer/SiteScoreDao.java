package com.bmtech.spider.ext.scorer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.bmtech.utils.bmfs.MDir;
import com.bmtech.utils.bmfs.MFile;
import com.bmtech.utils.log.LogHelper;
import com.bmtech.utils.rds.RDS;

public class SiteScoreDao implements ScoreSaver {
	RDS addHostInfo;
	RDS addPageParsedRecord, checkAlreadyHasScored;
	RDS addPageParsedRecordAttach;
	MDir saveDir;
	LogHelper log = new LogHelper("SiteScoreDao");

	public SiteScoreDao(MDir saveDir) throws SQLException {
		init();
		this.saveDir = saveDir;
	}

	public void init() throws SQLException {
		checkAlreadyHasScored = RDS.getRDSByKey("checkAlreadyHasScored");
		addHostInfo = RDS.getRDSByKey("addHostParsedRecord");
		addPageParsedRecord = RDS.getRDSByKey("addPageParsedRecord");
		addPageParsedRecordAttach = RDS
				.getRDSByKey("addPageParsedRecordAttach");

	}

	@Override
	public synchronized void saveHostScore(String host, int scores[])
			throws Exception {
		addHostInfo.setString(1, host);
		addHostInfo.setInt(2, scores[1]);
		addHostInfo.setInt(3, scores[2]);
		addHostInfo.setInt(4, scores[3]);
		addHostInfo.setInt(5, scores[0]);
		addHostInfo.setString(6, "");
		addHostInfo.execute();
	}

	@Override
	public void finalize() {
		addHostInfo.close();
		addPageParsedRecord.close();
		addPageParsedRecordAttach.close();
	}

	public void close() {
		addHostInfo.close();
	}

	@Override
	public synchronized void save(String title, String lines, URL url, int score)
			throws SQLException, IOException {
		String host = url.getHost();
		int urlHash = urlHash(url);
		this.checkAlreadyHasScored.setString(1, host);
		this.checkAlreadyHasScored.setInt(2, urlHash);
		ResultSet rs = checkAlreadyHasScored.executeQuery();
		if (rs.next()) {
			log.warn("already has put %s:%s for url %s with score %s", host,
					urlHash, url, score);
			return;
		}
		this.addPageParsedRecord.setString(1, host);
		this.addPageParsedRecord.setInt(2, score);
		this.addPageParsedRecord.setInt(3, urlHash);
		this.addPageParsedRecord.execute();

		int genId = this.addPageParsedRecord.getGeneratedKey();

		this.addPageParsedRecordAttach.setInt(1, genId);
		this.addPageParsedRecordAttach.setString(2, stringLen(title, 200));
		this.addPageParsedRecordAttach.setString(3,
				stringLen(url.toString(), 800));
		this.addPageParsedRecordAttach.setString(4,
				saveAndGetPath(genId, host, lines));
		this.addPageParsedRecordAttach.execute();
	}

	private int urlHash(URL url) {
		return Math.abs(url.getPath().hashCode());
	}

	private String stringLen(String str, int len) {
		if (str == null) {
			str = "";
		} else {
			str = str.trim();
			if (str.length() > len) {
				str = str.substring(0, len);
			}
		}
		return str;
	}

	private String saveAndGetPath(int id, String host, String lines)
			throws IOException {
		MFile file = this.saveDir.addFile(id + "." + host,
				new ByteArrayInputStream(lines.getBytes()), true);
		return file.getFullFilePath();
	}
}
