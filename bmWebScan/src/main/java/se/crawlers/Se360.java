package se.crawlers;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;

public class Se360 extends AbstractCrawl {

	public Se360() throws IOException {
		super(SeCrawlConfig.Index360);
	}

	@Override
	URL getUrl(WordCount word) throws Exception {
		String pattern = "http://www.haosou.com/s?q=encWord&pn=pageNum";
		String encWord = URLEncoder.encode(word.word, "utf-8");
		int pageNum = word.crawl + 1;
		pattern = pattern.replace("encWord", encWord);
		pattern = pattern.replace("pageNum", pageNum + "");
		return new URL(pattern);

	}
}
