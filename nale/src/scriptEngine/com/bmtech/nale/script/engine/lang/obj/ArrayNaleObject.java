package com.bmtech.nale.script.engine.lang.obj;

import java.util.ArrayList;
import java.util.List;

import com.bmtech.nale.script.engine.jmo.lang.source.SourcePosition;
import com.bmtech.nale.script.engine.lang.NaleContext;
import com.bmtech.nale.script.engine.lang.NaleException;
import com.bmtech.nale.script.engine.lang.ast.VarNode;
import com.bmtech.nale.script.engine.lang.ast.itf.ReferNode;

import textmatchers.pattern.def.QMatchable;

public class ArrayNaleObject extends NaleObject {
	private SourcePosition pos;
	private final List<NaleObject> list;

	ArrayNaleObject(List<?> list, SourcePosition pos) {
		super(pos);
		this.list = new ArrayList<NaleObject>(list.size());

		for (Object x : list) {
			if (x instanceof String) {
				this.list.add(new StringNaleObject((String) x, pos));
			} else if (x instanceof NaleObject) {
				this.list.add((NaleObject) x);
			} else {
				this.list.add(new StringNaleObject(x.toString(), pos));
			}
		}

	}

	private boolean validIndex(int index) {
		if (index > 0 && index <= this.list.size()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public NaleObject callMember(String funcName, SourcePosition pos)
			throws NaleException {
		Integer index = null;

		index = number(funcName);

		if (index != null) {

			if (validIndex(index)) {
				return list.get(index - 1);
			} else {
				return new StringNaleObject("", this.pos);
			}
		} else {
			if (funcName.equals("size")) {
				return new NumberNaleObject(this.list.size(), pos);
			}
		}

		NaleObject obj = tryDelete(funcName);
		if (obj != null) {
			return obj;
		}
		return super.callMember(funcName, pos);
	}

	private BooleanNaleObject tryDelete(String funcName) {
		if (funcName.startsWith("d") || funcName.startsWith("s")) {
			Integer itg = number(funcName);
			if (itg != null) {
				itg--;
				if (itg < this.list.size()) {
					list.remove(itg);
					return new BooleanNaleObject(true, pos);
				} else {
					return new BooleanNaleObject(false, pos);
				}
			}
		}
		return null;
	}

	@Override
	public QMatchable toQMatchable() throws NaleException {
		throw new NaleException("array can not to pattern", pos);
	}

	@Override
	public String toString() {
		if (list.size() == 0) {
			return "";
		}
		return list.toString();
	}

	@Override
	public NaleObject call(VarNode var, String funcName, List<ReferNode> paras,
			List<NaleObject> parasValue, SourcePosition pos, NaleContext ctx) {
		NaleObject obj = tryDelete(funcName);
		if (obj != null) {
			return obj;
		}
		return super.call(var, funcName, paras, parasValue, pos, ctx);
	}

	public int size() {
		return this.list.size();
	}

	@Override
	public boolean booleanValue() {
		return size() > 0;
	}

	public List<QMatchable> toList() {
		List<QMatchable> list = new ArrayList<QMatchable>();
		for (NaleObject obj : this.list) {
			list.add(obj.toQMatchable());
		}
		return list;
	}

	public NaleObject[] toArray() {
		return this.list.toArray(new NaleObject[this.list.size()]);

	}

	public void add(NaleObject obj) {
		this.list.add(obj);
	}

}
