package com.funtv.webkit;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.funtv.player.entity.WebPageErrorInfo;
import com.funtv.utils.LogUtil;

public class FunWebViewClient extends WebViewClient{

	public static final String TAG = "FunWebViewClient";

	private Activity mActivity;


	private WebPageErrorInfo mWebPageErrorInfo;

	private long mLoadPageTime;

	public FunWebViewClient(Activity activity){
		this.mActivity = activity;
		this.mWebPageErrorInfo = new WebPageErrorInfo();
	}

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		if (!TextUtils.isEmpty(url)) {
			LogUtil.e(TAG, "shouldOverrideUrlLoading() url=" + url);
			if(url.toLowerCase().endsWith(".mp4")){
				Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(url), "video/mp4");
                mActivity.startActivity(intent);
                return true;
			}
			if (url.startsWith("fsp://")) {
//				Class<?> playerClass = FunVideoPlayerActivity.class;
//				P2PUtils.setP2pPlay(true);
//				Intent intent = new Intent(mActivity, playerClass);
//				intent.setData(Uri.parse(url));
////				intent.setData(Uri.parse("http://125.39.66.178:5050/livestream/0b49884b3b85f7ccddbe4e96e4ae2eae7a6dec56.m3u8?codec=ts"));
//				mActivity.startActivity(intent);
//				return true;
			} else if (url.startsWith("fhp://")) {
//				Class<?> playerClass = FunVideoPlayerActivity.class;
//				P2PUtils.setP2pPlay(false);
//				String source = url.replace("fhp:", "http:");
//				Intent intent = new Intent(mActivity, playerClass);
//				intent.setData(Uri.parse(source));
//				mActivity.startActivity(intent);
//				return true;
			} else {
				view.loadUrl(url);
				return true;
			}
		}
		return super.shouldOverrideUrlLoading(view, url);
	}

	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		LogUtil.i(TAG, "onPageStarted() url=" + url);
		mLoadPageTime = System.currentTimeMillis();
		super.onPageStarted(view, url, favicon);
	}

	@Override
	public void onPageFinished(WebView view, String url) {
		LogUtil.i(TAG, "onPageFinished() url=" + url);
		super.onPageFinished(view, url);
	}

	@Override
	public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
		LogUtil.i(TAG, "shouldInterceptRequest() url=" + url);
		return super.shouldInterceptRequest(view, url);
	}

	@Override
	public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
		LogUtil.e(TAG, "onReceivedError() errorCode=" + errorCode+" description=" + description + " failingUrl="+failingUrl);
		if(errorCode < 0){
			mWebPageErrorInfo.setLt(System.currentTimeMillis() - mLoadPageTime);
			mWebPageErrorInfo.setCode(errorCode);
			mWebPageErrorInfo.setDesc(description);
			mWebPageErrorInfo.setUrl(failingUrl);
			((MainActivity)mActivity).showTip();
		}
		super.onReceivedError(view, errorCode, description, failingUrl);
	}

	@Override
	public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
		if(null != event){
			LogUtil.e(TAG, "shouldOverrideKeyEvent() event=" + event.toString());
		}
		return super.shouldOverrideKeyEvent(view, event);
	}

//	private void initErrorView() {
//		if(null == mActivity){
//			return;
//		}
//		isFirstError = false;
//		ViewStub viewStub = (ViewStub) mActivity.findViewById(R.id.main_activity_error_viewstub);
//		if(viewStub != null){
//			viewStub.inflate();
//		}
//		mErrorLayout = (LinearLayout)mActivity.findViewById(R.id.no_net_layout);
//		TextView errorTip = (TextView) mActivity.findViewById(R.id.no_net_tip_tv);
//		errorTip.setText(mActivity.getString(R.string.load_webpage_error_tip));
//		mErrorLayout.setVisibility(View.VISIBLE);
//		Button exitBtn = (Button) mActivity.findViewById(R.id.no_net_back_btn);
//		Button retryBtn = (Button) mActivity.findViewById(R.id.no_net_retry_btn);
//		retryBtn.requestFocus();
//		exitBtn.setOnClickListener(this);
//		retryBtn.setOnClickListener(this);
//	}

//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.no_net_retry_btn :
//			if(null != mWebView && !StringUtil.isEmpty(mFailingUrl)){
//				mErrorLayout.setVisibility(View.GONE);
//				mWebView.loadUrl(mFailingUrl);
//				mWebView.setVisibility(View.VISIBLE);
//			}
//			break;
//		case R.id.no_net_back_btn :
//			if (mActivity != null){
//				mActivity.finish();
//			}
//			break;
//		default:
//			break;
//		}
//	}

//	public boolean isShowingErrorView(){
//		boolean isShow = false;
//		if(null != mErrorLayout){
//			isShow = mErrorLayout.isShown();
//			return isShow;
//		}
//		return isShow;
//	}

	public void doUpdateVisitedHistory(WebView view, String url,
			boolean isReload){
		LogUtil.i("doUpdateVisitedHistory view = " + view + ", url = " +
			url + ", isReload = " + isReload);
		if(view instanceof FunWebview){
			FunWebview fwv = (FunWebview) view;
			fwv.history.markUrlLoadFinished(url);
		}
	}


}

