package com.bmtech.nale.script.engine.lang.test;

import java.io.IOException;
import java.nio.charset.Charset;

import com.bmtech.nale.script.engine.lang.LineParser;
import com.bmtech.nale.script.engine.lang.NaleException;
import com.bmtech.nale.script.engine.lang.ast.itf.NaleNode;
import com.bmtech.utils.io.LineReader;

public class TestLineparser {

	public static void test(String line) throws NaleException {
		LineParser lp = new LineParser("testor.nale", 1, line);
		NaleNode node = lp.parse();
		System.out.println(" " + node);
	}

	public static void main(String... args) throws IOException {
		LineReader lr = new LineReader("test.nales/test.nale", Charset
				.forName("utf-8"));

		while (lr.hasNext()) {
			String line = lr.next();
			System.out.println("\n>" + line);
			try {
				test(line);
			} catch (Exception exp) {
				exp.printStackTrace();
			}
		}
		lr.close();
	}
}