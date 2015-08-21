package com.indoorsy.frash.homepage.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.util.Log;
import android.view.View;
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
import com.indoorsy.frash.homepage.adapter.ProductDetailImgsListAdapter;
import com.indoorsy.frash.homepage.data.bean.Imgs;
import com.indoorsy.frash.http.core.HttpParam;
import com.indoorsy.frash.http.core.HttpResult;
import com.indoorsy.frash.http.core.HttpTask;
import com.indoorsy.frash.util.CommonDataUtil;
import com.indoorsy.frash.util.StringUtil;

public class IMGDetail extends BasicActivity {
	
	private static final String TAG = IMGDetail.class.getSimpleName();

	// 顶栏
	private CustomTitleView customTitleView;
	private CommonProgressDialog pd; 
	
	// 图文详情列表
	private HttpTask searchImgHttpTask;
	private int goodsid ;
	private ListView ImgListView;
	private ProductDetailImgsListAdapter imagesListAdapter;

	@Override
	public int initLayout() {
		return R.layout.homepage_imgdetail;
	}

	@Override
	public void initUI() {
		if (getIntent().getIntExtra(Constants.HOMEPAGE_PRODUCT_ID , 0)+"" != null) {
			goodsid = getIntent().getIntExtra(Constants.HOMEPAGE_PRODUCT_ID , 0) ;
			Log.e(TAG, "收到的goodsid = "+ goodsid);
		}
		
		// 顶栏
		customTitleView = (CustomTitleView) findViewById(R.id.customTitleView);
		customTitleView.setTitle("图文详情");
		customTitleView.setLeftImageBg(R.drawable.comm_back);
		customTitleView.setRightTextViewVisibility(View.INVISIBLE);
		customTitleView.setOnClickListener(this);
		pd = new CommonProgressDialog(this);
		
		// 详情列表
		ImgListView = (ListView) findViewById(R.id.ImgListView);
		imagesListAdapter = new ProductDetailImgsListAdapter(getApplicationContext());
		ImgListView.setAdapter(imagesListAdapter);
		
		//查询图文详情
		searchImg();
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.leftImageView:
			finish();
			break;
		}

	}

	
	private void searchImg() {
		HashMap<String, String> paramMap = CommonDataUtil.getCommonParams(getApplicationContext());
		paramMap.put(Constants.HOMEPAGE_PRODUCT_DETAIL_GOODSID, String.valueOf(goodsid));
		HttpParam httpParam = new HttpParam(ReleaseConfigure.HOMEPAGE_VALUE_TYPE_IMG_DETAIL, false); // GET
		searchImgHttpTask = new HttpTask(getApplicationContext(), this);
		httpParam.setParams(paramMap);
		searchImgHttpTask.execute(httpParam);
		pd.loadDialog();
	}

	
	@Override
	public void onLoadFinish(HttpTask task, HttpResult result) {
		Log.e(TAG, result.getData());
		pd.removeDialog();
		if (result != null && !StringUtil.isEmpty(result.getData()) && StringUtil.isGoodJson(result.getData())) {
			CommonResult commonResult = JSON.parseObject(result.getData(),CommonResult.class);
			if (null != commonResult) {
				if (task == searchImgHttpTask) {
					if (commonResult.validate()) {
						JSONArray jsonArray = JSON.parseArray(commonResult.getData());
						if (null != jsonArray && jsonArray.size() > 0) {
							initImg(jsonArray);
						} 
					} else {
//						ToastUtil.toast(getApplicationContext(),commonResult.getErrMsg());
					}
				}
			}
		}
	}

	private void initImg(JSONArray jsonArray) {
		List<Imgs> imgList = new ArrayList<Imgs>();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			Imgs imgs = JSONObject.toJavaObject(jsonObject, Imgs.class);
			imgList.add(imgs);
		}

		if (imgList != null && imgList.size() > 0) {
			imagesListAdapter.resetData(imgList);
		}
	}

	@Override
	public void initData() {
	}

}
