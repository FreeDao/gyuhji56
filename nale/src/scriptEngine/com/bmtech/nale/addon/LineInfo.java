package com.bmtech.nale.addon;

public class LineInfo {

	private String line;
	private int lineNo;
	private SemMatchInfo[] matchedInfo;

	public LineInfo(String line, int lineNo) {
		this.line = line;
		this.lineNo = lineNo;
	}

	public String getLine() {
		return line;
	}

	public int getLineNo() {
		return this.lineNo;
	}

	public void setMatch(SemMatchInfo[] infos) {
		this.matchedInfo = infos;
	}

	public boolean hasMatch() {
		return matchedInfo != null && matchedInfo.length > 0;
	}

	public SemMatchInfo[] getMatched() {
		return this.matchedInfo;
	}
}
