package com.indoorsy.frash.home.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.indoorsy.frash.R;
import com.indoorsy.frash.common.activity.BasicActivity;
import com.indoorsy.frash.home.adapter.IndoorsyGuideAdapter;
import com.indoorsy.frash.http.core.HttpResult;
import com.indoorsy.frash.http.core.HttpTask;
import com.indoorsy.frash.myinfo.activity.LoginActivity;
import com.indoorsy.frash.util.SharedPreferencesUtil;
/**
 * @author Administrator
 * 引导页
 */
@SuppressLint("InflateParams") 
public class IndoorsyGuideActivity extends BasicActivity implements OnPageChangeListener {
	
	private List<View> viewList;

	private ImageView[] dotsImageView;

	private ViewPager guideViewPager;
	
	private IndoorsyGuideAdapter indoorsyGuideAdapter;

	private TextView oqGuideStartTextView;
	
	private int[] ids = { R.id.guideOneImageView, R.id.guideTwoImageView,R.id.guideThreeImageView };

	@Override
	public void onClick(View v) {

	}

	@Override
	public int initLayout() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		return R.layout.indoorsy_guide;
	}

	@Override
	public void initUI() {
		LayoutInflater inflater = LayoutInflater.from(this);

		viewList = new ArrayList<View>();
		viewList.add(inflater.inflate(R.layout.indoorsy_guide_one, null));
		viewList.add(inflater.inflate(R.layout.indoorsy_guide_two, null));
		viewList.add(inflater.inflate(R.layout.indoorsy_guide_three, null));
		indoorsyGuideAdapter = new IndoorsyGuideAdapter(viewList);
		guideViewPager = (ViewPager) findViewById(R.id.guideViewPager);
		guideViewPager.setAdapter(indoorsyGuideAdapter);
		oqGuideStartTextView = (TextView) viewList.get(2).findViewById(R.id.guideStartTextView);
		oqGuideStartTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//设置已经引导
				SharedPreferencesUtil.putBoolean(getApplicationContext(), SharedPreferencesUtil.APP_INFO_FILE_NAME, SharedPreferencesUtil.APP_INFO_IS_FIRST_IN, false);
				int usid = SharedPreferencesUtil.getUloginid(getApplicationContext());
				if(usid == 0){
					startActivity(new Intent(IndoorsyGuideActivity.this, LoginActivity.class));
				}else{
					startActivity(new Intent(IndoorsyGuideActivity.this, IndoorsyActivity.class));
				}
				finish();
			}
		});
		guideViewPager.setOnPageChangeListener(this);
	}
	
	@Override
	public void initData() {
		dotsImageView = new ImageView[viewList.size()];
		for (int i = 0; i < viewList.size(); i++) {
			dotsImageView[i] = (ImageView) findViewById(ids[i]);
		}
	}
	
	@Override
	public void onPageScrollStateChanged(int arg0) {
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}
	
	@Override
	public void onPageSelected(int arg0) {
		for (int i = 0; i < ids.length; i++) {
			if (i == arg0)
				dotsImageView[i].setImageResource(R.drawable.indoorsy_guide_dot_focused);
			else {
				dotsImageView[i].setImageResource(R.drawable.indoorsy_guide_dot_normal);
			}
		}
	}

	@Override
	public void onLoadFinish(HttpTask task, HttpResult result) {

	}

}
