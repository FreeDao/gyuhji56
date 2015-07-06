package dfcf;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import com.bmtech.utils.http.HttpCrawler;

public class Crawl {

	public static void main(String[]args) throws IOException{
		String str = "http://guba.eastmoney.com/news,cjpl,98226029_PAGER.html";
		File base = new File(Env.tmpFile, "dfcf");
		base.mkdirs();
		for(int x = 1; x <= 27; x ++){
			String u = str.replace("PAGER",  "" + x);
			System.out.println(u);
			URL url = new URL(u);
			HttpCrawler crl = new HttpCrawler(url);
			crl.connect();
			crl.dumpTo(new File(base, x + ".html"));
			
		}
	}
}
