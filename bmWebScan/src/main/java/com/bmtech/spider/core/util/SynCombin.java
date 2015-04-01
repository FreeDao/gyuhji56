package com.bmtech.spider.core.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;

import com.bmtech.utils.io.InputStreamCombin;

public class SynCombin {
	static final String headV0 = "cmbHeaderV0-";

	public static InputStreamCombin getCombin(URL url, InputStream ips)
			throws IOException {
		String headInfo = URLEncoder.encode(url.toString(), "utf-8");
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
