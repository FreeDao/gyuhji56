package se.crawlers;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;

public class SeBing extends AbstractCrawl {

	public SeBing() throws IOException {
		super(SeCrawlConfig.IndexBing);
	}

	@Override
	URL getUrl(WordCount word) throws Exception {
		String pattern = "http://cn.bing.com/search?q=encWord&first=pageNum";
		String encWord = URLEncoder.encode(word.word, "utf-8");
		int pageNum = (word.crawl - 1) * 10;
		pattern = pattern.replace("encWord", encWord);
		pattern = pattern.replace("pageNum", pageNum + "");
		return new URL(pattern);

	}
}
