package com.bmtech.nale.script.engine.lang.debug;

import java.util.Arrays;

import com.bmtech.nale.script.engine.jmo.lang.source.SourcePosition;

public abstract class DebugCmd {
	final private String help;
	protected final NaleDebugger debugger;

	protected DebugCmd(String help, NaleDebugger debugger) {
		this.help = help;
		this.debugger = debugger;
	}

	protected DebugCmd(String[] helps, NaleDebugger debugger) {
		StringBuilder sb = new StringBuilder();
		for (String str : helps) {
			sb.append(str);
			sb.append("\n");
		}
		this.help = sb.toString();
		this.debugger = debugger;
	}

	public String help() {
		return help;
	}

	public abstract void run(String paras[], SourcePosition pos)
			throws Exception;

	public void expectParasNumber(String[] paras, int num) {
		if (num != paras.length) {
			throw new RuntimeException("expect " + num + " paras, but get "
					+ Arrays.asList(paras));
		}
	}

	protected static String asHelpInfo(String[] helpLines) {
		StringBuilder sb = new StringBuilder();
		for (String x : helpLines) {
			if (sb.length() > 0) {
				sb.append("\n\t\t");
			}
			sb.append(x);
		}
		return sb.toString();
	}

	public static Integer intValue(String str) {
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
			return null;
		}
	}

	public static Integer intValue(String paras[], int index) {
		return intValue(paras[index]);
	}
}