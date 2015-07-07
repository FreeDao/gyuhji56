package com.bmtech.nale.tools.cmds;

import com.bmtech.nale.tools.scriptshell.NaleShell;

public abstract class NaleShellCmd {

	protected String commandCalledByName;
	protected NaleShell env;

	public NaleShellCmd() {

	}

	public abstract void run(String[] paras) throws Exception;

	public abstract String usage();

	public void setCommandCalledByName(String commandCalledBy) {
		this.commandCalledByName = commandCalledBy;
	}

	public void setEnv(NaleShell env) {
		this.env = env;
	}

}
