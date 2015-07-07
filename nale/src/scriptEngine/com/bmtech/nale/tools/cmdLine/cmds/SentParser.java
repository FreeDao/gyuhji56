package com.bmtech.nale.tools.cmdLine.cmds;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.bmtech.nale.addon.sem.FileSemRuleExportHelper;
import com.bmtech.nale.script.engine.lang.obj.NaleObject;
import com.bmtech.nale.script.engine.lang.obj.StringMatchedInfo;
import com.bmtech.utils.Charsets;
import com.bmtech.utils.Consoler;
import com.bmtech.utils.Misc;
import com.bmtech.utils.io.LineReader;
import com.bmtech.utils.io.LineWriter;

public class SentParser {
	class TT {
		String lineName;
		String attr;
		NaleObject matcher;
		File file;

		public TT(File file) throws Exception {
			String name = file.getName();
			String prf = Misc.substring(name, null, "--~");
			int pos = prf.indexOf(".");
			this.lineName = prf.substring(0, pos);
			this.attr = prf.substring(pos + 1);
			this.file = file;
			FileSemRuleExportHelper helper = FileSemRuleExportHelper
					.getFromFile(file);
			this.matcher = helper.getToExportObj();
		}

	}

	ArrayList<TT> list = new ArrayList<TT>();

	public SentParser(File[] fs) {
		if (fs == null) {
			System.out.println("没有找到规则文件");
			return;
		}
		for (File file : fs) {
			try {
				TT tt = new TT(file);
				list.add(tt);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void parse(LineReader lr, LineWriter lw) throws IOException {
		while (lr.hasNext()) {
			lw.writeLine("\n\n------><------------");
			System.out.println("\n\n------><------------");
			String line = lr.next();
			List<String> sents = toSent(line);
			for (String sent : sents) {
				lw.writeLine("#" + sent);
				System.out.println("#" + sent);
				for (TT tt : list) {
					StringMatchedInfo[] matched = tt.matcher.scanMatcher(sent);
					if (matched.length > 0) {
						// matched
						lw.writeLine("\t\t" + tt.attr + "\t\t"
								+ tt.file.getName());

						System.out.println("\t\t" + tt.attr + "\t\t"
								+ tt.file.getName());
					}
				}
			}
		}
	}

	private List<String> toSent(String line) {
		StringBuilder sb = new StringBuilder();
		List<String> list = new ArrayList<String>();
		for (char c : line.trim().toCharArray()) {
			sb.append(c);
			if (c == '。' || c == '？' || c == '！') {
				list.add(sb.toString());
				sb.setLength(0);
			}
		}
		if (sb.length() > 0) {
			list.add(sb.toString());
		}
		return list;
	}

	public static void parse(File useFile, File[] rules) throws IOException {
		SentParser sp = new SentParser(rules);

		LineReader lr = new LineReader(useFile, Charsets.UTF8_CS);
		File f = new File(useFile.getParentFile().getParentFile(), "tmp");
		f.mkdirs();
		File writeTo = new File(f, "parsed." + useFile.getName());
		LineWriter lw = new LineWriter(writeTo, false, Charsets.UTF8_CS);
		sp.parse(lr, lw);
		lw.close();
	}

	public static void main(String[] args) throws IOException {
		File fs[] = new File(
				"E:\\github\\git\\commentparse\\nale\\wkrspc\\schema\\court.json.attr")
				.listFiles();
		SentParser sp = new SentParser(fs);

		File from = new File(Consoler.readString("test file ："));
		LineReader lr = new LineReader(from, Charsets.UTF8_CS);
		File f = new File(from.getParentFile().getParentFile(), "tmp");
		f.mkdirs();
		File writeTo = new File(f, "parsed." + from.getName());
		LineWriter lw = new LineWriter(writeTo, false);
		sp.parse(lr, lw);
		lw.close();
	}
}
