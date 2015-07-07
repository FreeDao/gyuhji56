package com.bmtech.nale.tools.cmdLine.cmds;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;

import com.bmtech.nale.script.engine.jmo.lang.source.SourcePosition;
import com.bmtech.nale.script.engine.lang.debug.NaleDebugger;
import com.bmtech.nale.tools.cmdLine.ShellCmd;

public class FileVars extends ShellCmd {

	public FileVars(NaleDebugger debugger) {
		super("列出文件变量", debugger);
	}

	@Override
	public void run(String[] paras, SourcePosition pos) throws Exception {

		expectParasNumber(paras, 0);

		File files[] = cmdLine.getBaseDir().listFiles();
		Arrays.sort(files, new Comparator<File>() {

			@Override
			public int compare(File o1, File o2) {
				return (int) (o1.lastModified() - o2.lastModified());
			}

		});
		SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm:ss");
		for (File f : files) {
			System.out.println(df.format(f.lastModified()) + "\t "
					+ f.getName() + "\t" + f.length());
		}
	}

}
