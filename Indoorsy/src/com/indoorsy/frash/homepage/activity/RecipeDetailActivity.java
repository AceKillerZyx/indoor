package com.indoorsy.frash.homepage.activity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.indoorsy.frash.common.view.GridViewForScrollView;
import com.indoorsy.frash.common.view.ListViewForScrollView;
import com.indoorsy.frash.homepage.adapter.RecipeDetailGridAdapter;
import com.indoorsy.frash.homepage.adapter.RecipeDetailImagesListAdapter;
import com.indoorsy.frash.homepage.data.bean.RecipeListc;
import com.indoorsy.frash.homepage.data.bean.RecipeListr;
import com.indoorsy.frash.homepage.data.bean.RecipeLists;
import com.indoorsy.frash.homepage.data.bean.RecipeMethod;
import com.indoorsy.frash.http.core.HttpParam;
import com.indoorsy.frash.http.core.HttpResult;
import com.indoorsy.frash.http.core.HttpTask;
import com.indoorsy.frash.util.CommonDataUtil;
import com.indoorsy.frash.util.SharedPreferencesUtil;
import com.indoorsy.frash.util.StringUtil;
import com.indoorsy.frash.util.ToastUtil;

public class RecipeDetailActivity extends BasicActivity {
	
	public static final String TAG = RecipeDetailActivity.class.getSimpleName();

	// 顶栏
	private CustomTitleView customTitleView;
	private CommonProgressDialog pd;
	
	//图片步骤列表
	private ListViewForScrollView RecipeDetailImagesListView;
	private RecipeDetailImagesListAdapter imagesListAdapter;
	
	//商品信息
	private int rid ;
	private String corecipeid = "0";
	private HttpTask searchRecipeHttpTask,searchcollectionHttpTask,searchNocollectionHttpTask;
	private boolean isCollection = false ;
	private TextView RecipeDetailNameTextView,RecipeDetailCommentTextView,RecipeDetailShareTextView,RecipeDetailColloctionTextView;
	private EditText RecipeDetailCommentEditText;
	
	//食材
	private GridViewForScrollView RecipeDetailMixGridView;
	private RecipeDetailGridAdapter detailGridAdapter;
	
	
	// 评论列表
	private HttpTask uploadRecipeCommentHttpTask;
	private TextView RecipeDetailSubmitTextView ,RecipeDetailCommentNumTextView;
	List<RecipeListc> recipeListcs ;
	private RelativeLayout RecipeDetailToCommentRelativeLayout;
//	private ListViewForScrollView RecipeDetailCommentListView;
//	private RecipeCommentListAdapter commentListAdapter;

	

	@Override
	public int initLayout() {
		return R.layout.homepage_recipe_detail;
	}
	
	@Override
	public void initUI() {
		rid = getIntent().getIntExtra(Constants.HOMEPAGE_RECIPE_RID, 0);
		
		// 顶栏
		pd = new CommonProgressDialog(this);
		pd.loadDialog();
		customTitleView = (CustomTitleView) findViewById(R.id.customTitleView);
		customTitleView.setTitle("食谱分享");
		customTitleView.setLeftImageBg(R.drawable.comm_back);
		customTitleView.setRightTextViewVisibility(View.INVISIBLE);
		customTitleView.setOnClickListener(this);
		
		//商品信息
		RecipeDetailNameTextView = (TextView) findViewById(R.id.RecipeDetailNameTextView);
		RecipeDetailCommentTextView = (TextView) findViewById(R.id.RecipeDetailCommentTextView);
		RecipeDetailShareTextView = (TextView) findViewById(R.id.RecipeDetailShareTextView);
		RecipeDetailColloctionTextView = (TextView) findViewById(R.id.RecipeDetailColloctionTextView);
		RecipeDetailCommentTextView.setOnClickListener(this);
		RecipeDetailShareTextView.setOnClickListener(this);
		RecipeDetailColloctionTextView.setOnClickListener(this);
		
		
		RecipeDetailCommentEditText = (EditText) findViewById(R.id.RecipeDetailCommentEditText);
		
		//食材
		RecipeDetailMixGridView = (GridViewForScrollView) findViewById(R.id.RecipeDetailMixGridView);
		detailGridAdapter = new RecipeDetailGridAdapter(getApplicationContext()); 
		RecipeDetailMixGridView.setAdapter(detailGridAdapter);
//		SetHightUtil.setGridViewHeightBasedOnChildren(RecipeDetailMixGridView);
		RecipeDetailMixGridView.setHaveScrollbar(false);//这句话很重要，否则GridView无法计算高度
		
		// 图片步骤列表
		RecipeDetailImagesListView = (ListViewForScrollView) findViewById(R.id.RecipeDetailImagesListView);
		imagesListAdapter = new RecipeDetailImagesListAdapter(getApplicationContext());
		RecipeDetailImagesListView.setAdapter(imagesListAdapter);
		
		// 评论列表
		RecipeDetailCommentNumTextView = (TextView) findViewById(R.id.RecipeDetailCommentNumTextView);
		RecipeDetailSubmitTextView = (TextView) findViewById(R.id.RecipeDetailSubmitTextView);
		RecipeDetailSubmitTextView.setOnClickListener(this);
		RecipeDetailToCommentRelativeLayout = (RelativeLayout) findViewById(R.id.RecipeDetailToCommentRelativeLayout);
		RecipeDetailToCommentRelativeLayout.setOnClickListener(this);
//		RecipeDetailCommentListView = (ListViewForScrollView) findViewById(R.id.RecipeDetailCommentListView);
//		commentListAdapter = new RecipeCommentListAdapter(getApplicationContext());
//		RecipeDetailCommentListView.setAdapter(commentListAdapter);
		
		//查询食谱内容
		searchRecipe();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.leftImageView:
			finish();
			break;
		case R.id.RecipeDetailCommentTextView:
			RecipeDetailCommentEditText.requestFocus();
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.showSoftInput(RecipeDetailCommentEditText, InputMethodManager.SHOW_IMPLICIT);
			break;
		case R.id.RecipeDetailShareTextView:
			Intent intent = new Intent(this,ShareActivity.class);
			startActivity(intent);
			break;
		case R.id.RecipeDetailColloctionTextView:
			if (isCollection) {
				searchNoCollection();
			}else {
				SearchCollection();
			}
			break;
		case R.id.RecipeDetailToCommentRelativeLayout:
			Intent comment = new Intent(this, RecipeMoreComment.class);
			comment.putExtra(Constants.HOMEPAGE_RECIPE_COMMENT_LIST, (Serializable)recipeListcs);
			startActivity(comment);
			break;
		case R.id.RecipeDetailSubmitTextView:
			if (!StringUtil.isEmpty(RecipeDetailCommentEditText.getText().toString()) ) {
				UploadRecipeComment(RecipeDetailCommentEditText.getText().toString());
			} else {
				ToastUtil.toast(getApplicationContext(), "至少说一个字吧！");
			}
			break;
			
		default:
			break;
		}

	}


	private void searchRecipe() {
		HashMap<String, String> paramMap = CommonDataUtil.getCommonParams(getApplicationContext());
		HttpParam httpParam = new HttpParam(ReleaseConfigure.HOMEPAGE_VALUE_TYPE_RECIPES_DETAIL, false); // GET
		searchRecipeHttpTask = new HttpTask(getApplicationContext(), this);
		paramMap.put(Constants.HOMEPAGE_RECIPE_RID, rid+"");
		httpParam.setParams(paramMap);
		searchRecipeHttpTask.execute(httpParam);
	}
	

	private void UploadRecipeComment(String comment) {
		HashMap<String, String> paramMap = CommonDataUtil.getCommonParams(getApplicationContext());
		HttpParam httpParam = new HttpParam(ReleaseConfigure.HOMEPAGE_VALUE_TYPE_RECIPES_DETAIL_COMMENT, false); // GET
		uploadRecipeCommentHttpTask = new HttpTask(getApplicationContext(), this);
		paramMap.put(Constants.HOMEPAGE_RECIPE_COMMCONTENT, comment);
		paramMap.put(Constants.HOMEPAGE_RECIPE_WHZRECIPEID, rid +"");
		paramMap.put(Constants.HOMEPAGE_RECIPE_WHZUSERID, SharedPreferencesUtil.getUsid(getApplicationContext())+"");
		httpParam.setParams(paramMap);
		uploadRecipeCommentHttpTask.execute(httpParam);
	}
	
	private void SearchCollection() {
		HashMap<String, String> paramMap = CommonDataUtil.getCommonParams(getApplicationContext());
		HttpParam httpParam = new HttpParam(ReleaseConfigure.HOMEPAGE_VALUE_TYPE_RECIPES_DETAIL_COLLECTION, false); // GET
		searchcollectionHttpTask = new HttpTask(getApplicationContext(), this);
		paramMap.put(Constants.HOMEPAGE_RECIPE_RECIPEID, rid +"");
		httpParam.setParams(paramMap);
		searchcollectionHttpTask.execute(httpParam);
	}
	
	private void searchNoCollection() {
		HashMap<String, String> paramMap = CommonDataUtil.getCommonParams(getApplicationContext());
		paramMap.put(Constants.HOMEPAGE_RECIPE_CORECIPEID, corecipeid);
		HttpParam httpParam = new HttpParam(ReleaseConfigure.HOMEPAGE_VALUE_TYPE_RECIPES_DETAIL_NOCOLLECTION, false); // GET
		searchNocollectionHttpTask = new HttpTask(getApplicationContext(), this);
		httpParam.setParams(paramMap);
		searchNocollectionHttpTask.execute(httpParam);
	}
	
	
	@Override
	public void onLoadFinish(HttpTask task, HttpResult result) {
		Log.e(TAG, result.getData());
		pd.removeDialog();
		if (result != null && !StringUtil.isEmpty(result.getData()) && StringUtil.isGoodJson(result.getData())) {
			CommonResult commonResult = JSON.parseObject(result.getData(),CommonResult.class);
			if (null != commonResult) {
				if (task == searchRecipeHttpTask) {
					if (commonResult.validate()) {
						JSONArray jsonArray = JSON.parseArray(commonResult.getData());
						if (null != jsonArray && jsonArray.size() > 0) {
							initRecipe(jsonArray);
						} 
					} else {
//						ToastUtil.toast(getApplicationContext(),commonResult.getErrMsg());
					}
				}
				
				if (task == uploadRecipeCommentHttpTask) {
					if (commonResult.validate()) {
						ToastUtil.toast(getApplicationContext(), "评论成功");
						RecipeDetailCommentEditText.setText("");
						RecipeDetailCommentEditText.clearFocus();
					} else {
//						ToastUtil.toast(getApplicationContext(), "评论失败");
					}
				}			
				
				if (task == searchcollectionHttpTask) {
					if (commonResult.validate()) {
						RecipeMethod recipeMethod = JSONObject.toJavaObject(JSON.parseArray(commonResult.getData()).getJSONObject(0), RecipeMethod.class);
						corecipeid =  recipeMethod.getCorecipeid();
						ToastUtil.toast(getApplicationContext(), "收藏成功");
						RecipeDetailColloctionTextView.setBackgroundResource(R.drawable.homepage_product_detail_collection_press);
						isCollection = true;
					} else {
//						ToastUtil.toast(getApplicationContext(), commonResult.getErrMsg());
					}
				}		
				
				if (task == searchNocollectionHttpTask) {
					if (commonResult.validate()) {
						ToastUtil.toast(getApplicationContext(), "取消收藏");
						RecipeDetailColloctionTextView.setBackgroundResource(R.drawable.homepage_product_detail_collection_normal);
						isCollection = false;
					} else {
//						ToastUtil.toast(getApplicationContext(), commonResult.getErrMsg());
					}
				}			
			}
		}
	}

	
	private void initRecipe(JSONArray jsonArray) {
		JSONObject jsonObject = jsonArray.getJSONObject(0);
		RecipeMethod recipeMethod = JSONObject.toJavaObject(jsonObject, RecipeMethod.class);
		RecipeDetailNameTextView.setText(recipeMethod.getRtitle());
		
		//是否收藏
		if (!recipeMethod.isCollect()) {
			RecipeDetailColloctionTextView.setBackgroundResource(R.drawable.homepage_product_detail_collection_normal);
			isCollection = false ;
		} else {
			corecipeid = recipeMethod.getCorecipeid();
			RecipeDetailColloctionTextView.setBackgroundResource(R.drawable.homepage_product_detail_collection_press);
			isCollection = true ;
		}
		
		
		// 作料集合
		List<RecipeLists> recipeLists = recipeMethod.getLists();
		if (recipeLists != null && recipeLists.size() > 0) {
			detailGridAdapter.resetData(recipeLists);
		}
		
		// 步骤集合
		List<RecipeListr> recipeListrs = recipeMethod.getListr();
		if (recipeLists != null && recipeLists.size() > 0) {
			imagesListAdapter.resetData(recipeListrs);
		}
		
		// 评论集合-传递到评论页
		recipeListcs = recipeMethod.getLisc();
		if (recipeListcs != null && recipeListcs.size() > 0) {
			RecipeDetailCommentNumTextView.setText("[ "+ recipeListcs.size()+" ]");
		} else {
			RecipeDetailCommentNumTextView.setText("[ 0 ]");
		}
		
		/*if (recipeListcs != null && recipeListcs.size() > 0) {
			commentListAdapter.resetData(recipeListcs);
		}*/
	}

	

	@Override
	public void initData() {
		//滚动到顶端显示
		RecipeDetailNameTextView.setFocusable(true);
		RecipeDetailNameTextView.setFocusableInTouchMode(true);
		RecipeDetailNameTextView.requestFocus();
	}

}
