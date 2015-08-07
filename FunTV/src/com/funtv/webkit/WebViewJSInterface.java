package com.funtv.webkit;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import android.content.Intent;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.funtv.player.PlayHistoryDao;
import com.funtv.utils.Constans;
import com.funtv.utils.DeviceInfoUtil;
import com.funtv.utils.LogUtil;
import com.funtv.utils.StringUtil;


/**
 * This class is used as a substitution of the local storage in Android webviews
 *
 * @author Diane
 */
public class WebViewJSInterface {

	private static final String TAG = "WebViewJSInterface";
	private Map<String, String> memCacheMap = new ConcurrentHashMap<String, String>();
	private final MainActivity mActivity;
	private final FunWebview webView;

	private LocalStorageDao mLocalStorageDao;
	private PlayHistoryDao mPlayHistoryDao;
	private DisplayMetrics mDisplayMetrics;

	private static final int PROP_DENSITY = 1;

	private static final int PROP_DENSITYDPI = 2;

	private static final int PROP_SCALEDDENSITY = 3;

	private static final int PROP_WIDTHPIXELS = 4;

	private static final int PROP_HEIGHTPIXELS = 5;

	private static final int PROP_XDPI = 6;

	private static final int PROP_YDPI = 7;

	private static final int PROP_BOARD = 8;

	private static final int PROP_BRAND = 9;

	private static final int PROP_CPU_ABI = 10;

	private static final int PROP_FINGERPRINT = 11;

	private static final int PROP_MANUFACTURER = 12;

	private static final int PROP_MODEL = 13;

	private static final int PROP_PRODUCT = 14;

	private static final int PROP_VERSION_RELEASE = 15;

	private static final int PROP_VERSION_SDK = 16;

	private static final int PROP_DEVICE_ID = 17;


	public WebViewJSInterface(MainActivity act, FunWebview webView) {
		mActivity = act;
		this.webView = webView;
		mDisplayMetrics = new DisplayMetrics();
		mActivity.getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
		mLocalStorageDao = LocalStorageDao.getInstance(mActivity.getApplicationContext());
		mPlayHistoryDao = PlayHistoryDao.getInstance(mActivity.getApplicationContext());
	}

	/**
	 * This method allows to get an item for the given key
	 *
	 * @param key
	 *            : the key to look for in the local storage
	 * @return the item having the given key
	 */
	@JavascriptInterface
	public String getItem(String key) {
		return mLocalStorageDao.getItem(key);
	}

	/**
	 * set the value for the given key, or create the set of datas if the key
	 * does not exist already.
	 *
	 * @param key
	 * @param value
	 */
	@JavascriptInterface
	public void setItem(String key, String value) {
		mLocalStorageDao.setItem(key, value);
	}

	/**
	 * removes the item corresponding to the given key
	 *
	 * @param key
	 */
	@JavascriptInterface
	public void removeItem(String key) {
		mLocalStorageDao.removeItem(key);
	}

	/**
	 * clears all the local storage.
	 */
	@JavascriptInterface
	public void clear() {
		mLocalStorageDao.clear();
	}

	@JavascriptInterface
	public synchronized boolean besureTableExists(String tableName, String createSql){
		return mPlayHistoryDao.besureTableExists(tableName, createSql);
	}

	public synchronized boolean execSQL(String sql, Object[] args){
		try {
			return mPlayHistoryDao.execSql(sql, args);
		} catch (Throwable e) {
			setToLastError(e);
			e.printStackTrace();
			return false;
		}
	}

	@JavascriptInterface
	public synchronized boolean execute(String sql, String args){
		try {
			return mPlayHistoryDao.executeSql(sql, args);
		} catch (Throwable e) {
			setToLastError(e);
			e.printStackTrace();
			return false;
		}
	}

	@JavascriptInterface
	public String testFunction() {
		try {
			mPlayHistoryDao.inject();
			return "OK";
		} catch (Throwable e) {
			setToLastError(e);
			return "Fail";
		}
	}

	/**
	 * if null return, error occur!
	 * @param sql
	 * @param args
	 * @return
	 * @throws Exception
	 */
	@JavascriptInterface
	public synchronized String query(String sql, String args) {
		try{
			return mPlayHistoryDao.query(sql, args);
		}catch(Throwable e){
			setToLastError(e);
			return null;
		}
	}

	private void setToLastError(Throwable e){
		try {
			if (LogUtil.isDEBUG())  {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				PrintStream ps = new PrintStream(bos, false, "UTF-8");
				e.printStackTrace(ps);
				LogUtil.e(TAG, new String(bos.toByteArray(), "UTF-8").replace("\n", "<br>"));
			}
		} catch (Exception e2) {

		}
	}

	/**
	 * get various of native property
	 */
	@JavascriptInterface
	public String getNativeProperty(String property) {
		String value = "";
		switch (Integer.parseInt(property)) {
		case PROP_DENSITY:
			value = Float.toString(mDisplayMetrics.density);
			break;
		case PROP_DENSITYDPI:
			value = Float.toString(mDisplayMetrics.densityDpi);
			break;
		case PROP_SCALEDDENSITY:
			value = Float.toString(mDisplayMetrics.scaledDensity);
			break;
		case PROP_WIDTHPIXELS:
			value = Float.toString(mDisplayMetrics.widthPixels);
			break;
		case PROP_HEIGHTPIXELS:
			value = Float.toString(mDisplayMetrics.heightPixels);
			break;
		case PROP_XDPI:
			value = Float.toString(mDisplayMetrics.xdpi);
			break;
		case PROP_YDPI:
			value = Float.toString(mDisplayMetrics.ydpi);
			break;
		case PROP_BOARD:
			value = Build.BOARD;
			break;
		case PROP_BRAND:
			value = Build.BRAND;
			break;
		case PROP_CPU_ABI:
			value = Build.CPU_ABI;
			break;
		case PROP_FINGERPRINT:
			value = Build.FINGERPRINT;
			break;
		case PROP_MANUFACTURER:
			value = Build.MANUFACTURER;
			break;
		case PROP_MODEL:
			value = Build.MODEL;
			break;
		case PROP_PRODUCT:
			value = Build.PRODUCT;
			break;
		case PROP_VERSION_RELEASE:
			value = Build.VERSION.RELEASE;
			break;
		case PROP_VERSION_SDK:
			value = Integer.toString(Build.VERSION.SDK_INT);
			break;
		case PROP_DEVICE_ID:
			value = DeviceInfoUtil.getudid();
			break;
		default:
			break;
		}
		return value;
	}


	@JavascriptInterface
	public String getMemItem(String key){
		return this.memCacheMap.get(key);
	}

	@JavascriptInterface
	public void setMemItem(String key, String value){
		this.memCacheMap.put(key, value);
	}

	@JavascriptInterface
	public void clearCache(){
		this.memCacheMap.clear();
	}

	/**
	 * js call this method force back to last page
	 */
	@JavascriptInterface
	public void jsNaviGoBack(){

		boolean canGoBack = webView.canGoBack();
		if(canGoBack){
			this.mActivity.runOnUiThread(new Runnable(){
				public void run(){
					if(!webView.tryGoBack()){//XXX can not goBack? strange
						mActivity.finish();
					};
				}
			});
		}else{
			this.mActivity.runOnUiThread(new Runnable(){
				public void run(){
					mActivity.finish();
				}
			});
		}
	}

	@JavascriptInterface
	public String jsGetEnv(){
		return this.webView.getEnvParas();
	}

	@JavascriptInterface
	public void jsSetEnv(String para){
		this.webView.setEnvParas(para);
	}

	@JavascriptInterface
	public String jsAllHistoryStack(){
		return this.webView.getHistoryStack().toString();
	}

	@JavascriptInterface
	public void jsManageGoBackEnable(){
		this.webView.webViewsetJsManageGoBackEnable(true);
	}

	@JavascriptInterface
	public void jsManageGoBackDisable(){
		this.webView.webViewsetJsManageGoBackEnable(false);
	}

	@JavascriptInterface
	public void log(String str){
		Log.i("jsLogUtil", str);
	}

	@JavascriptInterface
	public void jsToast(String toast){
		Toast.makeText(mActivity, toast, Toast.LENGTH_SHORT).show();
	}



	@JavascriptInterface
	public void jsStopPreloadTask(){
	}

	@JavascriptInterface
	public String setPageProperty(String key, String value){
		return this.webView.setPageProperty(key, value);
	}

	@JavascriptInterface
	public String getPageProperty(String key){
		return this.webView.getPageProperty(key);
	}

	@JavascriptInterface
	public String removePageProperty(String key){
		return this.webView.removePageProperty(key);
	}

	@JavascriptInterface
	public String getRefer(){
		return webView.history.getRefer();
	}

	@JavascriptInterface
	public String getVersion(){
		return DeviceInfoUtil.getAppVersionName();
	}

	@JavascriptInterface
	public String getFsudid(){
		return DeviceInfoUtil.getudid();
	}

	@JavascriptInterface
	public String getDevType(){
		return Constans.DEV_TYPE;
	}

	@JavascriptInterface
	public void reportUserBehavior(String userBehavior){
		//P2pHelper.getInstance(FunApplication.getInstance().getApplicationContext()).reportNetRequestToKernel(userBehavior);
	}





}

