package com.bmtech.spider.core;

import java.net.URL;

import com.bmtech.htmls.parser.Node;
import com.bmtech.htmls.parser.tags.LinkTag;
import com.bmtech.htmls.parser.util.NodeList;

public abstract class HtmlScore {
	public abstract int listScore(NodeList nl, URL url);
	public abstract int pageScore(NodeList nl, URL url);
	public abstract int urlScore(String linkText, String title, String link);
	public abstract boolean isGoodScore(int pageScore, int listScore);
	
	public static final void rewriteTree(NodeList nl, NodeList nl2){
		for(int y = 0; y < nl.size(); y ++){
			Node nx = nl.elementAt(y);
			if(nx instanceof LinkTag){
				continue;
			}
			NodeList child = nx.getChildren();
			if(child != null){
				NodeList nl2new = new NodeList();
				rewriteTree(child, nl2new);
				nx.setChildren(nl2new);
			}
			nl2.add(nx);
		}
	}
}
