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
 * 支付帮助
 */
public class PayHelpActivity extends BasicActivity{
	
	private CustomTitleView customTitleView;
	
	private WebView payHelpWebView;
	
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
		
	}

	@Override
	public int initLayout() {
		return R.layout.personal_payhelp;
	}

	@Override
	public void initUI() {
		customTitleView=(CustomTitleView) findViewById(R.id.customTitleView);
		customTitleView.setLeftImageBg(R.drawable.comm_back);
		customTitleView.setLeftImageVisibility(View.VISIBLE);
		customTitleView.setTitle(R.string.personal_payhelp_title);
		customTitleView.setOnClickListener(this);
		
		payHelpWebView = (WebView) findViewById(R.id.payHelpWebView);
		// WebView加载web资源
		payHelpWebView.loadUrl(ReleaseConfigure.WEBVIEW_VALUE_TYPE_PAY_HELP);
		// 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
		payHelpWebView.setWebViewClient(new WebViewClient() {
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
