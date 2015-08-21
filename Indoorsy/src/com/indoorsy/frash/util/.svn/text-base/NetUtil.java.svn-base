package com.indoorsy.frash.util;

import java.util.Locale;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
/**
 * @author gll
 * 网络帮助�?
 */
public class NetUtil {
	private static final String TAG = NetUtil.class.getSimpleName();

	/** 网络类型定义 */
	public static final int NO_NET = -1;
	public static final int WIFI_NET = 1;
	public static final int CMWAP = 2;
	public static final int CMNET = 3;
	public static final int CTWAP = 4;
	public static final int CTNET = 5;
	public static final int UNKNOW_NET = 6;

	public static Uri PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");

	public static int checkNetType(Context context) {
		int netType = NO_NET;
		if(context != null){
				
			ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			Log.e(TAG, "connectivityManager:" + connectivityManager);
			if (connectivityManager == null) {
				return netType;
			}
	
			NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
			if (networkInfo == null || !networkInfo.isAvailable()) {
				return netType;
			}
	
			if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
				netType = WIFI_NET;
			} else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
				final Cursor c = context.getContentResolver().query(
						PREFERRED_APN_URI, null, null, null, null);
				if (c != null) {
					c.moveToFirst();
					final String user = c.getString(c.getColumnIndex("user"));
					if (!TextUtils.isEmpty(user)) {
						Log.i(TAG, "代理�?" + c.getString(c.getColumnIndex("proxy")));
						c.close();
						if (user.toUpperCase(Locale.getDefault()).startsWith(
								"CTWAP")) {
							Log.i(TAG, "电信wap网络");
							netType = CTWAP;
						} else if (user.toUpperCase(Locale.getDefault())
								.startsWith("CTNET")) {
							Log.i(TAG, "电信net网络");
							netType = CTNET;
						}
					}
				}
	
				String netMode = networkInfo.getExtraInfo();
				if (!TextUtils.isEmpty(netMode)) {
					netMode = netMode.toUpperCase(Locale.getDefault());
					if (netMode.startsWith("CMWAP")) {
						Log.i(TAG, "移动联�?�wap网络");
						netType = CMWAP;
					} else if (netMode.startsWith("CMNET")) {
						Log.i(TAG, "移动联�?�net网络");
						netType = CMWAP;
					} else {
						netType = UNKNOW_NET;
					}
				}
			}
		}

		return netType;
	}

	public static boolean isNetworkNotAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			Log.i("NetWorkState", "Unavailabel");
			return true;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						Log.i("NetWorkState", "Availabel");
						return false;
					}
				}
			}
		}
		return true;
	}

	@SuppressWarnings("deprecation")
	public static int getAndroidSDKVersion() {
		int version = 0;
		try {
			version = Integer.valueOf(android.os.Build.VERSION.SDK);
		} catch (NumberFormatException e) {

		}
		return version;
	}

	/**
	 * �?测网�?
	 * 
	 * @param context
	 * @return true为无网络，false为有网络
	 */
	public static boolean noNet(Context context) {
		if (getAndroidSDKVersion() > 18) {
			return isNetworkNotAvailable(context);
		} else {
			return checkNetType(context) == NO_NET;
		}
	}
	
	
}
