package test;

import java.io.File;
import java.nio.charset.Charset;

import com.bmtech.spider.core.util.SynCombin;
import com.bmtech.spider.core.util.SynCombin.DecodeSynCombin;
import com.bmtech.utils.Charsets;
import com.bmtech.utils.Consoler;
import com.bmtech.utils.bmfs.MDir;
import com.bmtech.utils.bmfs.MFile;
import com.bmtech.utils.bmfs.MFileReader;

public class KK {

	public static void main(String[] args) throws Exception {
		MDir dir = MDir.open(new File(
				"E:\\datas\\rl2ee\\okCrawled\\aqv.h\\aqvtc.cn"));
		MFileReader reader = dir.openReader();
		while (reader.hasNext()) {
			MFile mf = reader.next();

			System.out.println(mf);
			byte[] bs = reader.getBytes();
			Charset cs = Charsets.getCharset(bs, true);
			if (mf.toString().endsWith("2950012.html")) {
				Consoler.readString(cs.toString(), "");
			}
			String x = new String(bs, cs);

			try {
				DecodeSynCombin vv = SynCombin.parse(x);
			} catch (Exception e) {

				Consoler.readString(cs.toString());
				Consoler.readString(x.substring(0, 20));
				System.out.println(e);
				System.out.println(x);
			}

		}

	}
}
