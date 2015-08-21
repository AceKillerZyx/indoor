package com.indoorsy.frash.personal.activity;

import java.util.HashMap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.indoorsy.frash.Constants;
import com.indoorsy.frash.R;
import com.indoorsy.frash.ReleaseConfigure;
import com.indoorsy.frash.common.activity.BasicActivity;
import com.indoorsy.frash.common.data.bean.CommonResult;
import com.indoorsy.frash.common.view.CircleImageView;
import com.indoorsy.frash.common.view.CommonProgressDialog;
import com.indoorsy.frash.common.view.CustomTitleView;
import com.indoorsy.frash.http.core.HttpParam;
import com.indoorsy.frash.http.core.HttpResult;
import com.indoorsy.frash.http.core.HttpTask;
import com.indoorsy.frash.myinfo.activity.LoginActivity;
import com.indoorsy.frash.util.CommonDataUtil;
import com.indoorsy.frash.util.SharedPreferencesUtil;
import com.indoorsy.frash.util.StringUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
/*
 * 我的会员
 */
public class MyMenberActivity extends BasicActivity {
	private static final String TAG = MyMenberActivity.class.getSimpleName();
	
	private HttpTask httpTask;
	
	private CommonProgressDialog commonProgressDialog;

	private CustomTitleView customTitleView;

	private CircleImageView profileHeaderCircleImageView;

	private RelativeLayout personalMymemberIntegralrecordRelativelLayout;

	private TextView personalMymembersNameTextView,
			personalMymembersLevelvalueTextView, personalMyMemberOrderTextView;
	@Override
	public void onClick(View v) {
		Intent intent=null;
		switch (v.getId()) {
			case R.id.leftImageView:
				this.finish();
				break;
			case R.id.personalMymemberIntegralrecordRelativelLayout:
				intent=new Intent(MyMenberActivity.this,IntegralRecordActivity.class);
				startActivity(intent);
				break;
			case R.id.personalMymembersNameTextView:
				intent=new Intent(MyMenberActivity.this,LoginActivity.class);
				startActivity(intent);
				break;
		}
	}
	
	//查询积分和等级
	public void searchIntegralRecord(){
		HashMap<String,String> paHashMap=CommonDataUtil.getCommonParams(getApplicationContext());
		HttpParam httpParam=new HttpParam(ReleaseConfigure.PERSONAL_VALUE_TYPE_SEARCHUSERINFO, false);
		httpTask=new HttpTask(getApplicationContext(), this);
		httpParam.setParams(paHashMap);
		httpTask.execute(httpParam);
		commonProgressDialog.loadDialog();
	}

	@Override
	public int initLayout() {
		return R.layout.personal_mymembers;
	}

	@Override
	public void initUI() {
		customTitleView=(CustomTitleView) findViewById(R.id.customTitleView);
		customTitleView.setTitle(R.string.personal_mymember_title);
		customTitleView.setLeftImageBg(R.drawable.comm_back);
		customTitleView.setVisibility(View.VISIBLE);
		customTitleView.setOnClickListener(this);
		
		commonProgressDialog = new CommonProgressDialog(this);
		
		personalMymemberIntegralrecordRelativelLayout=(RelativeLayout) findViewById(R.id.personalMymemberIntegralrecordRelativelLayout);
		personalMymemberIntegralrecordRelativelLayout.setOnClickListener(this);
		
		profileHeaderCircleImageView = (CircleImageView)findViewById(R.id.profileHeaderCircleImageView);
		
		personalMymembersNameTextView=(TextView) findViewById(R.id.personalMymembersNameTextView);
		personalMymembersNameTextView.setOnClickListener(this);
		personalMymembersLevelvalueTextView = (TextView)findViewById(R.id.personalMymembersLevelvalueTextView);
		personalMyMemberOrderTextView = (TextView)findViewById(R.id.personalMyMemberOrderTextView);
		
		searchIntegralRecord();
	}

	@Override
	public void initData() {
		if(SharedPreferencesUtil.getUsid(getApplicationContext()) == 0){
			personalMymembersNameTextView.setClickable(true);
			personalMymembersNameTextView.setText(R.string.myinfo_login);
		}else{
			personalMymembersNameTextView.setClickable(false);
			personalMymembersNameTextView.setText(SharedPreferencesUtil.getUsName(getApplicationContext()));
		}
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.personal_header_img)
				.showImageOnFail(R.drawable.personal_header_img)
				.resetViewBeforeLoading(true).cacheOnDisk(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.displayer(new FadeInBitmapDisplayer(300)).build();
		ImageLoader.getInstance().displayImage(SharedPreferencesUtil.getHeaderImage(getApplicationContext()), profileHeaderCircleImageView,options, new SimpleImageLoadingListener(){
			@Override
			public void onLoadingStarted(String imageUri, View view) {
			}
			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
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
				Log.e(TAG, message);
			}
		
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				
			}
		});
	}
	
	@Override
	public void onLoadFinish(HttpTask task, HttpResult result) {
		Log.e(TAG, result.getData());
		commonProgressDialog.removeDialog();
		if (result != null && !StringUtil.isEmpty(result.getData()) && StringUtil.isGoodJson(result.getData())) {
			CommonResult commonResult = JSON.parseObject(result.getData(),CommonResult.class);
			if (null != commonResult) {
				if (commonResult.validate()) {
					JSONObject jsonObject = JSON.parseObject(commonResult.getData());
					if(jsonObject != null && jsonObject.size() > 0){
						initIntegral(jsonObject);
					}
				} else {
//					ToastUtil.toast(getApplicationContext(),commonResult.getErrMsg());
				}
			}
		}
	}
	
	private void initIntegral(JSONObject jsonObject){
		personalMymembersLevelvalueTextView.setText(jsonObject.getString(Constants.CLASS));
		personalMyMemberOrderTextView.setText(jsonObject.getString(Constants.INTEGRAL));
	}

}
