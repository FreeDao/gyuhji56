package com.bmtech.nale.tools.cmdLine.cmds;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.bmtech.nale.script.engine.lang.debug.NaleDebugger;
import com.bmtech.nale.tools.cmdLine.CmdLine;

public class BindAttrCmd extends AbstractBindCmd {

	public BindAttrCmd(NaleDebugger debugger) {
		super("bindAttr", debugger);
	}

	@Override
	protected File getParDir(String fieldName) {
		File schema = CmdLine.getInstance().getSchemaFile();
		File f = new File(schema.getParentFile(), schema.getName() + ".attr");
		f = new File(f, fieldName);
		f.mkdirs();
		return f;
	}

	@Override
	protected boolean hasName(Map<String, List<String>> schema, String fieldName) {
		for (List<String> list : schema.values()) {
			for (String x : list) {
				if (x.trim().equals(fieldName)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	protected void avaliblaVars() {
		Map<String, List<String>> schema;
		try {
			schema = CmdLine.getInstance().loadSchema(false);
			Set<String> used = new HashSet<String>();
			for (List<String> ll : schema.values()) {
				for (String x : ll) {
					if (used.contains(x)) {
						continue;
					} else {
						used.add(x);
						System.out.println("\t" + x);
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	protected boolean validSubName(Map<String, List<String>> schema,
			String fieldName, String subName) {
		if (subName == null || subName.trim().length() == 0) {
			return false;
		}
		subName = subName.trim();
		return true;
	}
}
