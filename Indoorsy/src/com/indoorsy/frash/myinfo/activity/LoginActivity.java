package com.indoorsy.frash.myinfo.activity;

import java.util.HashMap;

import android.content.Intent;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.indoorsy.frash.Constants;
import com.indoorsy.frash.R;
import com.indoorsy.frash.ReleaseConfigure;
import com.indoorsy.frash.common.activity.BasicActivity;
import com.indoorsy.frash.common.data.bean.CommonResult;
import com.indoorsy.frash.common.view.CommonProgressDialog;
import com.indoorsy.frash.common.view.CustomTitleView;
import com.indoorsy.frash.home.activity.IndoorsyActivity;
import com.indoorsy.frash.http.core.HttpParam;
import com.indoorsy.frash.http.core.HttpResult;
import com.indoorsy.frash.http.core.HttpTask;
import com.indoorsy.frash.myinfo.third.OnLoginListener;
import com.indoorsy.frash.myinfo.third.SignupPage;
import com.indoorsy.frash.util.CommonDataUtil;
import com.indoorsy.frash.util.SharedPreferencesUtil;
import com.indoorsy.frash.util.StringUtil;
import com.indoorsy.frash.util.ToastUtil;
import com.indoorsy.frash.util.UpdateManager;

/*
 * 登录
 */
public class LoginActivity extends BasicActivity implements Callback,
		PlatformActionListener {
	private static final String TAG = LoginActivity.class.getSimpleName();

	// 第三方
	private static final int MSG_AUTH_CANCEL = 2;
	
	private static final int MSG_AUTH_ERROR = 3;
	
	private static final int MSG_AUTH_COMPLETE = 4;
	
	private OnLoginListener signupListener;
	
	private Handler handler;
	
	private int typeid = 0;

	private HttpTask loginHttpTask,thirdLoginHttpTask;
	
	private CustomTitleView customTitleView;
	
//	private CommonLockUpView lockUpView;
	private CommonProgressDialog pd;
	
	private Button myinfoLoginButton;
	
	private String thirdName,thirdIcon;
	
	private UpdateManager updateManager;
	
	private TextView myinfoLoginRegisterTextView, myinfoLoginProblemTextView,
			myinfoLoginQQTextView, myinfoLoginWeiBoTextView,
			myinfoLoginWeiXinTextView;

	private EditText myinfoLoginUserNameEditText, myinfoLoginPasswordEditText;

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.myinfoLoginButton:
				if (validate()) {
					login();
				}
				break;
			case R.id.myinfoLoginRegisterTextView:
				Intent registerIntent = new Intent(this, RegisterActivity.class);
				startActivity(registerIntent);
				break;
			case R.id.myinfoLoginProblemTextView:
				Intent retrieveIntent = new Intent(this,
						RetrievePasswordActivity.class);
				startActivity(retrieveIntent);
				break;
			case R.id.myinfoLoginQQTextView:
				// QQ登录
				typeid = 1;
				Platform qzone = ShareSDK.getPlatform(QZone.NAME);
				authorize(qzone);
				break;
			case R.id.myinfoLoginWeiBoTextView:
				// 新浪微博登录
				typeid = 3;
				Platform sina = ShareSDK.getPlatform(SinaWeibo.NAME);
				authorize(sina);
				break;
			case R.id.myinfoLoginWeiXinTextView:
				// 微信登录
				typeid = 2;
				Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
				authorize(wechat);
		}
	}

	// 登录验证
	private boolean validate() {
		String username = myinfoLoginUserNameEditText.getText().toString()
				.trim();
		if (TextUtils.isEmpty(username)) {
			ToastUtil.toast(getApplicationContext(),R.string.myinfo_login_username_null_eror);
			return false;
		}

		String password = myinfoLoginPasswordEditText.getText().toString().trim();
		if (TextUtils.isEmpty(password)) {
			ToastUtil.toast(getApplicationContext(),R.string.myinfo_login_password_null_eror);
			return false;
		}
		return true;
	}

	@Override
	public int initLayout() {
		return R.layout.myinfo_login;
	}

	// 初始化UI控件
	@Override
	public void initUI() {
		customTitleView = (CustomTitleView) findViewById(R.id.customTitleView);
		customTitleView.setTitle(R.string.myinfo_login);
		customTitleView.setOnClickListener(this);

//		lockUpView = (CommonLockUpView) findViewById(R.id.lockUpView);
		pd = new CommonProgressDialog(this);

		myinfoLoginButton = (Button) findViewById(R.id.myinfoLoginButton);
		myinfoLoginButton.setOnClickListener(this);

		myinfoLoginUserNameEditText = (EditText) findViewById(R.id.myinfoLoginUserNameEditText);
		myinfoLoginPasswordEditText = (EditText) findViewById(R.id.myinfoLoginPasswordEditText);

		myinfoLoginRegisterTextView = (TextView) findViewById(R.id.myinfoLoginRegisterTextView);
		myinfoLoginRegisterTextView.setOnClickListener(this);
		myinfoLoginProblemTextView = (TextView) findViewById(R.id.myinfoLoginProblemTextView);
		myinfoLoginProblemTextView.setOnClickListener(this);
		myinfoLoginWeiXinTextView = (TextView) findViewById(R.id.myinfoLoginWeiXinTextView);
		myinfoLoginWeiXinTextView.setOnClickListener(this);
		myinfoLoginQQTextView = (TextView) findViewById(R.id.myinfoLoginQQTextView);
		myinfoLoginQQTextView.setOnClickListener(this);
		myinfoLoginWeiBoTextView = (TextView) findViewById(R.id.myinfoLoginWeiBoTextView);
		myinfoLoginWeiBoTextView.setOnClickListener(this);

		handler = new Handler(this);
		ShareSDK.initSDK(getApplicationContext());
	}

	@Override
	public void initData() {
		updateManager = new UpdateManager(getApplicationContext(), 0);
		updateManager.checkUpdateInfo();
	}

	//普通登录
	public void login(){
		HashMap<String, String> parHashMap=CommonDataUtil.getCommonParams(getApplicationContext());
		parHashMap.put(Constants.TEL,myinfoLoginUserNameEditText.getText().toString().trim());
		parHashMap.put(Constants.PWD,myinfoLoginPasswordEditText.getText().toString().trim());
		HttpParam httpParam=new HttpParam(ReleaseConfigure.MYINFO_VALUE_TYPE_LOGIN, false);//get
		loginHttpTask=new HttpTask(getApplicationContext(), this);
		httpParam.setParams(parHashMap);
		loginHttpTask.execute(httpParam);
//		lockUpView.setVisibility(View.VISIBLE);
		pd.loadDialog();
	}
	
	//第三方登录
	public void thirdLogin(String thirdLoginOpenId,String uname,String sex,String userImages){
		HashMap<String, String> parHashMap=CommonDataUtil.getCommonParams(getApplicationContext());
		Log.e(TAG, "OPENID=" + thirdLoginOpenId + "--typeid=" + typeid + "--uname=" + uname + "--sex=" + sex + "--userImages=" + userImages);
		SharedPreferencesUtil.putString(getApplicationContext(), SharedPreferencesUtil.USER_INFO_FILE_NAME, SharedPreferencesUtil.USER_INFO_KEY_OPENID, thirdLoginOpenId);
		parHashMap.put(Constants.OPENID, String.valueOf(thirdLoginOpenId));
		parHashMap.put(Constants.TYPEID, String.valueOf(typeid));
		thirdName = uname;
		parHashMap.put(Constants.UNAME, thirdName);
		String resultSex = "";
		Log.e(TAG, "--sex=" + sex );
		if(sex.equals("m")){
			resultSex = "男";
		}else if(sex.equals("f")){
			resultSex = "女";
		}else if(StringUtil.isEmpty(sex)){
			resultSex = "男";
		}
		Log.e(TAG, "--resultSex=" + resultSex );
		parHashMap.put(Constants.SEX, resultSex);
		thirdIcon = userImages;
		parHashMap.put(Constants.USERIMAGES, thirdIcon);
		HttpParam httpParam=new HttpParam(ReleaseConfigure.MYINFO_VALUE_TYPE_THIRD_LOGIN, false);//get
		thirdLoginHttpTask=new HttpTask(getApplicationContext(), this);
		httpParam.setParams(parHashMap);
		thirdLoginHttpTask.execute(httpParam);
//		lockUpView.setVisibility(View.VISIBLE);
		pd.loadDialog();
	}
	
	@Override
	public void onLoadFinish(HttpTask task, HttpResult result) {
//		lockUpView.setVisibility(View.GONE);
		pd.removeDialog();
		Log.e(TAG, result.getData());
		if (result != null && !StringUtil.isEmpty(result.getData()) && StringUtil.isGoodJson(result.getData())) {
			CommonResult commonResult = JSON.parseObject(result.getData(),CommonResult.class);
			if (null != commonResult) {
				if (task == loginHttpTask) {
					if (commonResult.validate()) {
//						ToastUtil.toast(getApplicationContext(), R.string.myinfo_login_success);
						JSONArray jsonArray = JSON.parseArray(commonResult.getData());
						if (jsonArray != null && jsonArray.size() > 0) {
							JSONObject jsonObject = jsonArray.getJSONObject(0);
							SharedPreferencesUtil.putInt(getApplicationContext(), SharedPreferencesUtil.USER_INFO_FILE_NAME, SharedPreferencesUtil.USER_INFO_KEY_ULOGINID, jsonObject.getIntValue(Constants.LOGINID));
							SharedPreferencesUtil.putString(getApplicationContext(), SharedPreferencesUtil.USER_INFO_FILE_NAME, SharedPreferencesUtil.USER_INFO_KEY_HEADER_IMAGE, jsonObject.getString(Constants.IMAGE));
							SharedPreferencesUtil.putString(getApplicationContext(), SharedPreferencesUtil.USER_INFO_FILE_NAME, SharedPreferencesUtil.USER_INFO_KEY_USNAME, myinfoLoginUserNameEditText.getText().toString().trim());
							SharedPreferencesUtil.putString(getApplicationContext(), SharedPreferencesUtil.USER_INFO_FILE_NAME, SharedPreferencesUtil.USER_INFO_KEY_PASSWORD, myinfoLoginPasswordEditText.getText().toString().trim());
							SharedPreferencesUtil.putInt(getApplicationContext(), SharedPreferencesUtil.USER_INFO_FILE_NAME, SharedPreferencesUtil.USER_INFO_KEY_USID, jsonObject.getIntValue(Constants.USID));
							SharedPreferencesUtil.putBoolean(getApplicationContext(), SharedPreferencesUtil.USER_INFO_FILE_NAME, SharedPreferencesUtil.USER_INFO_KEY_IS_THIRD_LOGIN, false);
							goHome();
						}
					} else {
						ToastUtil.toast(getApplicationContext(),commonResult.getErrMsg());
					}
				}
				
				if (task == thirdLoginHttpTask) {
					if (commonResult.validate()) {
//						ToastUtil.toast(getApplicationContext(), R.string.myinfo_login_success);
						JSONArray jsonArray = JSON.parseArray(commonResult.getData());
						if (jsonArray != null && jsonArray.size() > 0) {
							JSONObject jsonObject = jsonArray.getJSONObject(0);
							SharedPreferencesUtil.putString(getApplicationContext(), SharedPreferencesUtil.USER_INFO_FILE_NAME, SharedPreferencesUtil.USER_INFO_KEY_HEADER_IMAGE, thirdIcon);
							SharedPreferencesUtil.putString(getApplicationContext(), SharedPreferencesUtil.USER_INFO_FILE_NAME, SharedPreferencesUtil.USER_INFO_KEY_USNAME, thirdName);
							SharedPreferencesUtil.putInt(getApplicationContext(), SharedPreferencesUtil.USER_INFO_FILE_NAME, SharedPreferencesUtil.USER_INFO_KEY_USID, jsonObject.getIntValue(Constants.USID));
							SharedPreferencesUtil.putBoolean(getApplicationContext(), SharedPreferencesUtil.USER_INFO_FILE_NAME, SharedPreferencesUtil.USER_INFO_KEY_IS_THIRD_LOGIN, true);
							goHome();
						}
					} else {
						ToastUtil.toast(getApplicationContext(),commonResult.getErrMsg());
					}
				}
			}
		}
	}

	/**
	 * 设置授权回调，用于判断是否进入注册
	 */
	public void setOnLoginListener(OnLoginListener l) {
		this.signupListener = l;
	}

	// 执行授权,获取用户信息
	// 文档：http://wiki.mob.com/Android_%E8%8E%B7%E5%8F%96%E7%94%A8%E6%88%B7%E8%B5%84%E6%96%99
	private void authorize(Platform plat) {
		plat.setPlatformActionListener(this);
		//关闭SSO授权
		plat.SSOSetting(true);
		plat.showUser(null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
			case MSG_AUTH_CANCEL: 
				// 取消授权
				ToastUtil.toast(getApplicationContext(), R.string.auth_cancel);
				break;
			case MSG_AUTH_ERROR: 
				// 授权失败
				ToastUtil.toast(getApplicationContext(), R.string.auth_error);
				break;
			case MSG_AUTH_COMPLETE: 
				// 授权成功
				ToastUtil.toast(getApplicationContext(), R.string.auth_complete);
				Object[] objs = (Object[]) msg.obj;
				JSONObject jsonObject = (JSONObject) objs[0];
				Log.e(TAG, "platform:" + jsonObject.getString(Constants.PLATFORM));
				HashMap<String, Object> res = (HashMap<String, Object>) objs[1];
				Log.e(TAG, "res:" + res);
				if (signupListener != null && signupListener.onSignin(jsonObject.getString(Constants.PLATFORM), res)) {
					SignupPage signupPage = new SignupPage();
					signupPage.setOnLoginListener(signupListener);
					signupPage.setPlatform(jsonObject.getString(Constants.PLATFORM));
					signupPage.show(LoginActivity.this, null);
				}
				thirdLogin(jsonObject.getString(Constants.OPENID),jsonObject.getString(Constants.UNAME),jsonObject.getString(Constants.SEX),jsonObject.getString(Constants.USERIMAGES));
				break;
		}
		return false;
	}

	// PlatformActionListener···
	@Override
	public void onCancel(Platform arg0, int action) {
		if (action == Platform.ACTION_USER_INFOR) {
			handler.sendEmptyMessage(MSG_AUTH_CANCEL);
		}
	}

	@Override
	public void onComplete(Platform platform, int action,HashMap<String, Object> res) {
		if (action == Platform.ACTION_USER_INFOR) {
			Message msg = new Message();
			msg.what = MSG_AUTH_COMPLETE;
			JSONObject jsonObject = new JSONObject();
			jsonObject.put(Constants.OPENID, platform.getDb().getUserId());
			jsonObject.put(Constants.UNAME, platform.getDb().getUserName());
			jsonObject.put(Constants.SEX, platform.getDb().getUserGender());
			jsonObject.put(Constants.USERIMAGES, platform.getDb().getUserIcon());
			jsonObject.put(Constants.PLATFORM, platform.getName());
			msg.obj = new Object[] {jsonObject, res };
			handler.sendMessage(msg);
		}
	}

	@Override
	public void onError(Platform arg0, int action, Throwable t) {
		if (action == Platform.ACTION_USER_INFOR) {
			handler.sendEmptyMessage(MSG_AUTH_ERROR);
		}
		t.printStackTrace();
	}
		
	public void goHome(){
		startActivity(new Intent(LoginActivity.this, IndoorsyActivity.class));
		finish();
	}
}
