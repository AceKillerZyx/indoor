package com.indoorsy.frash.util;

import java.util.HashMap;

import com.indoorsy.frash.util.SharedPreferencesUtil;
import com.indoorsy.frash.Constants;

import android.content.Context;
import android.util.Log;

public class CommonDataUtil {
	
	private static HashMap<String, String> commonParams = new HashMap<String, String>();
	
	/**
     * 获取请求公共参数
     * @param context
     * @return
     */
    public static HashMap<String, String> getCommonParams(Context context) {
        if (null != context) {
           commonParams.put(Constants.USID,String.valueOf(SharedPreferencesUtil.getUsid(context)));   //用户ID
           Log.e("CommonDataUtil","usid==" + SharedPreferencesUtil.getUsid(context));
        }
        return commonParams;
    }
}
