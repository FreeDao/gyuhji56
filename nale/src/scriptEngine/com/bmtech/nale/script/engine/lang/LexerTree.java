package com.bmtech.nale.script.engine.lang;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.bmtech.nale.script.engine.jmo.lang.source.SourcePosition;
import com.bmtech.nale.script.engine.lang.ast.BlockNode;
import com.bmtech.nale.script.engine.lang.ast.IFEndNode;
import com.bmtech.nale.script.engine.lang.ast.IFNode;
import com.bmtech.nale.script.engine.lang.ast.itf.NaleNode;

public class LexerTree {
	private static final String IF_END = "IF END", FILE_END = "FILE END";
	final List<NaleNode> list;
	final String sourceName;

	LexerTree(String sourceName, List<NaleNode> nodes) {
		this.list = Collections.unmodifiableList(compile(nodes));
		this.sourceName = sourceName;
	}

	private List<NaleNode> compile(List<NaleNode> nodeList) {
		List<NaleNode> nodes = new ArrayList<NaleNode>();
		Iterator<NaleNode> itr = nodeList.iterator();
		list(itr, nodes, FILE_END);
		return nodes;
	}

	private void list(Iterator<NaleNode> itr, List<NaleNode> nodes2,
			String expect) {

		while (itr.hasNext()) {

			final NaleNode nn = itr.next();
			if (nn == null) {
				continue;
			}

			if (nn instanceof IFNode) {
				List<NaleNode> nodes = new ArrayList<NaleNode>();
				list(itr, nodes, IF_END);
				BlockNode bm = new BlockNode(nodes, nn.getPosition());
				((IFNode) nn).setBody(bm);
			} else if (nn instanceof IFEndNode) {
				if (expect == IF_END) {
					return;
				} else {
					throw new NaleException("expect fi, but get " + nn,
							nn.getPosition());
				}
			}
			nodes2.add((NaleNode) nn.copyof());
		}
		if (expect != FILE_END) {
			throw new NaleException("expect " + expect
					+ ", but get end of script", new SourcePosition(sourceName,
					-1, -1));
		}
	}

	public NaleContext newEnv() {

		return new NaleContext(this.sourceName, list);
	}

}
