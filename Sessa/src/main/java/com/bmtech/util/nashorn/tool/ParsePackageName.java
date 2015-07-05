package com.bmtech.util.nashorn.tool;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.bmtech.utils.Consoler;

public class ParsePackageName {
	public ArrayList<String> packageName = new ArrayList<String>();

	public void fileParse(File file) throws IOException {
		if (file.isDirectory()) {
			String name = file.getCanonicalPath();
			packageName.add(name);
			File[] fs = file.listFiles();
			for (File f : fs) {
				fileParse(f);
			}
		}
	}

	public static void main(String[] args) throws IOException {
		String xx = Consoler.readString(":");
		File base = new File(xx);
		ParsePackageName ppn = new ParsePackageName();
		ppn.fileParse(base);

		String replace = Consoler.readString("cut head like:");
		System.out.println("results:\n");
		for (String x : ppn.packageName) {
			String line = x.replace('\\', '/').replace("/", ".");
			if (line.startsWith(replace)) {
				line = line.substring(replace.length());
			}
			System.out.println(line + ",");
		}
	}
}
