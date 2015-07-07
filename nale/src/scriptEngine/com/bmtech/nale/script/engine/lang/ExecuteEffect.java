package com.bmtech.nale.script.engine.lang;

import java.util.ArrayList;
import java.util.List;

public class ExecuteEffect {
	private final List<String> declared;
	private final List<String> refered;
	private final List<String> traced;

	public ExecuteEffect() {
		this(new ArrayList<String>(), new ArrayList<String>(),
				new ArrayList<String>());
	}

	private ExecuteEffect(List<String> declaired, List<String> refered,
			List<String> traced) {
		this.declared = declaired;
		this.refered = refered;
		this.traced = traced;
	}

	public void declared(String varName) {
		this.declared.add(varName);
	}

	public void refered(String varName) {
		this.refered.add(varName);
	}

	public void traced(String varName) {
		this.traced.add(varName);
	}

	public ExecuteEffect copy() {
		List<String> declaired = new ArrayList<String>(this.declared);
		List<String> refered = new ArrayList<String>(this.refered);
		ExecuteEffect eff = new ExecuteEffect(declaired, refered, traced);
		return eff;
	}

	public void clear() {
		this.declared.clear();
		this.refered.clear();
		this.traced.clear();
	}

	public List<String> getRefered() {
		return refered;
	}

	public List<String> getTraced() {
		return this.traced;
	}

	@Override
	public String toString() {
		return "assgin:" + this.declared + ", refer:" + this.refered
				+ ", trace:" + this.traced;
	}

	public List<String> getDeclared() {
		return declared;
	}

}