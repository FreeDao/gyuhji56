package dfcf;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.GZIPOutputStream;

import com.bmtech.htmls.parser.util.ParserException;
import com.bmtech.utils.Consoler;
import com.bmtech.utils.http.HttpCrawler;
import com.bmtech.utils.io.LineReader;

public class Detailcrawl {
	static final AtomicInteger has = new AtomicInteger();
	static final AtomicInteger check = new AtomicInteger();
	static int total = 0;
	static{
		new Thread(){
			public void run(){
				SimpleDateFormat sdf = new SimpleDateFormat("dd HH:mm:ss");
				while(true){
					try {
						Thread.sleep(1000);
						System.out.println(sdf.format(System.currentTimeMillis()) + "   ????????????????????????????????????????!!!!!!!!!!!!!       " + check + ":" + has + "(" + total + ")            !!!!!!!!!!!!!       ");
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
	static final File base1 = new File(Env.tmpFile, "all");
	static final File base2 = new File(Env.tmpFile2, "all");
	static File newFile(File base, File f){
		
		String name = f.getName();
		int hashCode = name.hashCode();
		int[] par = new int[]{
				hashCode & 0x00ff0000,
				hashCode & 0x0000ff00,
				hashCode & 0x00000ff
		};
		File retDir = new File(base, par[0] +"/" + par[1] +"/" + par[2] +"/" );
		if(!retDir.exists()){
			retDir.mkdirs();
		}
		return new File(retDir, name + ".gz");
	}
	LinkedBlockingQueue<URL> queue = new LinkedBlockingQueue<URL>();
	
	void load() throws IOException, ParserException{
		Set<String>set = new HashSet<String>();
		LineReader lr = new LineReader(new File(Env.tmpFile, "urls.txt"));
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
			if(set.contains(line)){
				continue;
			}
			try{
				URL u = new URL(line);
				if(!u.getHost().equals("guba.eastmoney.com")){
					continue;
				}
				
				queue.put(u);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		lr.close();
		total = queue.size();
	}

	void crawlEnThread() throws IOException, ParserException{
		Thread t = new Thread(){
			public void run(){
				ByteArrayOutputStream bios	 = new ByteArrayOutputStream();
				while(true){
					URL u;
					try {
						u = queue.poll();
						if(u == null){
							break;
						}
						bios.reset();
						crawlEn3_1(u, bios);
					} catch (Exception e) {
						e.printStackTrace();
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
	void crawlEn3_1(URL url, ByteArrayOutputStream baos) throws IOException{
		check.incrementAndGet();
//		System.out.println(url);

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
		File dmp = new File( "d:/dfcf/all/" + p);
		//FIXME
		if(dmp.exists()){
//			System.out.println("skip " + dmp);
			return;
		}
		File dmp1 = newFile(base1, dmp);
		if(dmp1.exists() /**&& dmp.length() > 1000 **/){
//			System.out.println("skip " + dmp);
			return;
		}
		dmp = newFile(base2, dmp);
		if(dmp.exists()){
//			System.out.println("skip " + dmp);
			return;
		}
		has.addAndGet(1);
		{
			HttpCrawler crl = new HttpCrawler(url);
			crl.connect();
			
			GZIPOutputStream ops = new GZIPOutputStream(baos);
			crl.dumpTo(ops);
			ops.close();
		}
		byte[]bs = baos.toByteArray();
		FileOutputStream fos = new FileOutputStream(dmp);
		fos.write(bs);
		fos.close();
		System.out.println("got!" + dmp.getName() + "size " + dmp.length()/1000.0 + " KB, from " + dmp.getAbsolutePath());
//		Consoler.readString("waiting");
	}
	public static void main(String[] args) throws Exception {
		String threadsNum = Consoler.readString("threads:");
		int threads = Integer.parseInt(threadsNum.trim());

		Detailcrawl d = new Detailcrawl();
		d.load();
		System.out.println("load ok:" + d.queue.size());

		for(int x = 0; x < threads; x ++){
			d.crawlEnThread();
		}
	}
}
