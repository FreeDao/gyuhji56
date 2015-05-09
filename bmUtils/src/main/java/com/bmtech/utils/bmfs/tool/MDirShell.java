package com.bmtech.utils.bmfs.tool;

import java.io.File;
import java.nio.charset.Charset;

import com.bmtech.utils.Charsets;
import com.bmtech.utils.Consoler;
import com.bmtech.utils.bmfs.MDir;
import com.bmtech.utils.bmfs.MFileReader;
import com.bmtech.utils.bmfs.MFileReaderIterator;

public class MDirShell {
	public static void main(String[] args) throws Exception {
		String path = Consoler.readString("mdir path: ");

		File dir = new File(path);
		MDir mdir = MDir.makeMDir(dir, false);
		MFileReaderIterator reader = mdir.openReader();
		while (reader.hasNext()) {
			MFileReader r = reader.next();
			System.out.println(r.getMfile());
			// InputStream ips = r.getInputStreamUnGZiped();//
			// r.getInputStream();
			// ByteArrayOutputStream baos = new ByteArrayOutputStream();
			// while (true) {
			// int x = ips.read();
			// if (x == -1) {
			// break;
			// }
			// baos.write(x);
			// }
			// byte[] bs = baos.toByteArray();
			byte[] bs = r.getBytesUnGZiped();
			Charset xx = Charsets.getCharset(bs, true);
			System.out.println(xx);
			String str = new String(bs, xx);
			System.out.println(str.trim());
			Consoler.readString("enter~>");
		}
		System.out.println("size:" + mdir.size());

	}
}
