package rays.web.salon.conf;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.ibatis.io.Resources;

import com.bmtech.utils.KeyValuePair;
import com.bmtech.utils.io.ConfigReader;
import com.bmtech.utils.log.LogHelper;

public class ScoredDataDir {
	static final LogHelper log = new LogHelper("ScoredDataDir");

	public static List<File> listScoredDir() throws IOException {
		List<File> ret = new LinkedList<File>();
		File cfgFile = Resources.getResourceAsFile("scoredDataDir.conf");
		log.info("scoredDataDir config is %s", cfgFile);
		ConfigReader cr = new ConfigReader(cfgFile, "main");

		List<KeyValuePair<String, String>> cfg = cr.getAllConfig();
		for (KeyValuePair<String, String> pair : cfg) {
			log.info("got scoredDataDir: %s", pair);
			if (pair.key.startsWith("scored_")) {
				if (pair.value != null) {
					File f = new File(pair.value);
					if (f.exists()) {
						ret.add(f);
					} else {
						LogHelper.log
								.warn("skip scoredDataDir becouse file not exists %s:%s",
										pair.key, f.getAbsoluteFile());
					}
				}
			}

		}
		return ret;
	}

	/**
	 * 
	 * @param fileName
	 * @return File instance, if not found return null
	 * @throws IOException
	 */
	public static File getScoredFile(String fileName) throws IOException {
		List<File> files = listScoredDir();
		for (File f : files) {
			log.info("try find %s in dir %s", fileName, f);
			File tmp = new File(f, fileName);
			if (tmp.exists()) {
				return tmp;
			}
		}
		return null;
	}
}
