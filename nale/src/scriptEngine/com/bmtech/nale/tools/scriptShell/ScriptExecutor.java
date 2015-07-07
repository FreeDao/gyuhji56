package com.bmtech.nale.tools.scriptShell;

import java.io.IOException;
import java.util.Iterator;

import com.bmtech.nale.script.engine.lang.NaleContext;
import com.bmtech.nale.script.engine.lang.debug.NaleDebugger;

public abstract class ScriptExecutor {

	public final String scriptName;

	public ScriptExecutor(String scriptName) {
		this.scriptName = scriptName;
	}

	public void execute() throws Exception {
		String input = getInput();
		Iterator<String> lineIterator = getScriptIterator();
		NaleContext ctx = NaleExecutorTool.getContext(scriptName, lineIterator);
		NaleExecutorTool.execute(input, ctx, new NaleDebugger(ctx));
	}

	protected abstract String getInput() throws IOException;

	protected abstract Iterator<String> getScriptIterator() throws Exception;

	public abstract boolean save(String name);

	public abstract boolean needSave();
}
