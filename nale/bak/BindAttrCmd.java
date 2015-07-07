package nale.tools.cmdLine.cmds;

import java.io.File;
import java.util.List;
import java.util.Map;

import nale.script.engine.lang.debug.NaleDebugger;
import nale.tools.cmdLine.CmdLine;

public class BindAttrCmd extends AbstractBindCmd {

	public BindAttrCmd(String cmdName, NaleDebugger debugger) {
		super("bindAttr", debugger);
	}

	@Override
	protected File getParDir() {
		File schema = CmdLine.getInstance().getSchemaFile();
		File f = new File(schema.getParentFile(), schema.getName() + ".attr");
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

}
