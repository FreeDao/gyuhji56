package com.funtv.webkit;

import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.funtv.utils.LogUtil;

public class FunWebChromeClient extends WebChromeClient{

	public static final String TAG = "FunWebChromeClient";
	
	@Override
	public void onProgressChanged(WebView view, int newProgress) {
		LogUtil.i(TAG, "onProgressChanged() newProgress=" + newProgress);
		super.onProgressChanged(view, newProgress);
	}

	@Override
	public void onReceivedTitle(WebView view, String title) {
		super.onReceivedTitle(view, title);
	}

	@Override
	public void onReceivedTouchIconUrl(WebView view, String url, boolean precomposed) {
		super.onReceivedTouchIconUrl(view, url, precomposed);
	}

	@Override
	public void onRequestFocus(WebView view) {
		super.onRequestFocus(view);
	}

	@Override
	public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
		if(null != consoleMessage){
			LogUtil.i(TAG, "onConsoleMessage():" + (
					consoleMessage.message() + ", lineNumber = " +  consoleMessage.lineNumber() + 
					", sourceId = " + 
	                consoleMessage.sourceId()));
		}
		return super.onConsoleMessage(consoleMessage);
	}

	
}
