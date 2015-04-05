package se.crawlers;

import java.io.File;
import java.net.URL;

import com.bmtech.htmls.parser.Parser;
import com.bmtech.htmls.parser.filters.NodeClassFilter;
import com.bmtech.htmls.parser.tags.LinkTag;
import com.bmtech.htmls.parser.util.NodeList;
import com.bmtech.utils.Charsets;
import com.bmtech.utils.Consoler;
import com.bmtech.utils.bmfs.MDir;
import com.bmtech.utils.bmfs.MFile;
import com.bmtech.utils.bmfs.MFileReader;
import com.bmtech.utils.io.LineWriter;

public class UrlExt {

	int[] toParse;
	File saveTo;
	LineWriter lw;
	SeCrawlConfig conf;

	void parseAll() throws Exception {
		for (int x : toParse) {
			conf = new SeCrawlConfig(x);
			System.out.println("using " + conf.saveDir);
			MDir dir = MDir.open(conf.saveDir);
			parse(dir);
		}
		lw.flush();
	}

	void parse(MDir mdir) throws Exception {
		MFileReader reader = mdir.openReader();
		while (reader.hasNext()) {
			MFile mf = reader.next();
			System.out.println(mf);
			byte[] bytes = reader.getBytesUnGZiped();
			String str = new String(bytes, Charsets.UTF8_CS);
			Parser p = new Parser(str);
			NodeList nl = p.parse(new NodeClassFilter(LinkTag.class));
			for (int x = 0; x < nl.size(); x++) {
				LinkTag lt = (LinkTag) nl.elementAt(x);
				String link = lt.getLink();
				try {
					URL url = new URL(link);
					if (conf.exclude(url.getHost())) {
						continue;
					}
					lw.writeLine(url);
				} catch (Exception e) {
					// System.out.println("ERROR:" + link);
				}
			}
		}
	}

	UrlExt() throws Exception {
		int index = 0;
		String cmdDft = "0,2,3";
		for (String x : SeCrawlConfig.names) {
			System.out.println((index++) + "\t" + x);
		}
		String cmd = Consoler.readString("parse indexes", cmdDft);
		String[] cmds = cmd.split(",");
		toParse = new int[cmds.length];
		for (int x = 0; x < cmds.length; x++) {
			int y = Integer.parseInt(cmds[x].trim());
			System.out.println("accept " + SeCrawlConfig.names[y]);
			toParse[x] = y;
		}

		String saveTo = Consoler.readString("saveToFile: ");
		this.saveTo = new File(saveTo);
		if (this.saveTo.exists()) {
			throw new Exception("file already exists:"
					+ this.saveTo.getAbsolutePath());
		}
		lw = new LineWriter(saveTo, false);
	}

	public static void main(String[] args) throws Exception {
		UrlExt ext = new UrlExt();
		ext.parseAll();
	}
}
