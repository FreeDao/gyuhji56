package com.bmtech.utils;

import java.util.Vector;

import com.bmtech.htmls.parser.Attribute;
import com.bmtech.htmls.parser.Node;
import com.bmtech.htmls.parser.Parser;
import com.bmtech.htmls.parser.Tag;
import com.bmtech.htmls.parser.nodes.TextNode;
import com.bmtech.htmls.parser.tags.ScriptTag;
import com.bmtech.htmls.parser.tags.StyleTag;
import com.bmtech.htmls.parser.util.NodeList;
import com.bmtech.htmls.parser.util.ParserException;
import com.bmtech.utils.log.BmtLogger;

public class LessTags {
	void rewriteTextNode(NodeList nl) {
		for(int i = 0; i < nl.size(); i++) {
			Node n = nl.elementAt(i);
			if(n instanceof TextNode) {
				rewriteTextNode((TextNode)n);
			}else if(n instanceof Tag) {
				NodeList nl2 = ((Tag)n).getChildren();
				if(nl2 != null) {
					rewriteTextNode(nl2);
				}
			}
		}
	}
	void rewriteTextNode(TextNode tn) {
		if(tn != null) {
			String text = tn.getText();
			try {
				text = rewriteText(text);
			}catch(Exception e) {
				BmtLogger.log.warn(e, "when rewrite text");
			}
			tn.setText(text);
		}
	}
	public static String rewriteText(String content) {
		if(content == null)
			return "";
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < content.length(); i++) {
			char c = content.charAt(i);
			if(c == '&') {
				if((i + 1 < content.length())&&content.charAt(i + 1) == '#') {
					int end = getEnd(content, i + 2);
					if(end < 3) {
						sb.append(c);
					}else {
						String s = content.substring(i + 2, 
								i + 2 + end);
						try {
							c = (char) Integer.parseInt(s);
							sb.append(c);
							i = i + 2 + end;
						}catch(Exception e) {
							sb.append(c);
						}

					}
				}else {
					sb.append(c);
				}
			}else if(c == '\\') {
				Character ch = null;
				if((i + 1 < content.length())&&content.charAt(i + 1) == 'u') {
					if(i + 1 + 4 < content.length()){
						int uch = 0;
						boolean match = true;
						for(int var = 0; var  < 4; var ++){
							char cx = content.charAt(i + 2 + var);
							int ss = 0;
							if(cx >= '0' && cx <= '9'){
								ss = cx - '0';
							}else if(cx >= 'A' && cx <= 'F'){
								ss = cx - 'A' + 10;
							}else if(cx >= 'a' && cx <= 'f'){
								ss = cx - 'a' + 10;
							}else{
								match = false;
								break;
							}
							uch = (uch << 4) + ss;
						}
						if(match){
							ch = (char) uch;
						}
					}
				}
				i = i + 5;
				if(ch == null){
					sb.append(c);
				}else{
					sb.append(ch);
				}
				
			}else {
				sb.append(c);
			}
		}
		return sb.toString();
	}
	static int getEnd(String content, int start) {
		int end = 0;
		for(int i = 0; i < 7; i++) {
			if(i + start >= content.length()) {
				if(i < 2) {
					return -1;
				}
				return i;
			}
			char c = content.charAt(i + start);
			if(c == ';') {
				end = i;
				break;
			}else if(c >= '0' && c <= '9') {
				continue;
			}else {
				return  -1;
			}
		}
		if(end < 2) {
			return -1;
		}
		return end;
	}
	public static class AcceptAttribute{
		final String tagname;
		final String attname;
		public static final String allAcc = "*";
		/**
		 * accept all attribute
		 * @param tagname
		 */
		public AcceptAttribute(String tagname) {
			this(tagname, allAcc);
		}
		public AcceptAttribute(String tagname, String attname) {
			this.tagname = tagname;
			this.attname = attname;
			if(attname == null) {
				throw new RuntimeException("null attname not accept");
			}
		}
		public boolean accept(Tag t, Attribute att) {
			if(!tagname.equalsIgnoreCase(t.getTagName())) {
				return false;
			}
			if(allAcc == this.attname) {
				return true;
			}
			if(att.isEmpty() || att.isWhitespace()) {
				return false;
			}
			String attName = att.getName();
			if(attName == null) {
				return false;
			}
			if(attName.equalsIgnoreCase(tagname)) {
				return true;
			}
			if(attName.equalsIgnoreCase(attname)) {
				return true;
			}
			return false;
		}
	}
	static final String []accTagNames = {
		"table", "tr", "td", "div", "p", "br", "a", "ul", "li"
	};
	static final AcceptAttribute[]acc = new AcceptAttribute[] {
		new AcceptAttribute("a"),
		new AcceptAttribute("table", "border"),
		new AcceptAttribute("td", "colspan"),
		new AcceptAttribute("td", "rowspan"),
	};
	NodeList nl;
	boolean canNotParse = false;
	boolean hasRewrite = false;
	String otherAccTagnames[];
	private LessTags(String tagnames[],String html) throws ParserException {
		//		html = rewriteHtml(html);
		this.otherAccTagnames = tagnames;
		html = html.replaceAll("<\\?[xX][mM][lL]\\:.*?\\>", "");
		Parser p;
		p = new Parser(html);
		nl = p.parse(null);
	}
	private LessTags(String tagnames[], NodeList nl) throws ParserException {
		//		html = rewriteHtml(html);
		this.otherAccTagnames = tagnames;
		this.nl = nl;
	}
	private void trim() {
		trimNode();
		rewriteTextNode(nl);
		mergeTextNode(nl);

	}
	private void mergeTextNode(NodeList nw) {
		for(int i = 0; i < nw.size(); i++) {
			if(nw.elementAt(i) instanceof TextNode) {
				if(i == 0)
					continue;
				TextNode tn = (TextNode) nw.elementAt(i);
				if(nw.elementAt(i - 1) instanceof TextNode) {
					TextNode brother = (TextNode) nw.elementAt(i - 1);
					brother.setText(
							brother.getText() + tn.getText());
					nw.remove(i);
					i--;
				}
			}
		}
		for(int i = 0; i < nw.size(); i++) {
			if(nw.elementAt(i) instanceof Tag) {
				NodeList nLst = 
					(((Tag)nw.elementAt(i)).getChildren());
				if(nLst == null)
					continue;
				mergeTextNode(nLst);
			}
		}
	}

	private boolean trimNode() {
		hasRewrite = false;
		NodeList newlst = new NodeList();
//		System.out.println(nl);
		for(int i = 0; i < nl.size(); i ++) {
			Node n = nl.elementAt(i);
			trimNode(n, newlst);
		}
		nl = newlst;
		return hasRewrite;
	}
	private void trimNode(Node n, NodeList par) {
		if(n instanceof TextNode) {
			par.add(n);
			return;
		}else if(n instanceof Tag) {
			if((n instanceof StyleTag) ||
					(n instanceof ScriptTag)) {
				return;
			}
			Tag tg = (Tag) n;
			String name = tg.getTagName();
			if(name == null) {
				hasRewrite = true;
				NodeList lst = tg.getChildren();
				if(lst != null) {
					for(int i = 0; i < lst.size(); i++) {
						trimNode(lst.elementAt(i), par);
					}
				}
				return;
			}
			if(tg.isEndTag()) {
				if(!tg.isEmptyXmlTag()) {
					return;
				}
			}
			//now check if an acceptable tag
			boolean acc = false;
			for(String s : accTagNames) {
				if(name.equalsIgnoreCase(s)) {
					acc = true;
					break;
				}
			}
			if(!acc) {
				if(this.otherAccTagnames != null) {
					for(String s : otherAccTagnames) {
						if(name.equalsIgnoreCase(s)) {
							acc = true;
							break;
						}
					}
				}
			}
			if(acc) {
				trimAtt(tg);
				par.add(tg);
				NodeList newlst = new NodeList();
				NodeList chd = tg.getChildren();
				if(chd != null) {
					for(int i = 0; i < chd.size(); i ++) {
						trimNode(chd.elementAt(i), newlst);
					}
				}
				tg.setChildren(newlst);
				return;
			}
			hasRewrite = true;
			//not accept tag, we should remove
			NodeList lst = tg.getChildren();
			if(lst != null) {
				for(int i = 0; i < lst.size(); i++) {
					trimNode(lst.elementAt(i), par);
				}
				return;
			}
		}
	}
	private void trimAtt(Tag tg) {
		//trim end tag
		boolean isStandAlone = tg.isEmptyXmlTag();
		if(!isStandAlone) {
			Tag et = tg.getEndTag();
			if(et != null) {
				String tn = et.getRawTagName().toLowerCase();
				Vector<Attribute> vec = et.getAttributesEx();
				if(vec != null && vec.size() > 0) {
					vec.clear();
				}
				et.setTagName(tn);
			}
		}

		Vector<Attribute> vec = tg.getAttributesEx();
		if(vec == null)
			return;
		Vector<Attribute> newvec = 
			new Vector<Attribute>();
		boolean needSp = false;
		for(Attribute a : vec) {
			if(a.isWhitespace()) {
				continue;
			}
			if(tg.getTagName().equalsIgnoreCase(a.getName())) {
				if(needSp) {
					newvec.add(new Attribute(" "));
				}
				needSp = true;
				a.setName(a.getName().toLowerCase());
				newvec.add(a);
			}else {
				for(AcceptAttribute ac : acc) {
					if(ac.accept(tg, a)) {
						if(needSp) {
							newvec.add(new Attribute(" "));
						}
						needSp = true;
						a.setName(a.getName().toLowerCase());
						newvec.add(a);
					}
				}
			}
		}
		if(newvec.size() > 0) {
			tg.setAttributesEx(newvec);
			if(isStandAlone) {
				newvec.add(new Attribute(" "));
				newvec.add(new Attribute("/", null));
			}
		}
	}
	public static NodeList trimOffedList(String[]tagName, NodeList input) {
		try {
			LessTags tmot = new LessTags(tagName, input);
			tmot.trim();
			return  tmot.nl;
		} catch (ParserException e) {
			e.printStackTrace();
			return null;
		}
	}
	public static NodeList trimOffedList(String[]tagName, String input) {
		try {
			LessTags tmot = new LessTags(tagName, input);
			tmot.trim();
			return  tmot.nl;
		} catch (ParserException e) {
			e.printStackTrace();
			return null;
		}
	}
	public static void formatWhitSpace(NodeList nl) {
		for(int i = 0; i < nl.size(); i++) {
			Node n = nl.elementAt(i);
			if(n instanceof TextNode) {
				formatWhiteSpace((TextNode) n);
			}else if(n instanceof Tag) {
				NodeList nl2 = ((Tag)n).getChildren();
				if(nl2.size() > 0) {
					formatWhitSpace(nl2);
				}
			}
		}
	}
	public static void formatWhiteSpace(TextNode tn) {
		if(tn != null) {
			String text = tn.getText();
			text = text.replace('\t', ' ');
			text = text.replace('\r', ' ');
			text = text.replace('\n', ' ');
			text = text.replaceAll(" {2,}", " ");
			tn.setText(text);
		}
	}
	public static NodeList trimOffedList(String input) {
		return trimOffedList(null, input);
	}
	
	public static NodeList trimOffedList(NodeList input) {
		return trimOffedList(null, input);
	}
}
