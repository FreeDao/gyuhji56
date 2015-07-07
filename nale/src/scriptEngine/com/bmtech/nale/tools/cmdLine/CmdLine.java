package com.bmtech.nale.tools.cmdLine;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.bmtech.nale.script.engine.lang.NaleContext;
import com.bmtech.nale.tools.scriptShell.NaleExecutorTool;
import com.bmtech.nale.tools.scriptShell.NaleShell;
import com.bmtech.utils.io.ConfigReader;
import com.bmtech.utils.io.LineReader;
import com.bmtech.utils.io.TchFileTool;
import com.bmtech.utils.log.LogHelper;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CmdLine {
	private static final CmdLine instance;
	static {
		try {
			instance = new CmdLine();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public static CmdLine getInstance() {
		return instance;
	}

	private File inputFile;
	public final String scriptName;
	protected NaleContext context;
	private File baseDir;
	private final File historyDir;
	private final String inputFileSaveToken = "lastInputFile";
	private final LogHelper log = new LogHelper("cmdLine");
	private final File schemaDir;
	private final File schemaFile;

	private CmdLine() throws Exception {
		this.scriptName = "cmdLine";
		ConfigReader cr = new ConfigReader(NaleShell.cfgFile, "cmdLine");
		String historyFile = cr.getValue("historySaveTo");
		this.historyDir = new File(historyFile);
		this.historyDir.mkdirs();
		baseDir = new File(cr.getValue("baseDir"));

		if (!baseDir.exists()) {
			baseDir.mkdirs();
		}
		File inputDefault = getDefaultInputFile();
		if (inputDefault != null) {
			this.setInputFile(inputDefault);
		}

		String v = cr.getValue("schema");
		schemaFile = new File(v);
		log.info("use schema file %s, len %s", schemaFile, schemaFile.length());
		loadSchema(true);

		schemaDir = new File(schemaFile.getParent(), schemaFile.getName()
				+ ".def");
		if (!getSchemaDir().exists()) {
			getSchemaDir().mkdirs();
		}

	}

	@SuppressWarnings("unchecked")
	public Map<String, List<String>> loadSchema(boolean print) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, List<String>> docValues = objectMapper.readValue(
				schemaFile, Map.class);
		StringBuilder sb = new StringBuilder();
		Set<Entry<String, List<String>>> set = docValues.entrySet();

		for (Entry<String, List<String>> e : set) {
			List<String> list = e.getValue();
			sb.append("\n\t");
			sb.append(e.getKey());
			sb.append(" : ");
			for (int x = 0; x < list.size(); x++) {
				String str = list.get(x);
				if (str.trim().length() == 0) {
					list.remove(x);
					x--;
				}
			}
			sb.append(list);
		}
		if (print) {
			log.info("schema as %s", sb);
		}
		return docValues;
	}

	public File getDefaultInputFile() throws IOException {
		String str = TchFileTool.get(this.historyDir, inputFileSaveToken);

		File ret;
		if (str == null) {
			ret = new File(this.historyDir, "defaultInput.tmp");
		} else {
			ret = new File(str);
		}
		if (!ret.exists())
			ret.createNewFile();
		return ret;
	}

	public void setDefaultInputFile(File file) {
		TchFileTool.put(this.historyDir, inputFileSaveToken,
				file.getAbsolutePath());
	}

	public void execute() throws Exception {
		Iterator<String> lineIterator = getScriptIterator();
		context = NaleExecutorTool.getContext(scriptName, lineIterator);

		CmdLineDebugger debugger = new CmdLineDebugger(this, context,
				this.historyDir);

		NaleExecutorTool.execute("", context, debugger);
	}

	protected Iterator<String> getInput() throws IOException {
		return new LineReader(this.inputFile, "utf-8");
	}

	protected Iterator<String> getScriptIterator() throws IOException {
		String[] xx = new String[] { "debug" };
		return Arrays.asList(xx).iterator();
	}

	public boolean save(String name) {
		throw new DebugException("not support");
	}

	public void setInputFile(File inputFile) throws IOException {
		if (!inputFile.exists()) {
			throw new DebugException("can not use file "
					+ inputFile.getAbsoluteFile().getCanonicalPath()
					+ ", maybe not exits");
		}
		this.inputFile = inputFile;
		System.out.println("set input file '" + inputFile + "', size "
				+ this.inputFile.length());
		this.setDefaultInputFile(this.inputFile);
	}

	public File getInputFile() {
		return inputFile;
	}

	public static void main(String[] args) throws Exception, IOException {

		instance.execute();
	}

	public void setBaseDir(File baseDir) {
		this.baseDir = baseDir;
	}

	public File getBaseDir() {
		return baseDir;
	}

	public File getSchemaDir() {
		return schemaDir;
	}

	public File getSchemaFile() {
		return schemaFile;
	}
}
