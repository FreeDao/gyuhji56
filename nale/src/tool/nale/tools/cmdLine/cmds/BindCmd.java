package nale.tools.cmdLine.cmds;

import java.io.File;
import java.util.List;
import java.util.Map;

import nale.script.engine.lang.debug.NaleDebugger;
import nale.tools.cmdLine.CmdLine;

public class BindCmd extends AbstractBindCmd {

	public BindCmd(NaleDebugger debugger) {
		super("bind", debugger);
	}

	@Override
	protected File getParDir(String fieldName) {
		return CmdLine.getInstance().getSchemaDir();
	}

	@Override
	protected boolean hasName(Map<String, List<String>> schema, String fieldName) {
		return schema.containsKey(fieldName);
	}

	@Override
	protected void avaliblaVars() {
		try {
			CmdLine.getInstance().loadSchema(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected boolean validSubName(Map<String, List<String>> schema,
			String fieldName, String subName) {
		return true;
	}

}
