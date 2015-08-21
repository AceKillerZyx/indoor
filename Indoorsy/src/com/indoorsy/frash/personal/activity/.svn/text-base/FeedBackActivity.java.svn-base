package com.indoorsy.frash.personal.activity;

import java.util.HashMap;

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
import com.indoorsy.frash.util.StringUtil;
import com.indoorsy.frash.util.ToastUtil;
/*
 * 意见反馈
 */
public class FeedBackActivity extends BasicActivity {
	
	private static final String TAG = FeedBackActivity.class.getSimpleName();
	
	private CustomTitleView customTitleView;
	
	private HttpTask httpTask;
	
	private Button personalFeedbackButton;
	
	private CommonProgressDialog commonProgressDialog;
	
	private EditText personalFeedbackEditText,personalFeedbackPhoneEditText;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.leftImageView:
				this.finish();
				break;
			case R.id.personalFeedbackButton:
				if(validate()){
					feedback();
				}
				break;
		}
	}
	
	private boolean validate(){
		String content = personalFeedbackEditText.getText().toString().trim();
		if(TextUtils.isEmpty(content)){
			ToastUtil.toast(getApplicationContext(), R.string.personal_feedback_content_null_error);
			return false;
		}
		return true;
	}
	
	private void feedback(){
		HashMap<String, String> paramMap = CommonDataUtil.getCommonParams(getApplicationContext());
		paramMap.put(Constants.OPINIONCONETNT, personalFeedbackEditText.getText().toString().trim());
		paramMap.put(Constants.OPINIONTEL, personalFeedbackPhoneEditText.getText().toString().trim());
		HttpParam httpParam = new HttpParam(ReleaseConfigure.PERSONAL_VALUE_TYPE_FEEDBACK, false); // GET
		httpTask = new HttpTask(getApplicationContext(), this);
		httpParam.setParams(paramMap);
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
					ToastUtil.toast(getApplicationContext(), R.string.personal_feedback_success);
					personalFeedbackEditText.setText("");
					personalFeedbackPhoneEditText.setText("");
				}else{
//					ToastUtil.toast(getApplicationContext(), commonResult.getErrMsg());
				}
			}
		}
	}

	@Override
	public int initLayout() {
		return R.layout.personal_feedback;
	}

	@Override
	public void initUI() {
		customTitleView=(CustomTitleView) findViewById(R.id.customTitleView);
		customTitleView.setLeftImageBg(R.drawable.comm_back);
		customTitleView.setTitle(R.string.personal_feedback_title);
		customTitleView.setOnClickListener(this);
		customTitleView.setVisibility(View.VISIBLE);
		
		commonProgressDialog = new CommonProgressDialog(this);
		
		personalFeedbackEditText = (EditText)findViewById(R.id.personalFeedbackEditText);
		personalFeedbackPhoneEditText = (EditText)findViewById(R.id.personalFeedbackPhoneEditText);
		
		personalFeedbackButton = (Button)findViewById(R.id.personalFeedbackButton);
		personalFeedbackButton.setOnClickListener(this);
	}

	@Override
	public void initData() {

	}

}
