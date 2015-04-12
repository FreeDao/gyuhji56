package rays.httpproxy.prob;

import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.bmtech.utils.http.HttpCrawler;

public class Test {
	public static Object invockeJs(File js, Object... args) throws Exception {
		ScriptEngine engine = load(new File("jsTest/aaa.js"));
		Invocable invoke = (Invocable) engine;
		Object obj = invoke.invokeFunction("javaCall", args);
		return obj;
	}

	public static Object invockeJs(File js, String functionName, Object... args)
			throws Exception {
		ScriptEngine engine = load(new File("jsTest/aaa.js"));
		Invocable invoke = (Invocable) engine; // 调用merge方法，并传入两个参数
		Object obj = invoke.invokeFunction(functionName, args);
		return obj;
	}

	public static void main(String[] args) throws Exception {

		HttpCrawler crl = new HttpCrawler(new URL("http://www.baidu.com"));
		crl.connect();
		Map<String, List<String>> info = crl.getHeadInfo();
		Map<String, List<String>> infoNew = new HashMap<String, List<String>>(
				info);
		File jsFile = new File("jsTest/aaa.js");
		Object obj = invockeJs(jsFile, "testMap", infoNew);
		System.out.println(obj);
		System.out.println(obj == infoNew);

	}

	public static ScriptEngine load(File f) throws Exception {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("nashorn");

		FileReader reader = new FileReader(f); // 执行指定脚本
		engine.eval(reader);
		reader.close();
		return engine;
	}

}
