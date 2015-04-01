package com.bmtech.utils.bmfs.tool;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

import com.bmtech.utils.Consoler;
import com.bmtech.utils.bmfs.MDir;
import com.bmtech.utils.bmfs.MFile;
import com.bmtech.utils.bmfs.MFileReader;

public class MDirShell {
	public static void main(String[] args) throws Exception {
		String path = Consoler.readString("mdir path: ");

		File dir = new File(path);
		MDir mdir = MDir.makeMDir(dir, false);
		MFileReader reader = mdir.openReader();
		while (reader.hasNext()) {
			MFile mfile = reader.next();
			System.out.println(mfile);
			InputStream ips = reader.getInputStreamUnGZiped();
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
