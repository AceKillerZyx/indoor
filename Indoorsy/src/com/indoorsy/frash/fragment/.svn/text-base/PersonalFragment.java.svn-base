package com.indoorsy.frash.fragment;

import java.util.HashMap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.indoorsy.frash.Constants;
import com.indoorsy.frash.R;
import com.indoorsy.frash.ReleaseConfigure;
import com.indoorsy.frash.common.data.bean.CommonResult;
import com.indoorsy.frash.common.view.CircleImageView;
import com.indoorsy.frash.common.view.CommonProgressDialog;
import com.indoorsy.frash.common.view.CustomTitleView;
import com.indoorsy.frash.homepage.activity.ShareActivity;
import com.indoorsy.frash.http.core.HttpListener;
import com.indoorsy.frash.http.core.HttpParam;
import com.indoorsy.frash.http.core.HttpResult;
import com.indoorsy.frash.http.core.HttpTask;
import com.indoorsy.frash.myinfo.activity.LoginActivity;
import com.indoorsy.frash.personal.activity.AboutUSActivity;
import com.indoorsy.frash.personal.activity.ContactUsActivity;
import com.indoorsy.frash.personal.activity.FeedBackActivity;
import com.indoorsy.frash.personal.activity.MyCollectionActivity;
import com.indoorsy.frash.personal.activity.MyMenberActivity;
import com.indoorsy.frash.personal.activity.MyOrderActivity;
import com.indoorsy.frash.personal.activity.PayHelpActivity;
import com.indoorsy.frash.personal.activity.UpdatePersonalActivity;
import com.indoorsy.frash.util.CleanCacheUtil;
import com.indoorsy.frash.util.CommonDataUtil;
import com.indoorsy.frash.util.SharedPreferencesUtil;
import com.indoorsy.frash.util.StringUtil;
import com.indoorsy.frash.util.ToastUtil;
import com.indoorsy.frash.util.UpdateManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * 个人
 */
public class PersonalFragment extends Fragment implements HttpListener,OnClickListener{
	private static final String TAG = PersonalFragment.class.getSimpleName();
	private HttpTask exitLoginHttpTask,searchUserInfoHttpTask;
//	private CommonLockUpView lockUpView;
	private CommonProgressDialog pd;
	private CustomTitleView customTitleView;

	private TextView personalFragmentMyOrderTextView,
			personalFragmentMyMemberTextView,
			personalFragmentMycollectTextView, personalFragmentPayHelpTextView,
			personalFragmentInviteFriendsTextView,
			personalFragmentFeedBackTextView,
			personalFragmentCheckUpdateTextView,
			personalFragmentAnoutUSTextView, personalFragmentContactTextView,
			personalFragmentCleanextView, personalFragmentDaiFuKuanTextView,
			personalFragmentDaiFaHuoTextView,
			personalFragmentDaiShouHuoTextView,
			personalFragmentDaiPingJiaTextView,
			personalFragmentTuiKuanJiLuTextView, personalFragmentLoginTextView,
			personalNameTextView, personalGradeTextView,
			personalIntegralTextView;
	private Button personalFragmentLogOutButton;
	
	private RelativeLayout personalHeaderRelativeLayout;
	
	private CircleImageView personalHeaderCircleImageView;
	
	private UpdateManager updateManager;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		return inflater.inflate(R.layout.personal_fragment, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		customTitleView = (CustomTitleView)getView().findViewById(R.id.customTitleView);
        customTitleView.setTitle(R.string.home_personal);
        
        personalHeaderCircleImageView = (CircleImageView)getView().findViewById(R.id.personalHeaderCircleImageView);
        personalNameTextView = (TextView)getView().findViewById(R.id.personalNameTextView);
        personalGradeTextView = (TextView)getView().findViewById(R.id.personalGradeTextView);
        personalIntegralTextView = (TextView)getView().findViewById(R.id.personalIntegralTextView);
        
        pd = new CommonProgressDialog(getActivity());
		
		personalFragmentMyOrderTextView=(TextView) getView().findViewById(R.id.personalFragmentMyOrderTextView);
		personalFragmentMyOrderTextView.setOnClickListener(this);
		personalFragmentMyMemberTextView=(TextView) getView().findViewById(R.id.personalFragmentMyMemberTextView);
		personalFragmentMyMemberTextView.setOnClickListener(this);
		personalFragmentMycollectTextView=(TextView) getView().findViewById(R.id.personalFragmentMycollectTextView);
		personalFragmentMycollectTextView.setOnClickListener(this);
		personalFragmentPayHelpTextView=(TextView) getView().findViewById(R.id.personalFragmentPayHelpTextView);
		personalFragmentPayHelpTextView.setOnClickListener(this);
		personalFragmentInviteFriendsTextView=(TextView) getView().findViewById(R.id.personalFragmentInviteFriendsTextView);
		personalFragmentInviteFriendsTextView.setOnClickListener(this);
		personalFragmentFeedBackTextView=(TextView) getView().findViewById(R.id.personalFragmentFeedBackTextView);
		personalFragmentFeedBackTextView.setOnClickListener(this);
		personalFragmentCheckUpdateTextView=(TextView) getView().findViewById(R.id.personalFragmentCheckUpdateTextView);
		personalFragmentCheckUpdateTextView.setOnClickListener(this);
		personalFragmentAnoutUSTextView=(TextView) getView().findViewById(R.id.personalFragmentAnoutUSTextView);
		personalFragmentAnoutUSTextView.setOnClickListener(this);
		personalFragmentContactTextView=(TextView) getView().findViewById(R.id.personalFragmentContactTextView);
		personalFragmentContactTextView.setOnClickListener(this);
		personalFragmentCleanextView=(TextView) getView().findViewById(R.id.personalFragmentCleanextView);
		personalFragmentCleanextView.setOnClickListener(this);
		personalFragmentLogOutButton=(Button) getView().findViewById(R.id.personalFragmentLogOutButton);
		personalFragmentLogOutButton.setOnClickListener(this);
		
		personalFragmentDaiFuKuanTextView=(TextView) getView().findViewById(R.id.personalFragmentDaiFuKuanTextView);
		personalFragmentDaiFuKuanTextView.setOnClickListener(this);
		personalFragmentDaiFaHuoTextView=(TextView) getView().findViewById(R.id.personalFragmentDaiFaHuoTextView);
		personalFragmentDaiFaHuoTextView.setOnClickListener(this);
		personalFragmentDaiShouHuoTextView=(TextView) getView().findViewById(R.id.personalFragmentDaiShouHuoTextView);
		personalFragmentDaiShouHuoTextView.setOnClickListener(this);
		personalFragmentDaiPingJiaTextView=(TextView) getView().findViewById(R.id.personalFragmentDaiPingJiaTextView);
		personalFragmentDaiPingJiaTextView.setOnClickListener(this);
		personalFragmentTuiKuanJiLuTextView=(TextView) getView().findViewById(R.id.personalFragmentTuiKuanJiLuTextView);
		personalFragmentTuiKuanJiLuTextView.setOnClickListener(this);
		
		personalHeaderRelativeLayout=(RelativeLayout) getView().findViewById(R.id.personalHeaderRelativeLayout);
		personalHeaderRelativeLayout.setOnClickListener(this);
		personalFragmentLoginTextView=(TextView) getView().findViewById(R.id.personalFragmentLoginTextView);
		personalFragmentLoginTextView.setOnClickListener(this);
		if(SharedPreferencesUtil.getUsid(getActivity()) == 0){
			personalFragmentLoginTextView.setVisibility(View.VISIBLE);
		}else{
			personalFragmentLoginTextView.setVisibility(View.GONE);
		}
		updateManager = new UpdateManager(getActivity(), 1);
		searchIntegralRecord();
	}
	
	private void initUserInfo(JSONObject jsonObject){
		String name = SharedPreferencesUtil.getUsName(getActivity());
        if(SharedPreferencesUtil.isThirdLogin(getActivity())){
        	personalNameTextView.setText(name);
        }else{
        	personalNameTextView.setText(name);
        }
        
        personalGradeTextView.setText(jsonObject.getString(Constants.CLASS));
        personalIntegralTextView.setText(jsonObject.getString(Constants.INTEGRAL));
        
        DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.personal_header_img)
				.showImageOnFail(R.drawable.personal_header_img)
				.resetViewBeforeLoading(true).cacheOnDisk(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.displayer(new FadeInBitmapDisplayer(300)).build();
		ImageLoader.getInstance().displayImage(SharedPreferencesUtil.getHeaderImage(getActivity()), personalHeaderCircleImageView,options, new SimpleImageLoadingListener(){
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
	public void noNet(HttpTask task) {
		pd.removeDialog();		
	}

	@Override
	public void noData(HttpTask task, HttpResult result) {
		pd.removeDialog();		
		
	}

	@Override
	public void onLoadFailed(HttpTask task, HttpResult result) {
		pd.removeDialog();		
	}

	@Override
	public void onLoadFinish(HttpTask task, HttpResult result) {
		pd.removeDialog();		
		Log.e(TAG, result.getData());
		if (result != null && !StringUtil.isEmpty(result.getData()) && StringUtil.isGoodJson(result.getData()) && getActivity() != null && !getActivity().isFinishing()) {
			CommonResult commonResult = JSON.parseObject(result.getData(),CommonResult.class);
			if (null != commonResult) {
				if(task == exitLoginHttpTask){
					if (commonResult.validate()) {
						ToastUtil.toast(getActivity(), R.string.myinfo_fragment_loginout_success);
						goLogin();
					}else{
//						ToastUtil.toast(getActivity(), commonResult.getErrMsg());
					}
				}
				
				if(task == searchUserInfoHttpTask){
					if (commonResult.validate()) {
						JSONObject jsonObject = JSON.parseObject(commonResult.getData());
						if(jsonObject != null && jsonObject.size() > 0){
							initUserInfo(jsonObject);
						}
					}else{
//						ToastUtil.toast(getActivity(), commonResult.getErrMsg());
					}
				}
			}
		}
		
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
			case R.id.personalFragmentMyOrderTextView:
				intent=new Intent(getActivity(),MyOrderActivity.class);
				intent.putExtra(Constants.TYPE, 0);
				startActivity(intent);
				break;
			case R.id.personalFragmentMyMemberTextView:
				intent=new Intent(getActivity(),MyMenberActivity.class);
				startActivity(intent);
				break;
			case R.id.personalFragmentMycollectTextView:
				intent=new Intent(getActivity(),MyCollectionActivity.class);
				startActivity(intent);
				break;
			case R.id.personalFragmentPayHelpTextView:
				intent=new Intent(getActivity(),PayHelpActivity.class);
				startActivity(intent);
				break;
			case R.id.personalFragmentInviteFriendsTextView:
				intent = new Intent(getActivity(),ShareActivity.class);
				startActivity(intent);
				break;
			case R.id.personalFragmentFeedBackTextView:
				intent=new Intent(getActivity(),FeedBackActivity.class);
				startActivity(intent);
				break;
			case R.id.personalFragmentCheckUpdateTextView:
				updateManager.checkUpdateInfo();
				break;
			case R.id.personalFragmentAnoutUSTextView:
				intent=new Intent(getActivity(),AboutUSActivity.class);
				startActivity(intent);
				break;
			case R.id.personalFragmentContactTextView:
				intent=new Intent(getActivity(),ContactUsActivity.class);
				startActivity(intent);
				break;
			case R.id.personalFragmentCleanextView:
				CleanCacheUtil.cleanInternalCache(getActivity());
				break;
			case R.id.personalFragmentDaiFuKuanTextView:
				intent=new Intent(getActivity(),MyOrderActivity.class);
				intent.putExtra(Constants.TYPE, 1);
				startActivity(intent);
				break;
			case R.id.personalFragmentDaiFaHuoTextView:
				intent=new Intent(getActivity(),MyOrderActivity.class);
				intent.putExtra(Constants.TYPE, 2);
				startActivity(intent);
				break;
			case R.id.personalFragmentDaiShouHuoTextView:
				intent=new Intent(getActivity(),MyOrderActivity.class);
				intent.putExtra(Constants.TYPE, 3);
				startActivity(intent);
				break;
			case R.id.personalFragmentDaiPingJiaTextView:
				intent=new Intent(getActivity(),MyOrderActivity.class);
				intent.putExtra(Constants.TYPE, 4);
				startActivity(intent);
				break;
			case R.id.personalFragmentTuiKuanJiLuTextView:
				intent=new Intent(getActivity(),MyOrderActivity.class);
				intent.putExtra(Constants.TYPE, 5);
				startActivity(intent);
				break;
			case R.id.personalHeaderRelativeLayout:
				intent=new Intent(getActivity(),UpdatePersonalActivity.class);
				startActivityForResult(intent, 0);
				break;
			case R.id.personalFragmentLoginTextView:
				intent=new Intent(getActivity(),LoginActivity.class);
				startActivity(intent);
				break;
			case R.id.personalFragmentLogOutButton:
				Log.e(TAG, "isThirdLogin=" + SharedPreferencesUtil.isThirdLogin(getActivity()));
				if(SharedPreferencesUtil.isThirdLogin(getActivity())){
					exitLogin(ReleaseConfigure.MYINFO_VALUE_TYPE_THIRD_LOGINOUT);
				}else{
					exitLogin(ReleaseConfigure.MYINFO_VALUE_TYPE_LOGINOUT);
				}
				break;
		}
	}
	
	//查询积分和等级
	public void searchIntegralRecord(){
		HashMap<String,String> paHashMap=CommonDataUtil.getCommonParams(getActivity());
		HttpParam httpParam=new HttpParam(ReleaseConfigure.PERSONAL_VALUE_TYPE_SEARCHUSERINFO, false);
		searchUserInfoHttpTask=new HttpTask(getActivity(), this);
		httpParam.setParams(paHashMap);
		searchUserInfoHttpTask.execute(httpParam);
		pd.loadDialog();
	}
	
	//退出登录
	public void exitLogin(String url){
		HashMap<String,String> parHashMap=CommonDataUtil.getCommonParams(getActivity());
		if(SharedPreferencesUtil.isThirdLogin(getActivity())){
			parHashMap.put(Constants.OPENID, SharedPreferencesUtil.getOpenId(getActivity()));
		}else{
			parHashMap.put(Constants.ULOGINID, String.valueOf(SharedPreferencesUtil.getUloginid(getActivity())));
		}
		HttpParam httpParam=new HttpParam(url, false);
		exitLoginHttpTask=new HttpTask(getActivity(), this);
		httpParam.setParams(parHashMap);
		exitLoginHttpTask.execute(httpParam);
		pd.loadDialog();
	}
	
	//去登陆界面
	public void goLogin(){
		Intent intent = new Intent();  
        intent.setAction(Constants.EXIT_LOGIN);  
        getActivity().sendBroadcast(intent);  
        SharedPreferencesUtil.putInt(getActivity(), SharedPreferencesUtil.USER_INFO_FILE_NAME, SharedPreferencesUtil.USER_INFO_KEY_USID, 0);
		startActivity(new Intent(getActivity(),LoginActivity.class));
		getActivity().finish();
	}
	
	@SuppressWarnings("static-access")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == getActivity().RESULT_OK){
			searchIntegralRecord();
			Log.e(TAG, "修改后返回再次查");
		}
	}
}
