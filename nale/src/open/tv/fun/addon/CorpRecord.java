package tv.fun.addon;

import java.util.ArrayList;
import java.util.List;

public class CorpRecord {
	LineInfo[] lines;
	private final int lineCount;

	public CorpRecord(List<String> lines, int lineOffset) {
		int lineNo = 0;
		List<LineInfo> list = new ArrayList<LineInfo>();

		for (String x : lines) {
			lineNo++;
			x = x.trim();
			if (x.length() == 0) {
				continue;
			}
			LineInfo li = new LineInfo(x, lineNo + lineOffset);
			list.add(li);
		}
		this.lines = list.toArray(new LineInfo[list.size()]);
		this.lineCount = lineNo;
	}

	public LineInfo[] getLines() {
		return this.lines;
	}

	public int getLineCount() {
		return lineCount;
	}
}
