package com.indoorsy.frash.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.indoorsy.frash.Constants;
import com.indoorsy.frash.R;
import com.indoorsy.frash.ReleaseConfigure;
import com.indoorsy.frash.common.data.bean.CommonResult;
import com.indoorsy.frash.common.view.CommonProgressDialog;
import com.indoorsy.frash.common.view.CustomTitleView;
import com.indoorsy.frash.common.view.expandtab.ExpandTabView;
import com.indoorsy.frash.common.view.expandtab.ViewMiddle;
import com.indoorsy.frash.common.view.expandtab.ViewRight;
import com.indoorsy.frash.homepage.activity.HomePageSearchActivtiy;
import com.indoorsy.frash.homepage.activity.ProductDetailActivity;
import com.indoorsy.frash.homepage.adapter.HomePageListAdapter;
import com.indoorsy.frash.homepage.data.bean.AllCategory;
import com.indoorsy.frash.homepage.data.bean.Goods;
import com.indoorsy.frash.http.core.HttpListener;
import com.indoorsy.frash.http.core.HttpParam;
import com.indoorsy.frash.http.core.HttpResult;
import com.indoorsy.frash.http.core.HttpTask;
import com.indoorsy.frash.util.CommonDataUtil;
import com.indoorsy.frash.util.StringUtil;
import com.indoorsy.frash.util.TypeUtil;
/**
 * 商品
 */
public class CommodityFragment extends Fragment implements HttpListener,
		OnClickListener,OnItemClickListener {
	private static final String TAG = CommodityFragment.class.getSimpleName();
	
	// 顶栏
	private CommonProgressDialog commonProgressDialog;
	private CustomTitleView customTitleView;
	
	// 分类导航
	private ExpandTabView CommodityExpandTabView;
	private ArrayList<View> mViewArray = new ArrayList<View>();
	private ViewMiddle viewMiddle;
	private ViewRight viewRight;
	
	// 商品列表
	private ListView CommodityProductListListView;
	private HomePageListAdapter homePageListAdapter;
	
	private HttpTask searchAllTypeHttpTask,searchGoodsHttpTask;
	
	private int onClickType = 0;
	
	private int getTypeid = 0;
	
	private int orderid = 1;
	
	private List<AllCategory> allCategorieList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.commodity_fragment, container, false);
	}
	
	private void searchTypeAll() {
		HashMap<String, String> paramMap = CommonDataUtil.getCommonParams(getActivity());
		HttpParam httpParam = new HttpParam(ReleaseConfigure.HOMEPAGE_VALUE_PRODUCT_LIST_TYPE_ALL, false); // GET
		searchAllTypeHttpTask = new HttpTask(getActivity(), this);
		httpParam.setParams(paramMap);
		searchAllTypeHttpTask.execute(httpParam);
		commonProgressDialog.loadDialog();
	}
	
	//查询新品上线
	private void searchGoods() {
		HashMap<String, String> paramMap = CommonDataUtil.getCommonParams(getActivity());
		HttpParam httpParam = new HttpParam(ReleaseConfigure.HOMEPAGE_VALUE_PRODUCT_LIST_TYPE_NEW, false); // GET
		searchGoodsHttpTask = new HttpTask(getActivity(), this);
		httpParam.setParams(paramMap);
		searchGoodsHttpTask.execute(httpParam);
		commonProgressDialog.loadDialog();
	}
	
	//查询商品分类二级
	private void searchTwo(int typeid,int orderid) {
		HashMap<String, String> paramMap = CommonDataUtil.getCommonParams(getActivity());
		paramMap.put(Constants.TYPEID, String.valueOf(typeid));
		paramMap.put(Constants.ORDERID, String.valueOf(orderid));
		HttpParam httpParam = new HttpParam(ReleaseConfigure.HOMEPAGE_VALUE_TWO_GOODS, false); // GET
		searchGoodsHttpTask = new HttpTask(getActivity(), this);
		httpParam.setParams(paramMap);
		searchGoodsHttpTask.execute(httpParam);
		commonProgressDialog.loadDialog();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		commonProgressDialog = new CommonProgressDialog(getActivity());

		// 顶栏
		customTitleView = (CustomTitleView) getActivity().findViewById(R.id.customTitleView);
		customTitleView.setTitle(R.string.home_commodity);
		customTitleView.setLeftImageVisibility(View.GONE);
		customTitleView.setRightImageBg(R.drawable.homepage_titlebar_search);
		customTitleView.setRightTextViewVisibility(View.GONE);
		customTitleView.setOnClickListener(this);
		
		// 分类导航
		CommodityExpandTabView = (ExpandTabView) getActivity().findViewById(R.id.CommodityExpandTabView);
		viewMiddle = new ViewMiddle(getActivity());
		viewRight = new ViewRight(getActivity());
		mViewArray.add(viewMiddle);
		mViewArray.add(viewRight);
		ArrayList<String> mTextArray = new ArrayList<String>();
		mTextArray.add("分类筛选");
		mTextArray.add("综合排序");
		CommodityExpandTabView.setValue(mTextArray, mViewArray);
		initExpandTabListener();
		// 产品列表
		CommodityProductListListView = (ListView) getActivity().findViewById(R.id.CommodityProductListView);
		homePageListAdapter = new HomePageListAdapter(getActivity());
		CommodityProductListListView.setAdapter(homePageListAdapter);
		CommodityProductListListView.setOnItemClickListener(this);
		
		searchTypeAll();
		searchGoods();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.rightImageView:
				startActivity(new Intent(getActivity(),HomePageSearchActivtiy.class));
				break;
		}
	}

	@Override
	public void noNet(HttpTask task) {
		commonProgressDialog.removeDialog();
	}

	@Override
	public void noData(HttpTask task, HttpResult result) {
		commonProgressDialog.removeDialog();

	}

	@Override
	public void onLoadFailed(HttpTask task, HttpResult result) {
		commonProgressDialog.removeDialog();

	}

	@Override
	public void onLoadFinish(HttpTask task, HttpResult result) {
		Log.e(TAG, result.getData());
		commonProgressDialog.removeDialog();
		if (result != null && !StringUtil.isEmpty(result.getData()) && StringUtil.isGoodJson(result.getData()) && getActivity() != null && !getActivity().isFinishing()) {
			CommonResult commonResult = JSON.parseObject(result.getData(),CommonResult.class);
			if (null != commonResult) {
				if (task == searchAllTypeHttpTask) {
					if (commonResult.validate()) {
						JSONArray jsonArray = JSON.parseArray(commonResult.getData());
						if(jsonArray != null && jsonArray.size() > 0){
							initAllType(jsonArray);
						}
					}else{
//						ToastUtil.toast(getActivity(),commonResult.getErrMsg());
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
//						ToastUtil.toast(getActivity(),commonResult.getErrMsg());
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
			viewMiddle.setAdapter(getActivity(), groups, children);
		}
	}

	private void initExpandTabListener() {

		viewMiddle.setOnSelectListener(new ViewMiddle.OnSelectListener() {
			@Override
			public void getValue(String showText) {
				onClickType = 0;
				onRefresh(viewMiddle,showText);
			}
		});

		viewRight.setOnSelectListener(new ViewRight.OnSelectListener() {
			@Override
			public void getValue(String showText) {
				onClickType = 1;
				onRefresh(viewRight, showText);
			}
		});

	}
	
	private void onRefresh(View view, String showText) {
		CommodityExpandTabView.onPressBack();
		int position = getPositon(view);
		if (position >= 0 && !CommodityExpandTabView.getTitle(position).equals(showText)) {
			CommodityExpandTabView.setTitle(showText, position);
		}
//		Toast.makeText(getActivity(), showText, Toast.LENGTH_SHORT).show();
		switch (onClickType) {
			case 0:
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
			case 1:
				orderid = TypeUtil.byShowTextGetId(showText);
				break;
		}
		Log.e(TAG, "getTypeid==" + getTypeid+"--orderid==" + orderid);
		searchTwo(getTypeid,orderid);
	}

	private int getPositon(View tView) {
		for (int i = 0; i < mViewArray.size(); i++) {
			if (mViewArray.get(i) == tView) {
				return i;
			}
		}
		return -1;
	}

	public void onBackPressed() {
		if (!CommodityExpandTabView.onPressBack()) {
			getActivity().finish();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		Intent  intent = new Intent(getActivity(),ProductDetailActivity.class);
		intent.putExtra(Constants.HOMEPAGE_PRODUCT_ID, ((Goods)parent.getAdapter().getItem(position)).getGoodsid());
		intent.putExtra(Constants.HOMEPAGE_PRODUCT_NAME, ((Goods)parent.getAdapter().getItem(position)).getGoodsName());
		intent.putExtra(Constants.HOMEPAGE_PRODUCT_DESC, ((Goods)parent.getAdapter().getItem(position)).getGoodsDesc());
		intent.putExtra(Constants.HOMEPAGE_PRODUCT_THUMB, ((Goods)parent.getAdapter().getItem(position)).getGoodsThumb());
		startActivity(intent);
	}
}
