package com.bmtech.nale.tools.cmdLine.cmds;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.bmtech.nale.script.engine.jmo.lang.source.SourcePosition;
import com.bmtech.nale.script.engine.lang.debug.NaleDebugger;
import com.bmtech.nale.tools.cmdLine.CmdLine;
import com.bmtech.nale.tools.cmdLine.ShellCmd;

public class UseLineCmd extends ShellCmd {

	public UseLineCmd(NaleDebugger debugger) {
		super("使用句行的扫描结果文件", debugger);
	}

	@Override
	public void run(String[] paras, SourcePosition pos) throws Exception {

		if (1 != paras.length) {
			System.out.println("没有指定句行匹配模式名称（lineXXXXX）");
			return;
		}
		String lineName = paras[0].trim();
		Map<String, List<String>> map = CmdLine.getInstance().loadSchema(false);
		if (!map.containsKey(lineName)) {
			System.out.println("不存在的句行:" + lineName);
			return;
		}

		File lineGenFile = this.getLineGenFile(lineName);

		if (!lineGenFile.exists() || lineGenFile.isDirectory()) {
			System.out.println("文件不存在或者是文件夹：" + lineGenFile);
			System.out.println("请使用scanLine命令生成对应文件");
			return;
		}

		cmdLine.setInputFile(lineGenFile);
	}

}
