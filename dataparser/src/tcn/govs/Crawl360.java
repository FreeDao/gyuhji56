package govs;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import com.bmtech.htmls.parser.Parser;
import com.bmtech.htmls.parser.filters.NodeClassFilter;
import com.bmtech.htmls.parser.tags.LinkTag;
import com.bmtech.htmls.parser.util.NodeList;
import com.bmtech.utils.Areas;
import com.bmtech.utils.bmfs.MDir;
import com.bmtech.utils.http.HttpCrawler;
import com.bmtech.utils.http.HttpHandler;
import com.bmtech.utils.http.IteratorableCrawler;
import com.bmtech.utils.http.IteratorableCrawler.GenEntry;
import com.bmtech.utils.http.IteratorableCrawler.LineGenor;
import com.bmtech.utils.io.FileGet;
import com.bmtech.utils.io.LineWriter;

public class Crawl360 {

	public static void main(String[] args) throws IOException {
		final String suf[] = new String[]{
				"site:gov.cn", "site:edu.cn", "site:org.cn", "www"
		};
		LineWriter lw = new LineWriter("/x.txt", true);
		HttpHandler hdl = HttpHandler.getCrawlHandler(true);
		for(String v : suf){
			for(int pg = 1; pg <= 100; pg ++){
				String urls = "http://www.so.com/s?q=" + URLEncoder.encode(v, "utf-8") + "&pn=" + pg + "&j=0&ls=0&src=srp_paging&fr=srp_paging&psid=f24214a7aae3474ad51a126a263c6df4";
				try {
					URL url = new URL(urls);
					HttpCrawler crl = new HttpCrawler(url, hdl);
					crl.connect();
					String xx = crl.getString();

					Parser p = new Parser(xx);
					NodeList nl = p.parse(new NodeClassFilter(LinkTag.class));
					for(int x = 0; x < nl.size(); x ++){
						try{
							LinkTag t = (LinkTag) nl.elementAt(x);
							URL u = new URL(t.getLink());
							String ustr = u.toString();
							lw.writeLine(ustr);
							System.out.println(ustr);
							lw.flush();
						}catch(Exception e){
							e.printStackTrace();
						}
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			
		}
	}
}
