package Scorer;

import java.sql.SQLException;

import com.bmtech.utils.rds.RDS;

public class SiteScoreDao {
	RDS addInfo;

	public SiteScoreDao() throws SQLException {
		addInfo = RDS
				.getRDSByDefine(
						"webscan",
						"insert into webscan_source_detect( host, level_1_count, level_2_count, level_3_count, scan_num, path) values(?,?,?,?,?,?)");
	}

	public synchronized void saveHostScore(String host, MDirScorer scorer)
			throws Exception {
		addInfo.setString(1, host);
		addInfo.setInt(2, scorer.getLevel1Num());
		addInfo.setInt(3, scorer.getLevel2Num());
		addInfo.setInt(4, scorer.getLevel3Num());
		addInfo.setInt(5, scorer.getScanNum());
		addInfo.setString(6, scorer.scoreDir.getCanonicalFile()
				.getAbsolutePath());
		addInfo.execute();
	}

	@Override
	public void finalize() {
		this.close();
	}

	public void close() {
		addInfo.close();
	}
}
