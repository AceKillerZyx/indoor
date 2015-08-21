package com.indoorsy.frash.homepage.activity;

import java.util.List;

import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.indoorsy.frash.Constants;
import com.indoorsy.frash.R;
import com.indoorsy.frash.common.activity.BasicActivity;
import com.indoorsy.frash.common.view.CustomTitleView;
import com.indoorsy.frash.homepage.adapter.ProductDetailCommentListAdapter;
import com.indoorsy.frash.homepage.data.bean.DetailComment;
import com.indoorsy.frash.http.core.HttpResult;
import com.indoorsy.frash.http.core.HttpTask;

public class ProductMoreComment extends BasicActivity {
	
	private static final String TAG = ProductMoreComment.class.getSimpleName();

	// 顶栏
	private CustomTitleView customTitleView;

	// 评论列表
	private ListView CommentListView;
	private ProductDetailCommentListAdapter commentListAdapter;
	List<DetailComment> listevs ;

	@Override
	public int initLayout() {
		return R.layout.homepage_product_more_comment;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initUI() {
		if (getIntent().getSerializableExtra(Constants.HOMEPAGE_PRODUCT_COMMENT_LIST) != null) {
			listevs = (List<DetailComment>) getIntent().getSerializableExtra(Constants.HOMEPAGE_PRODUCT_COMMENT_LIST);
			Log.e(TAG, "收到的commentlist = "+ listevs);
		}
		
		// 顶栏
		customTitleView = (CustomTitleView) findViewById(R.id.customTitleView);
		customTitleView.setTitle("评论");
		customTitleView.setLeftImageBg(R.drawable.comm_back);
		customTitleView.setRightTextViewVisibility(View.INVISIBLE);
		customTitleView.setOnClickListener(this);

		// 评论列表
		CommentListView = (ListView) findViewById(R.id.CommentListView);
		commentListAdapter = new ProductDetailCommentListAdapter(getApplicationContext());
		CommentListView.setAdapter(commentListAdapter);
		commentListAdapter.resetData(listevs);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.leftImageView:
			finish();
			break;
		}

	}

	@Override
	public void onLoadFinish(HttpTask task, HttpResult result) {
	}

	@Override
	public void initData() {
	}

}
