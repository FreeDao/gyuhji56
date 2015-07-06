package baidu;

import java.io.File;
import java.io.IOException;
import java.net.URL;


import com.bmtech.htmls.parser.Parser;
import com.bmtech.htmls.parser.filters.HasAttributeFilter;
import com.bmtech.htmls.parser.filters.TagNameFilter;
import com.bmtech.htmls.parser.util.NodeList;
import com.bmtech.htmls.parser.util.ParserException;
import com.bmtech.utils.http.HttpCrawler;
import com.bmtech.utils.io.FileGet;
import com.bmtech.utils.io.LineWriter;

public class airi {

	public static void parse(File f) throws ParserException, IOException{
		String str = FileGet.getStr(f);
		Parser p = new Parser(str);
		NodeList ln = p.parse(null);
		NodeList nl2 = ln.extractAllNodesThatMatch(new HasAttributeFilter("class", "search_ci"), true);
		NodeList nl3 = nl2.extractAllNodesThatMatch(new TagNameFilter("a"), true);
		LineWriter lw = new LineWriter(new File(f.getParentFile(),"all.txt"), true);
		for(int x = 0; x < nl3.size(); x ++){
			String var = nl3.elementAt(x).toPlainTextString();
			lw.writeLine(var);
		}
		lw.close();
	}
	public static void main(String[]args) throws IOException, ParserException{
		File xx = new File("e:/airi");
		xx.mkdirs();
		for(File f : xx.listFiles()){
			parse(f);
		}
//		for(int x = 0; x < 26; x ++){
//			String url = "http://www.iresearch.cn/search_" +(char)('a' + x) + "/";
//			HttpCrawler crl = new HttpCrawler(
//					new URL(url));
//			crl.connect();
//			crl.dumpTo(new File(xx, "" + (char)('a' + x)) + ".html");
//		}
	}
}
