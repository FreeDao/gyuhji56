package com.funtv.utils;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Looper;
import android.util.Log;

// Unified to catch the exception, saved to a file upload next open in the Application
public class HandlerError implements UncaughtExceptionHandler {
	private static final String TAG = "HandlerError";

	// Whether to open the log output, open the Debug state, turn off to
	// indicate program performance in Release state
	public static final boolean DEBUG = true;

	private String mCrashInfo;

	// The system default of UncaughtException of handler class
	private Thread.UncaughtExceptionHandler mDefaultHandler;

	// CrashHandler instance
	private static HandlerError mInstance;

	// Context object of the program
	private Context mContext;

	// Ensure that only one CrashHandler instance
	private HandlerError() {
	}

	// Get CrashHandler instance, single-case mode
	public static HandlerError getInstance() {
		if (mInstance == null) {
			mInstance = new HandlerError();
		}
		return mInstance;
	}

	// Initialized Context object for the system default UncaughtException
	// processors, the default processor set the CrashHandler for the program
	public void init(Context ctx) {
		mContext = ctx;
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	// When occurs UncaughtException transferred to the function to handle
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			// If the user does not have to deal with the system default
			// exception handler to handle
			mDefaultHandler.uncaughtException(thread, ex);
		} else { // Handle the exception, not the pop-up error dialog box, you
					// need to manually exit the app
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// android.os.Process.killProcess(android.os.Process.myPid());
			// System.exit(10);
		}
	}

	// Custom error handling, collection of error messages Send error reports
	// and other operations are complete. The developer can customize according
	// to their own exception handling logic
	// True, on behalf of the exception is no longer up throwing an exception,
	// False, the representative does not handle the exception (the log
	// information is stored) and then to the upper layer (to the exception
	// handling system) to deal with. It simply is true does not pop up that
	// error message box, false will pop up

	private boolean handleException(final Throwable ex) {
		if (ex == null) {
			return false;
		}
		Log.e(TAG, "HandlerError =" + ex.toString());

		try {
			mCrashInfo = getCrashInfo(ex);
			if (null != mContext) {
				mCrashInfo = mCrashInfo + "\n" + "DeviceType: " + DeviceInfoUtil.getDeviceModel() + " SDKVersion:"
						+ DeviceInfoUtil.getOSVersion()+" ClientVersion: "+DeviceInfoUtil.getAppVersionName()
						;
			}
			WriteCrashInfo2File();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// 获取详细的错误信息
	// @param ex
	// @return
	private String getCrashInfo(Throwable ex) {
		Writer writer = new StringWriter();
		String result = "";
		try {
			PrintWriter printWriter = new PrintWriter(writer);
			ex.printStackTrace(printWriter);
			Throwable cause = ex.getCause();
			if (cause != null) {
				cause.printStackTrace(printWriter);
				cause.getCause();
			}
			printWriter.close();
			result = writer.toString();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	// 将崩溃日志文件写入文件
	// @param stack
	private void WriteCrashInfo2File() {
		// Toast to display the exception information
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				try {
					// 获取崩溃日志信息名称
					String file = FileUtil.logFileName(Constans.CRASH_INFO);
					Log.e(TAG, "崩溃日志文件名称" + file);
					// 判断用户的SD卡是否存在，如果存在则存储日志的路径为SD卡内路径，如果不存在则写入手机内存
					if (FileUtil.isLogFolderExist()) {
						File filePath = new File(FileUtil.getLogFilePath(), file);
						if (filePath != null) {
							SharedPreferences sp = mContext
									.getSharedPreferences("reported_error", Context.MODE_PRIVATE);
							// 首次取值肯定为false，当下次启动上报时读取完数据后，将该值在置为false，这样每次进行写操作时会清除掉之前的崩溃日志.
							boolean flag = sp.getBoolean("error", false);
							String fileName = filePath.toString();
							FileUtil.WriteStringToFile(mCrashInfo, fileName, flag);
							SharedPreferences.Editor editor = sp.edit();
							editor.putBoolean("error", true);
							editor.commit();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				Looper.loop();
			}
		}.start();
	}

}
