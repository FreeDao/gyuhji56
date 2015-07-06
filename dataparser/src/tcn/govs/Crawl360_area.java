package govs;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;

import com.bmtech.htmls.parser.Parser;
import com.bmtech.htmls.parser.filters.NodeClassFilter;
import com.bmtech.htmls.parser.tags.LinkTag;
import com.bmtech.htmls.parser.util.NodeList;
import com.bmtech.utils.http.HttpCrawler;
import com.bmtech.utils.http.HttpHandler;
import com.bmtech.utils.io.LineReader;
import com.bmtech.utils.io.LineWriter;

public class Crawl360_area {

	public static void main(String[] args) throws IOException {
		final String suf[] = new String[]{
				"site:gov.cn", "site:edu.cn", "site:org.cn", "www"
		};
		LineWriter lw = new LineWriter("/x.txt", true);
		HttpHandler hdl = HttpHandler.getCrawlHandler(true);
		for(String v : suf){
			for(int pg = 2; pg <= 100; pg ++){
				LineReader lr = new LineReader("config/areas/areas");
				while(lr.hasNext()){
					String line = lr.next();
					String token[] = line.split("\\,");
					if(token.length < 3){
						continue;
					}
					
					String urls = "http://www.so.com/s?q=" + URLEncoder.encode(token[1] + " " + v, "utf-8") + "&pn=" + pg + "&j=0&ls=0&src=srp_paging&fr=srp_paging&psid=f24214a7aae3474ad51a126a263c6df4";
					System.out.println(token[1] + " " + urls);
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
								if(ustr.contains("so.com") ||
										ustr.contains("360webcache.com")){
									continue;
								}
								lw.writeLine(ustr);
								System.out.println(ustr);
								lw.flush();
							}catch(java.net.MalformedURLException xxx){
								
							} catch(Exception e){
								e.printStackTrace();
							}
						}

					}catch(java.net.MalformedURLException xxx){
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		}
	}
}
