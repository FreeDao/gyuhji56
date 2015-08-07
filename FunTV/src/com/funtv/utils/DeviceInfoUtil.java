package com.funtv.utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLEncoder;
import java.util.Enumeration;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.funtv.FunApplication;

public class DeviceInfoUtil {
	private final static String TAG = "DeviceInfoUtil";
	private final static String UNKNOWN_MAC_ADDRESS = "00:00:00:00:00:00";
	private final static String CPUINFO_PATH = "/proc/cpuinfo";
	private final static int BUFFER_SIZE = 8192;
	private final static int BASE_SIZE = 1024;
	private final static int TYPE_TOTAL = 1;
	private final static int TYPE_AVAIABLE = 2;
	private final static String DEVICE_CONFIG = "device_config";
	private final static String MAC_ADDRESS_KEY = "mac_address";
	private final static String CHANNEL_NUMBER_KEY = "channel_number";
	public final static String UNKNOWN_VERSION = "versionUnknown";
	private static String mDeviceModel = "";
	private static String mMacAddress = UNKNOWN_MAC_ADDRESS;
	public final static int DPI_DIVIDER = 320;
	public static final String KAIBOER = "rk31sdk";
	public static final String SMARTTVBOX = "elite-standard";
	private static final String INPHIC12 = "yunos_inphic_12";
	private static final String BESTV = "f20ref";
	/**
	 * Get android system version number
	 *
	 * @param mContext
	 * @return
	 */
	public static String getOSVersion() {
		return android.os.Build.VERSION.RELEASE;
	}

	public static String getDeviceModel() {
		if (TextUtils.isEmpty(mDeviceModel)) {
			try {
				mDeviceModel = URLEncoder.encode(android.os.Build.MODEL, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		return mDeviceModel;
	}

	private static void setCacheMacAddress(Context context, String macAddress) {
		if (context != null && !TextUtils.isEmpty(macAddress)) {
			SharedPreferences preference = context.getSharedPreferences(DEVICE_CONFIG, Context.MODE_PRIVATE);
			Editor editor = preference.edit();
			editor.putString(MAC_ADDRESS_KEY, macAddress);
			editor.commit();
		}
	}

	private static String getCacheMacAddress(Context context) {
		if (context == null) {
			return null;
		}
		SharedPreferences sharedPreferences = context.getSharedPreferences(DEVICE_CONFIG, Context.MODE_PRIVATE);
		return sharedPreferences.getString(MAC_ADDRESS_KEY, null);
	}

	public static String getMacAddress(Context context) {
		if (context == null) {
			context = FunApplication.getInstance();
		}

		if ((!UNKNOWN_MAC_ADDRESS.equals(mMacAddress))) {
			return mMacAddress;
		}
		try {
			NetworkInfo networkInfo =  NetWorkUtil.getNetworkInfo(context);
			if(null != networkInfo && networkInfo.isAvailable()){
				int netType = networkInfo.getType();
				if(ConnectivityManager.TYPE_WIFI == netType){
					WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
					WifiInfo info = wifi.getConnectionInfo();
					if (info != null && !TextUtils.isEmpty(info.getMacAddress())) {
						setCacheMacAddress(context, info.getMacAddress());
						return (mMacAddress = info.getMacAddress());
					}
				}else if(ConnectivityManager.TYPE_ETHERNET == netType){
					String  macAddress = getCurrentSysMac();
					if ((!UNKNOWN_MAC_ADDRESS.equals(macAddress))){
						setCacheMacAddress(context, macAddress);
						return (mMacAddress = macAddress);
					}
				}
			}
			if(UNKNOWN_MAC_ADDRESS.equals(mMacAddress)){
				String mac = getCacheMacAddress(context);
				if (!TextUtils.isEmpty(mac)) {
					return (mMacAddress = mac);
				} else {
					// "AF"标识来自android客户端随机生成的mac地址
						return mMacAddress;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return mMacAddress;
	}

	/**
	 * 获取当前系统连接网络的网卡的mac地址
	 * @return
	 */
	public static final String getCurrentSysMac() {
		String  macAddress = UNKNOWN_MAC_ADDRESS;
		byte[] mac = null;
		StringBuffer sb = new StringBuffer();
		try {
			Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
			while (netInterfaces.hasMoreElements()) {
				NetworkInterface ni = netInterfaces.nextElement();
				Enumeration<InetAddress> address = ni.getInetAddresses();
				while (address.hasMoreElements()) {
					InetAddress ip = address.nextElement();
					if (ip.isAnyLocalAddress() || !(ip instanceof Inet4Address) || ip.isLoopbackAddress())
						continue;
					if (ip.isSiteLocalAddress())
						mac = ni.getHardwareAddress();
					else if (!ip.isLinkLocalAddress()) {
						mac = ni.getHardwareAddress();
						break;
					}
				}
			}
			if(mac != null){
				for(int i=0 ;i<mac.length ;i++){
					sb.append(parseByte(mac[i]));
				}
				macAddress = sb.substring(0, sb.length()-1);
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}

		return macAddress;
	}

	private static String parseByte(byte b) {
		String s = "00" + Integer.toHexString(b)+":";
		return s.substring(s.length() - 3);
	}

	private static double getExternalMemory(int type) {
		double memorySize = 0;
		try {
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				File path = Environment.getExternalStorageDirectory();
				StatFs externaStat = new StatFs(path.getPath());
				long blockSize = externaStat.getBlockSize();
				long totalBlocks = (TYPE_TOTAL == type) ? externaStat.getBlockCount() : externaStat
						.getAvailableBlocks();
				memorySize = (blockSize * totalBlocks) / (BASE_SIZE * BASE_SIZE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return memorySize;
	}

	private static double getUsedMedmory() {
		double usedSize = 0;
		try {
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				String path = Environment.getExternalStorageDirectory().getPath();
				StatFs statFs = new StatFs(path);
				long blockSize = statFs.getBlockSize();
				long totalBlocks = statFs.getBlockCount();
				long availableBlocks = statFs.getAvailableBlocks();
				long usedBlocks = totalBlocks - availableBlocks;
				usedSize = (blockSize * usedBlocks) / (BASE_SIZE * BASE_SIZE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return usedSize;
	}

	/**
	 * Phone the SDcard external available memory (free space)
	 */
	public static double getAvailableExternalMemory() {
		return getExternalMemory(TYPE_AVAIABLE);
	}

	/**
	 * Get equipment available total memory (including mobile phone memory andthe SDcard Memory)
	 */
	public static double getDeviceAvailableMemory() {
		double totalDeviceAvailableMemory = 0;
		try {
			double mAvailableInternalMemory = getAvailableInternalMemory();
			double mAvailableExternalMemory = getAvailableExternalMemory();
			if (mAvailableInternalMemory == mAvailableExternalMemory) {
				totalDeviceAvailableMemory = mAvailableInternalMemory;
			} else {
				totalDeviceAvailableMemory = (mAvailableInternalMemory + mAvailableExternalMemory);
			}
			return totalDeviceAvailableMemory;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return totalDeviceAvailableMemory;
	}

	public static long getInstallAPPTime(){
		long installTime=0;
		try {
			if(installTime <= 0 && Build.VERSION.SDK_INT >= 9){
				PackageInfo pi = FunApplication.getInstance().getPackageManager().
						getPackageInfo(FunApplication.getInstance().getPackageName(), 0);
				installTime = pi.firstInstallTime/1000;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return installTime;
	}

	public static double getAvailableInternalMemory() {
		return getInternalMemory(TYPE_AVAIABLE);
	}

	public static String getAppVersionName() {
		String appVersion = "";
		try {
			Context context = FunApplication.getInstance();
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
			if (info != null && !TextUtils.isEmpty(info.versionName)) {
				appVersion = info.versionName;
			}
		} catch (Exception e) {
			e.printStackTrace();
			appVersion = UNKNOWN_VERSION;
		}
		return appVersion;
	}


	public static String getudid() {
		String fsudid = "";
		fsudid = SharedPrefenrenceUtil.getShareString(Constans.FSUDID);
		if (StringUtil.isEmpty(fsudid)) {
			try {
				fsudid = 
						getMacAddress(FunApplication.getInstance()) + "##" + getPhoneImei(FunApplication.getInstance()) + "##" +  getDeviceId();
			} catch (Exception e) {
				e.printStackTrace();
				return "fail." + Math.random();
			}
			SharedPrefenrenceUtil.saveShareString(Constans.FSUDID, fsudid);
		}
		return fsudid;
	}

	public static String getDeviceId() {
		String tmDevice = "";
		TelephonyManager tm = (TelephonyManager) FunApplication.getInstance().getSystemService(
				Context.TELEPHONY_SERVICE);
		if (tmDevice != null)
			tmDevice = tm.getDeviceId();
		return tmDevice;
	}


	private static double getInternalMemory(int type) {
		double memorySize = 0;
		try {
			File path = Environment.getDataDirectory();
			StatFs internalStat = new StatFs(path.getPath());
			long blockSize = internalStat.getBlockSize();
			long internalBlocks = (TYPE_TOTAL == type) ? internalStat.getBlockCount() : internalStat
					.getAvailableBlocks();
			memorySize = (blockSize * internalBlocks) / (BASE_SIZE * BASE_SIZE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return memorySize;
	}

	// Get total memory of the device (including mobile phone memory and
	// theSDcard Memory)

	public static double getDeviceMemory() {
		double totalDeviceMemory = 0;
		try {
			double mTotalInternalMemory = getTotalInternalMemory();
			double mTotalExternaMemory = getTotalExternaMemory();
			if (mTotalInternalMemory == mTotalExternaMemory) {
				totalDeviceMemory = mTotalInternalMemory;
			} else {
				totalDeviceMemory = (mTotalInternalMemory + mTotalExternaMemory);
			}
			return totalDeviceMemory;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return totalDeviceMemory;

	}
	public static String getPhoneImei(Context context) {
		String phoneImei = "";
		String UNKNOWN_IMEI = "ImeiUnknown";

		try {
			TelephonyManager telephonyManager = null;
			telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			if (null != telephonyManager) {
				phoneImei = telephonyManager.getDeviceId();
				if (phoneImei == null) {
					phoneImei = UNKNOWN_IMEI;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			phoneImei = UNKNOWN_IMEI;
			Log.i(TAG, "in the catch.");
		}
		return phoneImei;
	}

	public static String getDevice(String appType) {
		String device = appType + "_" + DeviceInfoUtil.getOSVersion() + "_" + DeviceInfoUtil.getDeviceModel();
		return device;
	}

	public static String getLocalIp() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address)) {
						if (inetAddress != null && inetAddress.getHostAddress() != null)
							return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static double getTotalInternalMemory() {
		return getInternalMemory(TYPE_TOTAL);
	}

	public static double getTotalExternaMemory() {
		return getExternalMemory(TYPE_TOTAL);
	}

	public static double getUsedMemory() {
		return getUsedMedmory();
	}

	public static boolean useSoftDecode(){
		return KAIBOER.equals(Build.DEVICE)
			|| SMARTTVBOX.equals(Build.DEVICE)
			|| BESTV.equals(Build.DEVICE);
	}
}
