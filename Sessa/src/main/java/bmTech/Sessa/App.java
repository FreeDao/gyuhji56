package bmTech.Sessa;

import java.io.File;
import java.io.IOException;

import com.bmtech.utils.Misc;

/**
 * Hello world!
 *
 */
public class App {

	public static final File sessaWorkDir;
	static {
		String sessaWorkDirStr = System.getProperty("sessa_work_dir");
		if (sessaWorkDirStr == null) {
			sessaWorkDirStr = "/Sessa";
		}
		try {
			sessaWorkDir = Misc.besureDirExists(sessaWorkDirStr);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}
}
