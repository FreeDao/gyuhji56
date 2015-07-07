package com.bmtech.nale.script.engine.lang;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.bmtech.nale.script.engine.jmo.lang.source.SourcePosition;
import com.bmtech.nale.script.engine.lang.ast.itf.NaleNode;
import com.bmtech.nale.script.engine.lang.debug.NaleDebugger;
import com.bmtech.nale.script.engine.lang.obj.NaleObject;
import com.bmtech.nale.script.engine.lang.obj.StringNaleObject;
import com.bmtech.utils.log.LogHelper;

public class NaleContext {

	private NaleContext par;
	private final Map<String, NaleObject> vars = new HashMap<String, NaleObject>();
	private final Map<String, NaleObject> exps = new HashMap<String, NaleObject>();
	private final List<NaleNode> nodes;
	final String sourceName;
	final ExecuteEffect effected = new ExecuteEffect();
	private NaleDebugger debugger;

	NaleContext(String sourceName, List<NaleNode> nodes) {
		List<NaleNode> nodesn = new ArrayList<NaleNode>(nodes.size());
		for (NaleNode n : nodes) {
			nodesn.add((NaleNode) n.copyof());
		}
		this.nodes = Collections.unmodifiableList(nodesn);
		this.sourceName = sourceName;
	}

	// public NaleObject getVar(VarNode var) throws NaleException {
	// return var.eval(this);
	// }

	public NaleDebugger getDebugger() {
		return this.debugger;
	}

	public ExecuteEffect copyExecuteEffect() {
		ExecuteEffect ret = this.effected.copy();
		return ret;
	}

	public void clearExecuteEffect() {
		this.effected.clear();

	}

	public NaleObject getVar(String name, SourcePosition position)
			throws NaleException {
		this.effected.refered(name);
		return lookupVar(name, position);
	}

	/**
	 * look up var without being traced
	 * 
	 * @param name
	 * @param position
	 * @return
	 * @throws NaleException
	 */
	public NaleObject lookupVar(String name, SourcePosition position)
			throws NaleException {

		NaleObject ret = this.vars.get(name);
		if (ret == null) {
			if (par != null) {
				ret = par.lookupVarInExp(name);
			}
			if (ret == null) {
				throw new NaleException("undefined var '" + name + "'",
						position);
			}
		}
		return ret;
	}

	private NaleObject lookupVarInExp(String name) {
		return this.exps.get(name);
	}

	public void export(String varName, NaleObject obj) {
		this.exps.put(varName, obj);
	}

	public NaleObject putVar(String varName, NaleObject varValue,
			SourcePosition position) {
		this.effected.declared(varName);
		if (isprotected(varName)) {
			throw new NaleException("cannot override var '" + varName + "'",
					position);
		}
		NaleObject ret = this.vars.put(varName, varValue);
		if (ret != null) {
			LogHelper.log.debug("switch value for $%s, from %s to %s", varName,
					ret, varValue);
		}
		return varValue;
	}

	private boolean isprotected(String varName) {
		if (this.exps.containsKey(varName)) {
			return true;
		} else {
			if (par == null) {
				return false;
			} else {
				return par.isprotected(varName);
			}
		}
	}

	public boolean isLibVar(String name) {
		if (par != null) {
			return par.lookupVarInExp(name) != null;
		}
		return false;
	}

	public void execute() throws NaleException {
		execute(true, this);
	}

	public NaleObject execute(boolean exePar, NaleContext ctx)
			throws NaleException {
		List<NaleNode> nodes = ctx.getNodes();
		if (par != null && exePar) {
			par.execute();
		}
		NaleObject obj = null;
		for (int x = 0; x < nodes.size(); x++) {
			NaleNode nn = nodes.get(x);
			obj = nn.eval(this);
		}
		return obj;
	}

	protected NaleContext getPar() {
		return par;
	}

	public void setPar(NaleContext par) {
		this.par = par;
	}

	public void setGlobalEnv(String string, StringNaleObject stringNaleObject) {
		if (par != null) {
			par.setGlobalEnv(string, stringNaleObject);
		} else {
			this.putVar(string, stringNaleObject, stringNaleObject.getPos());
			export(string, stringNaleObject);
		}
	}

	@Override
	public String toString() {
		return "env @ " + this.sourceName;
	}

	public void setDebugger(NaleDebugger debugger) {
		this.debugger = debugger;
	}

	public int nodeSize() {
		return this.nodes.size();
	}

	public void addNodes(NaleContext ctx) {
		this.nodes.addAll(ctx.nodes);

	}

	public Set<String> getVarsName() {
		return this.vars.keySet();
	}

	public String getExportVars() {
		StringBuilder sb = new StringBuilder();
		this.getExportVars(sb);
		return sb.toString();
	}

	protected void getExportVars(StringBuilder sb) {
		if (par != null) {
			par.getExportVars(sb);
		}
		if (sb.length() > 0) {
			sb.append("\n");
		}
		sb.append("\n//in " + this.sourceName);
		for (Entry<String, NaleObject> xx : this.exps.entrySet()) {
			sb.append("\n$");
			sb.append(xx.getKey() + " = " + xx.getValue());
		}

	}

	protected List<NaleNode> getNodes() {
		return nodes;
	}

	public boolean hasVar(String varName, SourcePosition pos) {
		try {
			this.lookupVar(varName, pos);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public void trace(String varName) {
		this.effected.traced(varName);
	}
}
