package com.indoorsy.frash.personal.activity;

import android.view.View;

import com.indoorsy.frash.R;
import com.indoorsy.frash.common.activity.BasicActivity;
import com.indoorsy.frash.common.view.CustomTitleView;
import com.indoorsy.frash.http.core.HttpResult;
import com.indoorsy.frash.http.core.HttpTask;
/**
 * 清理缓存
 */
public class CleanActivity extends BasicActivity {
	private CustomTitleView customTitleView;
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
		return R.layout.personal_clean;
	}

	@Override
	public void initUI() {
		customTitleView=(CustomTitleView) findViewById(R.id.customTitleView);
		customTitleView.setLeftImageBg(R.drawable.comm_back);
		customTitleView.setTitle(R.string.personal_clean_title);
		customTitleView.setOnClickListener(this);
		customTitleView.setVisibility(View.VISIBLE);

	}

	@Override
	public void initData() {

	}

}
