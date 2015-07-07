package nale.tools.scriptShell;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

import nale.script.engine.jmo.lang.JmoException;
import nale.script.engine.lang.LexerTree;
import nale.script.engine.lang.NaleContext;
import nale.script.engine.lang.NaleParser;
import nale.script.engine.lang.debug.NaleDebugger;
import nale.script.engine.lang.obj.NaleObject;
import nale.script.engine.lang.obj.StringNaleObject;

import com.bmtech.utils.io.LineReader;
import com.funshion.gamma.jmo.lang.source.SourcePosition;

public class NaleExecutorTool {
	public static final SourcePosition libPos = new SourcePosition("libs", 0, 0);
	public static final String OUTPUT_NAME = "output";
	public static final String INPUT_NAME = "input";

	public static NaleContext getContext(NaleContext par, String name,
			Iterator<String> lineIterator) throws Exception {

		NaleParser p = new NaleParser(name, lineIterator);
		LexerTree lt = p.parse();
		NaleContext context = lt.newEnv();
		context.setPar(par);
		return context;
	}

	private static NaleContext getSingleFileContext(File file) throws Exception {
		LineReader lr = new LineReader(file, Charset.forName("utf-8"));
		return getContext(null, file.getName(), lr);
	}

	public static NaleContext getContext(String name,
			Iterator<String> lineIterator) throws Exception {
		File f = new File("./config/nale/basic.nale");
		NaleContext par = getSingleFileContext(f);
		NaleContext context = getContext(par, name, lineIterator);
		context.setGlobalEnv("env", new StringNaleObject("env", libPos));

		return context;
	}

	public static void execute(String input, NaleContext context,
			NaleDebugger debugger) throws Exception {
		try {
			context.setGlobalEnv(INPUT_NAME,
					new StringNaleObject(input, libPos));
			context.setGlobalEnv(OUTPUT_NAME, new StringNaleObject(OUTPUT_NAME,
					libPos));
			context.setDebugger(debugger);

			context.execute();
			NaleObject objs = context.getVar(OUTPUT_NAME, libPos);
			Set<Entry<String, NaleObject>> set = objs.listMembers();
			System.out.println("\n--->\noutput members");
			for (Entry<String, NaleObject> e : set) {
				System.out.println("\t" + e.getKey() + " = '" + e.getValue()
						+ "'");
			}
		} catch (JmoException e) {
			System.err.println("\nERROR: " + e.getMessage() + "\n");
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("\nERROR: " + e.getMessage() + "\n");
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws Exception {

		File f = new File("config/nale/example.nale");
		LineReader lr = new LineReader(f, Charset.forName("utf-8"));
		NaleContext ctx = getContext("testor", lr);
		execute("", ctx, new NaleDebugger(ctx));
	}
}
