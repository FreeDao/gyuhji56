package com.funtv.webkit;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;

import com.fun.tv.R;
import com.funtv.utils.LogUtil;
import com.funtv.utils.PlayUtil;
import com.funtv.webkit.NaviHistory.HistoryStackInfo;

@SuppressLint("SetJavaScriptEnabled")
public class FunWebview extends WebView{

	private static final String TAG = "FunWebview";

	private Context mContext;

	NaviHistory history = new NaviHistory();

	public FunWebview(Context context) {
		super(context);
		/**if (Build.VERSION.SDK_INT >= 11){
		    setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		}**/
		this.mContext = context;
		attrs();
	}

	public FunWebview(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		attrs();
	}

	public FunWebview(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
		attrs();
	}


	static enum JsSensitiveKey{
		LEFT(37), UP(38), RIGHT(39), DOWN(40), ENTER(13), MENU(93);

		public final int keyCode;
		private JsSensitiveKey(int keyCode){
			this.keyCode = keyCode;
		}
	}

	//FIXME should be configurable
	private JsSensitiveKey vaildKeyCode(int keyCode){
		JsSensitiveKey valKey = null;

		switch(keyCode){
		case KeyEvent.KEYCODE_DPAD_UP:
			valKey = JsSensitiveKey.UP;
			break;

		case KeyEvent.KEYCODE_DPAD_DOWN:
			valKey = JsSensitiveKey.DOWN;
			break;

		case KeyEvent.KEYCODE_DPAD_LEFT:
			valKey = JsSensitiveKey.LEFT;
			break;

		case KeyEvent.KEYCODE_DPAD_RIGHT:
			valKey = JsSensitiveKey.RIGHT;
			break;

		case KeyEvent.KEYCODE_MENU:
			valKey = JsSensitiveKey.MENU;
			break;

		case KeyEvent.KEYCODE_DPAD_CENTER:
		case KeyEvent.KEYCODE_ENTER:
			valKey = JsSensitiveKey.ENTER;
			break;
		}
		return valKey;
	}
	private int toJsKeyCode(int keyCode) {
		JsSensitiveKey valKey = this.vaildKeyCode(keyCode);
		int toValue = 0;
		if (valKey == null)  {
			if(keyCode == KeyEvent.KEYCODE_HOME || keyCode == KeyEvent.KEYCODE_BACK){
				return -1;
			}else{
				toValue = 1000 + keyCode;
			}
		}else{
			toValue = valKey.keyCode;
		}
		return toValue;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		int valKey = this.toJsKeyCode(keyCode);
		Log.i(TAG, "onKeyDown() JsSensitiveKey, code ="  +
				keyCode + ", convert to " + valKey);
		if(null != mContext){
			PlayUtil.playTone(mContext, AudioManager.FX_KEY_CLICK);
		}
		if (valKey < 0)  {
			if(keyCode == KeyEvent.KEYCODE_BACK && null != this.history.getCurrent() &&
					this.history.getCurrent().jsManageGoBack){
				super.loadUrl("javascript:OTT.exportsToApp.goBackByJs('keydown')");
				return true;
			}
			return super.onKeyDown(keyCode, event);
		}

		super.loadUrl("javascript:OTT.exportsToApp.jsKeyEventInject('keydown', " + valKey + ")");
		return true;
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		int valKey = this.toJsKeyCode(keyCode);
		Log.i(TAG, "onKeyUp() JsSensitiveKey, code ="  +
				keyCode + ", convert to " + valKey);

		if (valKey < 0)  {
			if(keyCode == KeyEvent.KEYCODE_BACK && null != this.history.getCurrent() &&
					this.history.getCurrent().jsManageGoBack){
				super.loadUrl("javascript:OTT.exportsToApp.goBackByJs('keyup')");
				return true;
			}
			return super.onKeyUp(keyCode, event);
		}
		super.loadUrl("javascript:OTT.exportsToApp.jsKeyEventInject('keyup', " + valKey + ")");
		return true;
	}

	//	@SuppressLint("SetJavaScriptEnabled")
	private void attrs() {
		setBackgroundColor(0x00000000);
		loadSplashImage();

		WebSettings settings = this.getSettings();
		settings.setRenderPriority(RenderPriority.HIGH);
		settings.setCacheMode(WebSettings.LOAD_DEFAULT);
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		settings.setUseWideViewPort(true);// Initializes double-tap zoom control
		settings.setPluginState(PluginState.ON);

		settings.setGeolocationEnabled(true);
		settings.setJavaScriptEnabled(true);
		settings.setDomStorageEnabled(true);

		settings.setDatabaseEnabled(true);

		settings.setDatabasePath(mContext.getFilesDir().getParentFile().getPath()+"/databases/");

		setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		setHorizontalScrollBarEnabled(false);
		setVerticalScrollBarEnabled(false);
		setScrollbarFadingEnabled(false);

		//add here to try to resolve webview blank screen
		//setBackgroundColor(Color.parseColor("#919191"));
		if (Build.VERSION.SDK_INT >= 11) {
			setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
		}
	}

	public boolean canGoBack(){
		//maybe we should check homePage and let homePage can goBack
		//maybe should let use check if she wants to quit
		return this.history.canGoBack();
	}
	public boolean tryGoBack() {
		LogUtil.i(TAG, "calling history goBack()");
		HistoryStackInfo url = this.history.goBackHistory();
		if(url == null){
			Log.e(TAG, "forceGoBack to null");
			//FIXME maybe we should finish activity at this point ~_~
			//FIXME becouse we assume that history stack always contains at least one element

			LogUtil.i(TAG, "goBack got null url");
			return false;
		}
		this.clearHistory();

		Log.i(TAG, "goBack from historyItem:" + url);
		url.jsManageGoBack = false; //clear this flag
		super.loadUrl(url.url);
		return true;
	}

	public void goBack() {
		tryGoBack();
	}

	public void loadUrl(String url){
		LogUtil.i("loadUrl adding to history " + url);
		this.history.addVisitHistoryUrl(url);
		Map<String,String> extraHeaders = new HashMap<String, String>();
		String keyName = "mailAuthKey";
		String key = LocalStorageDao.getInstance(mContext).getItem(keyName)  + "";
		LogUtil.i(keyName + " is '" +  key + "' for url " + url);
	    extraHeaders.put(keyName, key);
		super.loadUrl(url, extraHeaders);
	}

	public String getHistoryStack() {
		return this.history.toString();
	}

	public void webViewsetJsManageGoBackEnable(boolean b) {
		HistoryStackInfo hi = this.history.getCurrent();
		Log.i(TAG, "set jsManageManageGoBackEnable " + b + " for " + hi);
		if(null != hi){
			hi.jsManageGoBack = b;
		}
	}

	public String getEnvParas() {
		HistoryStackInfo hi = this.history.getCurrent();
		if(null != hi){
			return hi.getEnvParas();
		}
		return "";
	}

	public void setEnvParas(String envParas) {
		HistoryStackInfo hi = this.history.getCurrent();
		if(null != hi){
			hi.setEnvParas(envParas);
		}
	}

	public void loadSplashImage(){
		setBackgroundResource(R.drawable.splash);
	}

	public String setPageProperty(String key, String value){
		return this.history.setPageProperty(key, value);
	}

	public String getPageProperty(String key){
		return this.history.getPageProperty(key);
	}

	public String removePageProperty(String key){
		return this.history.removePageProperty(key);
	}
}

