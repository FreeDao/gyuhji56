package com.bmtech.lexcion.ip202_114_40_175.port8080;

import com.bmtech.htmls.parser.Node;
import com.bmtech.htmls.parser.Parser;
import com.bmtech.htmls.parser.filters.HasAttributeFilter;
import com.bmtech.htmls.parser.util.NodeList;
import com.bmtech.utils.Charsets;
import com.bmtech.utils.HtmlRemover;
import com.bmtech.utils.ReadAllInputStream;
import com.bmtech.utils.ZipUnzip;
import com.bmtech.utils.bmfs.MDir;
import com.bmtech.utils.bmfs.MFile;
import com.bmtech.utils.bmfs.MFileReader;
import com.bmtech.utils.io.LineWriter;

public class Reader {
	static LineWriter lw;
	static class WordEntry{
		WordEntry(String name, String html) throws Exception{
			parse(html);
		}
		void parse(String html) throws Exception{
			Parser p = new Parser(html);
			HasAttributeFilter haf = new HasAttributeFilter("align", "right");
			NodeList nl = p.parse(haf);
			for(int x = 2; x < nl.size(); x ++){
				Node n = nl.elementAt(x).getParent();
				String str = HtmlRemover.htmlToLineFormat(n.getChildren()).lines;
				System.out.println(str);
				synchronized(lw){
					lw.writeLine(str);
				}
			}
		}
	}
	public static void main(String[]args) throws Exception{
		MDir mdir = new MDir(CrawlFirstPage.mdirFile, false);
		final MFileReader reader = mdir.openReader();
		lw = new LineWriter("/ulk-new", false);
		for(int x = 0; x < 4; x ++){
			Thread t = new Thread(){
				public void run(){
					byte[] buf ;
					MFile mfile;
					while(true){
						try{
							synchronized(reader){
								if(reader.hasNext()){
									mfile = reader.next();
									ReadAllInputStream ips = reader.getInputStream();
									buf = ips.readAll();
								}else{
									break;
								}
							}
							buf = ZipUnzip.unGzip(buf);
							String html = new String(buf, Charsets.UTF8_CS);
							new WordEntry(mfile.name, html);
						}catch(Exception e){
							throw new RuntimeException(e);
						}
					}
				}
			};
			t.start();

		}
		System.out.println("size:" + mdir.size());
		Runtime.getRuntime().addShutdownHook(new Thread(){
			public void run(){
				lw.close();
			}
		});
		
	}
}
