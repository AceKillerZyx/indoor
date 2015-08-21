package com.indoorsy.frash.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

public class ValidateUtil {
	private static final String TAG  = ValidateUtil.class.getSimpleName();
	
	/*
	 * 手机号验�?
	 */
	public static boolean isPhone(String phone){     
	     String regex="^1(3|5|8)\\d{9}$";
	     return match(regex,phone);     
	}
	
	/*
	 * 邮箱验证
	 */
	public static boolean isEmail(String email){     
	     String regex="^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$";
	     return match(regex,email);     
	}
	
	/*
	 * QQ验证
	 */
	public static boolean isQQ(String qq){     
	     String regex="^[1-9][0-9]{4,}+$";
	     return match(regex,qq);     
	}
	
	/*
	 * 验证输入用户条件(字符与数据同时出�?)
	 * @param 待验证的字符�?
	 * @return 如果是符合格式的字符�?,返回 <b>true </b>,否则�? <b>false </b>
	 */
	public static boolean isUserName(String name){
		String regex = "^(?!\\D+$)(?!\\d+$)[a-zA-Z0-9]{6,64}$";
		return match(regex, name);
	}
	
	/*
	 * 验证输入用户密码(字符与数据同时出�?)
	 * @param 待验证的字符�?
	 * @return 如果是符合格式的字符�?,返回 <b>true </b>,否则�? <b>false </b>
	 */
	public static boolean isPassword(String password){
		String regex = "^(?!\\D+$)(?!\\d+$)[a-zA-Z0-9]{6,64}$";
		return match(regex, password);
	}
	
	/*
	 * 验证输入用户�?请码(字符与数据同时出�?)
	 * @param 待验证的字符�?
	 * @return 如果是符合格式的字符�?,返回 <b>true </b>,否则�? <b>false </b>
	 */
	public static boolean isInviteCode(String invitecode){
		String regex = "^\\d{6}$";
		return match(regex, invitecode);
	}
	
	public static boolean match(String regex,String str) {
		Pattern p = Pattern.compile(regex);     
	     Matcher m = p.matcher(str);     
	     Log.e(TAG,"---" + m.matches());     
	     return m.matches();     
	}
}
