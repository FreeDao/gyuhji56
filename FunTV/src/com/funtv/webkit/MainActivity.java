package com.funtv.webkit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fun.tv.R;
import com.funtv.utils.Constans;
import com.funtv.utils.DeviceInfoUtil;
import com.funtv.utils.LogUtil;
import com.funtv.utils.StringUtil;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;

public class MainActivity extends Activity implements  android.view.View.OnClickListener{

	private static final String TAG = "MainActivity";

	private FunWebview mFunWebview;

	private FunWebViewClient mFunWebViewClient;



	private WebMediaBroadCast mWebMediaBroadCast;


	private static final int LOADING_DELAY = 500;

//	private static final String SERVER_IP = "ott-srv1.fun.tv";
//	private static final String SERVER_IP = "ott-alpha.fun.tv";
//	private static final String SERVER_IP = "ott-srv.fun.tv";
//	private static final String SERVER_IP = "192.168.136.243";

	private static final String EXTRA_AS_LIB = "com.fun.tv.extra.aslib";
	
	private Animation mAnimation;

	private LinearLayout mErrorLayout;

	private ImageView mImageViewLoading;

	private Button mRetryBtn;

	private Button mBackBtn;

	private String mUrl;
	
	private boolean mIsLib;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		MobclickAgent.updateOnlineConfig(getApplicationContext());
//		AnalyticsConfig.setChannel(DeviceInfoUtil.getChannelNumber());
		MobclickAgent.setDebugMode(true);
		init();
	}


	public void showTip() {
		mBackBtn.setFocusable(true);
		mRetryBtn.setFocusable(true);
		mErrorLayout.setFocusable(true);
		mImageViewLoading.clearAnimation();
		mImageViewLoading.setVisibility(View.GONE);

		mErrorLayout.setVisibility(View.VISIBLE);
		mRetryBtn.requestFocus();
		if(null != mFunWebview){
			mFunWebview.loadUrl("about:blank");
		}
	}

	private void init() {
		initWeb();
		ViewStub viewStub = (ViewStub) findViewById(R.id.main_activity_error_viewstub);
		viewStub.inflate();
		mErrorLayout = (LinearLayout)findViewById(R.id.no_net_layout);
		mImageViewLoading = (ImageView) findViewById(R.id.loading);

		mBackBtn = (Button) findViewById(R.id.no_net_back_btn);
		mRetryBtn = (Button) findViewById(R.id.no_net_retry_btn);

		//注意，这里弹出的提示框，右边按钮显示"退出"
		mBackBtn.setText(R.string.exit);
		mBackBtn.setOnClickListener(this);
		mRetryBtn.setOnClickListener(this);

		mAnimation = AnimationUtils.loadAnimation(this, R.anim.buffer_rotate);
		LinearInterpolator lin = new LinearInterpolator();
		mAnimation.setInterpolator(lin);
	}

	private void initWeb() {
		mFunWebview = (FunWebview)findViewById(R.id.main_webView);
		mFunWebview.addJavascriptInterface(new WebViewJSInterface(this,mFunWebview),"JSUtil");
		mFunWebview.setWebChromeClient(new FunWebChromeClient());
		mFunWebViewClient = new FunWebViewClient(this);
		mFunWebview.setWebViewClient(mFunWebViewClient);
		mFunWebview.loadUrl(INDEX_URL);

	}

	@Override
	protected void onStart() {
		super.onStart();
	}


	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		mUrl = null;
		mIsLib = false;
		Intent intent = getIntent();
		if(null != intent){
			if(null != intent.getData()){
				mUrl = intent.getDataString();
			}
			mIsLib = intent.getBooleanExtra(EXTRA_AS_LIB, false);			
		}
		if(null != mFunWebview){
			if(TextUtils.isEmpty(mUrl)){
//				mFunWebview.loadUrl("javascript:OTT.exportsToApp.updatePlayStatus()");
//				mFunWebview.loadUrl("javascript:OTT.exportsToApp.loadingFun('hidden')");
			}
		}
		LogUtil.i("after javascript:OTT.exportsToApp.loadingFun('hidden')");
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		LogUtil.e(TAG, "-----onDestroy()----");
		super.onDestroy();
		unregisterReceiver();
		if(null != mFunWebview){
			mFunWebview.removeAllViews();
			mFunWebview.destroy();
			mFunWebview = null;
		}
	}


	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		Log.i(TAG, "main activity keyup: keyCode" + keyCode + ", event=" + event);
		return super.onKeyUp(keyCode, event);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		LogUtil.i(TAG, "onKeyDown() keyCode=" + keyCode);
		if(keyCode == KeyEvent.KEYCODE_BACK){
//			if(mFunWebViewClient.isShowingErrorView()){
//				Log.i("error","showingErrorView");
//				return true;
//			}else
			if(null != mFunWebview && mFunWebview.canGoBack()){
				Log.i("error","can go back");
				mFunWebview.goBack();
			}else {
				if(!mIsLib){
					promptUserExit();
				} else{
					finish();
				} 
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void promptUserExit(){
		new AlertDialog.Builder(this)
		//.setTitle(this.getResources().getString(R.string.version_upgrade_title))
		.setMessage(this.getResources().getString(R.string.user_exit_tips))
		.setPositiveButton(this.getResources().getString(R.string.user_exit_ok), new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				arg0.dismiss();
				finish();
			}
		})
		.setNegativeButton(this.getResources().getString(R.string.user_exit_cancel), new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				arg0.dismiss();
			}
		})
		.show().getButton(AlertDialog.BUTTON_NEGATIVE).requestFocus();
	}



	private void unregisterReceiver() {
		if(null != mWebMediaBroadCast){
			unregisterReceiver(mWebMediaBroadCast);
		}

	}

	public class WebMediaBroadCast extends BroadcastReceiver {

		private static final String TAG = "WebMediaBroadCast";
		@Override
		public void onReceive(Context context, Intent intent) {
			if (null != context && null != intent) {
				String action = intent.getAction();
				String mediaUrl = intent.getStringExtra(Constans.RELATED_MEDIA_LINK_URL);
				LogUtil.e(TAG, "WebMediaBroadCast onReceive() mediaUrl=" + mediaUrl +" action = " + action);
				if(!StringUtil.isEmpty(action) && action.equals(Constans.RELATED_MEDIA_ACTIONG)){
					if (!StringUtil.isEmpty(mediaUrl) && null != mFunWebview) {
						mFunWebview.loadUrl(mediaUrl);
					}
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		
	}
}