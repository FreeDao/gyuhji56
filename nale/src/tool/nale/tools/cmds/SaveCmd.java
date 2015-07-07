package nale.tools.cmds;

import java.util.Arrays;

import com.bmtech.utils.Consoler;
import com.bmtech.utils.Misc;

public class SaveCmd extends NaleShellCmd {

	@Override
	public void run(String[] paras) {
		String name = null;
		if (paras.length == 0) {
			name = Consoler.readString("save name:");
		} else if (paras.length == 1) {
			name = paras[0];
		} else {
			System.out.println("参数错误:" + Arrays.toString(paras));
			return;
		}
		String formated = Misc.toFileNameFormat(name, '-').trim();
		if (formated.length() == 0) {
			System.out.println("ERROR: empty file name");
			return;
		}
		boolean ret = this.env.save(formated);

		String prm;
		if (ret) {
			prm = "[save Ok]";

		} else {
			prm = "[save FAIL]";
		}
		System.out.println(prm);
	}

	@Override
	public String usage() {
		return ("保存当前文件，参见 test ");
	}

}
