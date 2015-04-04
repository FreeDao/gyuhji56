package se.crawlers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.bmtech.utils.io.ConfigReader;

class SeCrawlConfig {
	static final int Index360 = 0;
	static final int IndexBaidu = 1;
	static final int IndexSougou = 2;
	static final int IndexBing = 3;
	static final String names[] = new String[] { "360", "baidu", "sougou",
			"bing" };
	final File saveDir;
	final int sleepItvSec;
	ArrayList<String> exclude = new ArrayList<String>();

	SeCrawlConfig(int index) throws IOException {
		ConfigReader cr = new ConfigReader("./config/seCrawl/site.conf",
				names[index]);
		File saveDir = new File(cr.getValue("saveDir", "/seCrawl"));
		this.saveDir = new File(saveDir, names[index]);
		sleepItvSec = cr.getInt("sleepItvSec", 11) * 1000;

		cr = new ConfigReader("./config/seCrawl/site.conf", "exclude");
		String str = cr.getValue("exclude");
		if (str != null) {
			String[] tokens = str.replace('\t', ' ').split(" ");
			for (String x : tokens) {
				x = x.trim().toLowerCase();
				if (x.length() == 0)
					continue;
				System.out.println("exclude:" + x);
				this.exclude.add(x);
			}
		}
	}

	public boolean exclude(String host) {
		host = host.toLowerCase().trim();
		for (String x : this.exclude) {
			if (host.endsWith(x)) {
				return true;
			}
		}
		return false;
	}
}