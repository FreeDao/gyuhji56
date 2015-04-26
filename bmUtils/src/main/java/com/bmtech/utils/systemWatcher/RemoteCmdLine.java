package com.bmtech.utils.systemWatcher;

import java.io.File;
import java.io.IOException;

import com.bmtech.utils.Consoler;
import com.bmtech.utils.io.ConfigReader;
import com.bmtech.utils.systemWatcher.SystemWatcher.WatcherVo;

public class RemoteCmdLine {
	/**
	 * @param args
	 *            [0] config_path<br>
	 * @param args
	 *            [1] cmd_value<br>
	 *            if args[1] not set, check config, if still no cmd got, ask
	 *            user enter cmd
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			File conf;
			String section;
			if (args.length == 1) {
				section = args[0];
				conf = SystemWatcher.sysWatcherConfigFile;
			} else if (args.length == 2) {
				section = args[0];
				conf = new File(args[1]);
			} else {
				throw new IOException(
						"param fail! expect [configFile] [section]");
			}

			if (!conf.exists()) {
				throw new IOException("not found config %s" + conf);
			}

			CMD(conf, section);
		} catch (Exception e) {
			System.out.println("ERROR:" + e);
		}
	}

	static void CMD(File conf, String section) throws IOException {
		String addr = null;
		String cmd = null;

		if (!conf.exists()) {
			throw new IOException("not found config %s" + conf);
		}
		WatcherVo vo = SystemWatcher.getWatcherConfig(conf);

		ConfigReader cr = new ConfigReader(conf, section);

		addr = cr.getValue("addr");

		if (addr == null) {
			addr = Consoler.readString("remote host(no input as 127.0.0.1):");
			if (addr.length() == 0) {
				addr = "127.0.0.1";
			}
		}

		cmd = cr.getValue("cmd");
		if (cmd == null) {
			cmd = Consoler.readString("cmd(^ as CR):");
		}
		cmd = cmd.trim();
		if (cmd.length() == 0) {
			System.out.println("no CMD to execute!");
			return;
		}
		cmd = cmd.replace("^", "\n");
		System.out.println("connecting " + addr + ":" + vo.port);
		RemoteWatchClient clt = new RemoteWatchClient(addr, vo.port, vo.encKey);
		System.out.println("connected!");
		String ret = clt.writeCommand(cmd);
		System.out.println("\n------got reply-----------\n'" + ret + "'");
	}
}