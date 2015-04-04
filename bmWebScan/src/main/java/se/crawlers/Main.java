package se.crawlers;

import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {

		AbstractCrawl[] crawls = new AbstractCrawl[] { new Se360(),
				new SeSogou(), new SeBaidu(), new SeBing() };
		for (AbstractCrawl crawl : crawls) {
			crawl.start();
		}
	}
}
