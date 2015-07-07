package com.bmtech.nale.script.engine.lang.debug;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.bmtech.nale.script.engine.lang.ExecuteEffect;

public class ScriptRecord {
	private final boolean okExe;
	protected final String script;
	protected final long time;
	// declared
	private String declare;
	// called
	private Set<String> refer;
	// not effected
	private String trace;

	public ScriptRecord(String script, boolean okExe) {
		this.script = script;
		this.okExe = okExe;
		this.time = System.currentTimeMillis();
	}

	private String checkAndGetOnlyOne(List<String> lst) {
		String ret = null;
		if (lst.size() > 1) {
			throw new RuntimeException("too much delared/traced variables:"
					+ lst);
		} else if (lst.size() == 1) {

			ret = lst.get(0);
		}
		return ret;
	}

	public void setExecuteEffect(ExecuteEffect exe) {
		this.declare = this.checkAndGetOnlyOne(exe.getDeclared());
		this.trace = this.checkAndGetOnlyOne(exe.getTraced());
		this.refer = new HashSet<String>(exe.getRefered());

	}

	public boolean isDeclare(String varName) {
		return this.declare != null && this.declare.equals(varName);
	}

	public void setRefer(Set<String> refer) {
		this.refer = refer;
	}

	public Set<String> getRefer() {
		return refer;
	}

	public boolean isTrace(String varName) {
		return traced(varName) != null;
	}

	public boolean isOkExe() {
		return okExe;
	}

	@Override
	public String toString() {
		return script + "\t#"
				+ (this.declare == null ? "" : ", declare: " + this.declare)
				+ (this.trace == null ? "" : ", trace:" + trace) + ", refer:"
				+ this.refer;
	}

	public String traced(String varName) {

		if (this.trace != null) {
			if (this.refer.size() == 2) {
				if (this.refer.contains(varName)) {
					if (varName.equals(this.trace)) {
						return null;
					}
					return this.trace;
				}
			}
		}
		return null;
	}

}