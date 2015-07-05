package com.bmtech.util.nashorn;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Test2 {

	void xx1() throws IOException {
		Document doc = Jsoup.connect("http://en.wikipedia.org/").get();
		Elements newsHeadlines = doc.select("#mp-itn b a");
		System.out.println(newsHeadlines.size());
		Element e = newsHeadlines.get(0);
		String str = e.attr("href");
		System.out.println(newsHeadlines);
		System.out.println(str);

		String html = "<html><head><title>First parse</title></head>"
				+ "<body><p>Parsed HTML into a doc.</p></body></html>";
		doc = Jsoup.parse(html);
		Element ex = doc.select("title").get(0);
		System.out.println(ex.text());
		System.out.println(ex.tag());
		System.out.println(ex.html());
		System.out.println(ex.attributes());

		html = "<p>An <a href='http://example.com/'><b>example</b></a> link.</p>";
		doc = Jsoup.parse(html);
		Element link = doc.select("a").first();

		String text = doc.body().text(); // "An example link"
		String linkHref = link.attr("href"); // "http://example.com/"
		String linkText = link.text(); // "example""

		String linkOuterH = link.outerHtml();
		// "<a href="http://example.com"><b>example</b></a>"
		String linkInnerH = link.html(); // "<b>example</b>"
	}

	public static void main(String... ags) throws IOException {
		Test2 tt = new Test2();
		tt.xx1();
	}
}
