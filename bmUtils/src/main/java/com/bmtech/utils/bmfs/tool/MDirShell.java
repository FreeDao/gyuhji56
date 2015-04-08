package com.bmtech.utils.bmfs.tool;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

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
			InputStream ips = r.getInputStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while (true) {
				int x = ips.read();
				if (x == -1) {
					break;
				}
				baos.write(x);
			}
			byte[] bs = baos.toByteArray();
			String str = new String(bs);
			System.out.println(str);
			Consoler.readString("enter~>");
		}
		System.out.println("size:" + mdir.size());

	}
}
