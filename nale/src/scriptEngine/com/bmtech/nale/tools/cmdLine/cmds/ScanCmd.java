package com.bmtech.nale.tools.cmdLine.cmds;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

import com.bmtech.nale.script.engine.jmo.lang.source.SourcePosition;
import com.bmtech.nale.script.engine.lang.NaleContext;
import com.bmtech.nale.script.engine.lang.debug.NaleDebugger;
import com.bmtech.nale.script.engine.lang.obj.NaleObject;
import com.bmtech.nale.script.engine.lang.obj.StringMatchedInfo;
import com.bmtech.nale.tools.cmdLine.DebugException;
import com.bmtech.nale.tools.cmdLine.ShellCmd;
import com.bmtech.nale.tools.scriptShell.NaleShell;
import com.bmtech.utils.Consoler;
import com.bmtech.utils.io.ConfigReader;
import com.bmtech.utils.io.LineWriter;

public class ScanCmd extends ShellCmd {
	public static final String help[] = new String[] {
			"scan: inputFile use $pattern and get the matched",
			"scan $pattern  扫描 inputFile using 表达式$pattern",
			"scan $pattern @file , 扫描 @file using 表达式$pattern",
			"scan $pattern into @file , 扫描 inputFile using 表达式$pattern, 并将结果写入文件 $file",
			"scan $pattern @fromFile into @file , 扫描 inputFile using 表达式$pattern, 并将结果写入文件 $file", };
	private final File baseDir;

	public ScanCmd(NaleDebugger debugger) throws IOException {
		super(asHelpInfo(help), debugger);
		ConfigReader cr = new ConfigReader(NaleShell.cfgFile, "scan");
		baseDir = new File(cr.getValue("baseDir", "wkrspc/cmdLine/scan.saved"));
		if (!baseDir.exists()) {
			baseDir.mkdirs();
		}

	}

	@Override
	public void run(String paras[], SourcePosition pos) throws DebugException,
			IOException {
		if (paras.length == 0) {
			System.out.println(" usage:");
			for (String x : help) {

				System.out.println("\t" + x);
			}
			return;
		}

		String varName = paras[0];

		if (varName.startsWith("$") || varName.startsWith("#")) {
			varName = varName.substring(1).trim();
		}
		if (varName.length() == 0) {
			throw new DebugException("varName is empty", pos);
		}
		int gGroupPos = varName.lastIndexOf("."), group = 0;
		// System.out.println(gGroupPos);
		if (gGroupPos != -1) {
			// System.out.println(varName.substring(gGroupPos + 1));
			Integer itg = intValue(varName.substring(gGroupPos + 1));
			if (itg != null && itg >= 0) {
				group = itg;
				varName = varName.substring(0, gGroupPos);
			}
		}

		NaleContext ctx = this.debugger.getContext();

		if (!ctx.hasVar(varName, pos)) {
			System.out.println("there is no var : " + varName);
			return;
		}

		File writeTo = null;
		LineWriter lw = null;
		File inputFile;
		switch (paras.length) {
		case 1:
			inputFile = cmdLine.getInputFile();
			break;
		case 2:
			inputFile = this.file(paras, 1);
			break;
		case 3:
			inputFile = cmdLine.getInputFile();
			this.paraEqualsIgnorecase(paras[1], "into");
			writeTo = file(paras, 2);
			break;
		case 4:
			inputFile = file(paras, 1);
			this.paraEqualsIgnorecase(paras[2], "into");
			writeTo = file(paras, 3);
			break;
		default:
			throw new DebugException("too much arguments, they are "
					+ Arrays.asList(paras));
		}
		besureFileExsits(inputFile);

		if (writeTo != null) {
			writeTo = writeTo.getAbsoluteFile();
			if (writeTo.exists()) {
				throw new DebugException(
						"can not save into file becouse the file already exists， file is '"
								+ writeTo.getAbsoluteFile().getCanonicalPath()
								+ "'");
			}
			System.out.println("write to file " + writeTo.getAbsolutePath());
			writeTo.getParentFile().mkdirs();
			lw = new LineWriter(writeTo, false, Charset.forName("utf-8"));

		}

		NaleObject obj = ctx.getVar(varName, pos);

		String info = FileStringItrator.reminderPrefix + "0 input file "
				+ inputFile + ", size " + inputFile.length();
		if (lw != null) {
			lw.writeLine(info);
		}
		Consoler.readString(info + "\n press any key to continue...");
		FileStringItrator lr = new FileStringItrator(inputFile);
		int matchedLines = 0, matchedPattern = 0, scanLines = 0;
		int scanNumber = ScanNumCmd.scanNumber();
		while (lr.hasNext()) {

			if (scanNumber != -1) {
				if (scanLines >= scanNumber) {
					break;
				}
			}
			String line = lr.next();
			// System.out.println(line);
			scanLines++;

			StringMatchedInfo[] infos = obj.scanMatcher(line);

			if (infos.length > 0) {
				matchedLines++;
				String reminder = lr.getLineReminder();
				System.out.println("");
				System.out.println(reminder);
				if (lw != null) {
					lw.writeLine("");
					lw.writeLine(reminder);
				}

				for (StringMatchedInfo smi : infos) {
					matchedPattern++;
					String varMatched;
					if (group <= 0) {
						varMatched = smi.matchedString();
					} else {
						varMatched = smi.matchedString(group - 1);
					}
					String toPrint = "\t" + varMatched;
					System.out.println(toPrint);
					if (lw != null) {
						lw.writeLine(toPrint);
					}
				}
			}
		}
		String dieMsg = FileStringItrator.reminderPrefix + "end  scanLines = "
				+ scanLines + ", matchedLines = " + matchedLines
				+ ", matchedPattern = " + matchedPattern;
		System.out.println(dieMsg);

		if (lw != null) {
			lw.writeLine(dieMsg);
			lw.close();
		}
		if (writeTo != null) {
			System.out.println("save to file " + writeTo + ", size "
					+ writeTo.length());
		}

	}

}
