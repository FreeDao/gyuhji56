package nale.tools.cmds;

import com.bmtech.utils.Consoler;

public class QuitCmd extends NaleShellCmd {

	@Override
	public void run(String[] paras) {
		if (this.env.needSave()) {
			boolean wantSave = Consoler
					.confirm("you want save the script first(y/n)?");
			if (wantSave) {
				NaleShellCmd cmd;
				try {
					cmd = this.env.getCommand("save");
					cmd.run(new String[0]);
				} catch (Exception exc) {
					exc.printStackTrace();
				}

			} else {
				System.exit(0);
			}
		} else {
			System.exit(0);
		}

	}

	@Override
	public String usage() {
		return ("退出程序");

	}

}
