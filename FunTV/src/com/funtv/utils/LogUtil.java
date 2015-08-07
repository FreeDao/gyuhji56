package com.funtv.utils;

import android.util.Log;

public class LogUtil {

	public static void i(String tag, String msg) {
		Log.i(tag, msg);
		
	}
	public static void w(String tag, String msg) {
		Log.w(tag, msg);
		
	}

	public static void e(String tag, String msg) {
		Log.e(tag, msg);
		
	}

	public static boolean isDEBUG() {
		return true;
	}

	public static void e(String msg) {
		Log.e("funTv", msg);
		
	}

	public static void w(String msg) {
		Log.w("funTv", msg);
	}

	public static void i(String msg) {
		Log.i("funTv", msg);
		
	}

}
