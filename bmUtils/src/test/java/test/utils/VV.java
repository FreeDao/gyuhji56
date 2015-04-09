package test.utils;

import java.io.ByteArrayInputStream;
import java.io.File;

import com.bmtech.utils.bmfs.MDir;
import com.bmtech.utils.bmfs.MFileReader;
import com.bmtech.utils.bmfs.MFileReaderIterator;

public class VV {

	void xxx() throws Exception {
		File f = new File("R:\\新建文件夹\\hns.h\\hnscss.gov.cn");
		MDir dir = MDir.open(f);
		File f2 = new File(f.getParent(), f.getName() + "00");
		MDir dir2 = MDir.open4Write(f2);
		MFileReaderIterator itr = dir.openReader();
		while (itr.hasNext()) {
			MFileReader reader = itr.next();
			byte[] bs = reader.getBytes();
			dir2.addFile(reader.getMfile().getName(), new ByteArrayInputStream(
					bs), true);
		}
		dir2.close();
	}

	public static void main(String[] args) throws Exception {
		VV vv = new VV();
		vv.xxx();
	}
}
