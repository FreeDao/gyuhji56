package se.crawlers;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;

public class SeSogou extends AbstractCrawl {

	public SeSogou() throws IOException {
		super(SeCrawlConfig.IndexSougou);
	}

	@Override
	URL getUrl(WordCount word) throws Exception {
		String pattern = "http://www.sogou.com/web?query=encWord&hp=0&stj0=0&stj1=0&page=pageNum&ie=utf8";
		String encWord = URLEncoder.encode(word.word, "utf-8");
		int pageNum = word.crawl + 1;
		pattern = pattern.replace("encWord", encWord);
		pattern = pattern.replace("pageNum", pageNum + "");
		return new URL(pattern);

	}
}
