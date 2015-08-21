package com.indoorsy.frash.homepage.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

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
import com.indoorsy.frash.homepage.adapter.HomePageListAdapter;
import com.indoorsy.frash.homepage.data.bean.Goods;
import com.indoorsy.frash.http.core.HttpParam;
import com.indoorsy.frash.http.core.HttpResult;
import com.indoorsy.frash.http.core.HttpTask;
import com.indoorsy.frash.util.CommonDataUtil;
import com.indoorsy.frash.util.StringUtil;
import com.indoorsy.frash.util.ToastUtil;

public class HomePageSearchActivtiy extends BasicActivity  {
	
	private static final String TAG = HomePageSearchActivtiy.class.getSimpleName();

	// 顶栏
	private CustomTitleView customTitleView;
	private Boolean isSearch = false;
	private CommonProgressDialog pd;
	
	// 查询
	private HttpTask searchHttpTask;
	private int goodsid ;
	private ListView SearchListView;
	private HomePageListAdapter searchAdapter; 
	private EditText SearchEditText;
	private ImageButton SearchImageButton;
	

	@Override
	public int initLayout() {
		return R.layout.homepage_titlebar_search;
	}

	@Override
	public void initUI() {
		if (getIntent().getIntExtra(Constants.HOMEPAGE_PRODUCT_ID , 0)+"" != null) {
			goodsid = getIntent().getIntExtra(Constants.HOMEPAGE_PRODUCT_ID , 0) ;
			Log.e(TAG, "收到的goodsid = "+ goodsid);
		}
		
		// 顶栏
		customTitleView = (CustomTitleView) findViewById(R.id.customTitleView);
		customTitleView.setTitle("搜索");
		customTitleView.setLeftImageBg(R.drawable.comm_back);
		customTitleView.setRightTextViewVisibility(View.INVISIBLE);
		customTitleView.setOnClickListener(this);
		pd = new CommonProgressDialog(this);

		// 详情列表
		SearchImageButton = (ImageButton) findViewById(R.id.SearchImageButton);
		SearchImageButton.setOnClickListener(this);
		SearchListView = (ListView) findViewById(R.id.SearchListView);
		searchAdapter = new HomePageListAdapter(getApplicationContext());
		SearchListView.setAdapter(searchAdapter);
		SearchListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				Intent  intent = new Intent(HomePageSearchActivtiy.this, ProductDetailActivity.class);
				intent.putExtra(Constants.HOMEPAGE_PRODUCT_ID, ((Goods)arg0.getAdapter().getItem(arg2)).getGoodsid());
				intent.putExtra(Constants.HOMEPAGE_PRODUCT_NAME, ((Goods)arg0.getAdapter().getItem(arg2)).getGoodsName());
				intent.putExtra(Constants.HOMEPAGE_PRODUCT_DESC, ((Goods)arg0.getAdapter().getItem(arg2)).getGoodsDesc());
				intent.putExtra(Constants.HOMEPAGE_PRODUCT_THUMB, ((Goods)arg0.getAdapter().getItem(arg2)).getGoodsThumb());
				startActivity(intent);
			}
		});
		
		SearchEditText = (EditText) findViewById(R.id.SearchEditText);
		SearchEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
				//查询
				if (!StringUtil.isEmpty(SearchEditText.getText().toString().trim()) && !isSearch) {
					search(SearchEditText.getText().toString().trim());
				}
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
			}
		});
		
		
		
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.leftImageView:
			finish();
			break;
		case R.id.SearchImageButton:
			//查询
			if (!StringUtil.isEmpty(SearchEditText.getText().toString().trim())) {
				if (!isSearch) {
					search(SearchEditText.getText().toString().trim());
				}
			} else {
				ToastUtil.toast(getApplicationContext(), "请输入搜索关键字");
			}
			break;
		}

	}

	
	private void search(String keyword) {
		isSearch = true ;
		HashMap<String, String> paramMap = CommonDataUtil.getCommonParams(getApplicationContext());
		paramMap.put(Constants.HOMEPAGE_PRODUCT_KEYWORDS, keyword);
		HttpParam httpParam = new HttpParam(ReleaseConfigure.HOMEPAGE_VALUE_TYPE_SEARCH, false); // GET
		searchHttpTask = new HttpTask(getApplicationContext(), this);
		httpParam.setParams(paramMap);
		searchHttpTask.execute(httpParam);
		pd.loadDialog();
	}

	
	@Override
	public void onLoadFinish(HttpTask task, HttpResult result) {
		Log.e(TAG, result.getData());
		pd.removeDialog();
		isSearch = false ;
		if (result != null && !StringUtil.isEmpty(result.getData()) && StringUtil.isGoodJson(result.getData()) && this != null && !this.isFinishing()) {
			CommonResult commonResult = JSON.parseObject(result.getData(),CommonResult.class);
			if (null != commonResult) {
				if (task == searchHttpTask) {
					if (commonResult.validate()) {
						JSONArray jsonArray = JSON.parseArray(commonResult.getData());
						if (null != jsonArray && jsonArray.size() > 0) {
							initSearch(jsonArray);
						} 
					} else {
//						ToastUtil.toast(getApplicationContext(),commonResult.getErrMsg());
					}
				}
			}
		}
	}

	private void initSearch(JSONArray jsonArray) {
		List<Goods> goodslist = new ArrayList<Goods>();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			Goods goods = JSONObject.toJavaObject(jsonObject, Goods.class);
			goodslist.add(goods);
		}

		if (goodslist != null && goodslist.size() > 0) {
			searchAdapter.resetData(goodslist);
		}
	}

	@Override
	public void initData() {
	}

}
