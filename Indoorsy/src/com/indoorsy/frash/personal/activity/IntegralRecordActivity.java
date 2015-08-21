package com.indoorsy.frash.personal.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.indoorsy.frash.R;
import com.indoorsy.frash.ReleaseConfigure;
import com.indoorsy.frash.common.activity.BasicActivity;
import com.indoorsy.frash.common.data.bean.CommonResult;
import com.indoorsy.frash.common.view.CommonProgressDialog;
import com.indoorsy.frash.common.view.CustomTitleView;
import com.indoorsy.frash.http.core.HttpParam;
import com.indoorsy.frash.http.core.HttpResult;
import com.indoorsy.frash.http.core.HttpTask;
import com.indoorsy.frash.personal.adapter.IntegralRecordAdapter;
import com.indoorsy.frash.personal.data.bean.IntegralRecord;
import com.indoorsy.frash.util.CommonDataUtil;
import com.indoorsy.frash.util.StringUtil;
/*
 * 积分记录
 */
public class IntegralRecordActivity extends BasicActivity {
	
	private static final String TAG = IntegralRecordActivity.class.getSimpleName();
	
	private HttpTask httpTask;
	
	private CommonProgressDialog commonProgressDialog;
	
	private CustomTitleView customTitleView;
	
	private ListView integralRecordListView;
	
	private IntegralRecordAdapter integralRecordAdapter;
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.leftImageView:
				this.finish();
				break;
		}
	}
	
	//查询积分记录
	private void searchIntegralRecord() {
		HashMap<String, String> paramMap = CommonDataUtil.getCommonParams(getApplicationContext());
		HttpParam httpParam = new HttpParam(ReleaseConfigure.PERSONAL_VALUE_TYPE_INTEGRALRECORD, false); // GET
		httpTask = new HttpTask(getApplicationContext(), this);
		httpParam.setParams(paramMap);
		httpTask.execute(httpParam);
		commonProgressDialog.loadDialog();
	}

	@Override
	public int initLayout() {
		return R.layout.personal_integralrecord;
	}

	@Override
	public void initUI() {
		customTitleView=(CustomTitleView) findViewById(R.id.customTitleView);
		customTitleView.setLeftImageBg(R.drawable.comm_back);
		customTitleView.setLeftImageVisibility(View.VISIBLE);
		customTitleView.setTitle(R.string.personal_integralrecord_title);
		customTitleView.setOnClickListener(this);
		
		commonProgressDialog = new CommonProgressDialog(this);
		
		integralRecordAdapter = new IntegralRecordAdapter(getApplicationContext());
		integralRecordListView = (ListView)findViewById(R.id.integralRecordListView);
		integralRecordListView.setAdapter(integralRecordAdapter);
		
		searchIntegralRecord();
	}

	@Override
	public void initData() {
		
	}
	
	@Override
	public void onLoadFinish(HttpTask task, HttpResult result) {
		Log.e(TAG, result.getData());
		commonProgressDialog.removeDialog();
		if (result != null && !StringUtil.isEmpty(result.getData()) && StringUtil.isGoodJson(result.getData())) {
			CommonResult commonResult = JSON.parseObject(result.getData(),CommonResult.class);
			if (null != commonResult) {
				if(commonResult.validate()){
					JSONArray jsonArray = JSON.parseArray(commonResult.getData());
					if(jsonArray != null && jsonArray.size() > 0){
						initIntegralRecord(jsonArray);
					}else{
						integralRecordAdapter.resetData(null);
					}
				}else{
					integralRecordAdapter.resetData(null);
//					ToastUtil.toast(getApplicationContext(), commonResult.getErrMsg());
				}
			}
		}
	}
	
	private void initIntegralRecord(JSONArray jsonArray){
		List<IntegralRecord> integralRecordList = new ArrayList<IntegralRecord>();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			IntegralRecord integralRecord = JSONObject.toJavaObject(jsonObject, IntegralRecord.class);
			integralRecordList.add(integralRecord);
		}
		
		if(integralRecordList != null && integralRecordList.size() > 0){
			integralRecordAdapter.resetData(integralRecordList);
		}
	}

}
