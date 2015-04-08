package test.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import com.bmtech.htmls.parser.Parser;
import com.bmtech.htmls.parser.filters.HasAttributeFilter;
import com.bmtech.htmls.parser.filters.TagNameFilter;
import com.bmtech.htmls.parser.tags.LinkTag;
import com.bmtech.htmls.parser.util.NodeList;
import com.bmtech.utils.bmfs.MDir;
import com.bmtech.utils.bmfs.MFileReader;
import com.bmtech.utils.bmfs.MFileReaderIterator;
import com.bmtech.utils.http.itrCrawl.FileGenor;
import com.bmtech.utils.http.itrCrawl.GenEntry;
import com.bmtech.utils.http.itrCrawl.IteratorableCrawler;
import com.bmtech.utils.http.itrCrawl.UrlIterator;
import com.bmtech.utils.io.LineWriter;

public class daxue {
	MDir mdirx;

	daxue() throws IOException {
		mdirx = MDir.makeMDir(

		new File("/test2/daxue"), true);
	}

	public static void main(String[] args) throws Exception {
		daxue d = new daxue();
		// d.crawl();
		MFileReaderIterator itr = d.mdirx.openReader();
		LineWriter lw = new LineWriter("./testConf/daxue.list.txt", false);
		while (itr.hasNext()) {
			MFileReader reader = itr.next();
			System.out.println("for " + reader.getMfile());
			byte[] bs = reader.getBytesUnGZiped();
			String html = new String(bs, "gbk");
			// System.out.println(html);
			d.extract(html, lw);
		}
		lw.close();
	}

	public void crawl() throws IOException {

		UrlIterator gen = new FileGenor(new File("testConf/daxue.txt")) {
			@Override
			protected GenEntry toEntry(String url) throws IOException {
				String numStr = lr.currentLineNumber() + ".htm";
				return new GenEntry(new URL(url), numStr);
			}

		};
		IteratorableCrawler crl = new IteratorableCrawler(mdirx, gen, 3000,
				3000);
		crl.setInterval(3000);
		crl.spawn(1);
	}

	public void extract(String html, LineWriter lw) throws Exception {
		Parser p = new Parser(html);
		NodeList nl = p.parse(null);

		NodeList nl2 = nl.extractAllNodesThatMatch(new HasAttributeFilter(
				"bgcolor", "C5D5C5"), true);
		// System.out.println(nl2.size());
		NodeList urls = nl2.extractAllNodesThatMatch(new TagNameFilter("a"),
				true);
		for (int x = 0; x < urls.size(); x++) {
			LinkTag lt = (LinkTag) urls.elementAt(x);
			System.out.println(lt.getLink() + "\t" + lt.getLinkText());
			lw.writeLine(lt.getLink() + "\t" + lt.getLinkText());
		}
	}
}
