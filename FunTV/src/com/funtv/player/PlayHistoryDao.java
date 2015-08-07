package com.funtv.player;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.alibaba.fastjson.JSON;
import com.funtv.player.entity.EpisodeInfo;
import com.funtv.player.entity.PlayerInfo;
import com.funtv.webkit.FunSQLiteHelper;

public class PlayHistoryDao {

	private final static String TAG = "PlayerHistoryDao";

	private FunSQLiteHelper mSqliteHelper;

	private static PlayHistoryDao mInstance = null;

	private PlayHistoryDao(Context context) {
		this.mSqliteHelper = FunSQLiteHelper.getInstance(context);
	}

	public static PlayHistoryDao getInstance(Context context) {
		if (null == mInstance) {
			synchronized (PlayHistoryDao.class) {
				if (null == mInstance) {
					mInstance = new PlayHistoryDao(context);
				}
			}
		}
		return mInstance;
	}

	protected String[] asStringItems(String columns) {

		Object[] o = this.asObjectItems(columns);
		if (o == null) {
			return null;
		}
		String ret[] = new String[o.length];
		for (int x = 0; x < o.length; x++) {
			ret[x] = o[x] == null ? null : o[x].toString();
		}
		return ret;
	}

	protected Object[] asObjectItems(String columns) {
		if (columns == null) {
			return null;
		}
		columns = columns.trim();
		if (columns.length() == 0) {
			return null;
		}
		List<Object> arr = JSON.parseArray(columns, Object.class);
		return arr.toArray(new Object[arr.size()]);
	}

	public synchronized boolean execSql(String sql, Object[] args) throws Throwable {
		SQLiteDatabase database = null;
		try {
			database = mSqliteHelper.getWritableDatabase();
			if (args == null) {
				args = new Object[0];
			}
			database.execSQL(sql, args);
		} finally {
			if (null != database) {
				database.close();
			}
		}
		return true;
	}

	public synchronized List<String[]> queryWithArgs(String sql, String[] vArgs) throws Throwable {
		Cursor cursor = null;
		SQLiteDatabase database = null;
		List<String[]> ret = new ArrayList<String[]>();
		try {
			database = mSqliteHelper.getWritableDatabase();
			cursor = database.rawQuery(sql, vArgs);
			String[] names = cursor.getColumnNames();
			ret.add(names);
			while (cursor.moveToNext()) {
				String[] record = new String[names.length];
				for (int x = 0; x < names.length; x++) {
					record[x] = cursor.getString(x);
				}
				ret.add(record);
			}

		} finally {
			try {
				if (null != cursor) {
					cursor.close();
				}
			} finally {
				database.close();
			}
		}
		return ret;
	}

	/**
	 * execute sql with args
	 *
	 * @param sql
	 *            the sql to execute, if sql is
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public synchronized boolean executeSql(String sql, String args) throws Throwable {
		Object[] oArgs = this.asObjectItems(args);
		return execSql(sql, oArgs);
	}

	/**
	 *
	 * @param sql
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public synchronized String query(String sql, String args) throws Throwable {
		String vArgs[] = asStringItems(args);
		List<String[]> ret = queryWithArgs(sql, vArgs);
		return JSON.toJSONString(ret);
	}

	public synchronized boolean besureTableExists(String tableName, String createSql) {
		try {
			List<String[]> ret = queryWithArgs("SELECT name FROM sqlite_master WHERE type='table' AND name=?", new String[] { tableName });
//			LogUtil.i("check table " + tableName + " if exsits, got as JSON: " + JSON.toJSONString(ret));
			if (ret.size() == 2) {
				return true;
			} else {
				executeSql(createSql, null);
			}
			return true;
		} catch (Throwable e) {
			return false;
		}
	}

	public void saveToSqlite(WatchHistory watchHistory) throws Throwable {
		SQLiteDatabase database = null;
		try {
			database = mSqliteHelper.getWritableDatabase();
			database.execSQL("insert or replace into fsc_history(" + "media_id, name_cn, display_type, max_index, "
					+ "virtual_user, user_id, " + "serial_id, serial_index, serial_title, serial_clarity, serial_language, "
					+ "play_time_in_seconds, operation_time)values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", new Object[] {
					watchHistory.mediaInfo.media_id, watchHistory.mediaInfo.name_cn, watchHistory.mediaInfo.display_type,
					watchHistory.mediaInfo.max_index,
					watchHistory.userInfo.virtual_user_id, watchHistory.userInfo.user_id,
					watchHistory.serialInfo.serial_id, watchHistory.serialInfo.serial_index, watchHistory.serialInfo.serial_title,
					watchHistory.serialInfo.serial_clarity, watchHistory.serialInfo.serial_language, watchHistory.play_time_in_seconds,
					System.currentTimeMillis() / 1000 });
		} finally {
			if (null != database) {
				database.close();
			}
		}
	}

	public void recordPlayHistory(PlayerInfo mPlayerData,String clarity, int episodePlayPosition,int playPosition,String userSelectedLanguage) {
//		try {
//			UserInfo ui = new UserInfo(0, 0);
//			int maxIndex = 0;
//			if (!mPlayerData.getIsall()) {
//				try {
//					maxIndex = Integer.parseInt(mPlayerData.getTorrents()[episodePlayPosition].getIndex());
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//			EpisodeInfo episodeInfo = mPlayerData.getTorrents()[episodePlayPosition];
//			MediaInfo mi = new MediaInfo(mPlayerData.getMid(), mPlayerData.getName_cn(), mPlayerData.getMtype(), maxIndex);
//			SerialInfo si = new SerialInfo(episodeInfo.getSerialid(), episodeInfo.getIndex(), episodeInfo.getTaskname(),
//					clarity, userSelectedLanguage);
//			WatchHistory watchHistory = new WatchHistory(mi, ui, si, playPosition);
//			saveToSqlite(watchHistory);
//			LogUtil.e(TAG,"recordPlayHistory() playPosition="+playPosition);
//		} catch (Throwable e) {
//			e.printStackTrace();
//		}
	}

	public void inject() throws Throwable {
//
//		{
//			UserInfo ui = new UserInfo(0, 0);
//			MediaInfo mi = new MediaInfo("93349", "尼基塔第一季", "tv", 0);
//			SerialInfo si = new SerialInfo("13754", "3", "第3集", "super_dvd", "ys");
//			WatchHistory wh = new WatchHistory(mi, ui, si, 2056);
//			saveToSqlite(wh);
//		}
//
//		{
//			UserInfo ui = new UserInfo(0, 0);
//			MediaInfo mi = new MediaInfo("97885", "肮脏电影", "movie", 0);
//			SerialInfo si = new SerialInfo("5282", "1", "肮脏电影", "super_dvd", "ys");
//			WatchHistory wh = new WatchHistory(mi, ui, si, 2056);
//			saveToSqlite(wh);
//		}
//		{
//			UserInfo ui = new UserInfo(0, 0);
//			MediaInfo mi = new MediaInfo("95283", "台湾Beautyleg真人秀", "variety", 309);
//			SerialInfo si = new SerialInfo("119226", "17", "第17集", "tv", "ys");
//			WatchHistory wh = new WatchHistory(mi, ui, si, 2056);
//			saveToSqlite(wh);
//		}
//		{
//			UserInfo ui = new UserInfo(0, 0);
//			MediaInfo mi = new MediaInfo("41299", "快乐大本营", "variety", 20131130);
//			SerialInfo si = new SerialInfo("371236", "20131019", "20131019(郑元畅现场展示洗杯子绝技)", "high_dvd", "ys");
//			WatchHistory wh = new WatchHistory(mi, ui, si, 2056);
//			saveToSqlite(wh);
//		}
	}
}
