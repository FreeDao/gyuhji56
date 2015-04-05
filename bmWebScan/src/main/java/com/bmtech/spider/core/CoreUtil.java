package com.bmtech.spider.core;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import com.bmtech.htmls.parser.filters.NodeClassFilter;
import com.bmtech.htmls.parser.tags.FrameTag;
import com.bmtech.htmls.parser.tags.LinkTag;
import com.bmtech.htmls.parser.tags.TitleTag;
import com.bmtech.htmls.parser.util.NodeList;

public class CoreUtil {

	public static String urlHost(URL url) {
		return url.getHost().toLowerCase().trim();
	}

	public static File createFile(File file) throws IOException {
		if (!file.getParentFile().exists()) {
			if (!file.getParentFile().mkdirs()) {
				throw new IOException("can not create file "
						+ file.getAbsolutePath());
			}
		}
		return file;
	}

	public static File createDir(File dir) throws IOException {
		if (!dir.exists()) {
			if (!dir.mkdirs()) {
				throw new IOException("can not create dir "
						+ dir.getAbsolutePath());
			}
		}
		return dir;
	}

	public static final ArrayList<LinkClass> lnkExtract(NodeList nodeList) {
		ArrayList<LinkClass> ret = new ArrayList<LinkClass>();

		NodeList nl = nodeList.extractAllNodesThatMatch(new NodeClassFilter(
				LinkTag.class), true);
		for (int x = 0; x < nl.size(); x++) {
			LinkTag lt = (LinkTag) nl.elementAt(x);
			String text = lt.getText();
			String title = lt.getAttribute("title");
			String link = lt.getLink();
			LinkClass lcs = new LinkClass(text, title, link);
			ret.add(lcs);
		}
		try {
			NodeList nl2 = nodeList.extractAllNodesThatMatch(
					new NodeClassFilter(FrameTag.class), true);
			for (int x = 0; x < nl2.size(); x++) {
				FrameTag ft = (FrameTag) nl2.elementAt(x);
				String title = ft.getAttribute("title");
				LinkClass lcs = new LinkClass(null, title,
						ft.getFrameLocation());
				ret.add(lcs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static String getHtmlTitle(NodeList nl) {
		NodeList titles = nl.extractAllNodesThatMatch(new NodeClassFilter(
				TitleTag.class), true);

		String title = "";
		if (titles.size() > 0) {
			title = titles.elementAt(0).toPlainTextString();
		}
		return title;
	}
}
