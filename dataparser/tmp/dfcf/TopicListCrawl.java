package dfcf;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import com.bmtech.htmls.parser.Parser;
import com.bmtech.htmls.parser.filters.NodeClassFilter;
import com.bmtech.htmls.parser.tags.LinkTag;
import com.bmtech.htmls.parser.util.NodeList;
import com.bmtech.htmls.parser.util.ParserException;
import com.bmtech.utils.http.HttpCrawler;
import com.bmtech.utils.io.LineReader;
import com.bmtech.utils.io.LineWriter;

public class TopicListCrawl {
	LineWriter lw ;
	LineReader lr;
	int count = 0;
	TopicListCrawl() throws IOException, ParserException{
		lw = new LineWriter(new File(Env.tmpFile, "topics_new.txt"), false);
		lr = new LineReader(new File(Env.tmpFile, "topics.txt"));
		
	}
	private synchronized URL poll() {
		while(lr.hasNext()){
			String line = lr.next();
			try{
			System.out.println("count " + ++count);
			
				return (new URL(line));
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
		return null;
	}
	void startThread() throws IOException, ParserException{

		Thread t = new Thread(){
			public void run(){
				while(true){
					URL u = poll();
					if(u == null){
						break;
					}
					try{
						crawlEn2_2(u, lw);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}

			
		};
		t.start();
	}
	void crawlEn2_2(URL url, LineWriter lw) throws IOException, ParserException{
		System.out.println("crawling " + url);
		HttpCrawler crl = new HttpCrawler(url);
		crl.connect();
		String html = crl.getString();
		Parser p = new Parser(html);
		NodeClassFilter flt = new NodeClassFilter(LinkTag.class);
		NodeList nl = p.parse(flt);
		for(int x = 0; x < nl.size(); x ++){
			String ux = ((LinkTag)(nl.elementAt(x))).getLink();
			if(ux == null){
				continue;
			}
			URL u = new URL(url, ux);
			synchronized(lw){
				lw.writeLine(u);
			}
		}
		lw.flush();
	}

	public static void main(String[] args) throws Exception {
		TopicListCrawl c = new TopicListCrawl();
		int thread = 1;
		for(int x = 0; x < thread; x ++){
			c.startThread();
		}
	}
}
