package com.bmtech.lexcion.ip202_114_40_175.port8080;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.concurrent.atomic.AtomicInteger;

import com.bmtech.htmls.parser.util.ParserException;
import com.bmtech.utils.Consoler;
import com.bmtech.utils.bmfs.MDir;
import com.bmtech.utils.bmfs.MFile;
import com.bmtech.utils.bmfs.MFileWriter;
import com.bmtech.utils.bmfs.MFileWriter.MFileOutputStream;
import com.bmtech.utils.http.HttpCrawler;
import com.bmtech.utils.io.LineReader;


public class CrawlFirstPage {
	public static final File mdirFile = new File("/202.114.40.175/8080-1/");
	static final AtomicInteger has = new AtomicInteger();
	static final AtomicInteger check = new AtomicInteger();
	Thread reporter = new Thread(){
		public void run(){
			SimpleDateFormat sdf = new SimpleDateFormat("dd HH:mm:ss");
			while(true){
				try {
					Thread.sleep(1000);
					System.out.println(sdf.format(System.currentTimeMillis()) + "   ????????????????????????????????????????!!!!!!!!!!!!!       " + check + ":" + has + "( mdir size " + mdir.size() + ")            !!!!!!!!!!!!!       ");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};

	LineReader lr;
	MDir mdir;
	CrawlFirstPage() throws IOException{
		lr = new LineReader(new File("cidian/tops.txt"));
		File toFile = mdirFile;
		if(toFile.exists() && !toFile.isDirectory()){
			System.out.println("toDir isDirectory unknown");
			return;
		}
		mdir = new MDir(toFile, true);
		reporter.start();
	}
	synchronized String load() throws IOException {
		while(lr.hasNext()){
			String line = lr.next();
			char c = line.charAt(0);
			if(Character.isLetter(c)){
				return line;
			}
		}
		return null;
	}

	void crawlEnThread() throws IOException, ParserException{
		Thread t = new Thread(){
			public void run(){
				MFileWriter wchan = mdir.openWriter();
				while(true){
					try {
						String word = load();
						if(word == null){
							break;
						}
						crawlEn3_1(word, wchan);
					} catch (Exception e) {
						e.printStackTrace();
						try {
							sleep(1000);
						} catch (InterruptedException xe) {
							xe.printStackTrace();
						}
					}
				}
				try {
					sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.exit(0);
			}
		};
		t.start();

	}
	void crawlEn3_1(String word, MFileWriter writer) throws IOException{
		final String pu = 
				"http://202.114.40.175:8080/cqs/query/search_words?query="+ URLEncoder.encode(word, "utf-8") + "&year_start=1900&year_end=2000&category=&subcategory=&sort_type=0&show_type=0";//+"&year_start=1900&year_end=2500&category=&subcategory=&sort_type=0&show_type=0&max=50000&offset=";

		for(int x = 0; x < 1; x ++){
			try{
				check.incrementAndGet();

				URL u = new URL(pu);// + (x * 100000));
				System.out.println("crawling " + u);
				MFile mfile = mdir.createMFileIfPossible(word + "__!!" + (1 + x));

				if(mfile == null){
					return;
				}
				has.addAndGet(1);
				HttpCrawler crl = new HttpCrawler(u);
				crl.handler.setReadTimeout(10000);
				crl.handler.setConnectTimeout(2000);
				crl.connect();
				MFileOutputStream ops = writer.openMFileGzip(mfile);

				crl.dumpTo(ops);
				ops.close();

				System.out.println("got!" + mfile + ", size " + mfile.getLength()/1000.0 + " KB");
				if(mfile.getLength() < 1 * 1000 * 1000){
					break;
				}
			}catch(Exception e){
				e.printStackTrace();
				break;
			}
		}
		//		Consoler.readString("waiting");
	}
	public static void main(String[] args) throws Exception {
		String threadsNum = Consoler.readString("threads:");
		int threads = Integer.parseInt(threadsNum.trim());

		CrawlFirstPage d = new CrawlFirstPage();

		for(int x = 0; x < threads; x ++){
			d.crawlEnThread();
		}
	}
}
