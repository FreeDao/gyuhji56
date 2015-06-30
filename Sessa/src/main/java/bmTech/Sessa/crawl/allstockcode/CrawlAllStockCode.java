package bmTech.Sessa.crawl.allstockcode;

import java.io.IOException;
import java.net.URL;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

import com.bmtech.util.nashorn.NashornEngine;
import com.bmtech.utils.Charsets;
import com.bmtech.utils.bmfs.MDir;
import com.bmtech.utils.http.HttpCrawler;

public class CrawlAllStockCode {

	MDir fileSave;
	final String path = "http://hqdigi2.eastmoney.com/EM_Quote2010NumericApplication/index.aspx?type=s&sortType=C&sortRule=-1&pageSize=10000&page=1&jsName=quote_123&style=33&";

	// @See page "http://quote.eastmoney.com/center/list.html#33";

	public String crawl() throws IOException {

		HttpCrawler crl = HttpCrawler.makeCrawler(new URL(path));
		String str = crl.getString(Charsets.UTF8_CS);
		return str;

	}

	public static void main(String[] args) throws IOException, ScriptException {
		CrawlAllStockCode crawler = new CrawlAllStockCode();
		String str = crawler.crawl();
		System.out.println(str);
		ScriptEngine engine = NashornEngine.execute(str);
		engine.eval("var rank = quote_123.rank");
		Object obj = engine.get("rank");

		ScriptObjectMirror mirror = (ScriptObjectMirror) obj;
		System.out.println(mirror.get(1));
		System.out.println(mirror.get("length"));
	}
}
