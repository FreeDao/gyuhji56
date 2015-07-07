package com.bmtech.nale.tools.cmds;

import java.util.Map.Entry;

public class HelpCmd extends NaleShellCmd {

	@Override
	public void run(String[] paras) throws Exception {
		if (paras.length == 0) {
			for (Entry<String, NaleShellCmd> e : this.env.getCmds().entrySet()) {
				System.out.println("\n\t" + e.getKey() + ":");
				NaleShellCmd cmd = env.getCommand(e.getKey());
				System.out.println("\t\t" + cmd.usage());

			}
		} else {
			String cmd = paras[0];
			NaleShellCmd cmd2 = this.env.getCmd(cmd);
			if (cmd != null) {
				System.out.println("\n\t" + cmd + ":");
				System.out.println("\t\t" + cmd2.usage());
			} else {
				System.out.println("没有找到命令:" + cmd);
			}
		}

	}

	@Override
	public String usage() {
		return "列出所有命令及使用方法";
	}
}