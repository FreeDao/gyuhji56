package baidu;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Stack;

import com.bmtech.utils.Charsets;
import com.bmtech.utils.http.HttpCrawler;
import com.bmtech.utils.io.LineReader;
import com.bmtech.utils.log.LogHelper;

public class baidu extends Thread{
	int tNum;
	baidu(int tNum){
		this.tNum = tNum;
		log = new LogHelper("t-" + tNum);
	}
	static final File base = new File("e:/sogou/");
	static Stack<String>stack = new Stack();
	LogHelper log ;
	public void run(){
		while(true){
			String x ;
			synchronized(stack){
				if(stack.isEmpty()){
					return;
				}
				x = stack.pop();
			}
			
			log.info("crawl %s  ", x);
			int dirId = Math.abs(x.hashCode() % 1000);

			File tmp = new File(base, dirId + "/" + x + ".tmp");
			if(tmp.exists()){
				tmp.delete();
			}else{
				tmp.getParentFile().mkdirs();
			}
			File ok = new File(base, dirId + "/" + x + ".html");
			if(ok.exists()){
				continue;
			}

			URL url;
			try {
//				url = new URL("http://www.baidu.com/s?wd=" + URLEncoder.encode(x, "utf-8"));
				url = new URL("http://www.sogou.com/web?query=" + URLEncoder.encode(x, "utf-8"));
				log.info("crawl %s from %s ", x, url);
				HttpCrawler crl = new HttpCrawler(url);
				crl.connect();
				crl.dumpTo(tmp);
				tmp.renameTo(ok);
				log.info("crawl %s of %sK for file %s", x, ok.length() / 1024, ok.getAbsolutePath());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
		}


	}

	public static void main(String[] args) throws IOException {
		LineReader lr = new LineReader(
				new File("config/segment/lexicon"), Charsets.UTF8_CS);
		while(lr.hasNext()){
			String w = lr.next();
			stack.push(w);
			System.out.println(w);
		}
		int thread = 2;
		for(int x = 0; x < thread; x ++){
			baidu b = new baidu(x);
			b.start();
		}
	}
}
