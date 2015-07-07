package com.bmtech.nale.tools.scriptshell;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bmtech.nale.tools.cmds.HelpCmd;
import com.bmtech.nale.tools.cmds.NaleShellCmd;
import com.bmtech.utils.Consoler;
import com.bmtech.utils.KeyValuePair;
import com.bmtech.utils.io.ConfigReader;

public class NaleShell {
	public static final File cfgFile = new File("./config/nale/shell.conf");
	private final Map<String, NaleShellCmd> cmds = new HashMap<String, NaleShellCmd>();

	final ScriptExecutor exe;

	NaleShell(ScriptExecutor exe) throws Exception {
		this.exe = exe;

		ConfigReader cr = new ConfigReader(cfgFile, "cmd");
		List<KeyValuePair<String, String>> lst = cr.getAllConfig();
		for (KeyValuePair<String, String> pair : lst) {
			String cmd = pair.getKeyString();
			Class<?> cls = Class.forName(pair.value);
			setCommand(cmd, cls);
		}
		setCommand("help", HelpCmd.class);
	}

	public void run() {
		while (true) {
			try {
				String cmd = cmd();
				String[] tokens = cmd.replaceAll("[ \t]+", " ").split(" ");
				String name = tokens[0].trim();

				NaleShellCmd command = getCommand(name);
				if (command == null) {
					System.out.println("不认识的命令: '" + name + "'");
					continue;
				}

				String[] paras = Arrays.copyOfRange(tokens, 1, tokens.length);

				command.run(paras);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public NaleShellCmd getCommand(String name) throws Exception {
		NaleShellCmd c = cmds.get(name.trim().toLowerCase());
		if (c != null) {
			c.setEnv(this);
			return c;
		} else {
			return null;
		}
	}

	public void setCommand(String name, Class<?> cmdClazz) throws Exception {

		cmds.put(name.trim().toLowerCase(), (NaleShellCmd) cmdClazz
				.newInstance());
	}

	private String cmd() {
		while (true) {
			String cmd = Consoler.readString("\n~> ");
			cmd = cmd.trim();
			if (cmd.length() == 0) {
				continue;
			}
			return cmd;
		}
	}

	public static void main(String[] args) throws Exception, IOException {
		String clazzStr = System.getProperty("scriptExecutor");
		if (clazzStr == null) {
			throw new Exception(
					"scriptExecutor not set in, use '-DscriptExecutor=clazzName' in java cmd");
		}
		Class<?> exeClazz = Class.forName(clazzStr);
		ScriptExecutor exe = (ScriptExecutor) exeClazz.newInstance();

		NaleShell shell = new NaleShell(exe);
		shell.run();
	}

	public String getScriptName() {
		return exe.scriptName;
	}

	public void test() throws Exception {
		this.exe.execute();
	}

	public boolean save(String name) {
		return this.exe.save(name);
	}

	public boolean needSave() {
		return exe.needSave();
	}

	public Map<String, NaleShellCmd> getCmds() {
		return cmds;
	}

	public NaleShellCmd getCmd(String str) {
		return this.cmds.get(str.trim().toLowerCase());
	}
}
