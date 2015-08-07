package com.funtv.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import android.content.Context;
import android.os.Environment;
import android.util.Log;


public class FileUtil {

	private static final String TAG = "FileUitl";

	public static final String CACHE_ROOT_PATH = "/funtv";
	// SD 卡路径
	private static final String SDCARD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
	// 创建风行文件夹路径
	public static final String SAVE_FILE_PATH_DIRECTORY = SDCARD_PATH + CACHE_ROOT_PATH;

	private static final String LOG_SDCARD_PATH = FileUtil.SAVE_FILE_PATH_DIRECTORY + "/log";
	private static final String LOG_MEMORY_PATH = "/data/data/com.funtv/log";

	/**
	 * Determine whether there is a memory card, and returns TRUE, otherwise
	 * FALSE
	 *
	 * @return
	 */
	public static boolean isSDcardExist() {
		boolean isExist = false;
		try {
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				File sdFile = Environment.getExternalStorageDirectory();
				if(sdFile.canWrite()){
					isExist = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isExist;

	}

	/**
	 * 删除文件
	 *
	 * @param sPath
	 *            被删除文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public static boolean deleteFile(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}

	/**
	 * 读取Asset文件字符
	 */
	public static String readAssetFile(Context context, String name) {
		String result = "";
		InputStream inputStream = null;
		try {
			inputStream = context.getResources().getAssets().open(name);
			result = read(inputStream);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != inputStream) {
					inputStream.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 进行IO流读写
	 *
	 * @param inputStream
	 * @return oStream.toString() or “文件读写失败”
	 */
	public static String read(InputStream inputStream) {
		ByteArrayOutputStream oStream = null;
		try {
			oStream = new ByteArrayOutputStream();
			int length;
			while ((length = inputStream.read()) != -1) {
				oStream.write(length);
			}
			return oStream.toString("UTF-8");
		} catch (Exception e) {
			if (null != oStream) {
				try {
					oStream.close();
					oStream = null;
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			return "";
		}
	}

	/**
	 * Upload log to server
	 */
	public static boolean uploadFile(String srcPath, String uploadUrl) throws Exception {
		String BOUNDARY = "---------------------------7db1c523809b2";
		File file = new File(srcPath);
		if (file.length() <= 0)
			return true;

		String fileName = srcPath.replace(getLogFilePath() + "/", "");
		StringBuilder sb = new StringBuilder();
		sb.append("--" + BOUNDARY + "\r\n");
		sb.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + fileName + "\"" + "\r\n");
		sb.append("Content-Type: application/octet-stream" + "\r\n");
		sb.append("\r\n");

		byte[] before = sb.toString().getBytes("UTF-8");
		byte[] after = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("UTF-8");
		URL url = new URL(uploadUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
		conn.setRequestProperty("Content-Length", String.valueOf(before.length + file.length() + after.length));
		conn.setRequestProperty("HOST", "xxxxxxxxxx.net:80");
		conn.setDoOutput(true);
		OutputStream out = conn.getOutputStream();

		InputStream in = new FileInputStream(file);
		out.write(before);
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) != -1) {
			out.write(buf, 0, len);
		}
		out.write(after);
		in.close();
		out.close();
		return conn.getResponseCode() == 200;
	}

	/**
	 * zip file
	 *
	 * @param srcFileString
	 * @param zipFileString
	 * @throws Exception
	 */
	public static void zipFile(String srcFileString, String zipFileString) throws Exception {
		ZipOutputStream outZip = new ZipOutputStream(new FileOutputStream(zipFileString));
		ZipFiles(srcFileString, outZip);
		outZip.finish();
		outZip.close();
	}

	public static void zipFiles(String srcFileString[], String zipFileString) throws Exception {
		ZipOutputStream outZip = new ZipOutputStream(new FileOutputStream(zipFileString));
		for (String srcFile : srcFileString)
			ZipFiles(srcFile, outZip);
		outZip.finish();
		outZip.close();
	}

	/**
	 * zip file
	 *
	 * @param folderString
	 * @param fileString
	 * @param zipOutputSteam
	 * @throws Exception
	 */
	private static void ZipFiles(String srcFile, ZipOutputStream zipOutputSteam) throws Exception {
		if (zipOutputSteam == null)
			return;
		File file = new File(srcFile);
		if (file.isFile()) {
			ZipEntry zipEntry = new ZipEntry(file.getName());
			FileInputStream inputStream = new FileInputStream(file);
			zipOutputSteam.putNextEntry(zipEntry);
			int len;
			byte[] buffer = new byte[4096];
			while ((len = inputStream.read(buffer)) != -1) {
				zipOutputSteam.write(buffer, 0, len);
			}
			inputStream.close();
			zipOutputSteam.closeEntry();
		}
		if (file.exists()) {
			file.delete();
		}
	}

	/**
	 * 保存文件
	 *
	 * @param sToSave
	 * @param sFileName
	 * @param isAppend
	 * @return
	 */
	public static boolean WriteStringToFile(String content, String fileName, boolean isAppend) {
		boolean bFlag = false;
		final int iLen = content.length();
		final File file = new File(fileName);
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			final FileOutputStream fos = new FileOutputStream(file, isAppend);
			byte[] buffer = new byte[iLen];
			try {
				buffer = content.getBytes("UTF-8");
				fos.write(buffer);
				fos.flush();
				bFlag = true;
			} catch (IOException ioex) {
				Log.e(TAG, "Excetion : ioexception  at write string to file! ");
			} finally {
				fos.close();
			}
		} catch (Exception ex) {
			Log.e(TAG, "Exception : write string to file");
		}
		return bFlag;
	}

	/**
	 * Create log's name
	 *
	 * @return
	 */
	public static String logFileName(String type) {
		String mac = DeviceInfoUtil.getMacAddress(null);
		if (mac != null) {
			mac = mac.replace(":", "");
		}
		String zipName = type + DeviceInfoUtil.getAppVersionName() + "_" + mac + "_" + 00 + "_" + 0 + ".log";
		return zipName;
	}

	public static boolean isLogFolderExist() {
		boolean ret = false;
		File file = new File(getLogFilePath());
		if (file.exists()) {
			ret = true;
		} else {
			ret = file.mkdir();
		}
		return ret;
	}

	public static String getLogFilePath() {
		String logPath = "";
		if (FileUtil.isSDcardExist()) {
			logPath = LOG_SDCARD_PATH;
		} else {
			logPath = LOG_MEMORY_PATH;
		}
		return logPath;
	}

	public static String getAvailablePath(Context context) {
		String path = "";
		File cacheDir = null;
		if (null != context) {
			if (isSDcardExist()) {
				cacheDir = new File(SAVE_FILE_PATH_DIRECTORY);
				if(null != cacheDir && !cacheDir.exists()){
					cacheDir.mkdir();
				}
			} else {
				cacheDir = context.getCacheDir();
			}
			if(null != cacheDir){
				path = cacheDir.getPath();
			}
		}
		return path;
	}

}
