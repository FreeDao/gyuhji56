package dfcf;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import textMatcher.Pattern.tools.LineTool;

import com.bmtech.htmls.parser.NodeFilter;
import com.bmtech.htmls.parser.Parser;
import com.bmtech.htmls.parser.filters.HasAttributeFilter;
import com.bmtech.htmls.parser.util.NodeList;
import com.bmtech.htmls.parser.util.ParserException;
import com.bmtech.utils.Consoler;
import com.bmtech.utils.HtmlRemover;
import com.bmtech.utils.LessTags;
import com.bmtech.utils.bmfs.MDir;
import com.bmtech.utils.bmfs.MFile;
import com.bmtech.utils.bmfs.MFileReader;
import com.bmtech.utils.counter.Counter;
import com.bmtech.utils.counter.NumCount;
import com.bmtech.utils.http.HttpCrawler;
import com.bmtech.utils.io.FileGet;
import com.bmtech.utils.io.LineWriter;

public class parse {
	Set<String>set = new HashSet<String>();
	Counter<String>c = new Counter<String>();
	LineWriter lw;
	parse() throws IOException{
		lw = new LineWriter("/sent.txt", false);
	}
	void putLine(String line) throws IOException{
		line = LineTool.lineFormat3(line);
		String[]xxx = line.split(" ");
		
		for(String x : xxx){
			x = LineTool.lineFormat3(x);
			c.count(x);
			if(x.length() < 2){
				continue;
			}
			if(set.contains(x)){
				continue;
			}
			set.add(x);
			
			lw.writeLine(x);
		}
	}
	NodeFilter flt = new HasAttributeFilter("class", "zwlitext");
	ByteArrayOutputStream ops = new ByteArrayOutputStream();
	byte[] buf = new byte[1024 * 40];
	String getText(InputStream ips) throws IOException{
		ops.reset();
		while(true){
			int readed = ips.read(buf);
			if(readed == -1){
				break;
			}
			ops.write(buf, 0, readed);
		}
		byte[]xx = ops.toByteArray();
		String x = new String(xx, "utf-8");
		return x;

	}
	void extract(InputStream ips) throws Exception{
		String html = getText(ips);
		Parser p = new Parser(html);
		NodeList nl = p.extractAllNodesThatMatch(flt);
		for(int x = 0; x < nl.size(); x ++){
			String htmlBlock = nl.toHtml();
			String txt = HtmlRemover.htmlToLineFormat(htmlBlock).lines.trim();
//			if(txt.length() > 140){
//				continue;
//			}
			putLine(txt);
//			System.out.println(x + "------------>" + txt );
		}
//		System.out.println("------------------------>------------>------------>------------>------------>------------>");
	}
	public static void main(String[]args) throws IOException, Exception{
		String toStr = Consoler.readString("readFrom:");
		File toFile = new File(toStr);
		if(toFile.exists() && !toFile.isDirectory()){
			System.out.println("toDir isDirectory unknown");
			return;
		}
		MDir mdir = new MDir(toFile, false);
		MFileReader reader = mdir.openReader();
		parse p = new parse();
		int x = 1300000;
		while(reader.hasNext()){
			MFile mfile = reader.next();
			InputStream ips = reader.getInputStreamUnGZiped();
			p.extract(ips);
			--x;
			if(x < 0){
				break;
			}
			if(x % 100 == 0){
				System.out.println(x);
			}
		}
		List<Entry<String, NumCount>> lst = p.c.topEntry(1000);
		for(Entry<String, NumCount> e : lst){
			System.out.println(e);
		}
		p.lw.close();
	}
}
