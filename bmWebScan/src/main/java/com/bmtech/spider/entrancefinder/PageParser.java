package com.bmtech.spider.entrancefinder;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import com.bmtech.htmls.parser.Parser;
import com.bmtech.htmls.parser.filters.NodeClassFilter;
import com.bmtech.htmls.parser.tags.LinkTag;
import com.bmtech.htmls.parser.util.NodeList;
import com.bmtech.htmls.parser.util.ParserException;

public class PageParser {
	public static class PageRecord {
		public final URL url;
		public final File file;
		public final String html;

		public PageRecord(String url, File file, String html)
		throws IOException {
			this.url = new URL(url);
			this.file = file;
			this.html = html;
		}
	}

	public void parse(PageRecord record, UrlSavor savor) throws ParserException, IOException {
		Parser p = new Parser(record.html);
		NodeList list = p.parse(new NodeClassFilter(LinkTag.class));
		HashMap<String, Integer>map = new HashMap<String, Integer>();
		int outLink = 0;
		for(int x = 0; x < list.size(); x ++){
			LinkTag lt = (LinkTag) list.elementAt(x);
			outLink += 10;
			String link = lt.getLink();
			try {
				URL url0 = new URL(record.url, link);
				String urlStr = url0.toString();
				String text = lt.getLinkText();
				if(text == null)
					text = "";
				text = text.toLowerCase().replace("&nbsp;", " ")
				.replace("&nbsp", " ").replace(" ", "");//.trim();
				double weight = 1.0;
				if(text.length() > 1){
					weight = 1 + 4.0 / (2 + text.length());
				}
				int w = (int) (weight * 10);
				Integer ww = map.get(urlStr);
				if(ww == null || ww < w){
					map.put(urlStr, w);
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		savor.addLinkOut(record.url, outLink);
		savor.addLinkTo(record.url, map);
	}

	public static void main(String args[]) {

	}
}
