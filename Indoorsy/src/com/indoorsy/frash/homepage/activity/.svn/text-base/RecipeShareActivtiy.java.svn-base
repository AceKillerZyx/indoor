package com.indoorsy.frash.homepage.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.indoorsy.frash.Constants;
import com.indoorsy.frash.R;
import com.indoorsy.frash.ReleaseConfigure;
import com.indoorsy.frash.common.activity.BasicActivity;
import com.indoorsy.frash.common.data.bean.CommonResult;
import com.indoorsy.frash.common.view.CommonProgressDialog;
import com.indoorsy.frash.common.view.CustomTitleView;
import com.indoorsy.frash.homepage.adapter.RecipeShareGridAdapter;
import com.indoorsy.frash.homepage.data.bean.Recipe;
import com.indoorsy.frash.http.core.HttpParam;
import com.indoorsy.frash.http.core.HttpResult;
import com.indoorsy.frash.http.core.HttpTask;
import com.indoorsy.frash.util.CommonDataUtil;
import com.indoorsy.frash.util.StringUtil;

public class RecipeShareActivtiy extends BasicActivity implements OnItemClickListener{
	
	private static final String TAG = RecipeShareActivtiy.class.getSimpleName();

	// 顶栏
	private CustomTitleView customTitleView;
	private CommonProgressDialog pd;
	private HttpTask httpTask;

	// Grid列表
	private GridView RecipeShareGridView;
	private RecipeShareGridAdapter recipeShareGridAdapter;
	
	
	@Override
	public int initLayout() {
		return R.layout.homepage_recipe_share;
	}

	@Override
	public void initUI() {
		// 顶栏
		pd = new CommonProgressDialog(this);
		pd.loadDialog();
		customTitleView = (CustomTitleView) findViewById(R.id.customTitleView);
		customTitleView.setTitle("食谱分享");
		customTitleView.setLeftImageBg(R.drawable.comm_back);
		customTitleView.setRightImageVisibility(View.GONE);
		customTitleView.setRightTextViewVisibility(View.GONE);
		customTitleView.setOnClickListener(this);

		// 食谱列表
		RecipeShareGridView = (GridView) findViewById(R.id.RecipeShareGridView);
		recipeShareGridAdapter = new RecipeShareGridAdapter(this);
		RecipeShareGridView.setAdapter(recipeShareGridAdapter);
		RecipeShareGridView.setOnItemClickListener(this);
		RecipeShareGridView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				return true;
			}
		});
	
		//查询食谱分享
		searchRecipes();
	}
	
	private void searchRecipes() {
		HashMap<String, String> paramMap = CommonDataUtil.getCommonParams(getApplicationContext());
		HttpParam httpParam = new HttpParam(ReleaseConfigure.HOMEPAGE_VALUE_TYPE_RECIPES, false); // GET
		httpTask = new HttpTask(getApplicationContext(), this);
		httpParam.setParams(paramMap);
		httpTask.execute(httpParam);
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
		Log.e(TAG, result.getData());
		pd.removeDialog();
		if (result != null && !StringUtil.isEmpty(result.getData()) && StringUtil.isGoodJson(result.getData())) {
			CommonResult commonResult = JSON.parseObject(result.getData(),CommonResult.class);
			if (null != commonResult) {
				if (commonResult.validate()) {
					JSONArray jsonArray = JSON.parseArray(commonResult.getData());
					if (null != jsonArray && jsonArray.size() > 0) {
						initRecipe(jsonArray);
					} else {
						recipeShareGridAdapter.resetData(null);
					}
				} else {
					recipeShareGridAdapter.resetData(null);
//					ToastUtil.toast(getApplicationContext(),commonResult.getErrMsg());
				}
			}
		}
	}
	
	private void initRecipe(JSONArray jsonArray){
		List<Recipe> recipeList = new ArrayList<Recipe>();
		for(int i = 0;i < jsonArray.size();i++){
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			Recipe recipe = JSONObject.toJavaObject(jsonObject, Recipe.class);
			recipeList.add(recipe);
		}
		if(recipeList != null && recipeList.size() > 0){
			recipeShareGridAdapter.resetData(recipeList);
		}
	}

	@Override
	public void initData() {

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		Intent intent  = new Intent(this,RecipeDetailActivity.class);
		intent.putExtra(Constants.HOMEPAGE_RECIPE_RID, ((Recipe)recipeShareGridAdapter.getItem(position)).getRecipeId());
		startActivity(intent);
	}
	
	//存放计算后的单元格相关信息    
	class ColumnInfo{         
		//单元格宽度         
		public int width = 0;         
		//每行所能容纳的单元格数量        
		public int countInRow = 2;     
	}

}
