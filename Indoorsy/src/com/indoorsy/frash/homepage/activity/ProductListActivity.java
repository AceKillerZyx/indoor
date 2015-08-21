package com.indoorsy.frash.homepage.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import android.content.Intent;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import com.indoorsy.frash.common.view.expandtab.ExpandTabView;
import com.indoorsy.frash.common.view.expandtab.ViewLeft;
import com.indoorsy.frash.common.view.expandtab.ViewMiddle;
import com.indoorsy.frash.common.view.expandtab.ViewRight;
import com.indoorsy.frash.homepage.adapter.HomePageListAdapter;
import com.indoorsy.frash.homepage.data.bean.AllCategory;
import com.indoorsy.frash.homepage.data.bean.Category;
import com.indoorsy.frash.homepage.data.bean.Goods;
import com.indoorsy.frash.http.core.HttpParam;
import com.indoorsy.frash.http.core.HttpResult;
import com.indoorsy.frash.http.core.HttpTask;
import com.indoorsy.frash.util.CommonDataUtil;
import com.indoorsy.frash.util.StringUtil;
import com.indoorsy.frash.util.TypeUtil;

public class ProductListActivity extends BasicActivity implements OnItemClickListener{

	private static final String TAG = ProductListActivity.class.getSimpleName();

	private int type = 0;
	
	private int typeid = 0;
	
	private int onClickType = 0;
	
	private int getTypeid = 0;
	
	private int orderid = 1;
	// 顶栏
	private CommonProgressDialog pd;
	private CustomTitleView customTitleView;

	// 分类导航
	private ExpandTabView HomePageExpandTabView;
	private ArrayList<View> mViewArray;
	private ViewLeft viewLeft;
	private ViewMiddle viewMiddle;
	private ViewRight viewRight;
	private List<Category> categoryList;

	// 商品列表
	private ListView HomePageProductListListView;
	private HomePageListAdapter homePageListAdapter;
	private HttpTask searchTypeHttpTask,searchGoodsHttpTask,searchAllTypeHttpTask;
	private List<AllCategory> allCategorieList;
	
	@Override
	public int initLayout() {
		return R.layout.homepage_productlist_activity;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.leftImageView:
				finish();
				break;
			case R.id.rightImageView:
				startActivity(new Intent(this, HomePageSearchActivtiy.class));
				break;
		}
	}
	
	//查询二级分类
	private void searchType() {
		HashMap<String, String> paramMap = CommonDataUtil.getCommonParams(getApplicationContext());
		paramMap.put(Constants.TYPEID, String.valueOf(typeid));
		HttpParam httpParam = new HttpParam(ReleaseConfigure.HOMEPAGE_VALUE_PRODUCT_LIST_TYPE, false); // GET
		searchTypeHttpTask = new HttpTask(getApplicationContext(), this);
		httpParam.setParams(paramMap);
		searchTypeHttpTask.execute(httpParam);
		pd.loadDialog();
	}
	
	//查询商品分类二级
	private void searchTwo(int typeid,int orderid) {
		HashMap<String, String> paramMap = CommonDataUtil.getCommonParams(getApplicationContext());
		paramMap.put(Constants.TYPEID, String.valueOf(typeid));
		paramMap.put(Constants.ORDERID, String.valueOf(orderid));
		HttpParam httpParam = new HttpParam(ReleaseConfigure.HOMEPAGE_VALUE_TWO_GOODS, false); // GET
		searchGoodsHttpTask = new HttpTask(getApplicationContext(), this);
		httpParam.setParams(paramMap);
		searchGoodsHttpTask.execute(httpParam);
		pd.loadDialog();
	}
	
	//查询商品分类一级
	private void searchOne(int typeid) {
		HashMap<String, String> paramMap = CommonDataUtil.getCommonParams(getApplicationContext());
		paramMap.put(Constants.TYPEID, String.valueOf(typeid));
		HttpParam httpParam = new HttpParam(ReleaseConfigure.HOMEPAGE_VALUE_ONW_GOODS, false); // GET
		searchGoodsHttpTask = new HttpTask(getApplicationContext(), this);
		httpParam.setParams(paramMap);
		searchGoodsHttpTask.execute(httpParam);
		pd.loadDialog();
	}
	
	//查询新品上线
	private void searchNewOnline() {
		HashMap<String, String> paramMap = CommonDataUtil.getCommonParams(getApplicationContext());
		HttpParam httpParam = new HttpParam(ReleaseConfigure.HOMEPAGE_VALUE_PRODUCT_LIST_TYPE_NEW, false); // GET
		searchGoodsHttpTask = new HttpTask(getApplicationContext(), this);
		httpParam.setParams(paramMap);
		searchGoodsHttpTask.execute(httpParam);
		pd.loadDialog();
	}
	
	//查询首页广告点击进入列表
	private void searchHomePageAdList() {
		HashMap<String, String> paramMap = CommonDataUtil.getCommonParams(getApplicationContext());
		paramMap.put(Constants.HOMEPAGEADVID, String.valueOf(getIntent().getIntExtra(Constants.ADVID, 0)));
		HttpParam httpParam = new HttpParam(ReleaseConfigure.HOMEPAGE_VALUE_PRODUCT_LIST_HOMEPAGE_AD, false); // GET
		searchGoodsHttpTask = new HttpTask(getApplicationContext(), this);
		httpParam.setParams(paramMap);
		searchGoodsHttpTask.execute(httpParam);
		pd.loadDialog();
	}
	
	//查询所有分类
	private void searchTypeAll() {
		HashMap<String, String> paramMap = CommonDataUtil.getCommonParams(getApplicationContext());
		HttpParam httpParam = new HttpParam(ReleaseConfigure.HOMEPAGE_VALUE_PRODUCT_LIST_TYPE_ALL, false); // GET
		searchAllTypeHttpTask = new HttpTask(getApplicationContext(), this);
		httpParam.setParams(paramMap);
		searchAllTypeHttpTask.execute(httpParam);
		pd.loadDialog();
	}

	@Override
	public void initUI() {
		// 顶栏
		customTitleView = (CustomTitleView) findViewById(R.id.customTitleView);
		customTitleView.setTitle(getIntent().getStringExtra(Constants.HOMEPAGE_PRODUCT_NAME));
		customTitleView.setLeftImageBg(R.drawable.comm_back);
		customTitleView.setRightImageBg(R.drawable.homepage_titlebar_search);
		customTitleView.setRightTextViewVisibility(View.GONE);
		customTitleView.setOnClickListener(this);

		pd = new CommonProgressDialog(this);
		
		// 分类导航
		HomePageExpandTabView = (ExpandTabView) findViewById(R.id.homePageExpandTabView);
		viewLeft = new ViewLeft(this);
		viewRight = new ViewRight(this);
		viewMiddle = new ViewMiddle(this);
		
		// 产品列表
		HomePageProductListListView = (ListView) findViewById(R.id.homePageProductListListView);
		homePageListAdapter = new HomePageListAdapter(getApplicationContext());
		HomePageProductListListView.setAdapter(homePageListAdapter);
		HomePageProductListListView.setOnItemClickListener(this);
		
		getTypeid = 0;
		orderid = 1;
	}

	@Override
	public void initData() {
		type = getIntent().getIntExtra(Constants.HOMEPAGE_TYPE, 0);
		typeid = getIntent().getIntExtra(Constants.HOMEPAGE_PRODUCT_ID, 0);
		Log.e(TAG, type + "--" + typeid);
		ArrayList<String> mTextArray = new ArrayList<String>();
		mViewArray = new ArrayList<View>();
		if(type == 0){//导航大图分类类型都是0
			HomePageExpandTabView.setVisibility(View.VISIBLE);
			mViewArray.add(viewLeft);
			mViewArray.add(viewRight);
			mTextArray.add("分类筛选");
			mTextArray.add("综合排序");
			searchType();
			searchOne(typeid);
		}else if(type == 2){
			HomePageExpandTabView.setVisibility(View.GONE);
			searchHomePageAdList();
		}else{ //商城所有商品类型
			HomePageExpandTabView.setVisibility(View.VISIBLE);
			mViewArray.add(viewMiddle);
			mViewArray.add(viewRight);
			mTextArray.add("分类筛选");
			mTextArray.add("综合排序");
			searchTypeAll();
			searchNewOnline();
		}
		HomePageExpandTabView.setValue(mTextArray, mViewArray);
		initExpandTabListener();
	}


	private void initExpandTabListener() {
		viewLeft.setOnSelectListener(new ViewLeft.OnSelectListener() {
			@Override
			public void getValue(String showText) {
				onClickType = 0;
				onRefresh(viewLeft, showText);
			}
		});
		
		viewMiddle.setOnSelectListener(new ViewMiddle.OnSelectListener() {
			@Override
			public void getValue(String showText) {
				onClickType = 1;
				onRefresh(viewMiddle, showText);
			}
		});
		
		viewRight.setOnSelectListener(new ViewRight.OnSelectListener() {
			@Override
			public void getValue(String showText) {
				onClickType = 2;
				onRefresh(viewRight, showText);
			}
		});

	}

	private void onRefresh(View view, String showText) {
		HomePageExpandTabView.onPressBack();
		int position = getPositon(view);
		if (position >= 0 && !HomePageExpandTabView.getTitle(position).equals(showText)) {
			HomePageExpandTabView.setTitle(showText, position);
		}
//		Toast.makeText(ProductListActivity.this, showText, Toast.LENGTH_SHORT).show();
		switch (onClickType) {
			case 0:
				if("分类筛选".equals(showText)){
					getTypeid = 0;
				}else{
					for(int i = 0;i < categoryList.size();i++){
						if(categoryList.get(i).getCatName().equals(showText)){
							getTypeid = categoryList.get(i).getCatId();
						}
					}
				}
				break;
			case 1:
				for(int i = 0;i < allCategorieList.size();i++){
					if(allCategorieList.get(i).getListc() != null && allCategorieList.get(i).getListc().size() > 0){
						for(int j = 0;j < allCategorieList.get(i).getListc().size();j++){
							if(allCategorieList.get(i).getListc().get(j).getCatName().equals(showText)){
								getTypeid = allCategorieList.get(i).getListc().get(j).getCatId();
							}
						}
					}
				}
				break;
			case 2:
				orderid = TypeUtil.byShowTextGetId(showText);
				break;
		}
		Log.e(TAG, "getTypeid==" + getTypeid+"--orderid==" + orderid);
		searchTwo(getTypeid, orderid);
	}
	private int getPositon(View tView) {
		for (int i = 0; i < mViewArray.size(); i++) {
			if (mViewArray.get(i) == tView) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public void onBackPressed() {
		if (!HomePageExpandTabView.onPressBack()) {
			finish();
		}
	}
	
	@Override
	public void onLoadFinish(HttpTask task, HttpResult result) {
		Log.e(TAG, result.getData());
		pd.removeDialog();
		if (result != null && !StringUtil.isEmpty(result.getData()) && StringUtil.isGoodJson(result.getData())) {
			CommonResult commonResult = JSON.parseObject(result.getData(),CommonResult.class);
			if (null != commonResult) {
				if (task == searchTypeHttpTask) {
					if (commonResult.validate()) {
						JSONArray jsonArray = JSON.parseArray(commonResult.getData());
						if(jsonArray != null && jsonArray.size() > 0){
							initType(jsonArray);
						}
					}else{
//						ToastUtil.toast(getApplicationContext(),commonResult.getErrMsg());
					}
				}
				
				if (task == searchGoodsHttpTask) {
					if (commonResult.validate()) {
						JSONArray jsonArray = JSON.parseArray(commonResult.getData());
						if(jsonArray != null && jsonArray.size() > 0){
							initGoods(jsonArray);
						}else{
							homePageListAdapter.resetData(null);
						}
					}else{
						homePageListAdapter.resetData(null);
//						ToastUtil.toast(getApplicationContext(),commonResult.getErrMsg());
					}
				}
				
				if (task == searchAllTypeHttpTask) {
					if (commonResult.validate()) {
						JSONArray jsonArray = JSON.parseArray(commonResult.getData());
						if(jsonArray != null && jsonArray.size() > 0){
							initAllType(jsonArray);
						}
					}else{
//						ToastUtil.toast(getApplicationContext(),commonResult.getErrMsg());
					}
				}
				
			}
		}
	}
	
	private void initGoods(JSONArray jsonArray) {
		List<Goods> goodsList = new ArrayList<Goods>();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			Goods goods = JSONObject.toJavaObject(jsonObject, Goods.class);
			goodsList.add(goods);
		}

		if (goodsList != null && goodsList.size() > 0) {
			homePageListAdapter.resetData(goodsList);
		}
	}

	private void initType(JSONArray jsonArray){
		categoryList = new ArrayList<Category>(); 
		String[] catStrings = new String[jsonArray.size() + 1]; 
		catStrings[0] = "分类筛选";
		for(int i = 1;i <= jsonArray.size();i++){
			JSONObject jsonObject = jsonArray.getJSONObject(i-1);
			Category category = JSONObject.toJavaObject(jsonObject, Category.class);
			categoryList.add(category);
			catStrings[i] = jsonObject.getString("catName");
		}
		
		if(catStrings != null && catStrings.length > 0){
			viewLeft.setAdapter(this, catStrings);
		}
	}
	
	private void initAllType(JSONArray jsonArray){
		allCategorieList = new ArrayList<AllCategory>();
		ArrayList<String> groups = new ArrayList<String>();
		SparseArray<LinkedList<String>> children = new SparseArray<LinkedList<String>>();
		for(int i = 0;i < jsonArray.size();i++){
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			AllCategory allCategory = JSONObject.toJavaObject(jsonObject, AllCategory.class);
			allCategorieList.add(allCategory);
			groups.add(allCategory.getTypename());
			LinkedList<String> tItem = new LinkedList<String>();
			if(allCategory.getListc() != null && allCategory.getListc().size() > 0){
				for(int j = 0;j < allCategory.getListc().size();j++){
					tItem.add(allCategory.getListc().get(j).getCatName());
				}
			}
			children.put(i, tItem);
		}
		
		if(groups != null && groups.size() > 0){
			viewMiddle.setAdapter(this, groups, children);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		Intent  intent = new Intent(this,ProductDetailActivity.class);
		intent.putExtra(Constants.HOMEPAGE_PRODUCT_ID, ((Goods)parent.getAdapter().getItem(position)).getGoodsid());
		intent.putExtra(Constants.HOMEPAGE_PRODUCT_NAME, ((Goods)parent.getAdapter().getItem(position)).getGoodsName());
		intent.putExtra(Constants.HOMEPAGE_PRODUCT_DESC, ((Goods)parent.getAdapter().getItem(position)).getGoodsDesc());
		intent.putExtra(Constants.HOMEPAGE_PRODUCT_THUMB, ((Goods)parent.getAdapter().getItem(position)).getGoodsThumb());
		startActivity(intent);
	}
}
