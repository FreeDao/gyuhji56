package crawl;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;

import com.bmtech.utils.bmfs.MDir;
import com.bmtech.utils.http.IteratorableCrawler;
import com.bmtech.utils.http.IteratorableCrawler.GenEntry;
import com.bmtech.utils.http.IteratorableCrawler.LineGenor;

public class CrawlSearch {

	public static void main(String[] args) throws IOException {
		MDir mdir = new MDir(
				new File("/stock/tcn"), true);
		File f = new File("./config/stock/kws.txt");
		LineGenor gen = new LineGenor(f){
			String line;
			int pos = 0;
			@Override
			public synchronized GenEntry next() throws Exception {
				if(line == null){
					line = this.nextLine();
				}
				if(line == null){
					return null;
				}
				if(pos >= 50){
					pos = 0;
					line = null;
					return next();
				}
				int crt = pos;
				pos ++;
				return new GenEntry(
						new URL("http://s.weibo.com/weibo/"+URLEncoder.encode(line, "utf-8")+"&b=1&page=" + (1 + crt)),
						line + "@" + crt);
			}
			
		};
		IteratorableCrawler.crawl(mdir, gen, 1, 1000);
	}
}
