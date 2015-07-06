package dfcf;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import com.bmtech.htmls.parser.util.ParserException;
import com.bmtech.utils.Consoler;
import com.bmtech.utils.bmfs.MDir;
import com.bmtech.utils.bmfs.MFile;
import com.bmtech.utils.bmfs.MFileWriter;
import com.bmtech.utils.bmfs.MFileWriter.MFileOutputStream;
import com.bmtech.utils.http.HttpCrawler;
import com.bmtech.utils.io.LineReader;

public class DetailcrawlWithMdir {
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
	static final File base1 = new File(Env.tmpFile, "all");
	static final File base2 = new File(Env.tmpFile2, "all");

	LineReader lr;
	MDir mdir;
	DetailcrawlWithMdir() throws IOException{
		lr = new LineReader(new File(Env.tmpFile, "urls.txt"));
		String toStr = Consoler.readString("  tosave:");
		File toFile = new File(toStr);
		if(toFile.exists() && !toFile.isDirectory()){
			System.out.println("toDir isDirectory unknown");
			return;
		}
		mdir = new MDir(toFile, true);
		reporter.start();
	}
	synchronized URL load() {
		while(lr.hasNext()){
			String line = lr.next();
			line = line.replaceAll("htmlhttp.+", "html");
			if(!line.contains("/news,")){
				continue;
			}
			if(!line.endsWith("html")){
				System.out.println("error:" + line);
				continue;
			}
			if(line.contains("htmlhttp")){
				System.out.println("error:" + line);
				continue;
			}
			try{
				URL u = new URL(line);
				if(!u.getHost().equals("guba.eastmoney.com")){
					continue;
				}
				return u;
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return null;
	}

	void crawlEnThread() throws IOException, ParserException{
		Thread t = new Thread(){
			public void run(){
				MFileWriter wchan = mdir.openWriter();
				while(true){
					URL u;
					try {
						u = load();
						if(u == null){
							break;
						}
						crawlEn3_1(u, wchan);
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
	void crawlEn3_1(URL url, MFileWriter writer) throws IOException{
		check.incrementAndGet();

		String p = url.getPath();
		int pos = p.lastIndexOf("/");
		if(pos == -1){
			pos = p.lastIndexOf("\\");
		}
		if(pos != -1){
			p = p.substring(pos + 1).trim();
		}
		if(p.length() == 0){
			return;
		}
		MFile mfile = mdir.createMFileIfPossible(p);

		if(mfile == null){
//			System.out.println("skip " + p);
			return;
		}
		has.addAndGet(1);
		HttpCrawler crl = new HttpCrawler(url);
		crl.connect();
		MFileOutputStream ops = writer.openMFileGzip(mfile);
		
		crl.dumpTo(ops);
		ops.close();
		
		System.out.println("got!" + mfile + ", size " + mfile.getLength()/1000.0 + " KB");
		//		Consoler.readString("waiting");
	}
	public static void main(String[] args) throws Exception {
		String threadsNum = Consoler.readString("threads:");
		int threads = Integer.parseInt(threadsNum.trim());

		DetailcrawlWithMdir d = new DetailcrawlWithMdir();

		for(int x = 0; x < threads; x ++){
			d.crawlEnThread();
		}
	}
}
