package com.indoorsy.frash.homepage.activity;

import java.util.HashMap;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.indoorsy.frash.R;
import com.indoorsy.frash.common.activity.BasicActivity;
import com.indoorsy.frash.http.core.HttpResult;
import com.indoorsy.frash.http.core.HttpTask;
import com.indoorsy.frash.util.StringUtil;
public class ShareActivity extends BasicActivity {
	
	private static final String TAG = ShareActivity.class.getSimpleName();
	
	private static final String TITLE = "我很宅";

	private static final String TEXT = "昆明老百姓的生鲜电子商城";
	
	private static final String URL = "http://101.200.183.79/whzdown/index.htm";
	
	private Button chatShareWXButton, chatShareWXFrinedButton,
			chatShareSinaButton, chatShareCancelButton;
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.chatShareCancelButton:
				this.finish();
				break;
			case R.id.chatShareWXButton:
				shareToWeiXin();
				break;
			case R.id.chatShareWXFrinedButton:
				shareToWeiXinFriend();
				break;
			case R.id.chatShareSinaButton:
				shareToSina();
				break;
		}
	}

	@Override
	public int initLayout() {
		return R.layout.homepage_share_dialog;
	}

	@SuppressWarnings("static-access")
	@Override
	public void initUI() {
		Window window = getWindow();  
        WindowManager.LayoutParams layoutParams = window.getAttributes();  
        //设置窗口的大小及透明度  
        layoutParams.width = LayoutParams.MATCH_PARENT;  
        layoutParams.height = layoutParams.WRAP_CONTENT;  
        layoutParams.gravity = Gravity.BOTTOM;
        window.setAttributes(layoutParams);  
        
        chatShareWXButton = (Button)findViewById(R.id.chatShareWXButton);
        chatShareWXButton.setOnClickListener(this);
        
        chatShareWXFrinedButton = (Button)findViewById(R.id.chatShareWXFrinedButton);
        chatShareWXFrinedButton.setOnClickListener(this);
        
        chatShareSinaButton = (Button)findViewById(R.id.chatShareSinaButton);
        chatShareSinaButton.setOnClickListener(this);
        
        ShareSDK.initSDK(this);// share SDk 的初始化
		
        chatShareCancelButton = (Button)findViewById(R.id.chatShareCancelButton);
        chatShareCancelButton.setOnClickListener(this);
	}

	@Override
	public void initData() {
		
	}
	
	@Override
	public void onLoadFinish(HttpTask task, HttpResult result) {

	}
    
    private void shareToSina() {
		Platform sinaPlatform = ShareSDK.getPlatform(this,SinaWeibo.NAME);
		// 新浪直接分享
		SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();
		sp.setText(TEXT + URL);
		sp.setImageUrl("http://101.200.183.79/whz/Public/images/icon.png");
		sp.setShareType(Platform. SHARE_WEBPAGE);
		sinaPlatform.setPlatformActionListener(shareSDKListener); //设置分享事件回调
		sinaPlatform.share(sp); //执行地址分享
	}

	public void shareToWeiXinFriend() {
		Platform plat = ShareSDK.getPlatform(this, Wechat.NAME);
		Wechat.ShareParams sp = new Wechat.ShareParams();
		sp.setTitle(TITLE);
		sp.setText(TEXT);
		sp.setUrl(URL);
		sp.setImageUrl("http://101.200.183.79/whz/Public/images/icon.png");
		sp.setShareType(Platform. SHARE_WEBPAGE);
		plat.setPlatformActionListener(shareSDKListener);
		plat.share(sp);
	}

	public void shareToWeiXin() {
		Platform plat = ShareSDK.getPlatform(this, WechatMoments.NAME);
		WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
		sp.setTitle(TITLE);
		sp.setText(TEXT);
		sp.setUrl(URL);
		sp.setImageUrl("http://101.200.183.79/whz/Public/images/icon.png");
		sp.setShareType(Platform. SHARE_WEBPAGE);
		plat.setPlatformActionListener(shareSDKListener);
		plat.share(sp);
	}

	PlatformActionListener shareSDKListener = new PlatformActionListener() {
		public void onError(Platform platform, int action, Throwable t) {
			// 操作失败的处理代码
			Log.e(TAG,  t.getMessage());
			if (t.getMessage() != null&& t.getMessage().equals("{\"error\":\"repeat content!\",\"error_code\":20019,\"request\":\"/2/statuses/update.json\"}")) {
				Log.e(TAG, "不允许短时间发表同样的微博");
			} else {
				Log.e(TAG, "分享失败" + t.getMessage());
			}
		}

		public void onCancel(Platform platform, int action) {
			// 操作取消的处理代码
			Log.e(TAG, "取消授权");
		}

		@Override
		public void onComplete(Platform arg0, int arg1,HashMap<String, Object> arg2) {
			// 操作成功的处理代码
			Log.e(TAG, "分享成功" + arg0.getName() + "--" + StringUtil.actionToString(arg1));
			if (arg0.getName().equals(SinaWeibo.NAME) && StringUtil.actionToString(arg1).equals("ACTION_SHARE")) {
				Log.e(TAG, "分享成功");
			}
			if (arg0.getName().equals(Wechat.NAME) && StringUtil.actionToString(arg1).equals("ACTION_SHARE")) {
				Log.e(TAG, "分享成功");
			}
			if (arg0.getName().equals(WechatMoments.NAME) && StringUtil.actionToString(arg1).equals("ACTION_SHARE")) {
				Log.e(TAG, "分享成功");
			}
			
			ShareActivity.this.finish();
		}
	};
   

}
