package com.funtv.webkit;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.funtv.utils.LogUtil;

public class FunSQLiteHelper  extends SQLiteOpenHelper{
	
	private final static String TAG = "FunSQLiteHelper";
	
	private static final int DATABASE_VERSION = 1;
	
	private static final String DATABASE_NAME = "fun.db";
	
	/**
	 * the name of the table 
	 */
	public static final String LOCALSTORAGE_TABLE_NAME = "local_storage_table";
	
	/**
	 * the id column of the table LOCALSTORAGE_TABLE_NAME
	 */
	public static final String LOCALSTORAGE_ID = "_id";
	
	/**
	 * the value column of the table LOCALSTORAGE_TABLE_NAME
	 */
	public static final String LOCALSTORAGE_VALUE = "value";
	
	private static final String DICTIONARY_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS " + LOCALSTORAGE_TABLE_NAME  
			+ " (" + LOCALSTORAGE_ID + " TEXT PRIMARY KEY, "
			+ LOCALSTORAGE_VALUE + " TEXT NOT NULL);";

	private static final String  CREATE_HISTORY_TABLE = "CREATE TABLE  IF NOT EXISTS `fsc_history` ("
			+ "`media_id` int(11) NOT NULL, "
			+ "`virtual_user` tinyint(4) NOT NULL default 0, "
			+ "`user_id` int(11) NOT NULL default '0', "
			+ "`name_cn` varchar(128) NOT NULL, "
			+ "`display_type` varchar(15) NOT NULL , "
			+ "`operation_time` int(11) NOT NULL , "
			+ "`play_time_in_seconds` int(11) NOT NULL default '0', "
			+ "`serial_id` int(11) NOT NULL , "
			+ "`serial_index` int(11) NOT NULL , "
			+ "`serial_title` varchar(128) NOT NULL , "
			+ "`serial_language` varchar(15) NOT NULL , "
			+ "`serial_clarity` varchar(15) NOT NULL, "
			+ "`ucs_status` tinyint(4) NOT NULL default '0', "
			+ "`max_index` int(11) NOT NULL, "
			+ "PRIMARY KEY (`media_id`) )";

	private String CREATE_FAVORITE_TABLE = "CREATE TABLE  IF NOT EXISTS `fsc_favorite` ( "
			+ "`media_id` int(11) NOT NULL, "
			+ "`virtual_user` tinyint(4) NOT NULL ,"
			+ "`user_id` int(11) NOT NULL default '0', "
			+ "`operation_time` int(11) NOT NULL, "
			+ "`name_cn` varchar(128) NOT NULL, "
			+ "`display_type` varchar(15) NOT NULL, "
			+ "`ucs_status` tinyint(4) default NULL, "
			+ "`max_index` int(11) NOT NULL, "
			+ "PRIMARY KEY (`media_id`) )";
	
	
	
	private static FunSQLiteHelper mInstance = null;
	
	
	public static FunSQLiteHelper getInstance (Context context) {
		if(null == mInstance ){
			synchronized(FunSQLiteHelper.class){
				if(null ==mInstance ){
					mInstance = new FunSQLiteHelper(context);
				}
			}
		}
		return mInstance;
	}
	
	public FunSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null,DATABASE_VERSION);
	}
	
	public FunSQLiteHelper(Context context, String name, CursorFactory factory, int version) {
		super(context,name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			db.execSQL(DICTIONARY_TABLE_CREATE);
			db.execSQL(CREATE_HISTORY_TABLE);
			db.execSQL(CREATE_FAVORITE_TABLE);
		} catch (Exception e) {
			LogUtil.i(TAG, "when onCreate, e=" + e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
