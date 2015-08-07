package com.funtv.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.funtv.FunApplication;

public class SharedPrefenrenceUtil {
	
    public static boolean getShareBoolean(String name) {
    	SharedPreferences share = FunApplication.getInstance().getSharedPreferences(name, Context.MODE_PRIVATE);
	    return share.getBoolean(name, false);
    }
    
    public static void saveShareBoolean (String name,boolean flag) {
    	SharedPreferences share = FunApplication.getInstance().getSharedPreferences(name, Context.MODE_PRIVATE);
    	Editor editor = share.edit();
		editor.putBoolean(name, flag);
		editor.commit();
    }
    
    public static int getShareInt(String name) {
    	SharedPreferences share = FunApplication.getInstance().getSharedPreferences(name, Context.MODE_PRIVATE);
	    return share.getInt(name, 0);
    }
    
    public static void saveShareInt (String name,int value) {
    	SharedPreferences share = FunApplication.getInstance().getSharedPreferences(name, Context.MODE_PRIVATE);
    	Editor editor = share.edit();
		editor.putInt(name, value);
		editor.commit();
    }
    
    public static String getShareString(String name) {
    	SharedPreferences share = FunApplication.getInstance().getSharedPreferences(name, Context.MODE_PRIVATE);
	    return share.getString(name, "");
    }
    
    public static void saveShareString (String name,String value) {
    	SharedPreferences share = FunApplication.getInstance().getSharedPreferences(name, Context.MODE_PRIVATE);
    	Editor editor = share.edit();
		editor.putString(name, value);
		editor.commit();
    }

}
