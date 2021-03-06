package com.indoorsy.frash.home.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;

import com.indoorsy.frash.R;
import com.indoorsy.frash.common.activity.BasicActivity;
import com.indoorsy.frash.http.core.HttpResult;
import com.indoorsy.frash.http.core.HttpTask;
import com.indoorsy.frash.myinfo.activity.LoginActivity;
import com.indoorsy.frash.util.SharedPreferencesUtil;
/**
 * @author Administrator
 * 欢迎页
 */
public class IndoorsyWelcomeActivity extends BasicActivity{
	
	private boolean isFirstIn = false;
	private static final int TIME = 2000;
	private static final int GO_HOME = 1001;
	private static final int GO_GUIDE = 1002;
	private static final int GO_LOGIN = 1003;
	
	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case GO_HOME:
					goHome();
					break;
				case GO_GUIDE:
					goGuide();
					break;
				case GO_LOGIN:
					goLogin();
					break;
			}
		};
	};

	@Override
	public void onClick(View v) {
		
	}

	@Override
	public int initLayout() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		return R.layout.indoorsy_welcome;
	}

	@Override
	public void initUI() {
		isFirstIn = SharedPreferencesUtil.isFirstIn(getApplicationContext());
		if (!isFirstIn) {
			int usid = SharedPreferencesUtil.getUsid(getApplicationContext());
			Log.e("IndoorsyWelcomeActivity", "usid:" + usid);
			if (usid == 0) {
				mHandler.sendEmptyMessageDelayed(GO_LOGIN, TIME);
			} else {
				// 进入页面
				mHandler.sendEmptyMessageDelayed(GO_HOME, TIME);
			}
		} else {
			mHandler.sendEmptyMessageDelayed(GO_GUIDE, TIME); 
		}
	}
	
	private void goHome() {
		startActivity(new Intent(IndoorsyWelcomeActivity.this, IndoorsyActivity.class));
		finish();
	}

	private void goLogin() {
		startActivity(new Intent(IndoorsyWelcomeActivity.this, LoginActivity.class));
		finish();
	}

	private void goGuide() {
		startActivity(new Intent(IndoorsyWelcomeActivity.this, IndoorsyGuideActivity.class));
		finish();
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		System.exit(0);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
            return true;
        }
        return false;
	}

	@Override
	public void initData() {
	}
	
	@Override
	public void onLoadFinish(HttpTask task, HttpResult result) {
		
	}

}
