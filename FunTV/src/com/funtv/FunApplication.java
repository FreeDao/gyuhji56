package com.funtv;

import android.app.Application;
import android.util.DisplayMetrics;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.funtv.utils.BitmapCache;
import com.funtv.utils.HandlerError;

public class FunApplication extends Application {
	
	private static final String TAG = "FunApplication";
	
	private static FunApplication mInstance;
	
	private RequestQueue mQueue;
	
	private ImageLoader mImageLoader;

	public int mScreenWidthPixels = 0;
	
	public int mScreenHeightPixels = 0;

	@Override
	public void onCreate() {
		super.onCreate();
		setInstance(this);
		HandlerError crashHandler = HandlerError.getInstance();
		crashHandler.init(this);
		initVolley();
		initDisplaydpi();
	}

	private void initDisplaydpi() {
		String str = "";
		DisplayMetrics dm = new DisplayMetrics();
		dm = getApplicationContext().getResources().getDisplayMetrics();
		mScreenWidthPixels = dm.widthPixels;
		mScreenHeightPixels = dm.heightPixels;
		float density = dm.density;
		int densityDpi = dm.densityDpi;
		str += "Device Resolution:" + mScreenWidthPixels + " * " + mScreenHeightPixels + "\n";
		str += "Device density:" + String.valueOf(density) + "\n";
		str += "Device densityDpi:" + String.valueOf(densityDpi) + "\n";
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	public static FunApplication getInstance() {
		return mInstance;
	}

	private static void setInstance(FunApplication application) {
		mInstance = application;
	}

	private void initVolley() {
		mQueue = Volley.newRequestQueue(this);
		mImageLoader = new ImageLoader(mQueue, new BitmapCache());
	}

	public RequestQueue getVolleyQueue() {
		return mQueue;
	}

	public ImageLoader getImageLoader() {
		return mImageLoader;
	}
}
