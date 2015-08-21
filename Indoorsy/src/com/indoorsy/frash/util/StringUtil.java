package com.indoorsy.frash.util;

import cn.sharesdk.framework.Platform;

/**
 * @author gll
 * 字符串帮助类
 */
public class StringUtil {
	public static final String G = "-";

	public static final String AND = "&";

	public static final String EMPTY = "";

	public static final String COLON = ":";

	public static final String SEMICOLON = ";";

	public static final String EQUALS = "=";

	public static final String PROBLEM = "?";

	public static final String COMMA = ",";

	public static boolean isEmpty(String param) {
		return null == param || "".equals(param) || "null".equals(param);
	}

	/*
	 * 判断字符串是否是有效的JSON
	 */
	public static boolean isGoodJson(String json){
		if(json.contains("status")  && json.contains("data") && json.contains("errMsg")){
			return true;
		}
		return false;
	}
	
	/** 将action转换为String */
	public static String actionToString(int action) {
		switch (action) {
		case Platform.ACTION_AUTHORIZING:
			return "ACTION_AUTHORIZING";
		case Platform.ACTION_GETTING_FRIEND_LIST:
			return "ACTION_GETTING_FRIEND_LIST";
		case Platform.ACTION_FOLLOWING_USER:
			return "ACTION_FOLLOWING_USER";
		case Platform.ACTION_SENDING_DIRECT_MESSAGE:
			return "ACTION_SENDING_DIRECT_MESSAGE";
		case Platform.ACTION_TIMELINE:
			return "ACTION_TIMELINE";
		case Platform.ACTION_USER_INFOR:
			return "ACTION_USER_INFOR";
		case Platform.ACTION_SHARE:
			return "ACTION_SHARE";
		default: {
			return "UNKNOWN";
		}
		}
	}
}
