package com.funtv.webkit;

import com.funtv.utils.LogUtil;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class LocalStorageDao {

	private final static String TAG = "PlayerHistoryDao";
	private FunSQLiteHelper mSqliteHelper;
	private static LocalStorageDao mInstance = null;

	private LocalStorageDao(Context context) {
		this.mSqliteHelper = FunSQLiteHelper.getInstance(context);
	}

	public static LocalStorageDao getInstance(Context context) {
		if (null == mInstance) {
			synchronized (LocalStorageDao.class) {
				if (null == mInstance) {
					mInstance = new LocalStorageDao(context);
				}
			}
		}
		return mInstance;
	}

	public synchronized String getItem(String key) {
		String value = "";
		if(key == null){
			return value;
		}

		SQLiteDatabase database = null;
		Cursor cursor = null;
		try {
			database = mSqliteHelper.getWritableDatabase();
			cursor = database.query(
					FunSQLiteHelper.LOCALSTORAGE_TABLE_NAME, null,
					FunSQLiteHelper.LOCALSTORAGE_ID + " = ?",
					new String[] { key }, null, null, null);
			if (cursor != null && cursor.moveToFirst()) {
				value = cursor.getString(1);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "when getItem for " + key);
		}finally{
			if(null != cursor){
				cursor.close();
			}
			if(null != database){
				database.close();
			}
		}
		if(value == null){//XXX null not premit
			value = "";
		}
		return value;
	}

	private String setSql = "insert or replace into " + FunSQLiteHelper.LOCALSTORAGE_TABLE_NAME + "(" + 
			FunSQLiteHelper.LOCALSTORAGE_ID + ","  + FunSQLiteHelper.LOCALSTORAGE_VALUE + ")values(?, ?)";

	public synchronized void setItem(String key, String value) {
		if(key == null){
			return;
		}
		if(value == null){
			value = "";
		}
		SQLiteDatabase database = null;
		try {//XXX use setSql
			LogUtil.i(TAG,"setItem() key="+ key +" value="+value);
			database = mSqliteHelper.getWritableDatabase();
			database.execSQL(setSql, new Object[]{
					key, value});

		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "when setItem for " + key + " to " + value);
		}finally{
			if(null != database){
				database.close();
			}
		}
	}

	public void removeItem(String key) {
		if(key == null){//shortcut for invalid key
			return;
		}
		SQLiteDatabase database = null;
		try {
			database = mSqliteHelper.getWritableDatabase();
			database.delete(FunSQLiteHelper.LOCALSTORAGE_TABLE_NAME,
					FunSQLiteHelper.LOCALSTORAGE_ID + "='" + key + "'", null);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "when removeItem setItem " + key);
		}finally{
			if(null != database){
				database.close();
			}
		}
	}

	public void clear() {
		SQLiteDatabase database = null;
		try {
			database = mSqliteHelper.getWritableDatabase();
			database.delete(FunSQLiteHelper.LOCALSTORAGE_TABLE_NAME, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(null != database){
				database.close();
			}
		}
	}
}
