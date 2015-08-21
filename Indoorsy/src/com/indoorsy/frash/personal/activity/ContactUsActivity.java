package com.indoorsy.frash.personal.activity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.indoorsy.frash.R;
import com.indoorsy.frash.common.activity.BasicActivity;
import com.indoorsy.frash.common.view.CustomTitleView;
import com.indoorsy.frash.http.core.HttpResult;
import com.indoorsy.frash.http.core.HttpTask;
/*
 * 联系我们
 */
public class ContactUsActivity extends BasicActivity {
	
	private WebView contactUsWebView;
	
	private CustomTitleView customTitleView;
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.leftImageView:
				this.finish();
				break;
		}
	}

	@Override
	public void onLoadFinish(HttpTask task, HttpResult result) {

	}

	@Override
	public int initLayout() {
		return R.layout.personal_invitefriends;
	}

	@Override
	public void initUI() {
		customTitleView=(CustomTitleView) findViewById(R.id.customTitleView);
		customTitleView.setLeftImageBg(R.drawable.comm_back);
		customTitleView.setLeftImageVisibility(View.VISIBLE);
		customTitleView.setTitle(R.string.personal_fragment_contact);
		customTitleView.setOnClickListener(this);
		
		contactUsWebView = (WebView) findViewById(R.id.contactUsWebView);
		contactUsWebView.getSettings().setJavaScriptEnabled(true);

		contactUsWebView.loadUrl("http://101.200.183.79:8080/indoorsy/files/contact_us.html");
		contactUsWebView.setWebViewClient(new WebViewClient(){
			@Override
			public void onLoadResource(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onLoadResource(view, url);
			}
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.contains("tel:")) {
					Uri uri = Uri.parse(url);
					Intent intent = new Intent(Intent.ACTION_DIAL, uri);
					startActivity(intent);
					return true;
				}
				return super.shouldOverrideUrlLoading(view, url);
			}
		});
		
		//Linkify.addLinks("打电话", Linkify.PHONE_NUMBERS);

	}

	@Override
	public void initData() {

	}

}
