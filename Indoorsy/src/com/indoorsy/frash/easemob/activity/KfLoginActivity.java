package com.indoorsy.frash.easemob.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.indoorsy.frash.R;
import com.indoorsy.frash.common.activity.BasicActivity;
import com.indoorsy.frash.home.activity.IndoorsyApplication;
import com.indoorsy.frash.homepage.activity.ProductDetailActivity;
import com.indoorsy.frash.http.core.HttpResult;
import com.indoorsy.frash.http.core.HttpTask;
import com.indoorsy.frash.util.SharedPreferencesUtil;
import com.indoorsy.frash.util.ToastUtil;

public class KfLoginActivity extends BasicActivity{
	
	public static final int REQUEST_CODE_SETNICK = 1;
	private String currentUsername;
	private String currentPassword;
	SharedPreferences sharedPreferences;
	private String image,price;
	@Override
	public void onClick(View v) {
		
	}

	@Override
	public int initLayout() {
		return R.layout.indoor_kf_login;
	}

	@Override
	public void initUI() {
		sharedPreferences = getSharedPreferences("shared", Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();// 获取编辑器
		if (sharedPreferences.getBoolean("flag", true)) {
			editor.putBoolean("flag", false);
			editor.commit();
			// 自动生成账号
			if(SharedPreferencesUtil.isThirdLogin(getApplicationContext())){
				currentUsername = SharedPreferencesUtil.getOpenId(getApplicationContext());
			}else{
				currentUsername = SharedPreferencesUtil.getUsName(getApplicationContext());
			}
			currentPassword = "123456";
			/*if(SharedPreferencesUtil.isThirdLogin(getApplicationContext())){
				currentPassword = "123456";
			}else{
				currentPassword = SharedPreferencesUtil.getUsPassword(getApplicationContext());
			}*/
			
			Log.e("KFLOGINACTIVITY--", "currentUsername:" + currentUsername + "--currentPassword:" + currentPassword);

			editor.putString("name", currentUsername);
			editor.putString("pwd", currentPassword);
			editor.commit();

			CreateAccountTask task = new CreateAccountTask();
			task.execute(currentUsername, currentPassword);
		} else {
			currentUsername = sharedPreferences.getString("name", "");
			currentPassword = sharedPreferences.getString("pwd", "");
			Log.e("KFLOGINACTIVITY**", "currentUsername:" + currentUsername + "--currentPassword:" + currentPassword);
			if(EMChat.getInstance().isLoggedIn()){
//				EMChatManager.getInstance().logout(null);
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							EMChatManager.getInstance().loadAllConversations();
						} catch (Exception e) {
							e.printStackTrace();
							// 取好友或者群聊失败，不让进入主页面，也可以不管这个exception继续进到主页面
							runOnUiThread(new Runnable() {
								public void run() {
									IndoorsyApplication.getInstance().logout(null);
									ToastUtil.toast(getApplicationContext(), R.string.is_contact_customer_failure);
								}
							});
							return;
						}
						
						KfLoginActivity.this.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								startActivity(new Intent(KfLoginActivity.this,ChatActivity.class).putExtra("userId","customers").putExtra("image", image).putExtra("price", price));
								finish();
							}
						});
						
					}
				}).start();
				
			}else{
				openChat();
			}
			
		}
	}

	@Override
	public void initData() {
		image = getIntent().getStringExtra("image");
		price = getIntent().getStringExtra("price");
	}
	
	@Override
	public void onLoadFinish(HttpTask task, HttpResult result) {
		
	}
	
	private class CreateAccountTask extends AsyncTask<String, Void, String> {
		protected String doInBackground(String... args) {
			String userid = args[0];
			String pwd = args[1];
			try {
				EMChatManager.getInstance().createAccountOnServer(userid, pwd);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return userid;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			openChat();
		}
	}
	
	private void openChat() {
		// 调用sdk登陆方法登陆聊天服务器
		EMChatManager.getInstance().login(currentUsername, currentPassword,
				new EMCallBack() {
					@Override
					public void onSuccess() {
						IndoorsyApplication.getInstance().setUserName(currentUsername);
						IndoorsyApplication.getInstance().setPassword(currentPassword);
						try {
							EMChatManager.getInstance().loadAllConversations();
						} catch (Exception e) {
							e.printStackTrace();
							// 取好友或者群聊失败，不让进入主页面，也可以不管这个exception继续进到主页面
							runOnUiThread(new Runnable() {
								public void run() {
									IndoorsyApplication.getInstance().logout(null);
									ToastUtil.toast(getApplicationContext(),R.string.is_contact_customer_failure);
								}
							});
							return;
						}
						// 进入主页面
						startActivity(new Intent(KfLoginActivity.this,ChatActivity.class).putExtra("userId","customers").putExtra("image", image).putExtra("price", price));
						finish();
					}

					@Override
					public void onProgress(int progress, String status) {
					}

					@Override
					public void onError(final int code, final String message) {
						runOnUiThread(new Runnable() {
							public void run() {
								Intent intent = new Intent();
								intent.setClass(getApplication(),ProductDetailActivity.class);
								startActivity(intent);
								ToastUtil.toast(getApplicationContext(),R.string.is_contact_customer_failure_seconed);
							}
						});
					}
				});
	}

}
