package com.indoorsy.frash.personal.activity;

import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.indoorsy.frash.R;
import com.indoorsy.frash.ReleaseConfigure;
import com.indoorsy.frash.common.activity.BasicActivity;
import com.indoorsy.frash.common.view.CustomTitleView;
import com.indoorsy.frash.http.core.HttpResult;
import com.indoorsy.frash.http.core.HttpTask;
/*
 * 关于我们
 */
public class AboutUSActivity extends BasicActivity {
	
	private CustomTitleView customTitleView;
	
	private WebView aboutWebView;
	
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
		return R.layout.personal_aboutus;
	}

	@Override
	public void initUI() {
		customTitleView=(CustomTitleView) findViewById(R.id.customTitleView);
		customTitleView.setLeftImageBg(R.drawable.comm_back);
		customTitleView.setLeftImageVisibility(View.VISIBLE);
		customTitleView.setTitle(R.string.personal_aboutus_title);
		customTitleView.setOnClickListener(this);
		
		aboutWebView = (WebView) findViewById(R.id.aboutWebView);
		// WebView加载web资源
		aboutWebView.loadUrl(ReleaseConfigure.WEBVIEW_VALUE_TYPE_ABOUT);
		// 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
		aboutWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
				view.loadUrl(url);
				return true;
			}
		});
	}

	@Override
	public void initData() {

	}

}
