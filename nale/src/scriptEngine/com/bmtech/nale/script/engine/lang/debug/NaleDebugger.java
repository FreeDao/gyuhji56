package com.bmtech.nale.script.engine.lang.debug;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.bmtech.nale.script.engine.jmo.lang.JmoException;
import com.bmtech.nale.script.engine.jmo.lang.source.SourcePosition;
import com.bmtech.nale.script.engine.lang.ExecuteEffect;
import com.bmtech.nale.script.engine.lang.NaleContext;
import com.bmtech.nale.tools.scriptshell.NaleExecutorTool;
import com.bmtech.utils.Consoler;
import com.bmtech.utils.io.LineWriter;

public class NaleDebugger implements Runnable {

	protected final NaleContext context;
	private final Map<String, DebugCmd> cmdMap = new HashMap<String, DebugCmd>();

	private boolean quit = false;
	private LineWriter historyWirter;
	protected final SimpleDateFormat sdf;
	protected List<ScriptRecord> scriptHistory = new ArrayList<ScriptRecord>();

	public NaleDebugger(final NaleContext context) throws IOException {
		this.sdf = new SimpleDateFormat("yyyy-MM-dd.HH:mm:ss.SSS");
		this.context = context;

	}

	protected void putCmd() throws IOException {
		this.putCmd("qd", new DebugCmd("退出debug", this) {
			@Override
			public void run(String paras[], SourcePosition pos) {
				quit = true;
			}
		});

		this.putCmd("lv", new DebugCmd("列出变量", this) {
			@Override
			public void run(String paras[], SourcePosition pos) {
				System.out.println(context.getVarsName());
			}
		});

		this.putCmd("le", new DebugCmd("列出全局量", this) {
			@Override
			public void run(String paras[], SourcePosition pos) {
				System.out.println(context.getExportVars());
			}
		});

		this.putCmd("help", new DebugCmd("帮助，列出可以用的debug命令", this) {
			@Override
			public void run(String paras[], SourcePosition pos) {
				if (paras.length == 0) {
					for (Entry<String, DebugCmd> x : cmdMap.entrySet()) {
						System.out.println();
						String[] tokens = x.getValue().help().split("\n");
						System.out.println("\t" + x.getKey() + ":");
						for (String t : tokens) {
							System.out.println("\t\t" + t);
						}
					}
				} else {
					String cmd = paras[0];
					DebugCmd cmd2 = cmdMap.get(cmd.trim().toLowerCase());
					if (cmd2 != null) {
						System.out.println();
						String[] tokens = cmd2.help().split("\n");
						System.out.println("\t" + cmd + ":");
						for (String t : tokens) {
							System.out.println("\t\t" + t);
						}
					} else {
						System.out.println("没有找到命令:" + cmd);
					}
				}
			}
		});
	}

	protected void putCmd(String strs, DebugCmd r) {
		putCmd(new String[] { strs }, r);
	}

	protected void putCmd(String[] strs, DebugCmd r) {
		for (String str : strs) {
			cmdMap.put(str.trim().toLowerCase(), r);
		}
	}

	protected void putCmd(DebugCmd r, String... strs) {
		putCmd(strs, r);
	}

	@Override
	public void run() {
		int lineNo = 0;
		try {
			this.putCmd();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		while (true) {
			if (quit) {
				break;
			}
			++lineNo;
			String cmdStr = Consoler.readString("\n[debug-" + lineNo + "] ")
					.trim();
			if (cmdStr.length() == 0) {
				continue;
			}
			String[] cmds = cmdStr.split("\n");
			for (String cmd : cmds) {
				cmd = cmd.trim();
				if (this.getHistoryWirter() != null) {
					try {
						String time = sdf.format(System.currentTimeMillis());
						this.getHistoryWirter().writeLine(
								"//\tdebug-" + lineNo + " " + time);
						this.getHistoryWirter().writeLine(cmd);
						this.getHistoryWirter().flush();
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
				try {
					execute(cmd, lineNo);
				} catch (Exception exc) {
					if (exc instanceof JmoException) {
						((JmoException) exc).printErrorMsg();
					} else {
						System.out.println(exc.getLocalizedMessage());
						exc.printStackTrace();
					}
				}
			}
		}
		dieMessage();

	}

	protected void dieMessage() {
		System.out.println("quit debug");

	}

	private void execute(String cmd, int line) throws Exception {
		int pos = cmd.indexOf(" ");
		int posTab = cmd.indexOf("\t");
		if (posTab != -1) {
			if (pos == -1) {
				pos = posTab;
			} else {
				if (pos > posTab) {
					pos = posTab;
				}
			}
		}
		String prvCmd = cmd;
		String paras[] = new String[0];
		if (pos != -1) {
			prvCmd = cmd.substring(0, pos);
			paras = cmd.substring(pos).trim().replace('\t', ' ').replaceAll(
					" +", " ").split(" ");
		}
		DebugCmd r = cmdMap.get(prvCmd.toLowerCase().trim());
		SourcePosition position = new SourcePosition("debug", line, -1);
		if (r == null) {
			executeScript(line, cmd);
		} else {
			r.run(paras, position);
		}
	}

	private void executeScript(int lineNo, String script) throws Exception {
		ScriptRecord rec;
		boolean okExe = false;
		ExecuteEffect eff = null;
		try {
			Iterator<String> itr = Arrays.asList(script.split("\n")).iterator();

			NaleContext ctx = NaleExecutorTool.getContext(this.context,
					"debug-" + lineNo, itr);
			context.clearExecuteEffect();
			context.execute(false, ctx);
			eff = context.copyExecuteEffect();
			okExe = true;

		} finally {
			rec = new ScriptRecord(script, okExe);
			if (okExe) {
				rec.setExecuteEffect(eff);
			}
			addToScriptHistory(rec);
		}
	}

	private void addToScriptHistory(ScriptRecord rec) {

		scriptHistory.add(rec);

	}

	public void setHistoryWirter(LineWriter historyWirter) {
		this.historyWirter = historyWirter;
	}

	public LineWriter getHistoryWirter() {
		return historyWirter;
	}

	public NaleContext getContext() {
		return this.context;
	}

	private int lookBackToFindDefine(String varName, int index) {

		while (index >= 0) {
			ScriptRecord sr = scriptHistory.get(index);
			if (sr.isOkExe()) {

				if (sr.isDeclare(varName)) {
					return index;
				}
			}
			index--;
		}
		return -1;
	}

	public String findDefine(String varName) throws Exception {
		Set<Integer> lineNos = new HashSet<Integer>();
		lookupEffect(varName, this.scriptHistory.size(), lineNos);
		StringBuilder sb = new StringBuilder();
		sb.append("// export @" + varName);
		sb.append('\n');
		int hisSize = this.scriptHistory.size();
		for (int x = 0; x < hisSize; x++) {
			ScriptRecord sr = this.scriptHistory.get(x);
			if (lineNos.contains(x)) {
				if (sr.isOkExe()) {
					sb.append(sr.script);
				} else {
					throw new Exception("not ok executed line " + x
							+ " in history, record is" + sr);
				}
			} else {
				sb.append("// ");
				sb.append(sr.script);
			}
			sb.append('\n');
		}
		// XXX maybe we should add a operation????
		return sb.toString();
	}

	private void lookupEffect(String varName, int fromIndex,
			Set<Integer> lineNos) throws Exception {
		if (context.isLibVar(varName)) {
			return;
		}
		final int defineLineNo = lookBackToFindDefine(varName, fromIndex - 1);
		if (defineLineNo == -1) {
			throw new Exception("can not find define for var " + varName);
		}
		lineNos.add(defineLineNo);
		// look forward to find trace
		lookForwardToFindTrace(defineLineNo + 1, varName, lineNos);

		// look backward to find refered
		Set<String> refered = scriptHistory.get(defineLineNo).getRefer();
		for (String x : refered) {
			lookupEffect(x, defineLineNo, lineNos);
		}
		// return defineLineNo;
	}

	private void lookForwardToFindTrace(int from, String varName,
			Set<Integer> lineNos) throws Exception {
		int end = scriptHistory.size();
		for (int index = from; index < end; index++) {
			ScriptRecord sr = scriptHistory.get(index);
			if (sr.isOkExe()) {
				if (sr.isDeclare(varName)) {
					return;
				} else {
					String var = sr.traced(varName);
					if (var != null) {
						lineNos.add(index);
						lookupEffect(var, index, lineNos);
					}
				}
			}
		}
	}

}
