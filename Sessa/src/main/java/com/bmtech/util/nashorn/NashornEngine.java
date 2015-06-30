package com.bmtech.util.nashorn;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class NashornEngine {
	public static ScriptEngine load(File f) throws Exception {
		FileReader reader = new FileReader(f); // 执行指定脚本
		try {
			ScriptEngine engine = load(reader);
			return engine;
		} finally {
			reader.close();
		}

	}

	public static ScriptEngine load(Reader reader) throws Exception {

		ScriptEngine engine = getEngine();
		engine.eval(reader);
		return engine;
	}

	public static ScriptEngine getEngine() {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("nashorn");
		return engine;
	}

	public static ScriptEngine execute(String str) throws ScriptException {
		ScriptEngine engine = getEngine();
		engine.eval(str);
		return engine;
	}
}
