package com.bmtech.spider.core.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

import com.bmtech.utils.Charsets;
import com.bmtech.utils.io.InputStreamCombin;

public class SynCombin {
	public static final String headV0 = "cmbHeaderV0-";
	static final int XXLen = 6;

	public static DecodeSynCombin parse(String str) throws Exception {
		return new DecodeSynCombin(str);
	}

	public static class DecodeSynCombin {
		private String html;
		private URL url;

		private DecodeSynCombin(String str) throws Exception {

			if (!str.startsWith(SynCombin.headV0)) {
				this.setUrl(null);
				this.setHtml(str.trim());
			} else {
				String str2 = str.substring(headV0.length());
				String urlLen = str2.substring(0, XXLen);
				int len = Integer.parseInt(urlLen);
				str2 = str2.substring(XXLen);

				String url = URLDecoder.decode(str2.substring(0, len),
						Charsets.UTF8_STR);
				this.setUrl(new URL(url));
				this.setHtml(str2.substring(len).trim());
			}
		}

		public String getHtml() {
			return html;
		}

		public void setHtml(String html) {
			this.html = html;
		}

		public URL getUrl() {
			return url;
		}

		public void setUrl(URL url) {
			this.url = url;
		}
	}

	public static InputStreamCombin getCombin(URL url, InputStream ips)
			throws IOException {
		String headInfo = URLEncoder.encode(url.toString(), Charsets.UTF8_STR);
		int len = headInfo.length() + 1;
		String toWrite = String.format("%s%06d%s\n", headV0, len, headInfo);
		ByteArrayInputStream xx = new ByteArrayInputStream(toWrite.getBytes());
		return new InputStreamCombin(xx, ips);
	}

	public static void main(String[] args) throws IOException {
		String str = "C:\\Users\\Administrator\\Desktop\\http-proxy0\\http-proxy\\pom.xml";
		FileInputStream fis = new FileInputStream(new File(str));

		InputStreamCombin cmb = getCombin(new File(str).toURI().toURL(), fis);
		FileOutputStream ops = new FileOutputStream(new File("./sfas.txt"));
		byte[] bs = new byte[4096];
		while (true) {
			int x = cmb.read(bs);
			if (x == -1) {
				break;
			}
			ops.write(bs, 0, x);
		}
		ops.close();
	}
}
