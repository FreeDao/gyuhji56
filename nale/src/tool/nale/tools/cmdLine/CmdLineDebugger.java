package nale.tools.cmdLine;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;

import nale.script.engine.lang.NaleContext;
import nale.script.engine.lang.debug.DebugCmd;
import nale.script.engine.lang.debug.NaleDebugger;
import nale.tools.cmdLine.cmds.BindAttrCmd;
import nale.tools.cmdLine.cmds.BindCmd;
import nale.tools.cmdLine.cmds.CatCmd;
import nale.tools.cmdLine.cmds.FileVars;
import nale.tools.cmdLine.cmds.ParseAttr;
import nale.tools.cmdLine.cmds.ScanCmd;
import nale.tools.cmdLine.cmds.ScanLineCmd;
import nale.tools.cmdLine.cmds.ScanNumCmd;
import nale.tools.cmdLine.cmds.UseCmd;
import nale.tools.cmdLine.cmds.UseLineCmd;

import com.bmtech.utils.io.LineWriter;

public class CmdLineDebugger extends NaleDebugger {
	CmdLine shell;

	public CmdLineDebugger(CmdLine shell, NaleContext context, File historyDir)
			throws IOException {
		super(context);
		this.shell = shell;

		File saveFile = new File(historyDir, new SimpleDateFormat("yyyy-MM-dd")
				.format(System.currentTimeMillis()));
		LineWriter lw = new LineWriter(saveFile, true, Charset.forName("utf-8"));
		this.setHistoryWirter(lw);
		lw.writeLine("//#new start at "
				+ new SimpleDateFormat().format(System.currentTimeMillis()));
	}

	@Override
	public void putCmd() throws IOException {
		super.putCmd();
		this.putCmd("scan", new ScanCmd(this));
		this.putCmd(new String[] { "scanNo", "scannum" }, new ScanNumCmd(this));
		this.putCmd("use", new UseCmd(this));
		this.putCmd("cat", new CatCmd(this));
		this.putCmd("files", new FileVars(this));
		this.putCmd("useLine", new UseLineCmd(this));
		this.putCmd("bindAttr", new BindAttrCmd(this));
		this.putCmd("bind", new BindCmd(this));
		this.putCmd("scanLine", new ScanLineCmd(this));
		this.putCmd("ParseAttr", new ParseAttr(this));
	}

	@Override
	public void putCmd(String strs, DebugCmd r) {
		super.putCmd(strs, r);
	}
}