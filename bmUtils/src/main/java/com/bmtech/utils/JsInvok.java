package com.bmtech.utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class JsInvok {
	public static final String defaultFunction = "javaCall";

	public static Object invoke(File jsFile, Object... args) throws Exception {
		return invoke(jsFile, defaultFunction, args);
	}

	public static ScriptEngine loadScriptEngine(File jsFile)
			throws ScriptException, IOException {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("nashorn");

		FileReader reader = new FileReader(jsFile); // 执行指定脚本
		engine.eval(reader);
		reader.close();
		return engine;
	}

	public static Object invoke(File jsFile, String functionName,
			Object... args) throws ScriptException, IOException,
			NoSuchMethodException {
		ScriptEngine engine = loadScriptEngine(jsFile);
		Invocable invoke = (Invocable) engine; // 调用merge方法，并传入两个参数
		Object obj = invoke.invokeFunction(functionName, args);
		return obj;
	}

}
