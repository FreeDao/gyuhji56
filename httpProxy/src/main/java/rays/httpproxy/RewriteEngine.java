package rays.httpproxy;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.bmtech.utils.Charsets;
import com.bmtech.utils.io.FileGet;

public class RewriteEngine {
	static final String getJs(File file) throws IOException{
		byte[] bs = FileGet.getBytes(file);
		String js = new String(bs, cs);
		return js;
	}
	static final Charset cs = Charsets.UTF8_CS;
	
	final ScriptEngine engine;
	Object rewriter;
	public RewriteEngine(File rewiteJsFile) throws ScriptException, IOException{

		ScriptEngineManager manager = new ScriptEngineManager();
		engine = manager.getEngineByName("nashorn");
		
		File baseImp = new File("config\\rewriters\\baseImp.js");
		
		String baseImpJs = getJs(baseImp);
		engine.eval(baseImpJs);
		
		String rewriteJs = getJs(rewiteJsFile);
		engine.eval(rewriteJs);
		
		rewriter = engine.get("rewriter");
		
	}
	
	public byte[] filter(String url, HttpHeader respHead, byte[] html) throws NoSuchMethodException, ScriptException{
		if (engine instanceof Invocable) {
			
			
			Invocable invoke = (Invocable) engine;
			Object obj = invoke.invokeMethod(rewriter, "filter", url, respHead, html);
			if(obj != null){
				html = (byte[]) obj;
			}
			return html;
		} else {
			throw new RuntimeException("engine is not Invocable:" + engine);
		}
	}
	
	public static RewriteEngine httpHeaderRewriter, requestRewriter,responseRewriter;
	static void init() throws ScriptException, IOException{
			httpHeaderRewriter = new RewriteEngine(new File("./config/rewriters/httpHeaderRewriter.js"));
			requestRewriter = new RewriteEngine(new File("./config/rewriters/requestRewriter.js"));
			responseRewriter = new RewriteEngine(new File("./config/rewriters/responseRewriter.js"));
	}
	public static void main(String[] args) throws ScriptException, IOException {
//		RewriteEngine re = new RewriteEngine(new File("./config/rewriters/httpHeaderRewriter.js"));
		init();
	}
}
