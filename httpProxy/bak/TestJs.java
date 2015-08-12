//package rays.httpproxy;
//
//import java.io.File;
//import java.io.FileReader;
//import java.io.UnsupportedEncodingException;
//import java.net.URL;
//import java.net.URLEncoder;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.script.Bindings;
//import javax.script.Invocable;
//import javax.script.ScriptContext;
//import javax.script.ScriptEngine;
//import javax.script.ScriptEngineManager;
//import javax.script.ScriptException;
//
//import com.bmtech.utils.http.HttpCrawler;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//public class TestJs {
//
//	public static void main(String[] args) throws Exception {
//		// ScriptEngineTest();
//		// testXX();
//		// enc("GT-MTczNDE0MjQ1Mw==-1423121366-gz-1058FEBD15FF31BE47822638370E8338");
//
//		HttpCrawler crl = new HttpCrawler(new URL("http://www.baidu.com"));
//		crl.connect();
//		Map<String, List<String>> info = crl.getHeadInfo();
//		ObjectMapper mapper = new ObjectMapper();
//		Map<String, List<String>> n = copy(info);
//		String str = mapper.writeValueAsString(n);
//
//		System.out.println(str);
//		ScriptEngine engine = load(new File("TestMap.js"));
//		if (engine instanceof Invocable) {
//			Invocable invoke = (Invocable) engine; // 调用merge方法，并传入两个参数
//			Object obj = invoke.invokeFunction("testMap", n);
//			System.out.println(obj);
//		} else {
//			System.out.println("engine is not Invocable:" + engine);
//		}
//	}
//
//	public static ScriptEngine load(File f) throws Exception {
//		ScriptEngineManager manager = new ScriptEngineManager();
//		ScriptEngine engine = manager.getEngineByName("nashorn");
//
//		FileReader reader = new FileReader(f); // 执行指定脚本
//		engine.eval(reader);
//		reader.close();
//		return engine;
//	}
//
//	static Map<String, List<String>> copy(Map<String, List<String>> map) {
//		Map<String, List<String>> ret = new HashMap<String, List<String>>(map);
//		List<String> str = ret.remove(null);
//		if (str != null) {
//			ret.put("HTTPSTATUSCODE", str);
//		}
//		return ret;
//	}
//
//	public static void enc(String str) throws UnsupportedEncodingException {
//		System.out.println(str);
//		System.out.println(URLEncoder.encode(str, "utf-8"));
//	}
//
//	public static void ScriptEngineTest() {
//
//		ScriptEngineManager manager = new ScriptEngineManager();
//		ScriptEngine engine = manager.getEngineByName("javascript");
//		engine.put("a", 4);
//		engine.put("b", 3);
//		Bindings bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);
//		// System.out.println(bindings);
//		try {
//			// 只能为Double，使用Float和Integer会抛出异常
//			Double result = (Double) engine.eval("a+b");
//			System.out.println("result = " + result);
//			result = (Double) engine
//					.eval("abc=function(a,b,c){return a*b*c};a=3;b=9;c=9\nabc(a,b,c)");
//			System.out.println("resultx= " + result);
//			engine.eval("c=a+b");
//			Double c = (Double) engine.get("c");
//			System.out.println("c = " + c);
//
//		} catch (ScriptException e) {
//			e.printStackTrace();
//
//		}
//	}
//
//	public static void testXX() throws Exception {
//		ScriptEngineManager manager = new ScriptEngineManager();
//		ScriptEngine engine = manager.getEngineByName("javascript");
//
//		String jsFileName = "exp.js"; // 读取js文件
//
//		FileReader reader = new FileReader(jsFileName); // 执行指定脚本
//		Object obj = engine.eval(reader);
//		reader.close();
//		System.out.println("eva js:" + obj);
//		if (engine instanceof Invocable) {
//			Invocable invoke = (Invocable) engine; // 调用merge方法，并传入两个参数
//			Double c = (Double) invoke.invokeFunction("merge", 2, 3);
//			System.out.println("c = " + c);
//		} else {
//			System.out.println("engine is not Invocable:" + engine);
//		}
//
//		// reader.close();
//
//	}
//}
