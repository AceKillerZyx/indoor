package com.indoorsy.frash.personal.activity;

import java.util.HashMap;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import com.indoorsy.frash.http.core.HttpParam;
import com.indoorsy.frash.http.core.HttpResult;
import com.indoorsy.frash.http.core.HttpTask;
import com.indoorsy.frash.personal.data.bean.OrderDetails;
import com.indoorsy.frash.util.CommonDataUtil;
import com.indoorsy.frash.util.StringUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class OrderDetailsActivity extends BasicActivity {
	private CustomTitleView customTitleView;
	private HttpTask searchOrderDetailsHttpTask;
	private OrderDetails details;
	private TextView orderDetailsJiaoyiStatesTextView,orderDetailsOrderIdTextView,orderDetailsCountTextView
	,orderDetailsMonryTextView,orderDetailsYunfeiTextView,orderDetailsOkTimeTextView,orderDetailsUsernameTextView
	,orderDetailsShoohuoAddressTextView,OrderDetailsNameTextView,OrderDetailsUnitTextView,OrderDetailsJinETextView;
	private ImageView OrderDetailsImageView;
	private CommonProgressDialog commonProgressDialog;
	private String orderSn;
	
	
	
	//查询交易详情
	public void searchOrderDetails(){
		HashMap<String,String> hashMap=CommonDataUtil.getCommonParams(getApplicationContext());
		hashMap.put(Constants.ORDERNUMBER_SN, orderSn);
		HttpParam httpParam=new HttpParam(ReleaseConfigure.ORDER_DETAILS, false);
		searchOrderDetailsHttpTask=new HttpTask(getApplicationContext(), this);
		httpParam.setParams(hashMap);
		searchOrderDetailsHttpTask.execute(httpParam);
		commonProgressDialog.loadDialog();
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.leftImageView:
			this.finish();
			break;

		default:
			break;
		}
	}

	@Override
	public void onLoadFinish(HttpTask task, HttpResult result) {
		Log.e("交易详情返回结果:", result.getData());
		commonProgressDialog.removeDialog();
		if (result!=null && !StringUtil.isEmpty(result.getData()) && StringUtil.isGoodJson(result.getData()) ) {
			CommonResult commonResult=JSON.parseObject(result.getData(),CommonResult.class);
			if (null!= commonResult) {
				if (commonResult.validate()) {
					JSONArray jsonArray=JSON.parseArray(commonResult.getData());
					if (jsonArray!=null && jsonArray.size()>0) {
						initDetails(jsonArray);
					}
				}
			}
		}
	}

	private void initDetails(JSONArray jsonArray) {
		JSONObject jsonObject=jsonArray.getJSONObject(0);
		details=JSONObject.toJavaObject(jsonObject, OrderDetails.class);
		orderDetailsJiaoyiStatesTextView.setText(details.getOrderstate());
		orderDetailsOrderIdTextView.setText(details.getOrderSn());
		orderDetailsCountTextView.setText(details.getOrderNumber()+" "+details.getUnit());
		orderDetailsMonryTextView.setText(String.valueOf(details.getOrderGoodsAmount()));
		orderDetailsYunfeiTextView.setText(String.valueOf(details.getOrderShippingFee()));
		orderDetailsOkTimeTextView.setText(details.getOrderAddTime());
		orderDetailsUsernameTextView.setText(details.getWhzUsername());
		orderDetailsShoohuoAddressTextView.setText(details.getAddAdderss());
		OrderDetailsNameTextView.setText(details.getGoodsName());
		OrderDetailsUnitTextView.setText(String.valueOf(details.getOrderGoodsAmount())+"元一"+String.valueOf(details.getUnit()));
		OrderDetailsJinETextView.setText(String.valueOf(details.getOrderAmount()));
		initImage();
		//ToastUtil.toast(getApplicationContext(), details.getGoodsBrief());
	}

	//接收图片
	public void initImage(){
		DisplayImageOptions options=new DisplayImageOptions.Builder()
		.showImageForEmptyUri(R.drawable.commodity_firm_product_default)
		.showImageOnFail(R.drawable.commodity_firm_product_default)
		.resetViewBeforeLoading(true).cacheOnDisk(true)
		.imageScaleType(ImageScaleType.EXACTLY)
		.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
		.displayer(new FadeInBitmapDisplayer(300)).build();
		ImageLoader.getInstance().displayImage(details.getGoodsThumb(), OrderDetailsImageView,options,new SimpleImageLoadingListener(){
			@Override
			public void onLoadingStarted(String imageUri, View view) {
			}
			
			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason) {
				String message = null;
				switch (failReason.getType()) {
				case IO_ERROR:
					message = "Input/Output error";
					break;
				case DECODING_ERROR:
					message = "Image can't be decoded";
					break;
				case NETWORK_DENIED:
					message = "Downloads are denied";
					break;
				case OUT_OF_MEMORY:
					message = "Out Of Memory error";
					break;
				case UNKNOWN:
					message = "Unknown error";
					break;
				}
				Log.e("加载图片异常", message);
			}
			
			@Override
			public void onLoadingComplete(String imageUri,
					View view, Bitmap loadedImage) {
			}
			
		});
	}

	@Override
	public int initLayout() {
		return R.layout.order_details;
	}

	@Override
	public void initUI() {
		customTitleView=(CustomTitleView) findViewById(R.id.customTitleView);
		customTitleView.setTitle(R.string.personal_deal_details);
		customTitleView.setLeftImageBg(R.drawable.comm_back);
		customTitleView.setVisibility(View.VISIBLE);
		customTitleView.setOnClickListener(this);
		commonProgressDialog = new CommonProgressDialog(this);
		
		//交易详情控件
		orderDetailsJiaoyiStatesTextView=(TextView) findViewById(R.id.orderDetailsJiaoyiStatesTextView);
		orderDetailsOrderIdTextView=(TextView) findViewById(R.id.orderDetailsOrderIdTextView);
		orderDetailsCountTextView=(TextView) findViewById(R.id.orderDetailsCountTextView);
		orderDetailsMonryTextView=(TextView) findViewById(R.id.orderDetailsMonryTextView);
		orderDetailsYunfeiTextView=(TextView) findViewById(R.id.orderDetailsYunfeiTextView);
		orderDetailsOkTimeTextView=(TextView) findViewById(R.id.orderDetailsOkTimeTextView);
		orderDetailsUsernameTextView=(TextView) findViewById(R.id.orderDetailsUsernameTextView);
		orderDetailsShoohuoAddressTextView=(TextView) findViewById(R.id.orderDetailsShoohuoAddressTextView);
		OrderDetailsNameTextView=(TextView) findViewById(R.id.OrderDetailsNameTextView);
		OrderDetailsUnitTextView=(TextView) findViewById(R.id.OrderDetailsUnitTextView);
		OrderDetailsJinETextView=(TextView) findViewById(R.id.OrderDetailsJinETextView);
		
		OrderDetailsImageView=(ImageView) findViewById(R.id.OrderDetailsImageView);
		orderSn=getIntent().getStringExtra(Constants.ORDERNUMBER_SN);
		//查询交易详情
		searchOrderDetails();
	}

	
	
	@Override
	public void initData() {

	}

}
