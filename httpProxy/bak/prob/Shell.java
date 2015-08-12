package rays.httpproxy.prob;

import java.io.File;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class Shell {

	public static void main(String[] args) throws Exception {

		ScriptEngine engine = load(new File("jsTest/shellExe.js"));

	}

	public static ScriptEngine load(File f) throws Exception {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("nashorn");

		FileReader reader = new FileReader(f); // 执行指定脚本
		engine.eval(reader);
		reader.close();
		return engine;
	}

	public static void enc(String str) throws UnsupportedEncodingException {
		System.out.println(str);
		System.out.println(URLEncoder.encode(str, "utf-8"));
	}

}
