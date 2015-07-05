package com.bmtech.util.nashorn;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map.Entry;

import com.bmtech.utils.Consoler;
import com.bmtech.utils.Misc;
import com.bmtech.utils.counter.Counter;
import com.bmtech.utils.counter.NumCount;
import com.bmtech.utils.io.FileGet;

public class ParsePackageName {
	public ArrayList<String> packageName = new ArrayList<String>();
	Counter<String> cnt = new Counter<String>();

	public void fileParse(File file) throws IOException {
		if (file.isDirectory()) {
			String name = file.getCanonicalPath();
			packageName.add(name);
			File[] fs = file.listFiles();
			for (File f : fs) {
				fileParse(f);
			}
		} else {
			String str = FileGet.getStr(file);
			String lines[] = str.split("\n");
			for (String x : lines) {
				String token = Misc.substring(x, "import ", ";");
				if (token != null) {
					if (token.startsWith("com.bmtech"))
						continue;

					int pos = token.lastIndexOf(".");
					if (pos > 0) {
						cnt.count(token.substring(0, pos));
					}
				}
			}
		}
	}

	public static void main(String[] args) throws IOException {
		String xx = Consoler.readString("source dir:");
		File base = new File(xx);
		ParsePackageName ppn = new ParsePackageName();
		ppn.fileParse(base);
		for (String x : ppn.packageName) {
			System.out.println(x);
		}
		System.out.println(" \n\n\n    top packages: ");
		for (Entry<String, NumCount> e : ppn.cnt.topEntry(100)) {
			System.out.println(e);
		}

	}
}
