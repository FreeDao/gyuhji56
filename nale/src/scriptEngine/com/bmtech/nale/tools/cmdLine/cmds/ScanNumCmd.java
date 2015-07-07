package com.bmtech.nale.tools.cmdLine.cmds;

import java.io.IOException;

import com.bmtech.nale.script.engine.jmo.lang.source.SourcePosition;
import com.bmtech.nale.script.engine.lang.debug.NaleDebugger;
import com.bmtech.nale.tools.cmdLine.DebugException;
import com.bmtech.nale.tools.cmdLine.ShellCmd;

public class ScanNumCmd extends ShellCmd {

	public ScanNumCmd(NaleDebugger debugger) {
		super("设置扫描行数", debugger);
	}

	public static int scanNumber() throws IOException {
		String str = System.getProperty("scanNo");
		if (str == null) {
			return 500;
		} else {
			return intValue(str);
		}
	}

	@Override
	public void run(String paras[], SourcePosition pos) throws DebugException,
			IOException {
		if (paras.length == 0) {
			System.out.println(scanNumber());
			return;
		}
		Integer itg = intValue(paras[0]);
		if (itg == null) {
			System.out.println("invalid number " + paras[0]);
		} else {
			System.setProperty("scanNo", paras[0]);
			System.out.println("set scan number to " + paras[0]);
		}

	}

}
