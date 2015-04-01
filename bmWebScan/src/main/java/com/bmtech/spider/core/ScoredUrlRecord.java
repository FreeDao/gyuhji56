package com.bmtech.spider.core;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Comparator;

import com.bmtech.utils.io.diskMerge.MRecord;
import com.bmtech.utils.io.diskMerge.MReduceSelect;

public class ScoredUrlRecord extends MRecord {
	public static final int maxPageScore = 999;
	public static final int maxUrlRefer = 999999;
	public static final Comparator<MRecord> scoreCmp = new Comparator<MRecord>() {
		@Override
		public int compare(MRecord o1, MRecord o2) {
			ScoredUrlRecord s1 = (ScoredUrlRecord) o1, s2 = (ScoredUrlRecord) o2;
			int ret = s2.score - s1.score;
			if (ret == 0) {
				ret = s1.refered - s2.refered;
				if (ret == 0) {
					return s1.hashId - s2.hashId;
				}
			}
			return ret;
		}
	};
	public static final Comparator<MRecord> urlHashCmp = new Comparator<MRecord>() {
		@Override
		public int compare(MRecord o1, MRecord o2) {
			return ((ScoredUrlRecord) o1).hashId
					- ((ScoredUrlRecord) o2).hashId;
		}
	};
	public static final MReduceSelect urlEqualor = new MReduceSelect() {
		@Override
		public boolean equals(MRecord o1, MRecord o2) {
			ScoredUrlRecord i1 = (ScoredUrlRecord) o1;
			ScoredUrlRecord i2 = (ScoredUrlRecord) o2;
			if (i1.hashId == i2.hashId) {
				return i1.getUrl().toString().equals(i2.getUrl().toString());
			} else {
				return false;
			}
		}

		@Override
		public MRecord select(MRecord me1, MRecord me2) {
			ScoredUrlRecord i1 = (ScoredUrlRecord) me1;
			ScoredUrlRecord i2 = (ScoredUrlRecord) me2;
			i1.setErrorCount(Math.max(i1.errorCount, i2.errorCount));
			i1.setScore(Math.max(i1.score, i2.score));
			i1.setRefered(i1.refered + i2.refered);
			return i1;
		}
	};

	public int reinitScore() {
		if (this.errorCount > 0) {
			this.setScore(this.score / this.errorCount);
		}
		return this.getScore();
	}

	private int hashId;
	private int score;
	private URL url;
	private int refered = 1;
	private int errorCount = 0;

	public ScoredUrlRecord() {

	}

	public ScoredUrlRecord(String str) throws MalformedURLException {
		this.init(str);
	}

	public ScoredUrlRecord(URL url, int score) {
		this.url = url;
		this.hashId = fmtHash(getUrl());
		setScore(score);
	}

	public static int fmtHash(URL url) {
		return Math.abs(url.toString().toLowerCase().hashCode());
	}

	@Override
	public String serialize() {
		return String.format("%010d %06d %03d %1d %s", this.hashId,
				this.refered, this.score, errorCount, url);
	}

	@Override
	public String toString() {
		return this.serialize();
	}

	@Override
	public void init(String str) throws MalformedURLException {
		if (str.charAt(14) == ' ') {
			String scores = str.substring(11, 14);
			String errorCnt = str.substring(15, 16);
			String urls = str.substring(17);
			this.setErrorCount(Integer.parseInt(errorCnt));
			this.url = new URL(urls);
			this.hashId = fmtHash(getUrl());
			setScore(Integer.parseInt(scores));
			this.setRefered(0);
		} else {
			String refered = str.substring(11, 17);
			String scores = str.substring(18, 21);

			String errorCnt = str.substring(22, 23);
			String urls = str.substring(24);
			this.setErrorCount(Integer.parseInt(errorCnt));
			this.url = new URL(urls);
			this.hashId = fmtHash(getUrl());
			setScore(Integer.parseInt(scores));
			this.setRefered(Integer.parseInt(refered));
		}
	}

	public URL getUrl() {
		return url;
	}

	public void setErrorCount(int errorCount) {
		if (errorCount > 9) {
			errorCount = 9;
		} else if (errorCount < 0) {
			this.errorCount = 0;
		} else {
			this.errorCount = errorCount;
		}
	}

	@Override
	public Object clone() {
		ScoredUrlRecord su = new ScoredUrlRecord();
		su.errorCount = this.errorCount;
		su.hashId = this.hashId;
		su.score = this.score;
		su.url = this.url;
		return su;
	}

	public void setScore(int scoreInner) {
		int score = 0;
		if (scoreInner > maxPageScore) {
			score = maxPageScore;
		} else if (scoreInner < 0) {
			score = 0;
		} else {
			score = scoreInner;
		}
		this.score = score;
	}

	public void increaseFailTime() {
		this.setErrorCount(1 + errorCount);

	}

	public void setRefered(int refered) {
		if (refered > maxUrlRefer) {
			refered = maxUrlRefer;
		} else if (refered < 0) {
			this.refered = 0;
		} else {
			this.refered = refered;
		}
	}

	public int getRefered() {
		return refered;
	}

	public int getHashId() {
		return hashId;
	}

	public int getScore() {
		return score;
	}

	public int getErrorCount() {
		return errorCount;
	}

	public static void main(String[] a) throws MalformedURLException {
		URL u = new URL("http://xxx.dd/dd");
		ScoredUrlRecord su = new ScoredUrlRecord(u, 5);
		System.out.println(su.serialize());
		su.init("2742611593 000880 005 0 http://xxx.dd/dd");
		System.out.println(su.serialize());

		su.init("2742611593 005 0 http://xxx.dd/dd");
		System.out.println(su.serialize());
	}
}
