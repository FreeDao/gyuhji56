package com.bmtech.nale.tools.cmdLine.cmds;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.bmtech.nale.script.engine.jmo.lang.source.SourcePosition;
import com.bmtech.nale.script.engine.lang.debug.DebugCmd;
import com.bmtech.nale.script.engine.lang.debug.NaleDebugger;
import com.bmtech.nale.script.engine.lang.obj.NaleObject;
import com.bmtech.nale.tools.cmdLine.CmdLine;
import com.bmtech.utils.Charsets;
import com.bmtech.utils.Misc;
import com.bmtech.utils.io.LineWriter;

public abstract class AbstractBindCmd extends DebugCmd {
	final String cmdName;

	public AbstractBindCmd(String cmdName, NaleDebugger debugger) {
		super(new String[] { "字段、规则绑定命令，用于将规则与字段（fieldName）关联起来。",
				cmdName + " --列出所有字段",
				cmdName + " fieldName --列出field的 catch group",
				cmdName + " fieldName varName varGroup --add a field group",
				" 例如： bind 审判人.审判员 spy 0\n", }, debugger);

		this.cmdName = cmdName;
	}

	@Override
	public void run(String[] paras, SourcePosition pos) throws Exception {
		Map<String, List<String>> schema = CmdLine.getInstance().loadSchema(
				false);

		String fieldName = null;
		String subName = null;
		if (paras.length > 0) {
			fieldName = paras[0];
			int dotPos = fieldName.indexOf(".");
			if (dotPos != -1) {
				subName = fieldName.substring(dotPos + 1);
				fieldName = fieldName.substring(0, dotPos);

			}
		}
		boolean hasVarName = hasName(schema, fieldName);
		boolean validSubName = validSubName(schema, fieldName, subName);
		if (!validSubName) {
			System.out.println("\t请指定 属性值 ");
			return;
		}
		switch (paras.length) {
		case 0:
			avaliblaVars();
			break;
		case 1:

			if (!hasVarName) {
				System.out.println("没有找到字段:'" + fieldName + "'");
				System.out.println(cmdName + " 命令查看下可用的字段");
				break;
			} else {
				System.out.println("暂不支持查看 " + fieldName + " 所有绑定值");
				// File dir = filedDir(fieldName);
				// File[] files = dir.listFiles();
				// System.out.println(fieldName + "'s catch group:");
				// for (File f : files) {
				// String str = FileGet.getStr(f, Charset.forName("utf-8"));
				// int group = group(f.getName());
				// System.out.println("\tgroup " + group + " in "
				// + f.getName());
				// String[] lines = str.split("\n");
				// for (String line : lines) {
				// System.out.println("\t\t" + line);
				// }
				//
				// }
			}
			break;
		case 3:

			if (!hasVarName) {
				System.out.println("没有找到字段:'" + fieldName + "'");
				System.out.println(cmdName + " 命令查看下可用的字段");
				break;
			}
			Integer group = intValue(paras[2]);
			if (group == null) {
				System.out.println("Error:最后一个参数必须是阿拉伯数字， 但是输入的不是数字'"
						+ paras[2] + "'");
				break;
			}
			String varName = paras[1];
			NaleObject var = super.debugger.getContext().lookupVar(varName,
					new SourcePosition("cmdLine's shell", 0, 0));
			if (var == null) {
				System.out.println("Error:变量'" + varName + "'不存在");
				break;
			}
			String def = super.debugger.findDefine(varName);
			System.out.println("def:\n" + def);
			String fileName = paras[0] + "-->" + varName + "." + group;
			fileName = Misc.formatFileName(fileName);
			File toSave = new File(getParDir(fieldName), fileName);

			if (toSave.exists()) {
				System.out.println("已经存在 " + fileName + ", 如果需要修改请先手动删除文件 "
						+ toSave.getAbsoluteFile().getCanonicalPath());
				break;
			}
			LineWriter lw = new LineWriter(toSave, false, Charsets.UTF8_CS);
			lw.writeLine(def);
			lw.close();

			break;
		default:
			throw new RuntimeException("Error bind cmd, paras are "
					+ Arrays.asList(paras));
		}
	}

	protected abstract boolean validSubName(Map<String, List<String>> schema,
			String fieldName, String subName);

	protected abstract void avaliblaVars();

	protected abstract File getParDir(String fieldName);

	protected abstract boolean hasName(Map<String, List<String>> schema,
			String fieldName);

}
