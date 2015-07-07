package com.bmtech.nale.script.engine.lang.ast.itf;

import com.bmtech.nale.script.engine.lang.NaleContext;
import com.bmtech.nale.script.engine.lang.NaleException;
import com.bmtech.nale.script.engine.lang.obj.NaleObject;

public interface NaleEvalable {

	public NaleObject eval(NaleContext context) throws NaleException;
}
