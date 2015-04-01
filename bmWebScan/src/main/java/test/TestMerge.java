package test;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;

import com.bmtech.utils.Misc;
import com.bmtech.utils.io.LineWriter;
import com.bmtech.utils.io.diskMerge.MRTool;
import com.bmtech.utils.io.diskMerge.MRecord;

public class TestMerge {


	static File dir = new File("test/merge");

	static void genTest() throws IOException {
		File d = new File(dir, "1");
		Misc.del(d);
		d.mkdirs();
		int crt = 0;
		for (int x = 0; x < 2000; x++) {
			LineWriter lw = new LineWriter(new File(d, x + ".txt"), false);
			for (int y = 0; y < 10; y++) {
				lw.writeLine(++crt);
			}
			lw.close();
		}
	}

	public static void main(String[] args) throws Exception {
		genTest();
		MRTool.mergeTo(new File(dir, "r1.txt"), TestRecord.class,
				new Comparator<MRecord>() {

					@Override
					public int compare(MRecord o1, MRecord o2) {
						return ((TestRecord) o1).value
								- ((TestRecord) o2).value;
					}

				}, 64 * 1024, new File(dir, "1").listFiles());

	}
}
