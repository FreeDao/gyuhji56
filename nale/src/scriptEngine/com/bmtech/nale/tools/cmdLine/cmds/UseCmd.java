package com.bmtech.nale.tools.cmdLine.cmds;

import java.io.File;

import com.bmtech.nale.script.engine.jmo.lang.source.SourcePosition;
import com.bmtech.nale.script.engine.lang.debug.NaleDebugger;
import com.bmtech.nale.tools.cmdLine.DebugException;
import com.bmtech.nale.tools.cmdLine.ShellCmd;

public class UseCmd extends ShellCmd {

	public UseCmd(NaleDebugger debugger) {
		super(
				"设置默认扫描目标的文件名， 如 [debug-1] use @javas.txt, 则 xx.match() 等效于 xx.match(@/javas.txt)",
				debugger);
	}

	@Override
	public void run(String[] paras, SourcePosition pos) throws Exception {

		if (paras.length == 0) {
			File f = cmdLine.getInputFile().getAbsoluteFile()
					.getCanonicalFile();
			System.out.println("use intput file '" + f + "', length "
					+ f.length());
			return;
		}
		expectParasNumber(paras, 1);

		File f;
		if (paras[0].startsWith("@")) {
			f = this.file(paras, 0);
		} else {
			f = new File(paras[0]).getAbsoluteFile();
		}
		if (!f.exists() || f.isDirectory()) {
			throw new DebugException("文件不存在或者是文件夹：" + f);
		}
		cmdLine.setInputFile(f);
	}

}
