package com.indoorsy.frash.homepage.activity;

import java.util.HashMap;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.indoorsy.frash.common.view.RushBuyCountDownTimerView;
import com.indoorsy.frash.homepage.adapter.SeckillAdapter;
import com.indoorsy.frash.homepage.data.bean.SeckKill;
import com.indoorsy.frash.homepage.data.bean.SeckTime;
import com.indoorsy.frash.homepage.data.bean.SeckTk;
import com.indoorsy.frash.http.core.HttpParam;
import com.indoorsy.frash.http.core.HttpResult;
import com.indoorsy.frash.http.core.HttpTask;
import com.indoorsy.frash.util.CommonDataUtil;
import com.indoorsy.frash.util.DateUtil;
import com.indoorsy.frash.util.StringUtil;
import com.indoorsy.frash.util.ToastUtil;

public class SeckillActivity extends BasicActivity implements OnItemClickListener{
	private static final String TAG = SeckillActivity.class.getSimpleName();

	
	private int type = 0;
	
	private SeckKill kill = null;
	// 顶栏
	private CustomTitleView customTitleView;
	private CommonProgressDialog pd;
	
	//导航栏
	private TextView SeckillNavLeft,SeckillNavMid,SeckillNavRight;
	private RushBuyCountDownTimerView SeckillTimerView;
	private LinearLayout SeckillCountDownTimerLinearLayout;

	//秒杀列表
	private HttpTask currentseckillHttpTask;
	private ListView SeckillListView;
	private SeckillAdapter scekillAdapter;
	
	
	@Override
	public int initLayout() {
		return R.layout.homepage_seckill;
	}

	@Override
	public void initUI() {
		// 顶栏
		pd = new CommonProgressDialog(this);
		
		customTitleView = (CustomTitleView) findViewById(R.id.customTitleView);
		customTitleView.setTitle("秒杀");
		customTitleView.setLeftImageBg(R.drawable.comm_back);
		customTitleView.setRightImageVisibility(View.GONE);
		customTitleView.setRightTextViewVisibility(View.GONE);
		customTitleView.setOnClickListener(this);

		// 导航栏
		SeckillNavLeft = (TextView) findViewById(R.id.SeckillNavLeft);
		SeckillNavMid = (TextView) findViewById(R.id.SeckillNavMid);
		SeckillNavRight = (TextView) findViewById(R.id.SeckillNavRight);
		SeckillNavLeft.setOnClickListener(this);
		SeckillNavMid.setOnClickListener(this);
		SeckillNavRight.setOnClickListener(this);
		SeckillTimerView = (RushBuyCountDownTimerView) findViewById(R.id.SeckillTimerView);
		SeckillCountDownTimerLinearLayout = (LinearLayout) findViewById(R.id.SeckillCountDownTimerLinearLayout);
		
		SeckillListView = (ListView) findViewById(R.id.SeckillListView);
		scekillAdapter = new SeckillAdapter(getApplicationContext());
		SeckillListView.setAdapter(scekillAdapter);
		SeckillListView.setOnItemClickListener(this);
		
		//查询进行中的秒杀
		searchKill();
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.leftImageView:
				finish();
				break;
			case R.id.SeckillNavLeft:
				type = 1;
				SeckillNavLeft.setTextColor(getResources().getColor(R.color.homepage_red_seckill));
				SeckillNavLeft.setBackgroundColor(getResources().getColor(android.R.color.white));
				SeckillNavMid.setTextColor(getResources().getColor(R.color.homepage_gray_text));
				SeckillNavMid.setBackgroundColor(getResources().getColor(R.color.homepage_gray_press));
				SeckillNavRight.setTextColor(getResources().getColor(R.color.homepage_gray_text));
				SeckillNavRight.setBackgroundColor(getResources().getColor(R.color.homepage_gray_press));
				SeckillCountDownTimerLinearLayout.setVisibility(View.GONE);
				if(kill != null && kill.getListk2() != null && kill.getListk2().size() > 0){
					scekillAdapter.resetData(kill.getListk2());
				}else{
					scekillAdapter.resetData(null);
				}
				break;
			case R.id.SeckillNavMid:
				type = 0;
				SeckillNavLeft.setTextColor(getResources().getColor(R.color.homepage_gray_text));
				SeckillNavLeft.setBackgroundColor(getResources().getColor(R.color.homepage_gray_press));
				SeckillNavMid.setTextColor(getResources().getColor(R.color.homepage_red_seckill));
				SeckillNavMid.setBackgroundColor(getResources().getColor(android.R.color.white));
				SeckillNavRight.setTextColor(getResources().getColor(R.color.homepage_gray_text));
				SeckillNavRight.setBackgroundColor(getResources().getColor(R.color.homepage_gray_press));
				SeckillCountDownTimerLinearLayout.setVisibility(View.VISIBLE);
				if(kill != null && kill.getListk() != null && kill.getListk().size() > 0){
					scekillAdapter.resetData(kill.getListk());
				}else{
					scekillAdapter.resetData(null);
				}
				break;
			case R.id.SeckillNavRight:
				type = 2;
				SeckillNavLeft.setTextColor(getResources().getColor(R.color.homepage_gray_text));
				SeckillNavLeft.setBackgroundColor(getResources().getColor(R.color.homepage_gray_press));
				SeckillNavMid.setTextColor(getResources().getColor(R.color.homepage_gray_text));
				SeckillNavMid.setBackgroundColor(getResources().getColor(R.color.homepage_gray_press));
				SeckillNavRight.setTextColor(getResources().getColor(R.color.homepage_red_seckill));
				SeckillNavRight.setBackgroundColor(getResources().getColor(android.R.color.white));
				SeckillCountDownTimerLinearLayout.setVisibility(View.GONE);
				if(kill != null && kill.getListk3() != null && kill.getListk3().size() > 0){
					scekillAdapter.resetData(kill.getListk3());
				}else{
					scekillAdapter.resetData(null);
				}
				break;
		}

	}
	
	private void searchKill() {
		HashMap<String, String> paramMap = CommonDataUtil.getCommonParams(getApplicationContext());
		HttpParam httpParam = new HttpParam(ReleaseConfigure.HOMEPAGE_VALUE_PRODUCT_LIST_TYPE_CURRENT_SECKKILL, false); // GET
		currentseckillHttpTask = new HttpTask(getApplicationContext(), this);
		httpParam.setParams(paramMap);
		currentseckillHttpTask.execute(httpParam);
		pd.loadDialog();
	}

	@Override
	public void onLoadFinish(HttpTask task, HttpResult result) {
		Log.e(TAG, result.getData());
		pd.removeDialog();
		if (result != null && !StringUtil.isEmpty(result.getData()) && StringUtil.isGoodJson(result.getData()) && this !=null && !this.isFinishing() ) {
			CommonResult commonResult = JSON.parseObject(result.getData(),CommonResult.class);
			if (null != commonResult) {
				if (task == currentseckillHttpTask) {
					if (commonResult.validate()) {
						JSONArray jsonArray = JSON.parseArray(commonResult.getData());
						if (null != jsonArray && jsonArray.size() > 0) {
							initCurrentKill(jsonArray);
						} else{
							scekillAdapter.resetData(null);
						}
					} else {
						scekillAdapter.resetData(null);
//						ToastUtil.toast(this,commonResult.getErrMsg());
					}
				}
				
			}
		}

	}

	private void initCurrentKill(JSONArray jsonArray) {
		try {
			JSONObject jsonObject = jsonArray.getJSONObject(0);
			kill = JSONObject.toJavaObject(jsonObject, SeckKill.class);
			if (kill != null && kill.getSecktime().size() > 0 ) {
				for(int i = 0;i < kill.getSecktime().size();i++){
					SeckTime seckTime = kill.getSecktime().get(i);
					if("已结束".equals(seckTime.getContent())){
						SeckillNavLeft.setText(seckTime.getTime() + seckTime.getContent());
					}
					
					if("进行中".equals(seckTime.getContent())){
						SeckillNavMid.setText(seckTime.getTime() + seckTime.getContent());
					}
					
					if("即将开始".equals(seckTime.getContent())){
						SeckillNavRight.setText(seckTime.getTime() + seckTime.getContent());
					}
					
				}
				String today = DateUtil.getToday();
				String endKill = SeckillNavRight.getText().toString().trim();
				String endDate = today.substring(0,10) + " " + endKill.substring(0,5) + ":00";
				String lag = DateUtil.getDatePoor(DateUtil.parse(endDate), DateUtil.parse(today));
				@SuppressWarnings("unused")
				int day = Integer.parseInt(lag.substring(0, lag.lastIndexOf("*")));
				int hours = Integer.parseInt(lag.substring(lag.lastIndexOf("*") + 1, lag.lastIndexOf("-")));
				int minute = Integer.parseInt(lag.substring(lag.lastIndexOf("-") + 1, lag.lastIndexOf("&")));
				int second = Integer.parseInt(lag.substring(lag.lastIndexOf("&")+1, lag.length()));
				SeckillTimerView.setTime(hours, minute, second);
				SeckillTimerView.start();
				if(kill.getListk() != null && kill.getListk().size() > 0){
					scekillAdapter.resetData(kill.getListk());
				}
			}else{
				scekillAdapter.resetData(null);
			}
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		} 
	}


	@Override
	public void initData() {
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent  intent = new Intent(this,ProductDetailActivity.class);
		intent.putExtra(Constants.HOMEPAGE_PRODUCT_TYPE_SECKILL, Constants.TYPE_SECKILL);
		intent.putExtra(Constants.HOMEPAGE_PRODUCT_ID, ((SeckTk)arg0.getAdapter().getItem(arg2)).getGoodsid());
		intent.putExtra(Constants.HOMEPAGE_PRODUCT_NAME, ((SeckTk)arg0.getAdapter().getItem(arg2)).getSeckillContnt());
		intent.putExtra(Constants.HOMEPAGE_PRODUCT_DESC, ((SeckTk)arg0.getAdapter().getItem(arg2)).getSeckillContnt());
		intent.putExtra(Constants.HOMEPAGE_PRODUCT_THUMB, ((SeckTk)arg0.getAdapter().getItem(arg2)).getGoodsImg());
		intent.putExtra(Constants.HOMEPAGE_PRODUCT_PRICE, ((SeckTk)arg0.getAdapter().getItem(arg2)).getSeckillUnit());
		switch (type) {
			case 0:
				startActivity(intent);
				break;
			case 1:
				ToastUtil.toast(getApplicationContext(), "秒杀已结束");
				break;
			case 2:
				ToastUtil.toast(getApplicationContext(), "秒杀即将开始");
				break;
		}
	}
}
