package com.indoorsy.frash.homepage.activity;

import java.util.List;

import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.indoorsy.frash.Constants;
import com.indoorsy.frash.R;
import com.indoorsy.frash.common.activity.BasicActivity;
import com.indoorsy.frash.common.view.CustomTitleView;
import com.indoorsy.frash.homepage.adapter.RecipeCommentListAdapter;
import com.indoorsy.frash.homepage.data.bean.RecipeListc;
import com.indoorsy.frash.http.core.HttpResult;
import com.indoorsy.frash.http.core.HttpTask;

public class RecipeMoreComment extends BasicActivity {
	
	private static final String TAG = RecipeMoreComment.class.getSimpleName();

	// 顶栏
	private CustomTitleView customTitleView;

	// 评论列表
	private ListView RecipeCommentListView;
	private RecipeCommentListAdapter commentListAdapter;
	private List<RecipeListc> recipeListcs;

	@Override
	public int initLayout() {
		return R.layout.homepage_recipe_more_comment;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initUI() {
		if (getIntent().getSerializableExtra(Constants.HOMEPAGE_RECIPE_COMMENT_LIST) != null) {
			recipeListcs = (List<RecipeListc>) getIntent().getSerializableExtra(Constants.HOMEPAGE_RECIPE_COMMENT_LIST);
			Log.e(TAG, "收到的Recipecommentlist = "+ recipeListcs);
		}
		
		// 顶栏
		customTitleView = (CustomTitleView) findViewById(R.id.customTitleView);
		customTitleView.setTitle("评论");
		customTitleView.setLeftImageBg(R.drawable.comm_back);
		customTitleView.setRightTextViewVisibility(View.INVISIBLE);
		customTitleView.setOnClickListener(this);

		// 评论列表
		RecipeCommentListView = (ListView) findViewById(R.id.RecipeCommentListView);
		commentListAdapter = new RecipeCommentListAdapter(getApplicationContext());
		RecipeCommentListView.setAdapter(commentListAdapter);
		commentListAdapter.resetData(recipeListcs);

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
