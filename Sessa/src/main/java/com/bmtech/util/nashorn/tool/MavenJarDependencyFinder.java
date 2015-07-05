package com.bmtech.util.nashorn.tool;

import java.io.File;
import java.io.IOException;

import com.bmtech.utils.Consoler;
import com.bmtech.utils.Misc;
import com.bmtech.utils.io.FileGet;

public class MavenJarDependencyFinder {

	public static void main(String[] args) throws IOException {
		String m2BaseDirStr = Consoler.readString("m2 repository dir:");
		File m2BaseDir = new File(m2BaseDirStr);
		if (!m2BaseDir.exists()) {
			System.out.println("m2 repository is not exists!");
			return;
		}

		String dirStr = Consoler.readString("dir:");
		File dir = new File(dirStr);
		File clsPathFile = new File(dir, ".classpath");
		if (!clsPathFile.exists()) {
			System.out.println("file is not Exists:" + clsPathFile);
			return;

		}
		String[] str = new String(FileGet.getBytes(clsPathFile)).split("\n");
		for (String line : str) {
			// System.out.println(line);

			String sub = Misc.substring(line, "path=\"M2_REPO/", "\"");
			if (sub == null) {
				continue;
			}
			File jar = new File(m2BaseDir, sub);
			System.out.println(jar.getCanonicalPath());
		}
	}
}
