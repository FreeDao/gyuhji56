package com.bmtech.utils.bmfs.tool;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.bmtech.utils.Consoler;
import com.bmtech.utils.bmfs.MDir;
import com.bmtech.utils.bmfs.MFile;
import com.bmtech.utils.bmfs.MFileReader;
import com.bmtech.utils.bmfs.MFileWriter;

public class MDirCopy {
	MDir to, from;

	MDirCopy() throws IOException {
		File fromFile, toFile;
		{
			String fromStr = Consoler.readString("from:");
			fromFile = new File(fromStr);
			if (!fromFile.exists() || !fromFile.isDirectory()) {
				System.out.println("fromDir unknown");
			}
		}
		{
			String toStr = Consoler.readString("  to:");
			toFile = new File(toStr);
			if (toFile.exists() && !toFile.isDirectory()) {
				System.out.println("toDir isDirectory unknown");
				return;
			}
		}
		to = MDir.makeMDir(toFile, true);
		from = MDir.makeMDir(fromFile, false);
	}

	public void read() throws Exception {
		MFileReader itr = from.openReader();
		MFileWriter w = to.openWriter();
		while (itr.hasNext()) {
			MFile mfile = itr.next();

			MFile add = to.createMFileIfPossible(mfile.name);
			if (add == null) {
				System.out.println("SKIP " + mfile);
				itr.skip();
			} else {
				InputStream ips = itr.getInputStream();
				System.out.println("adding " + mfile);
				w.openMFile(add).write(ips).close();
			}

		}
	}

	public static void main(String[] args) throws Exception {
		MDirCopy fc = new MDirCopy();
		fc.read();
	}

}
