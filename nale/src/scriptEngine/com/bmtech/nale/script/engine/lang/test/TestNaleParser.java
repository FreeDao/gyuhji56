package com.bmtech.nale.script.engine.lang.test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import com.bmtech.nale.script.engine.jmo.lang.source.SourcePosition;
import com.bmtech.nale.script.engine.lang.LexerTree;
import com.bmtech.nale.script.engine.lang.LineParser;
import com.bmtech.nale.script.engine.lang.NaleContext;
import com.bmtech.nale.script.engine.lang.NaleException;
import com.bmtech.nale.script.engine.lang.NaleParser;
import com.bmtech.nale.script.engine.lang.ast.itf.NaleNode;
import com.bmtech.nale.script.engine.lang.obj.StringNaleObject;
import com.bmtech.utils.io.LineReader;

public class TestNaleParser {

	public static void test(String line) throws NaleException {
		LineParser lp = new LineParser("testor.nale", 1, line);
		NaleNode node = lp.parse();
		System.out.println(node);
	}

	public static NaleContext getContext(File file) throws NaleException {
		return getContext(null, file);
	}

	public static NaleContext getContext(NaleContext par, File file)
			throws NaleException {
		LineReader lr;
		try {
			lr = new LineReader(file, Charset.forName("utf-8"));
		} catch (IOException exc) {
			throw new NaleException("can not read file " + file + ", exc = "
					+ exc.getMessage(),
					new SourcePosition(file.getName(), 0, 0));
		}
		NaleParser p = new NaleParser(file.getName(), lr);
		LexerTree lt = p.parse();
		NaleContext context = lt.newEnv();
		context.setPar(par);
		return context;
	}

	public static void main(String... args) throws NaleException {
		File f = new File("./config/nale/basic.nale");
		NaleContext par = getContext(f);

		f = new File("./config/nale/example.nale");
		NaleContext context = getContext(par, f);

		context.setGlobalEnv("env", new StringNaleObject("env",
				new SourcePosition("lib", 0, 0)));
		context.execute();
	}
}
