package com.indoorsy.frash.myinfo.activity;

import java.util.HashMap;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.indoorsy.frash.Constants;
import com.indoorsy.frash.R;
import com.indoorsy.frash.ReleaseConfigure;
import com.indoorsy.frash.common.activity.BasicActivity;
import com.indoorsy.frash.common.data.bean.CommonResult;
import com.indoorsy.frash.common.view.CommonProgressDialog;
import com.indoorsy.frash.common.view.CustomTitleView;
import com.indoorsy.frash.http.core.HttpParam;
import com.indoorsy.frash.http.core.HttpResult;
import com.indoorsy.frash.http.core.HttpTask;
import com.indoorsy.frash.util.CommonDataUtil;
import com.indoorsy.frash.util.SharedPreferencesUtil;
import com.indoorsy.frash.util.StringUtil;
import com.indoorsy.frash.util.ToastUtil;

/*
 * 修改密码
 */
public class UpdatePasswordActivity extends BasicActivity {

	private static final String TAG = UpdatePasswordActivity.class.getSimpleName();
	private EditText myinfoUpdateOldPasswordEditText,
			myinfoUpdateNewPasswordEditText, myinfoUpdateNew2PasswordEditText;
	private CustomTitleView customTitleView;
	private Button myinfoUpdatePwdButton;
	private HttpTask httpTask;
	private CommonProgressDialog commonProgressDialog;
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.leftImageView:
				this.finish();
				break;
			case R.id.myinfoUpdatePwdButton:
				if(validate()){
					updatePwd();
				}
				break;
		}

	}

	// 验证
	public boolean validate() {
		String oldPwd = myinfoUpdateOldPasswordEditText.getText().toString().trim();
		if (TextUtils.isEmpty(oldPwd)) {
			ToastUtil.toast(getApplicationContext(),R.string.myinfo_retrieve_oldpwd_null_error);
			return false;
		}

		String newPwd = myinfoUpdateNewPasswordEditText.getText().toString().trim();
		if (TextUtils.isEmpty(newPwd)) {
			ToastUtil.toast(getApplicationContext(),R.string.myinfo_retrieve_newpwd_null_error);
			return false;
		}

		String newPwd2 = myinfoUpdateNew2PasswordEditText.getText().toString().trim();
		if (TextUtils.isEmpty(newPwd2)) {
			ToastUtil.toast(getApplicationContext(),R.string.myinfo_retrieve_new2pwd_null_error);
			return false;
		}
		
		if (!newPwd.equals(newPwd2)) {
			ToastUtil.toast(getApplicationContext(),R.string.myinfo_register_password_not_eror);
			return false;
		}
		return true;
	}

	// 修改密码
	public void updatePwd() {
		HashMap<String, String> parHashMap = CommonDataUtil.getCommonParams(getApplicationContext());
		parHashMap.put(Constants.PWD, myinfoUpdateOldPasswordEditText.getText().toString().trim());
		parHashMap.put(Constants.NEWPWD, myinfoUpdateNewPasswordEditText.getText().toString().trim());
		parHashMap.put(Constants.ULOGINID, String.valueOf(SharedPreferencesUtil.getUloginid(getApplicationContext())));
		HttpParam httpParam = new HttpParam(ReleaseConfigure.MYINFO_VALUE_TYPE_UPDATE, false);
		httpTask = new HttpTask(getApplicationContext(), this);
		httpParam.setParams(parHashMap);
		httpTask.execute(httpParam);
		commonProgressDialog.loadDialog();
	}


	@Override
	public void onLoadFinish(HttpTask task, HttpResult result) {
		Log.e(TAG, result.getData());
		commonProgressDialog.removeDialog();
		if (result != null && !StringUtil.isEmpty(result.getData()) && StringUtil.isGoodJson(result.getData())) {
			CommonResult commonResult = JSON.parseObject(result.getData(),CommonResult.class);
			if (null != commonResult) {
				if (commonResult.validate()) {
					ToastUtil.toast(getApplicationContext(),R.string.myinfo_update_password_success);
					goLogin();
				} else {
					ToastUtil.toast(getApplicationContext(),commonResult.getErrMsg());
				}
			}
		}
	}

	@Override
	public int initLayout() {
		return R.layout.myinfo_updatepassword;
	}

	@Override
	public void initUI() {
		customTitleView = (CustomTitleView) findViewById(R.id.customTitleView);
		customTitleView.setLeftImageBg(R.drawable.comm_back);
		customTitleView.setTitle(R.string.myinfo_update_title);
		customTitleView.setVisibility(View.VISIBLE);
		customTitleView.setOnClickListener(this);
		
		commonProgressDialog = new CommonProgressDialog(this);

		myinfoUpdatePwdButton = (Button) findViewById(R.id.myinfoUpdatePwdButton);
		myinfoUpdatePwdButton.setOnClickListener(this);
		myinfoUpdateOldPasswordEditText = (EditText) findViewById(R.id.myinfoUpdateOldPasswordEditText);
		myinfoUpdateNewPasswordEditText = (EditText) findViewById(R.id.myinfoUpdateNewPasswordEditText);
		myinfoUpdateNew2PasswordEditText = (EditText) findViewById(R.id.myinfoUpdateNew2PasswordEditText);
	}

	@Override
	public void initData() {

	}
	
	//去登陆界面
	public void goLogin(){
		Intent intent = new Intent();  
        intent.setAction(Constants.EXIT_LOGIN);  
        sendBroadcast(intent);  
        SharedPreferencesUtil.putInt(getApplicationContext(), SharedPreferencesUtil.USER_INFO_FILE_NAME, SharedPreferencesUtil.USER_INFO_KEY_USID, 0);
		startActivity(new Intent(getApplicationContext(),LoginActivity.class));
		this.finish();
	}
	
}
