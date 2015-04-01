package test.utils;

import java.io.IOException;
import java.net.URL;

import com.bmtech.utils.http.HttpCrawler;
import com.bmtech.utils.http.HttpHandler;
import com.bmtech.utils.io.LineWriter;

public class XX {
	static HttpHandler hdl = HttpHandler.getCrawlHandler(true);

	public static void doFile(LineWriter lw, String str) throws IOException {
		System.out.println("\t" + str);
		String url = "http://pandavip.www.net.cn/check/check_ac1.cgi?domain="
				+ str
				+ "&callback=jQuery17107498727576806198_1419410180062&_=1419410193717";
		HttpCrawler crl = new HttpCrawler(new URL(url), hdl);
		crl.connect();
		str = crl.getString();

		System.out.println(str);
		lw.writeLine(str);
		lw.flush();
		crl.close();
	}

	static char c(int first) {
		char a;
		if (first < 26) {
			a = (char) ('a' + first);
		} else {
			a = (char) ('0' + first - 26);
		}
		return a;
	}

	static String ge(int first, int last) {
		char a = c(first), b = c(last);
		return "" + a + b;
	}

	public static void main(String[] args) throws IOException {
		LineWriter lw = new LineWriter("/domain.txt", true);
		String str;

		for (int x = 0; x < 26; x++) {
			for (int y = 0; y < 26; y++) {
				str = "" + ((char) ('a' + x)) + "" + ((char) ('a' + y))
						+ "fl.com";
				try {
					doFile(lw, str);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		lw.close();
	}
}
