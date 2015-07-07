package com.bmtech.nale.tools.cmdLine;

import java.io.File;
import java.util.Arrays;

import com.bmtech.nale.script.engine.lang.debug.DebugCmd;
import com.bmtech.nale.script.engine.lang.debug.NaleDebugger;
import com.bmtech.utils.Misc;

public abstract class ShellCmd extends DebugCmd {
	protected final CmdLine cmdLine;

	protected ShellCmd(String help, NaleDebugger debugger) {
		super(help, debugger);
		cmdLine = ((CmdLineDebugger) this.debugger).shell;
	}

	protected ShellCmd(String[] help, NaleDebugger debugger) {
		super(help, debugger);
		cmdLine = ((CmdLineDebugger) this.debugger).shell;
	}

	public File getLineGenFile(String lineName) {
		lineName = lineName.trim();
		File scm = this.cmdLine.getSchemaFile();

		File par = new File(scm.getParentFile(), scm.getName() + ".lines");
		File f = new File(par, Misc.formatFileName(lineName));
		return f;
	}

	protected void besureFileExsits(File inputFile) throws DebugException {
		if (!inputFile.exists() || inputFile.isDirectory()) {
			throw new DebugException("input file not exists or is a directory");
		}

	}

	public void paraEqualsIgnorecase(String para, String expect)
			throws DebugException {

		if (!para.equalsIgnoreCase(expect)) {
			throw new DebugException("expect '" + expect + "', but get '"
					+ para);
		}
	}

	public File file(String[] paras, int index) throws DebugException {
		this.hasPara(paras, index);
		return file(paras[index]);
	}

	protected File file(String string) throws DebugException {

		File par = null;
		String paraName = string;
		if (paraName.startsWith("@")) {
			par = this.cmdLine.getBaseDir();
			paraName = paraName.substring(1);
		} else {
			throw new DebugException("缺少文件前导符@");
		}

		if (paraName.length() == 0) {
			throw new DebugException("expect file name, but get empty token");
		}
		File ret = new File(par, paraName).getAbsoluteFile();
		return ret;
	}

	protected void hasPara(String[] paras, int index) throws DebugException {
		if (index >= paras.length) {
			throw new DebugException("missing para with index " + index
					+ ", they are" + Arrays.asList(paras));
		}
	}
}
