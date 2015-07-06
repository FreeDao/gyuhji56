package dfcf;

import java.io.File;

public class Env {

	public static final File tmpFile = new File("d:/dfcf/");
	public static final File tmpFile2 = new File("i:/dfcf/");
	static{
		if(!tmpFile.exists()){
			tmpFile.mkdirs();
		}
		if(!tmpFile2.exists()){
			tmpFile.mkdirs();
		}
	}
}
