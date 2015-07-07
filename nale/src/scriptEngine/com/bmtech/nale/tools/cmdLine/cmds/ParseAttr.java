package com.bmtech.nale.tools.cmdLine.cmds;

import java.io.File;

import com.bmtech.nale.script.engine.jmo.lang.source.SourcePosition;
import com.bmtech.nale.script.engine.lang.debug.NaleDebugger;
import com.bmtech.nale.tools.cmdLine.CmdLine;
import com.bmtech.nale.tools.cmdLine.ShellCmd;

public class ParseAttr extends ShellCmd {

	public ParseAttr(NaleDebugger debugger) {
		super("ParseAttr", debugger);
	}

	@Override
	public void run(String[] paras, SourcePosition pos) throws Exception {
		if (paras.length < 1) {
			System.out.println("请指定段行名， 如 原告诉称");
			return;
		}
		File useFile = this.cmdLine.getInputFile();
		File schema = CmdLine.getInstance().getSchemaFile();
		File fAttr = new File(schema.getParentFile(), schema.getName()
				+ ".attr");

		String str = paras[0];
		fAttr = new File(fAttr, str);

		SentParser.parse(useFile, fAttr.listFiles());
	}
}
