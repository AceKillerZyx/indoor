package com.indoorsy.frash.myinfo.activity;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import com.indoorsy.frash.util.CommonDataUtil;
import com.indoorsy.frash.util.SharedPreferencesUtil;
import com.indoorsy.frash.util.StringUtil;
import com.indoorsy.frash.util.ToastUtil;
import com.indoorsy.frash.util.ValidateUtil;
/*
 * 注册
 */
public class RegisterActivity extends BasicActivity{
	private static final String TAG = RegisterActivity.class.getSimpleName();
	private String resultCode;
	private HttpTask registerHttpTask,loginHttpTask,sendCodeHttpTask;
	private Button registerButton;
	private CommonProgressDialog commonProgressDialog;
	private CustomTitleView customTitleView;
	private TextView registerSendCodeTextView, registerTermTextView;
	private EditText registerPhoneEditText, registerCodeEditText,
			registerInvitationEditText, registerPasswordEditText,
			registerConfirmPasswordEditText;
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.leftImageView:
				this.finish();
				break;
			case R.id.registerButton:
				if (validate()) {
					register();
				}
				break;
			case R.id.registerTermTextView:
				Intent intent = new Intent(this, TermActivity.class);
				startActivity(intent);
				break;
			case R.id.registerSendCodeTextView:
				sendCode();
				break;
		}
	}
	//注册验证
	private boolean validate() {
		String phone = registerPhoneEditText.getText().toString().trim();
		if (TextUtils.isEmpty(phone)) {
			ToastUtil.toast(getApplicationContext(),R.string.myinfo_register_phone_null_eror);
			return false;
		}

		if (!TextUtils.isEmpty(phone) && !ValidateUtil.isPhone(phone)) {
			ToastUtil.toast(getApplicationContext(),R.string.myinfo_register_phone_not_eror);
			return false;
		}

		String code = registerCodeEditText.getText().toString().trim();
		if (TextUtils.isEmpty(code)) {
			ToastUtil.toast(getApplicationContext(),R.string.myinfo_register_code_null_eror);
			return false;
		}
		Log.e(TAG, "code="+code+"--resultCode=" + resultCode);
		if(!StringUtil.isEmpty(resultCode) && !code.equals(resultCode)){
			ToastUtil.toast(getApplicationContext(), R.string.myinfo_register_code_error);
			return false;
		}

		String password = registerPasswordEditText.getText().toString().trim();
		if (TextUtils.isEmpty(password)) {
			ToastUtil.toast(getApplicationContext(),R.string.myinfo_register_password_null_eror);
			return false;
		}

		String confirmPassword = registerConfirmPasswordEditText.getText().toString().trim();
		if (TextUtils.isEmpty(confirmPassword)) {
			ToastUtil.toast(getApplicationContext(),R.string.myinfo_register_confirm_password_null_eror);
			return false;
		}

		if (!password.equals(confirmPassword)) {
			ToastUtil.toast(getApplicationContext(),R.string.myinfo_register_password_not_eror);
			return false;
		}
		return true;
	}
	
	@Override
	public int initLayout() {
		return R.layout.myinfo_register;
	}
	
	int count = 0;
	boolean flag = true;

	public void timecount() {
		count = 45;
		flag = true;
		new Thread() {
			public void run() {
				try {
					while (flag) {
						sleep(1000);
						count--;
						timeHandler.sendEmptyMessage(0);
						if (count < 0) {
							count = 0;
							flag = false;
							timeHandler.sendEmptyMessage(1);
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			};
		}.start();
	}
	
	@SuppressLint("HandlerLeak")
	Handler timeHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				registerSendCodeTextView.setText(String.format(getString(R.string.myinfo_register_code_time), count));
				break;
			case 1:
				registerSendCodeTextView.setText(R.string.myinfo_register_code_resend);
				registerSendCodeTextView.setEnabled(true);
				break;
			}

		}
	};
	// 发送验证码
	private void sendCode() {
		String mobile = registerPhoneEditText.getText().toString().trim();
		if (!TextUtils.isEmpty(mobile) && mobile.length() == 11 && ValidateUtil.isPhone(mobile)) {
			HashMap<String, String> paramMap = CommonDataUtil.getCommonParams(getApplicationContext());
			paramMap.put(Constants.PHONE, mobile);
			HttpParam httpParam = new HttpParam(ReleaseConfigure.MYINFO_VALUE_TYPE_SEND_CODE, false); // GET
			sendCodeHttpTask = new HttpTask(getApplicationContext(), this);
			httpParam.setParams(paramMap);
			sendCodeHttpTask.execute(httpParam);
			commonProgressDialog.loadDialog();
			registerSendCodeTextView.setEnabled(false);
			timecount();
		} else {
			ToastUtil.toast(getApplicationContext(),R.string.myinfo_register_phone_error);
		}
	}
	
	@Override
	public void initUI() {
		customTitleView = (CustomTitleView) findViewById(R.id.customTitleView);
		customTitleView.setLeftImageBg(R.drawable.comm_back);
		customTitleView.setTitle(R.string.myinfo_register);
		customTitleView.setLeftImageVisibility(View.VISIBLE);
		customTitleView.setOnClickListener(this);
		
		commonProgressDialog = new CommonProgressDialog(this);

		registerPhoneEditText = (EditText) findViewById(R.id.registerPhoneEditText);
		registerCodeEditText = (EditText) findViewById(R.id.registerCodeEditText);
		registerInvitationEditText = (EditText)findViewById(R.id.registerInvitationEditText);
		registerPasswordEditText = (EditText) findViewById(R.id.registerPasswordEditText);
		registerConfirmPasswordEditText = (EditText) findViewById(R.id.registerConfirmPasswordEditText);

		registerSendCodeTextView = (TextView) findViewById(R.id.registerSendCodeTextView);
		registerSendCodeTextView.setOnClickListener(this);

		registerButton = (Button) findViewById(R.id.registerButton);
		registerButton.setOnClickListener(this);

		registerTermTextView = (TextView) findViewById(R.id.registerTermTextView);
		registerTermTextView.setOnClickListener(this);
	}
	
	//注册
	private void register() {
		HashMap<String, String> paramMap = CommonDataUtil.getCommonParams(getApplicationContext());
		paramMap.put(Constants.TEL, registerPhoneEditText.getText().toString().trim());
		paramMap.put(Constants.PWD, registerPasswordEditText.getText().toString().trim());
		paramMap.put(Constants.USERCODE, registerInvitationEditText.getText().toString().trim());
		HttpParam httpParam = new HttpParam(ReleaseConfigure.MYINFO_VALUE_TYPE_REGISTER, false); // GET
		registerHttpTask = new HttpTask(getApplicationContext(), this);
		httpParam.setParams(paramMap);
		registerHttpTask.execute(httpParam);
		commonProgressDialog.loadDialog();
	}
	
	//普通登录
	public void login(){
		HashMap<String, String> parHashMap=CommonDataUtil.getCommonParams(getApplicationContext());
		parHashMap.put(Constants.TEL,registerPhoneEditText.getText().toString().trim());
		parHashMap.put(Constants.PWD,registerPasswordEditText.getText().toString().trim());
		HttpParam httpParam=new HttpParam(ReleaseConfigure.MYINFO_VALUE_TYPE_LOGIN, false);//get
		loginHttpTask=new HttpTask(getApplicationContext(), this);
		httpParam.setParams(parHashMap);
		loginHttpTask.execute(httpParam);
		commonProgressDialog.loadDialog();
	}
	
	@Override
	public void onLoadFinish(HttpTask task, HttpResult result) {
		commonProgressDialog.removeDialog();
		Log.e(TAG, result.getData());
		if (result != null && !StringUtil.isEmpty(result.getData()) && StringUtil.isGoodJson(result.getData())) {
			CommonResult commonResult = JSON.parseObject(result.getData(),CommonResult.class);
			if (null != commonResult) {
				if (task == registerHttpTask) {
					if (commonResult.validate()) {
						login();
					} else {
						ToastUtil.toast(getApplicationContext(),commonResult.getErrMsg());
					}
				}

				if (task == loginHttpTask) {
					if (commonResult.validate()) {
						JSONArray jsonArray = JSON.parseArray(commonResult.getData());
						if (jsonArray != null && jsonArray.size() > 0) {
							JSONObject jsonObject = jsonArray.getJSONObject(0);
							SharedPreferencesUtil.putInt(getApplicationContext(), SharedPreferencesUtil.USER_INFO_FILE_NAME, SharedPreferencesUtil.USER_INFO_KEY_ULOGINID, jsonObject.getIntValue(Constants.LOGINID));
							SharedPreferencesUtil.putString(getApplicationContext(), SharedPreferencesUtil.USER_INFO_FILE_NAME, SharedPreferencesUtil.USER_INFO_KEY_HEADER_IMAGE, jsonObject.getString(Constants.IMAGE));
							SharedPreferencesUtil.putString(getApplicationContext(), SharedPreferencesUtil.USER_INFO_FILE_NAME, SharedPreferencesUtil.USER_INFO_KEY_USNAME, registerPhoneEditText.getText().toString().trim());
							SharedPreferencesUtil.putString(getApplicationContext(), SharedPreferencesUtil.USER_INFO_FILE_NAME, SharedPreferencesUtil.USER_INFO_KEY_PASSWORD, registerPasswordEditText.getText().toString().trim());
							SharedPreferencesUtil.putInt(getApplicationContext(), SharedPreferencesUtil.USER_INFO_FILE_NAME, SharedPreferencesUtil.USER_INFO_KEY_USID, jsonObject.getIntValue(Constants.USID));
							SharedPreferencesUtil.putBoolean(getApplicationContext(), SharedPreferencesUtil.USER_INFO_FILE_NAME, SharedPreferencesUtil.USER_INFO_KEY_IS_THIRD_LOGIN, false);
						}
						goHome();
					} else {
						ToastUtil.toast(getApplicationContext(),commonResult.getErrMsg());
					}
				}
				
				if (task == sendCodeHttpTask) {
					if (commonResult.validate()) {
						JSONObject jsonObject = JSON.parseObject(commonResult.getData());
						if (jsonObject != null && jsonObject.size() > 0) {
							resultCode = jsonObject.getString(Constants.CODE);
						}
					} else {
						ToastUtil.toast(getApplicationContext(),commonResult.getErrMsg());
					}
				}
			}
		}
	}
	@Override
	public void initData() {
		
	}
	
	public void goHome(){
		startActivity(new Intent(RegisterActivity.this, IndoorsyActivity.class));
		finish();
	}

}
