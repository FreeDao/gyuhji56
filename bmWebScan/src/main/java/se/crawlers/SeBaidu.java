package se.crawlers;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;

public class SeBaidu extends AbstractCrawl {

	public SeBaidu() throws IOException {
		super(SeCrawlConfig.IndexBaidu);
	}

	@Override
	URL getUrl(WordCount word) throws Exception {
		String pattern = "http://www.baidu.com/s?wd=encWord&pn=pageNum&oq=encWord&tn=baiduhome_pg&ie=utf-8";
		String encWord = URLEncoder.encode(word.word, "utf-8");
		int pageNum = (word.crawl - 1) * 10;
		pattern = pattern.replace("encWord", encWord);
		pattern = pattern.replace("pageNum", pageNum + "");
		return new URL(pattern);

	}
}
