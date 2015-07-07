package nale.tools.cmdLine.cmds;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import nale.script.engine.lang.debug.NaleDebugger;
import nale.tools.cmdLine.DebugException;
import nale.tools.cmdLine.ShellCmd;

import com.bmtech.utils.io.LineReader;
import com.funshion.gamma.jmo.lang.source.SourcePosition;

public class CatCmd extends ShellCmd {

	public CatCmd(NaleDebugger debugger) {
		super("打印inputFile， 如 [debug-1] cat [from] [len]", debugger);
	}

	@Override
	public void run(String[] paras, SourcePosition pos) throws DebugException,
			IOException {

		File file = cmdLine.getInputFile();
		if (!file.exists()) {
			System.out.println("input file is not exists");
			return;
		}
		int offset = 0;
		if (paras.length > 0) {
			if (paras[0].startsWith("@")) {
				file = file(paras[0]);
				offset = 1;
			}
		}
		Integer from = 0;
		Integer len = 100;
		if (paras.length == 1 + offset) {
			len = intValue(paras, 0 + offset);
		}
		if (paras.length == 2 + offset) {
			from = intValue(paras, 0 + offset);
			len = intValue(paras, 1 + offset);
		}
		if (from == null || len == null) {
			throw new DebugException("parameter error, see help please!");
		}
		this.besureFileExsits(file);
		LineReader lr = new LineReader(file, Charset.forName("utf-8"));
		int cnt = 0;
		while (lr.hasNext()) {
			String linex = lr.next();
			cnt++;
			if (cnt < from) {
				continue;
			}
			len--;
			if (len >= 0) {
				System.out.println(linex);
			} else {
				break;
			}
		}
		lr.close();
	}

}
