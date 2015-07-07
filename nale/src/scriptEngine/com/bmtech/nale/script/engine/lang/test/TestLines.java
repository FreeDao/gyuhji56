package com.bmtech.nale.script.engine.lang.test;

import com.bmtech.nale.script.engine.lang.LineParser;
import com.bmtech.nale.script.engine.lang.NaleException;
import com.bmtech.nale.script.engine.lang.ast.itf.NaleNode;
import com.bmtech.utils.Consoler;

public class TestLines {

	public static void test(String line) throws NaleException {
		LineParser lp = new LineParser("testor.nale", 1, line);
		NaleNode node = lp.parse();
		System.out.println(node);
	}

	public static void main(String... args) {

		while (true) {
			String line = Consoler.readString("> ");
			try {
				test(line);
			} catch (Exception exp) {
				exp.printStackTrace();
			}
		}
	}
}
