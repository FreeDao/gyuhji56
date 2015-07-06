package dfcf;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import com.bmtech.htmls.parser.Parser;
import com.bmtech.htmls.parser.filters.NodeClassFilter;
import com.bmtech.htmls.parser.tags.LinkTag;
import com.bmtech.htmls.parser.util.NodeList;
import com.bmtech.htmls.parser.util.ParserException;
import com.bmtech.utils.http.HttpCrawler;
import com.bmtech.utils.io.LineReader;
import com.bmtech.utils.io.LineWriter;

public class crawlEnter {
	protected static  void crawlOne() throws Exception{
		LineWriter lw = new LineWriter(new File(Env.tmpFile, "dfcf/urls.txt"), false);
		URL url = new URL("http://guba.eastmoney.com/remenba.aspx?type=1");
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

			if(!u.getHost().equals("guba.eastmoney.com")){
				continue;
			}
			String xx = u.toString();
			if(!xx.contains("list,")
					&& ! xx.contains("topic,")){
				continue;

			}
			System.out.println(xx);
			lw.writeLine(xx);
		}
		lw.close();
	}
	static void crawlEn2() throws IOException, ParserException{
		LineReader lr = new LineReader(new File(Env.tmpFile, "dfcf/urls.txt"));
		LineWriter lw = new LineWriter(new File(Env.tmpFile, "dfcf/urls2.txt"), true);

		while(lr.hasNext()){
			String lien = lr.next();
			try{
				crawlEn2_2(new URL(lien), lw);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		lw.close();
	}
	static void crawlEn2_2(URL url, LineWriter lw) throws IOException, ParserException{
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
			lw.writeLine(u);
		}
		lw.flush();
	}

	static void crawlEn3() throws IOException, ParserException{
		LineReader lr = new LineReader(new File(Env.tmpFile, "urls.txt"));
		while(lr.hasNext()){
			String line = lr.next();
			if(!line.contains("/news,")){
				continue;
			}
			try{
				URL u = new URL(line);
				if(!u.getHost().equals("guba.eastmoney.com")){
					continue;
				}
				crawlEn3_1(u);
			}catch(Exception e){
				e.printStackTrace();
			}
		}

	}
	static void crawlEn3_1(URL url) throws IOException{
		System.out.println(url);
		HttpCrawler crl = new HttpCrawler(url);
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
		dmp.getParentFile().mkdirs();
		if(dmp.exists() && dmp.length() > 1000){
			System.out.println("skip " + dmp);
			return;
		}
		crl.connect();
		crl.dumpTo(dmp);
		System.out.println("got!" + dmp.getName() + "size " + dmp.length()/1000.0 + " KB");
	}
	public static void main(String[] args) throws Exception {
		//		crawlEnter.crawlOne();
		//		crawlEn2();
		crawlEn3();
	}
}
