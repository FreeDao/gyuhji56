package rays.httpproxy.prob;

import java.io.File;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class TestToLoad {

	public static void main(String[] args) throws Exception {
		// ScriptEngineTest();
		// testXX();
		// enc("GT-MTczNDE0MjQ1Mw==-1423121366-gz-1058FEBD15FF31BE47822638370E8338");

		ScriptEngine engine = load(new File("jsTest/toload.js"));
		if (engine instanceof Invocable) {
			Invocable invoke = (Invocable) engine; // 调用merge方法，并传入两个参数
		} else {
			System.out.println("engine is not Invocable:" + engine);
		}
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
