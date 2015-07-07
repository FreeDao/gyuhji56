package com.bmtech.nale.script.engine.lang;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.bmtech.nale.script.engine.lang.ast.itf.NaleNode;

public class NaleParser {

	final Iterator<String> lineIterator;
	final String sourceName;

	public NaleParser(String sourceName, Iterator<String> lineIterator) {
		this.lineIterator = lineIterator;
		this.sourceName = sourceName;
	}

	public LexerTree parse() throws NaleException {
		int lineNum = 0;
		List<NaleNode> nodes = new ArrayList<NaleNode>();

		while (this.lineIterator.hasNext()) {
			String line = this.lineIterator.next();
			lineNum++;
			LineParser lp = new LineParser(sourceName, lineNum, line);
			NaleNode node = lp.parse();
			nodes.add(node);
		}
		LexerTree tree = new LexerTree(sourceName, nodes);
		return tree;
	}
}
