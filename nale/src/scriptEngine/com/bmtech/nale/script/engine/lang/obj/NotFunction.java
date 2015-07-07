package com.bmtech.nale.script.engine.lang.obj;

import java.util.List;

import com.bmtech.nale.script.engine.jmo.lang.source.SourcePosition;
import com.bmtech.nale.script.engine.lang.NaleContext;
import com.bmtech.nale.script.engine.lang.NaleException;
import com.bmtech.nale.script.engine.lang.ast.VarNode;
import com.bmtech.nale.script.engine.lang.ast.itf.ReferNode;

public class NotFunction extends NaleFunc {

	public NotFunction(NaleObject obj, String name) {
		super(name, obj);
	}

	@Override
	public NaleObject call(VarNode var, List<ReferNode> parasNames,
			List<NaleObject> parasValue, SourcePosition pos, NaleContext ctx)
			throws NaleException {
		if (parasValue.size() != 1) {
			throw new NaleException(
					"expect 1 parameter, but get " + parasValue, pos);
		}
		NaleObject notFunc = parasValue.get(0);
		obj.getNotFilter().add(notFunc);
		ctx.trace(parasNames.get(0).getVarName());
		return obj;
	}

	@Override
	public String toString() {
		return "!" + this.getName() + "()";
	}

}
