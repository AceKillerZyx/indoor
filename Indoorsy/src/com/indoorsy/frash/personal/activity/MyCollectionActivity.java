package com.indoorsy.frash.personal.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;

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
import com.indoorsy.frash.homepage.activity.ProductDetailActivity;
import com.indoorsy.frash.homepage.activity.RecipeDetailActivity;
import com.indoorsy.frash.http.core.HttpParam;
import com.indoorsy.frash.http.core.HttpResult;
import com.indoorsy.frash.http.core.HttpTask;
import com.indoorsy.frash.personal.adapter.CollectionGoodsAdapter;
import com.indoorsy.frash.personal.adapter.CollectionGoodsAdapter.onMyCollectionGoodsItemClickListener;
import com.indoorsy.frash.personal.adapter.CollectionRecipeAdapter;
import com.indoorsy.frash.personal.adapter.CollectionRecipeAdapter.onMyCollectionRecipeItemClickListener;
import com.indoorsy.frash.personal.data.bean.CollectionGoods;
import com.indoorsy.frash.personal.data.bean.CollectionRecipe;
import com.indoorsy.frash.util.CommonDataUtil;
import com.indoorsy.frash.util.StringUtil;
import com.indoorsy.frash.util.ToastUtil;
/*
 * 我的收藏
 */
@SuppressWarnings("deprecation")
public class MyCollectionActivity extends BasicActivity implements OnItemClickListener{
	
	private static final String TAG = MyCollectionActivity.class.getSimpleName();
	
	private int clickType = 0;
	
	private CustomTitleView customTitleView;
	
	private ListView mycollectionListView;
	
	private CollectionGoodsAdapter collectionGoodsAdapter;

	private GridView mycollectionGridView;
	
	private CollectionRecipeAdapter collectionRecipeAdapter;
	
	private CommonProgressDialog pd;
	
	private HttpTask searchBabyHttpTask, searchRecipeHttpTask,
			cancelBabyHttpTask, cancelRecipeHttpTask;
	
	private View mycollectionCollectionbabyView,
			mycollectionCollectionrecipeView;

	private LinearLayout mycollectionCollectionbabyLinearLayout,
			mycollectionCollectionrecipeLinearLayout;
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.leftImageView:
				this.finish();
				break;
			case R.id.mycollectionCollectionbabyLinearLayout:
				clickType = 0;
				mycollectionListView.setVisibility(View.VISIBLE);
				mycollectionGridView.setVisibility(View.GONE);
				mycollectionCollectionbabyView.setBackgroundDrawable(getResources().getDrawable(R.drawable.homepage_viewpager_item_dot_select));
				mycollectionCollectionrecipeView.setBackgroundDrawable(getResources().getDrawable(R.drawable.homepage_viewpager_item_dot_normal));
				searchBaby();
				break;
			case R.id.mycollectionCollectionrecipeLinearLayout:
				clickType = 1;
				mycollectionListView.setVisibility(View.GONE);
				mycollectionGridView.setVisibility(View.VISIBLE);
				mycollectionCollectionbabyView.setBackgroundDrawable(getResources().getDrawable(R.drawable.homepage_viewpager_item_dot_normal));
				mycollectionCollectionrecipeView.setBackgroundDrawable(getResources().getDrawable(R.drawable.homepage_viewpager_item_dot_select));
				searchRecipe();
				break;
		}
	}
	//查询收藏的宝贝
	private void searchBaby(){
		HashMap<String, String> paramMap = CommonDataUtil.getCommonParams(getApplicationContext());
		HttpParam httpParam = new HttpParam(ReleaseConfigure.HOMEPAGE_VALUE_TYPE_COLLECT_GOOD, false); // GET
		searchBabyHttpTask = new HttpTask(getApplicationContext(), this);
		httpParam.setParams(paramMap);
		searchBabyHttpTask.execute(httpParam);
		pd.loadDialog();
	}
	
	//取消收藏的宝贝
	private void cancelBaby(int collectId){
		HashMap<String, String> paramMap = CommonDataUtil.getCommonParams(getApplicationContext());
		paramMap.put(Constants.HOMEPAGE_PRODUCT_NO_COLLECTION, String.valueOf(collectId));
		HttpParam httpParam = new HttpParam(ReleaseConfigure.HOMEPAGE_VALUE_TYPE_NO_COLLECTION, false); // GET
		cancelBabyHttpTask = new HttpTask(getApplicationContext(), this);
		httpParam.setParams(paramMap);
		cancelBabyHttpTask.execute(httpParam);
		pd.loadDialog();
	}
	
	//查询收藏的食谱
	private void searchRecipe(){
		HashMap<String, String> paramMap = CommonDataUtil.getCommonParams(getApplicationContext());
		HttpParam httpParam = new HttpParam(ReleaseConfigure.HOMEPAGE_VALUE_TYPE_COLLECT_RECIPE, false); // GET
		searchRecipeHttpTask = new HttpTask(getApplicationContext(), this);
		httpParam.setParams(paramMap);
		searchRecipeHttpTask.execute(httpParam);
		pd.loadDialog();
	}
	
	//取消收藏的食谱
	private void cancelRecipe(int corecipeid){
		HashMap<String, String> paramMap = CommonDataUtil.getCommonParams(getApplicationContext());
		paramMap.put(Constants.HOMEPAGE_RECIPE_CORECIPEID, String.valueOf(corecipeid));
		HttpParam httpParam = new HttpParam(ReleaseConfigure.HOMEPAGE_VALUE_TYPE_RECIPES_DETAIL_NOCOLLECTION, false); // GET
		cancelRecipeHttpTask = new HttpTask(getApplicationContext(), this);
		httpParam.setParams(paramMap);
		cancelRecipeHttpTask.execute(httpParam);
		pd.loadDialog();
	}

	@Override
	public int initLayout() {
		return R.layout.personal_mycollection;
	}

	@Override
	public void initUI() {
		customTitleView=(CustomTitleView) findViewById(R.id.customTitleView);
		customTitleView.setLeftImageBg(R.drawable.comm_back);
		customTitleView.setVisibility(View.VISIBLE);
		customTitleView.setTitle(R.string.personal_fragment_mycollection_title);
		customTitleView.setOnClickListener(this);
		
		mycollectionCollectionbabyLinearLayout = (LinearLayout)findViewById(R.id.mycollectionCollectionbabyLinearLayout);
		mycollectionCollectionbabyLinearLayout.setOnClickListener(this);
		mycollectionCollectionrecipeLinearLayout = (LinearLayout)findViewById(R.id.mycollectionCollectionrecipeLinearLayout);
		mycollectionCollectionrecipeLinearLayout.setOnClickListener(this);
		mycollectionCollectionbabyView = (View)findViewById(R.id.mycollectionCollectionbabyView);
		mycollectionCollectionrecipeView = (View)findViewById(R.id.mycollectionCollectionrecipeView);
		
		collectionGoodsAdapter = new CollectionGoodsAdapter(getApplicationContext());
		mycollectionListView = (ListView)findViewById(R.id.mycollectionListView);
		mycollectionListView.setAdapter(collectionGoodsAdapter);
		mycollectionListView.setOnItemClickListener(this);
		
		collectionRecipeAdapter = new CollectionRecipeAdapter(getApplicationContext());
		mycollectionGridView = (GridView)findViewById(R.id.mycollectionGridView);
		mycollectionGridView.setAdapter(collectionRecipeAdapter);
		mycollectionGridView.setOnItemClickListener(this);
		
		pd = new CommonProgressDialog(this);
	
		clickType = 0;
		mycollectionListView.setVisibility(View.VISIBLE);
		mycollectionGridView.setVisibility(View.GONE);
		mycollectionCollectionbabyView.setBackgroundDrawable(getResources().getDrawable(R.drawable.homepage_viewpager_item_dot_select));
		mycollectionCollectionrecipeView.setBackgroundDrawable(getResources().getDrawable(R.drawable.homepage_viewpager_item_dot_normal));
		searchBaby();
	}

	@Override
	public void initData() {
		
	}
	
	@Override
	public void onLoadFinish(HttpTask task, HttpResult result) {
		Log.e(TAG, result.getData());
		pd.removeDialog();
		if (result != null && !StringUtil.isEmpty(result.getData()) && StringUtil.isGoodJson(result.getData())) {
			CommonResult commonResult = JSON.parseObject(result.getData(),CommonResult.class);
			if (null != commonResult) {
				if(task == searchBabyHttpTask){
					if(commonResult.validate()){
						JSONArray jsonArray = JSON.parseArray(commonResult.getData());
						if(jsonArray != null && jsonArray.size() > 0){
							initBaby(jsonArray);
						}else{
							collectionGoodsAdapter.resetData(null);
						}
					}else{
						collectionGoodsAdapter.resetData(null);
//						ToastUtil.toast(getApplicationContext(), commonResult.getErrMsg());
					}
				}
				
				if(task == cancelBabyHttpTask){
					if(commonResult.validate()){
						ToastUtil.toast(getApplicationContext(), "取消收藏商品成功");
						searchBaby();
					}else{
//						ToastUtil.toast(getApplicationContext(), commonResult.getErrMsg());
					}
				}
				
				if(task == searchRecipeHttpTask){
					if(commonResult.validate()){
						JSONArray jsonArray = JSON.parseArray(commonResult.getData());
						if(jsonArray != null && jsonArray.size() > 0){
							initRecipe(jsonArray);
						}else{
							collectionRecipeAdapter.resetData(null);
						}
					}else{
						collectionRecipeAdapter.resetData(null);
//						ToastUtil.toast(getApplicationContext(), commonResult.getErrMsg());
					}
				}
				
				if(task == cancelRecipeHttpTask){
					if(commonResult.validate()){
						ToastUtil.toast(getApplicationContext(), "取消收藏食谱成功");
						searchRecipe();
					}else{
//						ToastUtil.toast(getApplicationContext(), commonResult.getErrMsg());
					}
				}
				
			}
		}
	}
	
	private void initBaby(JSONArray jsonArray){
		final List<CollectionGoods> collectionGoodList = new ArrayList<CollectionGoods>();
		for (int i = 0; i < jsonArray.size(); i++) {
			CollectionGoods collectionGoods = JSONObject.toJavaObject(jsonArray.getJSONObject(i), CollectionGoods.class);
			collectionGoodList.add(collectionGoods);
		}
		
		if(collectionGoodList != null && collectionGoodList.size() > 0){
			collectionGoodsAdapter.resetData(collectionGoodList);
			collectionGoodsAdapter.setOnMyCollectionGoodsItemClickListener(new onMyCollectionGoodsItemClickListener() {
				@Override
				public void onMyCollectionGoodsItemClick(View v, int position) {
					int collectId = collectionGoodList.get(position).getCollectId();
					cancelBaby(collectId);
				}
			});
		}
	}
	
	private void initRecipe(JSONArray jsonArray){
		final List<CollectionRecipe> collectionRecipeList = new ArrayList<CollectionRecipe>();
		for (int i = 0; i < jsonArray.size(); i++) {
			CollectionRecipe collectionRecipe = JSONObject.toJavaObject(jsonArray.getJSONObject(i), CollectionRecipe.class);
			collectionRecipeList.add(collectionRecipe);
		}
		
		if(collectionRecipeList != null && collectionRecipeList.size() > 0){
			collectionRecipeAdapter.resetData(collectionRecipeList);
			collectionRecipeAdapter.setOnMyCollectionRecipeItemClickListener(new onMyCollectionRecipeItemClickListener() {
				@Override
				public void onMyCollectionRecipeItemClick(View v, int position) {
					int corecipeid = collectionRecipeList.get(position).getCorecipeid();
					cancelRecipe(corecipeid);
				}
			});
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if(clickType == 0){
			Intent  intent = new Intent(this,ProductDetailActivity.class);
			intent.putExtra(Constants.HOMEPAGE_PRODUCT_ID, ((CollectionGoods)arg0.getAdapter().getItem(arg2)).getGoodsid());
			intent.putExtra(Constants.HOMEPAGE_PRODUCT_NAME, ((CollectionGoods)arg0.getAdapter().getItem(arg2)).getGoodsName());
			intent.putExtra(Constants.HOMEPAGE_PRODUCT_DESC, ((CollectionGoods)arg0.getAdapter().getItem(arg2)).getGoodsDesc());
			intent.putExtra(Constants.HOMEPAGE_PRODUCT_THUMB, ((CollectionGoods)arg0.getAdapter().getItem(arg2)).getGoodsThumb());
			startActivity(intent);
		}else{
			Intent intent  = new Intent(this,RecipeDetailActivity.class);
			intent.putExtra(Constants.HOMEPAGE_RECIPE_RID, ((CollectionRecipe)arg0.getAdapter().getItem(arg2)).getRecipeId());
			startActivity(intent);
		}
	}

}
