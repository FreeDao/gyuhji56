package com.bmtech.nale.addon.sem;

import java.util.Arrays;
import java.util.Iterator;

import com.bmtech.nale.addon.SemToken;
import com.bmtech.nale.script.engine.lang.NaleContext;
import com.bmtech.nale.script.engine.lang.obj.NaleObject;
import com.bmtech.nale.tools.scriptShell.NaleExecutorTool;

public abstract class SemRuleExportHelper {

	private final String sourceCode;

	private final String toExportVar;
	private final SemToken attachTo;
	private final NaleObject toExportObj;

	SemRuleExportHelper(String toExportVar, SemToken attachTo, String sourceCode)
			throws Exception {

		this.attachTo = attachTo;

		this.sourceCode = sourceCode;
		String varName = checkVarName(toExportVar);
		this.toExportVar = varName;
		this.toExportObj = this.getExprot();
	}

	// FIXME besure
	protected abstract String checkVarName(String toExportVar2);

	SemRuleExportHelper(String toExportVar, String attachTo, String lines)
			throws Exception {
		this(toExportVar, SemToken.toSemToken(attachTo), lines);
	}

	private NaleObject getExprot() throws Exception {

		Iterator<String> itr = Arrays.asList(sourceCode.split("\n")).iterator();

		NaleContext context = NaleExecutorTool.getContext(toExportVar + "@"
				+ this.getAttachTo().toStringPattern(), itr);
		context.execute();
		NaleObject obj = context.getVar(toExportVar, NaleExecutorTool.libPos);
		return obj;
	}

	public String getToExportVar() {
		return toExportVar;
	}

	public SemToken getAttachTo() {
		return attachTo;
	}

	public NaleObject getToExportObj() {
		return toExportObj;
	}

	public abstract void save() throws Exception;

	public String getSourceCode() {
		return sourceCode;
	}
}
