package com.bmtech.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import com.bmtech.htmls.parser.Node;
import com.bmtech.htmls.parser.Remark;
import com.bmtech.htmls.parser.Tag;
import com.bmtech.htmls.parser.nodes.TextNode;
import com.bmtech.htmls.parser.tags.Div;
import com.bmtech.htmls.parser.tags.ParagraphTag;
import com.bmtech.htmls.parser.tags.TableRow;
import com.bmtech.htmls.parser.util.NodeList;

public class HtmlRemover {
	public class TrimResult{
		public final HashSet<Integer>breaks;
		public final String html;
		public final String lines;
		public final ArrayList<NodePosition>positions;
		public final NodeList nl;
		private TrimResult(NodeList nl, String lines, HashSet<Integer>breaks,
				ArrayList<NodePosition>positons) {
			this.lines = lines;
			this.html = nl.toHtml();
			this.nl = nl;
			this.positions = positons;
			Collections.sort(this.positions);
			this.breaks = breaks;
		}
	}

	class NodeBia{
		final int offset;
		final TextNode textNode;
		final int absOffset;
		public NodeBia(int offset, TextNode node, int absOffset) {
			this.offset = offset;
			this.textNode = node;
			this.absOffset = absOffset;
		}

	}
	public class NodePosition implements Comparable<NodePosition>{
		final Node node;
		final int start;
		final int end;
		final int gi;
		NodePosition(int gi, Node node,	int start,	int end){
			this.node = node;
			this.gi = gi;
			this.start = start;
			this.end = end;
		}
		public NodeBia  offset(int i) {
			if(this.node instanceof TextNode) {
				if(i >= start && i <= end) {
					return new NodeBia(i - start,
							(TextNode) node, i);
				}
			}
			return null;
		}
		public String toString() {
			if(node instanceof Tag) {
				return gi + "\t" + ((Tag)node).getTagName() + " " + start + " " + end;
			}else {
				return gi + "\tTextNode " + start + " " + end;
			}
		}
		@Override
		public int compareTo(NodePosition o) {
			return gi - o.gi;
		}
	}
	NodeList lst;
	StringBuilder sb = new StringBuilder();
	ArrayList<NodePosition>position = new ArrayList<NodePosition>();
	HashSet<Integer>breaks = new HashSet<Integer>();
	protected HtmlRemover(String html){
		this.lst = LessTags.trimOffedList(html);
	}
	protected HtmlRemover(NodeList lst){
		this.lst = LessTags.trimOffedList(lst);
	}
	public static boolean isLineEndTag(Node node) {
		if(!(node instanceof Tag)) {
			return false;
		}
		Tag tag = (Tag) node;
		if(tag instanceof Div) {
			return true;
		}else if(tag instanceof ParagraphTag) {
			return true;	
		}else if("BR".equalsIgnoreCase(tag.getTagName())) {
			return true;
		}else if(tag instanceof TableRow) {
			return true;
		}else if("HR".equalsIgnoreCase(tag.getTagName())) {
			return true;
		}
		return false;
	}

	TrimResult htmlToLineFormat() {
		toOneLine();
//		for(int i = sb.length(); i > 0; i --) {
//			char c = sb.charAt(i - 1);
//			if(c == '\n' || c == '\t' || c == ' '){
//				sb.setLength(sb.length() -1);
//			}else {
//				break;
//			}
//		}
		return new TrimResult(
				lst,
				sb.toString(),
				breaks,
				this.position);
	}
	private String trim(TextNode tn) {
		String line = tn.toPlainTextString();
		line = line.replace('\r', ' ');
		line = line.replace('\n', ' ');
		line = line.replace('\t', ' ');
		line = line.replaceAll("&[\\w|\\d]{2,5};", " ");
		line = line.replaceAll("&[\\w|\\d]{2,5}", " ");
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < line.length(); i ++) {
			char c = line.charAt(i);
			sb.append(c);
		}
		String ret = sb.toString();
		ret = ret.replace("   ", " ");
		ret = ret.replace("   ", " ");
		ret = ret.replace("  ", " ");
		ret = ret.replace("  ", " ");
		tn.setText(ret);
		return ret;
	}
	private void toOneLine() {
		for(int i = 0; i < lst.size(); i ++) {
			toOneLine(lst.elementAt(i));
		}
	}
	private int idx = 0;
	private void toOneLine(Node node) {
		int start = sb.length();
		int gi = idx++;
		if(node instanceof TextNode) {
			sb.append(trim((TextNode) node));
			NodePosition np = new NodePosition(gi, node, 
					start, sb.length());
			position.add(np);
			return;
		}
		if(node instanceof Remark) {
			return;
		}
		if(node instanceof Tag) {
			Tag tag = (Tag) node;
			NodeList nl = tag.getChildren();
			if(nl != null) {
				for(int i = 0; i < nl.size(); i++) {
					toOneLine(nl.elementAt(i));
				}
			}
			NodePosition np = 
				new NodePosition(gi, node, start, sb.length());
			position.add(np);
			if(isLineEndTag(node)) {
				this.breaks.add(sb.length());
				sb.append('\n');
			}
		}
	}
	void rebuildTagTree() {
		NodeList wnl = new NodeList();
		reBuildList(this.lst, wnl);
		lst = wnl;
	}
	void reBuildList(NodeList old, NodeList nw) {
		for(int i = 0; i < old.size();) {
			Node n = old.elementAt(i);
			if(n instanceof TextNode) {
				TextNode tn = (TextNode) n;
				for(int k =  1; (i + k) < old.size(); k++) {
					Node n2 = old.elementAt(i + k);
					if(n2 instanceof TextNode) {
						tn.setText(tn.toPlainTextString() + 
								n2.toPlainTextString());
						i++;
					}else {
						break;
					}
				}
				i++;
			}else {
				if(n instanceof Tag) {
					Tag tag = (Tag) n;
					NodeList nl = tag.getChildren();
					if(nl != null) {
						NodeList nlst = new NodeList();
						reBuildList(nl, nlst);
						tag.setChildren(nlst);
					}
				}
				i++;
			}
			nw.add(n);
		}
	}
	public static TrimResult htmlToLineFormat(String html){
		HtmlRemover trimer = new HtmlRemover(html);
		return trimer.htmlToLineFormat();
	}
	public static TrimResult htmlToLineFormat(NodeList nodeList){
		HtmlRemover trimer = new HtmlRemover(nodeList);
		return trimer.htmlToLineFormat();
	}
}
