package com.indoorsy.frash.home.activity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.android.pushservice.CustomPushNotificationBuilder;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.indoorsy.frash.Constants;
import com.indoorsy.frash.R;
import com.indoorsy.frash.fragment.CommodityFragment;
import com.indoorsy.frash.fragment.HomepageFragment;
import com.indoorsy.frash.fragment.PersonalFragment;
import com.indoorsy.frash.fragment.ShoppingCartFragment;
import com.indoorsy.frash.util.SharedPreferencesUtil;
import com.indoorsy.frash.util.Utils;


public class IndoorsyActivity  extends FragmentActivity implements OnClickListener {
	
	private static final String TAG = IndoorsyActivity.class.getSimpleName();

	private int i = 0;

	private int current_fg = 1;
	
	private boolean isReceiver = false;

	private static FragmentManager fragmentManager;

	private Fragment homePageFragment, commodityFragment, shoppingCartFragment,
			personalFragment;

	private RadioButton homePageRadioButton, homeCommodityRadioButton,
			homeShoppingCartRadioButton, homePersonalRadioButton;
	
	
	
		

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		SDKInitializer.initialize(getApplicationContext());
		super.onCreate(savedInstanceState);
		
		//百度推送
		Utils.logStringCache = Utils.getLogText(getApplicationContext());
        Resources resource = this.getResources();
        String pkgName = this.getPackageName();
		// Push: 以apikey的方式登录，一般放在主Activity的onCreate中。
        // 这里把apikey存放于manifest文件中，只是一种存放方式，
        // 您可以用自定义常量等其它方式实现，来替换参数中的Utils.getMetaValue(PushDemoActivity.this,
        // "api_key")
		PushManager.startWork(getApplicationContext(),PushConstants.LOGIN_TYPE_API_KEY,Utils.getMetaValue(this, "api_key"));
		// Push: 如果想基于地理位置推送，可以打开支持地理位置的推送的开关
        // PushManager.enableLbs(getApplicationContext());

        // Push: 设置自定义的通知样式，具体API介绍见用户手册，如果想使用系统默认的可以不加这段代码
        // 请在通知推送界面中，高级设置->通知栏样式->自定义样式，选中并且填写值：1，
        // 与下方代码中 PushManager.setNotificationBuilder(this, 1, cBuilder)中的第二个参数对应
        CustomPushNotificationBuilder cBuilder = new CustomPushNotificationBuilder(
                getApplicationContext(), resource.getIdentifier("notification_custom_builder", "layout", pkgName),
                resource.getIdentifier("notification_icon", "id", pkgName),
                resource.getIdentifier("notification_title", "id", pkgName),
                resource.getIdentifier("notification_text", "id", pkgName));
        cBuilder.setNotificationFlags(Notification.FLAG_AUTO_CANCEL);
        cBuilder.setNotificationDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        cBuilder.setStatusbarIcon(this.getApplicationInfo().icon);
        cBuilder.setLayoutDrawable(resource.getIdentifier("simple_notification_icon", "drawable", pkgName));
        PushManager.setNotificationBuilder(this, 1, cBuilder);
		
		//ToastUtil.toast(getApplicationContext(), ""+PushConstants.LOGIN_TYPE_API_KEY);
		//Log.e("开始推动",""+ PushConstants.LOGIN_TYPE_API_KEY);
		
		Log.i(TAG, "=========开始定位=========");
		setContentView(R.layout.indoorsy_home);
		fragmentManager = getSupportFragmentManager(); // 获取FragmentManager实例
		init(); // 初始化页面
		
		
	}

	private void init() {
		homePageRadioButton = (RadioButton) findViewById(R.id.homePageRadioButton);
		homePageRadioButton.setOnClickListener(this);
		homeCommodityRadioButton = (RadioButton) findViewById(R.id.homeCommodityRadioButton);
		homeCommodityRadioButton.setOnClickListener(this);
		homeShoppingCartRadioButton = (RadioButton) findViewById(R.id.homeShoppingCartRadioButton);
		homeShoppingCartRadioButton.setOnClickListener(this);
		homePersonalRadioButton = (RadioButton) findViewById(R.id.homePersonalRadioButton);
		homePersonalRadioButton.setOnClickListener(this);
		homePageFragment = new HomepageFragment();
		commodityFragment = new CommodityFragment();
		shoppingCartFragment = new ShoppingCartFragment();
		personalFragment = new PersonalFragment();
		// 初始化第一个页面
		changeFrament(homePageFragment, null, "homePageFragment");
		homePageRadioButton.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.home_homepage_tab_pressed2, 0, 0);
		homePageRadioButton.setTextColor(getResources().getColor(R.color.common_title_bg));
	}
	
	
	// 切界面
	public void changeFrament(Fragment fragment, Bundle bundle, String tag) {
		for (int i = 0, count = fragmentManager.getBackStackEntryCount(); i < count; i++) {
			fragmentManager.popBackStack();
		}
		FragmentTransaction fg = fragmentManager.beginTransaction();
		fragment.setArguments(bundle);
		fg.add(R.id.fragmentRootLinearLayout, fragment, tag);
		fg.addToBackStack(tag);
		fg.commitAllowingStateLoss();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.homePageRadioButton:
			if (current_fg != 1) {
				homePageFragment = new HomepageFragment();
				changeFrament(homePageFragment, null, "homePageFragment");
				changeRadioButtonImage(v.getId());
				current_fg = 1;
			}
			break;
		case R.id.homeCommodityRadioButton:
			if (current_fg != 2) {
				commodityFragment = new CommodityFragment();
				changeFrament(commodityFragment, null, "commodityFragment");
				changeRadioButtonImage(v.getId());
				current_fg = 2;
			}
			break;
		case R.id.homeShoppingCartRadioButton:
			if (current_fg != 3) {
				shoppingCartFragment = new ShoppingCartFragment();
				changeFrament(shoppingCartFragment, null,
						"shoppingCartFragment");
				changeRadioButtonImage(v.getId());
				current_fg = 3;
			}
			break;
		case R.id.homePersonalRadioButton:
			if (current_fg != 4) {
				personalFragment = new PersonalFragment();
				changeFrament(personalFragment, null, "personalFragment");
				changeRadioButtonImage(v.getId());
				current_fg = 4;
			}
			break;
		}
	}

	/*
	 * 更换RadioButton图片
	 */
	public void changeRadioButtonImage(int btids) {
		int[] imageh = { R.drawable.home_homepage_tab_normal2,
				R.drawable.home_commodity_tab_normal2,
				R.drawable.home_shopping_cart_tab_normal2,
				R.drawable.home_personal_tab_normal2 };
		int[] imagel = { R.drawable.home_homepage_tab_pressed2,
				R.drawable.home_commodity_tab_pressed2,
				R.drawable.home_shopping_cart_tab_pressed2,
				R.drawable.home_personal_tab_pressed2 };
		int[] rabt = { R.id.homePageRadioButton,
				R.id.homeCommodityRadioButton,
				R.id.homeShoppingCartRadioButton,
				R.id.homePersonalRadioButton };
		switch (btids) {
		case R.id.homePageRadioButton:
			changeImage(imageh, imagel, rabt, 0);
			break;
		case R.id.homeCommodityRadioButton:
			changeImage(imageh, imagel, rabt, 1);
			break;
		case R.id.homeShoppingCartRadioButton:
			changeImage(imageh, imagel, rabt, 2);
			break;
		case R.id.homePersonalRadioButton:
			changeImage(imageh, imagel, rabt, 3);
			break;
		}
	}

	/*
	 * 改变图片
	 */
	public void changeImage(int[] image1, int[] image2, int[] rabtid, int index) {
		for (int i = 0; i < image1.length; i++) {
			if (i != index) {
				((RadioButton) findViewById(rabtid[i]))
						.setCompoundDrawablesWithIntrinsicBounds(0, image1[i],
								0, 0);
				((RadioButton) findViewById(rabtid[i])).setTextColor(getResources().getColor(R.color.common_text));
			} else {
				((RadioButton) findViewById(rabtid[i]))
						.setCompoundDrawablesWithIntrinsicBounds(0, image2[i],
								0, 0);
				((RadioButton) findViewById(rabtid[i])).setTextColor(getResources().getColor(R.color.common_title_bg));
			}
		}
	}
	
	protected BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {  
        @Override  
        public void onReceive(Context context, Intent intent) {
            finish();  
        }  
    };  
    
    @Override
	protected void onResume() {
    	super.onResume();
    	// 在当前的activity中注册广播  
        IntentFilter filter = new IntentFilter();  
        filter.addAction(Constants.EXIT_LOGIN);  
        isReceiver = true;
        this.registerReceiver(this.broadcastReceiver, filter);
    };
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	if(isReceiver){
    		this.unregisterReceiver(this.broadcastReceiver);
    	}
    }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (i == 0) {
				Toast.makeText(this, "再点击一次将退出程序", Toast.LENGTH_SHORT).show();
				i++;
			} else {
				this.finish();
			}
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	


}
