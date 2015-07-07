package nale.tools.cmdLine.cmds;

import java.io.File;
import java.io.FileFilter;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nale.script.engine.lang.debug.NaleDebugger;
import nale.script.engine.lang.obj.NaleObject;
import nale.script.engine.lang.obj.StringMatchedInfo;
import nale.tools.cmdLine.CmdLine;
import nale.tools.cmdLine.ShellCmd;
import tv.fun.addon.sem.FileSemRuleExportHelper;

import com.bmtech.utils.Charsets;
import com.bmtech.utils.Consoler;
import com.bmtech.utils.Misc;
import com.bmtech.utils.io.LineReader;
import com.bmtech.utils.io.LineWriter;
import com.funshion.gamma.jmo.lang.source.SourcePosition;

public class ScanLineCmd extends ShellCmd {

	public ScanLineCmd(NaleDebugger debugger) {
		super(new String[] { "扫描句行,将匹配的文件保存起来", "scanLine genAll --生成所有的字段",
				"scanLine lineName --生成lineName的字段" }, debugger);
	}

	@Override
	public void run(String[] paras, SourcePosition pos) throws Exception {
		final int maxMatch = 1000;

		boolean echo = true;

		if (paras.length == 2) {
			echo = false;
		} else {
			if (1 != paras.length) {
				System.out.println("没有指定句行匹配模式名称（lineXXXXX）");
				return;
			}
		}

		String lineCmd = paras[0].trim();
		String names[];
		Map<String, List<String>> map = CmdLine.getInstance().loadSchema(false);

		if (lineCmd.equals("genAll")) {
			echo = false;
			Set<String> set = map.keySet();
			names = set.toArray(new String[set.size()]);
		} else {
			names = new String[] { lineCmd };
		}
		for (String lineName : names) {

			if (!map.containsKey(lineName)) {
				System.out.println("不存在的句行:" + lineName);
				return;
			}

			File lineGenFile = this.getLineGenFile(lineName);

			if (lineGenFile.exists()) {
				boolean override = !echo || Consoler.confirm("文件已经存在，是否删除?");
				if (override) {
					lineGenFile.delete();
					if (lineGenFile.exists()) {
						System.out.println("Error: 不能删除对应文件！");
						return;
					}
				} else {
					return;
				}
			}

			final String prefix = Misc.formatFileName(lineName) + "--";
			File files[] = this.cmdLine.getSchemaDir().listFiles(
					new FileFilter() {
						@Override
						public boolean accept(File pathname) {
							String namex = pathname.getName();
							if (namex.startsWith(prefix)) {
								return true;
							}
							return false;
						}

					});
			System.out.println("找到对应规则 :" + files.length);
			NaleObject objs[] = new NaleObject[files.length];
			for (int x = 0; x < objs.length; x++) {
				File ruleFile = files[x];
				System.out.println("\t" + ruleFile.getName());
				try {
					FileSemRuleExportHelper helper = FileSemRuleExportHelper
							.getFromFile(ruleFile);
					NaleObject obj = helper.getToExportObj();
					objs[x] = obj;
				} catch (Exception e) {
					System.out.println("Error 规则导入失败: "
							+ ruleFile.getCanonicalPath());
					e.printStackTrace();
				}
			}
			File input = this.cmdLine.getInputFile();
			Consoler.println("测试文件: %s", input);
			LineReader lr = new LineReader(input, Charsets.UTF8_CS);

			Consoler.println("输出文件: %s", lineGenFile);
			LineWriter lw = new LineWriter(lineGenFile, false, Charsets.UTF8_CS);
			int matched = 0;
			try {
				while (lr.hasNext()) {
					if (lr.currentLineNumber() % 1000 == 0) {
						Consoler.println("scan %s, now line %s, match %s",
								lineName, lr.currentLineNumber(), matched);
					}
					String line = lr.next();
					boolean match = false;
					for (NaleObject obj : objs) {
						try {
							StringMatchedInfo[] infos = obj.scanMatcher(line);

							if (infos.length > 0) {

								if (echo) {
									Consoler.println(
											"lineNo.%s nowMatched %s\t%s", lr
													.currentLineNumber(),
											matched, line);
								}
								match = true;
								break;
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if (match) {
						lw.writeLine(line);
						matched++;
						if (matched >= maxMatch) {
							break;
						}
					}
				}
			} finally {
				lr.close();
				lw.close();
			}

			Consoler.println("扫描文件 %s行， %s", lr.currentLineNumber(), input);
			Consoler.println("scan %s,  write to %s", matched, lineGenFile);
		}
	}
}
