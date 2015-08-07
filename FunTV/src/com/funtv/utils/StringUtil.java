package com.funtv.utils;


import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.DisplayMetrics;

public class StringUtil {
	 
	public static boolean isEmpty(String str) {
		if (TextUtils.isEmpty(str) || (str != null && "".equals(str.trim())||(str != null && "null".equals(str)))) {
			return true;
		}
		return false;
	}
	
	public static int calcTextWidth(String text, float textSize, DisplayMetrics dm) {
		Paint paint= new Paint();
		paint.setTextSize(textSize * dm.density + 0.5f);
		Rect rect = new Rect();
		paint.getTextBounds(text, 0, text.length(), rect);
		return rect.width();
	}
	 
}
