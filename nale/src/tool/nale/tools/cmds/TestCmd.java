package nale.tools.cmds;

import nale.script.engine.jmo.lang.JmoException;

public class TestCmd extends NaleShellCmd {

	@Override
	public void run(String[] paras) {
		try {
			this.env.test();
		} catch (Exception exc) {
			if (exc instanceof JmoException) {
				System.out.println("Error: " + exc.getMessage());
			} else {
				exc.printStackTrace();
			}

		}

	}

	@Override
	public String usage() {
		return ("测试文件 " + this.env.getScriptName());

	}

}
