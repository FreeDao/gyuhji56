package tv.fun.addon.sem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nale.script.engine.lang.obj.NaleObject;
import nale.script.engine.lang.obj.StringMatchedInfo;
import tv.fun.addon.CorpRecord;
import tv.fun.addon.CorpRecordIterator;
import tv.fun.addon.LineInfo;

import com.bmtech.utils.Charsets;
import com.bmtech.utils.Consoler;
import com.bmtech.utils.io.LineReader;

public class MatchResult {
	List<FileSemRuleExportHelper> expVars = new ArrayList<FileSemRuleExportHelper>();
	boolean showLine = true;
	boolean showMatched = true;
	boolean pauseAtNotMatch;
	int maxRecord;
	boolean pauseAtRecordEnd = true;

	MatchResult(boolean pauseAtNotMatch, int maxLine) throws Exception {
		this.pauseAtNotMatch = pauseAtNotMatch;
		this.maxRecord = maxLine;
	}

	public void loadVars() throws Exception {
		File dir = new File(
				"E:\\github\\git\\commentparse\\nale\\wkrspc\\schema\\court.json.def");
		File[] files = dir.listFiles();

		int varId = 0;

		for (File f : files) {
			FileSemRuleExportHelper helper = FileSemRuleExportHelper
					.getFromFile(f);
			NaleObject obj = helper.getToExportObj();
			String name = "I" + varId++;
			expVars.add(helper);
			System.out.println(name + "=" + obj);
		}
	}

	void parse() throws IOException {
		LineReader lr = new LineReader("/lihun.txt", Charsets.UTF8_CS);
		CorpRecordIterator itr = new CorpRecordIterator(lr);
		int scanNo = 0;
		int matched = 0;
		int recordNo = 0;
		while (itr.hasNext()) {
			recordNo++;
			CorpRecord rec = itr.next();
			LineInfo lis[] = rec.getLines();

			for (LineInfo li : lis) {
				scanNo++;

				String line = li.getLine();

				List<FileSemRuleExportHelper> matchedResult = new ArrayList<FileSemRuleExportHelper>(
						2);
				for (FileSemRuleExportHelper x : this.expVars) {
					try {
						NaleObject obj = x.getToExportObj();

						StringMatchedInfo[] infos = obj.scanMatcher(line);

						if (infos.length > 0) {

							matchedResult.add(x);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (this.showLine
						|| (this.showMatched && matchedResult.size() > 0)) {
					System.out.println("\n\n@" + li.getLineNo());
					System.out.println(line);
				}
				if (this.showMatched) {
					for (FileSemRuleExportHelper x : matchedResult) {
						System.out.println("--> " + x.getAttachTo() + "\t(by "
								+ x.getToExportVar() + ")");
					}
				}
				if (matchedResult.size() > 0) {
					matched++;
				} else {
					if (this.pauseAtNotMatch) {
						String str = Consoler.readString("-->");
						if (str.endsWith("q") || str.equals("quit")) {
							break;
						}
					}
				}

			}

			if (recordNo > this.maxRecord) {
				break;
			}
			if (pauseAtRecordEnd) {
				String str = Consoler.readString("~~~ " + recordNo
						+ " record end ~~~~~~~~");
				if (str.endsWith("q") || str.equals("quit")) {
					break;
				}
			}
		}
		System.out.println(String.format(
				"\ntotal scan %s, matched %s, 匹配百分比 %s", scanNo, matched,
				(matched * 1000 / scanNo) / 10.0));
	}

	public static void main(String[] args) throws Exception {

		boolean pause = true;
		int maxLine = 50;
		for (String x : args) {
			if (x.equalsIgnoreCase("c")) {
				pause = false;
			} else {
				try {
					if (x.startsWith("m")) {
						maxLine = Integer.parseInt(x.substring(1));
					}
				} catch (Exception e) {
				}
			}
		}

		MatchResult test = new MatchResult(pause, maxLine);
		test.loadVars();
		test.parse();
	}
}
