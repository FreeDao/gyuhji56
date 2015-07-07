package nale.tools.cmdLine.cmds;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;

import nale.script.engine.lang.debug.NaleDebugger;
import nale.tools.cmdLine.ShellCmd;

import com.funshion.gamma.jmo.lang.source.SourcePosition;

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
