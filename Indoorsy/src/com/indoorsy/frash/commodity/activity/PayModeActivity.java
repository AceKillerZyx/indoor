package com.indoorsy.frash.commodity.activity;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.indoorsy.frash.Constants;
import com.indoorsy.frash.R;
import com.indoorsy.frash.ReleaseConfigure;
import com.indoorsy.frash.common.activity.BasicActivity;
import com.indoorsy.frash.common.view.CommonProgressDialog;
import com.indoorsy.frash.http.core.HttpParam;
import com.indoorsy.frash.http.core.HttpResult;
import com.indoorsy.frash.http.core.HttpTask;
import com.indoorsy.frash.util.CommonDataUtil;
import com.indoorsy.frash.util.SharedPreferencesUtil;
import com.indoorsy.frash.util.ToastUtil;
import com.pingplusplus.android.PaymentActivity;

public class PayModeActivity extends BasicActivity {
	
	private static final String TAG = PayModeActivity.class.getSimpleName();
	
    private static final int REQUEST_CODE_PAYMENT = 1;
    
    private CommonProgressDialog commonProgressDialog;
    
    private HttpTask httpTask;
    
    private String amount = "";
    
    private String ordernumber = "";
    
	// 支付方式
	private TextView PayModeAlipayTextView, PayModeWeChatTextView,PayModeBankTextView;

	//支付成功 
	private HttpTask successStateHttpTask;
	
	@Override
	public int initLayout() {
		return R.layout.commodity_pay_mode;
	}

	@Override
	public void initUI() {
		PayModeAlipayTextView = (TextView) findViewById(R.id.PayModeAlipayTextView);
		PayModeWeChatTextView = (TextView) findViewById(R.id.PayModeWeChatTextView);
		PayModeBankTextView = (TextView) findViewById(R.id.PayModeBankTextView);
		PayModeAlipayTextView.setOnClickListener(this);
		PayModeWeChatTextView.setOnClickListener(this);
		PayModeBankTextView.setOnClickListener(this);
		
		commonProgressDialog = new CommonProgressDialog(this);
	}
	
	private void pay(String url) {
		HashMap<String, String> paramMap = CommonDataUtil.getCommonParams(getApplicationContext());
		paramMap.put(Constants.COMMODITY_AMOUNT, amount);
		paramMap.put(Constants.ORDERNUMBER_SN, ordernumber);
		HttpParam httpParam = new HttpParam(url, false); // GET
		httpTask = new HttpTask(this, this);
		httpParam.setParams(paramMap);
		httpTask.execute(httpParam);
		commonProgressDialog.loadDialog();
		PayModeAlipayTextView.setOnClickListener(null);
    	PayModeWeChatTextView.setOnClickListener(null);
    	PayModeBankTextView.setOnClickListener(null);
    	commonProgressDialog.loadDialog();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.PayModeAlipayTextView:
				pay(ReleaseConfigure.HOMEPAGE_VALUE_TYPE_PAY_ORDER_CREATEPAY);
				break;
			case R.id.PayModeWeChatTextView:
				pay(ReleaseConfigure.HOMEPAGE_VALUE_TYPE_PAY_ORDER_WEIXINPAY);
				break;
			case R.id.PayModeBankTextView:
				int isYinlian=SharedPreferencesUtil.getIntValue(getApplicationContext(), SharedPreferencesUtil.APP_INFO_FILE_NAME, SharedPreferencesUtil.APP_INFO_IS_YINLIAN);
				if (isYinlian!=1) {
					SharedPreferencesUtil.putInt(getApplicationContext(), SharedPreferencesUtil.APP_INFO_FILE_NAME, SharedPreferencesUtil.APP_INFO_IS_YINLIAN,1);
					Intent intent=new Intent();
					intent.setAction("android.intent.action.VIEW");
					Uri uri=Uri.parse("http://mobile.unionpay.com/getclient?platform=android&type=securepayplugin");
					intent.setData(uri);
					startActivity(intent);
				}else {
					pay(ReleaseConfigure.HOMEPAGE_VALUE_TYPE_PAY_ORDER_UNIONPAYPAY);
				}
				
				//ToastUtil.toast(getApplicationContext(), "暂未开通");
				break;
		}
	}
	
	/**
	 * onActivityResult 获得支付结果，如果支付成功，服务器会收到ping++ 服务器发送的异步通知。
	 * 最终支付成功根据异步通知为准
	 */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //支付页面返回处理
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getExtras().getString("pay_result");
                /* 处理返回值
                 * "success" - payment succeed
                 * "fail"    - payment failed
                 * "cancel"  - user canceld
                 * "invalid" - payment plugin not installed
                 */
                Log.e(TAG, "result:" + result);
                String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
                Log.e(TAG, "errorMsg:" + errorMsg);
                if("success".equals(result)){
                	ShowSuccess();
                }else if("fail".equals(result)){
                	 ToastUtil.toast(getApplicationContext(), "支付失败");
                }else if("cancel".equals(result)){
                	ToastUtil.toast(getApplicationContext(), "取消支付");
                }
            }else if (resultCode == Activity.RESULT_CANCELED) {
                ToastUtil.toast(getApplicationContext(), "User canceled");
            }
        }
    }
    
    @SuppressLint("NewApi")
	private void ShowSuccess() {
		final AlertDialog dialog = new AlertDialog.Builder(this,R.style.PopDialog).create();
		dialog.setCancelable(false);
		dialog.show();
		Window window = dialog.getWindow();
		window.setContentView(R.layout.homepage_paysuccess_dialog);
		Button BackButton = (Button) window.findViewById(R.id.BackButton);
		BackButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//修改订单状态
				uploadOrderState();
			}
		});
		
	}

	protected void uploadOrderState() {
		HashMap<String, String> paramMap = CommonDataUtil.getCommonParams(getApplicationContext());
		paramMap.put(Constants.ORDERNUMBER_SN, ordernumber);//订单号
		paramMap.put("orderstate", "1");
		paramMap.put("shippingstate", "0");
		paramMap.put("paystate", "1");
		HttpParam httpParam = new HttpParam(ReleaseConfigure.HOMEPAGE_VALUE_TYPE_PAY_SUCCESS_STATE, false); // GET
		successStateHttpTask = new HttpTask(this, this);
		httpParam.setParams(paramMap);
		successStateHttpTask.execute(httpParam);
		commonProgressDialog.loadDialog();
		
	}

	@Override
	public void onLoadFinish(HttpTask task, HttpResult result) {
		commonProgressDialog.removeDialog();
		Log.e(TAG, result.getData());
		if (task == httpTask) {
			PayModeAlipayTextView.setOnClickListener(this);
	    	PayModeWeChatTextView.setOnClickListener(this);
	    	PayModeBankTextView.setOnClickListener(this);
			JSONObject jsonObject = JSON.parseObject(result.getData());
			if("1".equals(jsonObject.getString("status"))){
				Intent intent = new Intent();
		        String packageName = getPackageName();
		        ComponentName componentName = new ComponentName(packageName, packageName + ".wxapi.WXPayEntryActivity");
		        intent.setComponent(componentName);
		        if (jsonObject.getString("data") != null) {
		        	intent.putExtra(PaymentActivity.EXTRA_CHARGE, jsonObject.getString("data"));
		        	startActivityForResult(intent, REQUEST_CODE_PAYMENT);
				} 
			}else{
				ToastUtil.toast(getApplicationContext(), "支付失败");
			}
		}
		
		if (task == successStateHttpTask) {
			finish();
		}
	}

	@Override
	public void initData() {
		amount = getIntent().getStringExtra(Constants.COMMODITY_AMOUNT);
		ordernumber = getIntent().getStringExtra(Constants.ORDERNUMBER_SN);
		Log.e(TAG, "获取到的付款金额 = " + amount + "--订单编号=" + ordernumber);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}

}
