package nale.tools.cmdLine.cmds;

import java.io.File;

import nale.script.engine.lang.debug.NaleDebugger;
import nale.tools.cmdLine.CmdLine;
import nale.tools.cmdLine.ShellCmd;

import com.funshion.gamma.jmo.lang.source.SourcePosition;

public class ParseAttr extends ShellCmd {

	public ParseAttr(NaleDebugger debugger) {
		super("ParseAttr", debugger);
	}

	@Override
	public void run(String[] paras, SourcePosition pos) throws Exception {
		if (paras.length < 1) {
			System.out.println("请指定段行名， 如 原告诉称");
			return;
		}
		File useFile = this.cmdLine.getInputFile();
		File schema = CmdLine.getInstance().getSchemaFile();
		File fAttr = new File(schema.getParentFile(), schema.getName()
				+ ".attr");

		String str = paras[0];
		fAttr = new File(fAttr, str);

		SentParser.parse(useFile, fAttr.listFiles());
	}
}
