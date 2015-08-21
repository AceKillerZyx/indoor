package com.indoorsy.frash.homepage.activity;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.indoorsy.frash.Constants;
import com.indoorsy.frash.R;
import com.indoorsy.frash.ReleaseConfigure;
import com.indoorsy.frash.commodity.activity.FirmOrderActivity;
import com.indoorsy.frash.common.activity.BasicActivity;
import com.indoorsy.frash.common.data.bean.CommonResult;
import com.indoorsy.frash.common.view.CommonProgressDialog;
import com.indoorsy.frash.common.view.CustomTitleView;
import com.indoorsy.frash.common.view.FixedSpeedScroller;
import com.indoorsy.frash.common.view.ListViewForScrollView;
import com.indoorsy.frash.easemob.activity.KfLoginActivity;
import com.indoorsy.frash.homepage.adapter.ProductDetailCommentListAdapter;
import com.indoorsy.frash.homepage.adapter.ViewPagerAdapter;
import com.indoorsy.frash.homepage.data.bean.DetailComment;
import com.indoorsy.frash.homepage.data.bean.DetailLictdl;
import com.indoorsy.frash.homepage.data.bean.DetailLictr;
import com.indoorsy.frash.homepage.data.bean.DetailListcd;
import com.indoorsy.frash.homepage.data.bean.DetailListpu;
import com.indoorsy.frash.homepage.data.bean.ProductDetail;
import com.indoorsy.frash.http.core.HttpParam;
import com.indoorsy.frash.http.core.HttpResult;
import com.indoorsy.frash.http.core.HttpTask;
import com.indoorsy.frash.util.CommonDataUtil;
import com.indoorsy.frash.util.StringUtil;
import com.indoorsy.frash.util.ToastUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class ProductDetailActivity extends BasicActivity {

	private static final String TAG = ProductDetailActivity.class.getSimpleName();
	private CommonProgressDialog pd;
	
	//顶栏
	private CustomTitleView customTitleView;
	private ScrollView ScrollView;

	/**
	 * 产品id
	 */
	private int goodsid = 0;
	private String goodsName,goodsDesc,goodsThumb,goodsPrice;
	
	//轮播大图
	private RelativeLayout ProductDetailViewPagerRelativeLayout;
	private ViewPager ProductDetailViewPager;
	private ViewGroup ProductDetailViewPagerLinearLayout;
	private ImageView[] imageViews = null;
	private ImageView imageView = null;
	private ArrayList<View> ProductDetailImagePageList;
	private boolean isContinue = true;
	private AtomicInteger what = new AtomicInteger(0);
	private FixedSpeedScroller mScroller;// 滚动速度
	private DisplayImageOptions options;

	//产品详情信息
	private HttpTask addCollectionHttpTask,noCollectionHttpTask;
	private String collectId = "0";
//	private Goods goods;
	private double unitprice, unitprice1, unitprice2;
	private String unit ,unit1 ,unit2;
	private String delid;//配送id
	List<DetailComment> listevs ;
	private boolean isCollection = false ;
	private HttpTask searchInfoHttpTask;
	private ImageView HomePageProductDetailProductImageImageView;//产品单张图片展示
	private TextView 
	HomePageProductDetailNameTextView,//产品名称
	HomePageProductDetailMoneyTextView,//产品价格
	HomePageProductDetailProductInfoTextView,//产品简介
	HomePageProductDetailSendViaTextView,//配送方式
	HomePageProductDetailSendTimeTextView,//配送时间
	HomePageProductDetailCommentNumTextView,//评论数量
	HomePageProductDetailSumTextView,//库存数量
	
	HomePageProductDetailShareTextView,HomePageProductDetailColloctionTextView,//分享 /收藏
	HomePageProductDeatailJinTextView,HomePageProductDeatailFenTextView,//购买的单位
	HomePageProductDeatailHeTextView,HomePageProductDeatailTiaoTextView,
	
	HomePageProductDeatail12TextView,HomePageProductDeatail23TextView,//鱼类/斤（默认gone）
	HomePageProductDeatail34TextView,HomePageProductDeatail5TextView;
	@SuppressWarnings("unused")
	private RelativeLayout HomePageProductDetailToDetailRelativeLayout,//进入图文详情
	HomePageProductDetailToCommentRelativeLayout,//进入评论
	HomePageProductDeatailFishUnitRelativeLayout;//鱼类单位（默认gone）
	private String size = "0";

	//评论列表
	private ListViewForScrollView ProductDetailCommentListView;
	private ProductDetailCommentListAdapter commentListAdapter;
	
	// 底栏
	private TextView ProductDetailRobotTextView,
					ProductDetailShopCarTextView,
					ProductDetailBuyTextView;
	private HttpTask addShopCarHttpTask;
	
	//库存数量
	private	int repertory ;
	
	@Override
	public int initLayout() {
		return R.layout.homepage_product_detail;
	}
	
	
	@Override
	public void initUI() {
		pd = new CommonProgressDialog(this);
		
		//scroll中嵌套自定义listview
		ScrollView = (ScrollView) findViewById(R.id.ScrollView);
		ScrollView.smoothScrollTo(0, 0);
		ProductDetailViewPagerRelativeLayout = (RelativeLayout) findViewById(R.id.ProductDetailViewPagerRelativeLayout);
		
		//顶栏
		customTitleView = (CustomTitleView) findViewById(R.id.customTitleView);
		customTitleView.setTitle("商品详情");
		customTitleView.setLeftImageBg(R.drawable.comm_back);
		customTitleView.setRightTextViewVisibility(View.INVISIBLE);
		customTitleView.setOnClickListener(this);
		
		//轮播大图
		ProductDetailViewPager = (ViewPager) findViewById(R.id.ProductDetailViewPager);
		ProductDetailViewPagerLinearLayout = (ViewGroup) findViewById(R.id.ProductDetailViewPagerLinearLayout);
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.homepage_product_detail_default)
				.showImageOnFail(R.drawable.homepage_product_detail_default)
				.resetViewBeforeLoading(true).cacheOnDisk(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.displayer(new FadeInBitmapDisplayer(300)).build();
		
		 
		//产品信息
		
		HomePageProductDetailProductImageImageView = (ImageView) findViewById(R.id.HomePageProductDetailProductImageImageView);
		HomePageProductDetailNameTextView = (TextView) findViewById(R.id.HomePageProductDetailNameTextView);
		HomePageProductDetailMoneyTextView = (TextView) findViewById(R.id.HomePageProductDetailMoneyTextView);
		HomePageProductDetailProductInfoTextView = (TextView) findViewById(R.id.HomePageProductDetailProductInfoTextView);
		HomePageProductDetailSendViaTextView = (TextView) findViewById(R.id.HomePageProductDetailSendViaTextView);
		HomePageProductDetailSendTimeTextView = (TextView) findViewById(R.id.HomePageProductDetailSendTimeTextView);
		HomePageProductDetailCommentNumTextView = (TextView) findViewById(R.id.HomePageProductDetailCommentNumTextView);
		HomePageProductDetailSumTextView = (TextView) findViewById(R.id.HomePageProductDetailSumTextView);
		
		HomePageProductDetailShareTextView = (TextView) findViewById(R.id.HomePageProductDetailShareTextView);
		HomePageProductDetailColloctionTextView = (TextView) findViewById(R.id.HomePageProductDetailColloctionTextView);
		HomePageProductDetailShareTextView.setOnClickListener(this);
		HomePageProductDetailColloctionTextView.setOnClickListener(this);
		
		HomePageProductDeatailJinTextView = (TextView) findViewById(R.id.HomePageProductDeatailJinTextView);
		HomePageProductDeatailFenTextView = (TextView) findViewById(R.id.HomePageProductDeatailFenTextView);
		HomePageProductDeatailHeTextView = (TextView) findViewById(R.id.HomePageProductDeatailHeTextView);
		HomePageProductDeatailTiaoTextView = (TextView) findViewById(R.id.HomePageProductDeatailTiaoTextView);
		HomePageProductDeatail12TextView = (TextView) findViewById(R.id.HomePageProductDeatail12TextView);
		HomePageProductDeatail23TextView = (TextView) findViewById(R.id.HomePageProductDeatail23TextView);
		HomePageProductDeatail34TextView = (TextView) findViewById(R.id.HomePageProductDeatail34TextView);
		HomePageProductDeatail5TextView = (TextView) findViewById(R.id.HomePageProductDeatail5TextView);
		HomePageProductDeatailJinTextView.setOnClickListener(this);
		HomePageProductDeatailFenTextView.setOnClickListener(this);
		HomePageProductDeatailHeTextView.setOnClickListener(this);
		HomePageProductDeatailTiaoTextView.setOnClickListener(this);
		HomePageProductDeatail12TextView.setOnClickListener(this);
		HomePageProductDeatail23TextView.setOnClickListener(this);
		HomePageProductDeatail34TextView.setOnClickListener(this);
		HomePageProductDeatail5TextView.setOnClickListener(this);
		
		HomePageProductDeatailFishUnitRelativeLayout = (RelativeLayout) findViewById(R.id.HomePageProductDeatailFishUnitRelativeLayout);
		HomePageProductDetailToDetailRelativeLayout = (RelativeLayout) findViewById(R.id.HomePageProductDetailToDetailRelativeLayout);
		HomePageProductDetailToCommentRelativeLayout = (RelativeLayout) findViewById(R.id.HomePageProductDetailToCommentRelativeLayout);
		HomePageProductDetailToDetailRelativeLayout.setOnClickListener(this);
		HomePageProductDetailToCommentRelativeLayout.setOnClickListener(this);
		
		//评论列表
		ProductDetailCommentListView = (ListViewForScrollView) findViewById(R.id.ProductDetailCommentListView);
		commentListAdapter = new ProductDetailCommentListAdapter(getApplicationContext());
		ProductDetailCommentListView.setAdapter(commentListAdapter);
		
		//底栏
		ProductDetailRobotTextView = (TextView) findViewById(R.id.ProductDetailRobotTextView);
		ProductDetailShopCarTextView = (TextView) findViewById(R.id.ProductDetailShopCarTextView);
		ProductDetailBuyTextView = (TextView) findViewById(R.id.ProductDetailBuyTextView);
		ProductDetailRobotTextView.setOnClickListener(this);
		ProductDetailShopCarTextView.setOnClickListener(this);
		ProductDetailBuyTextView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
			case R.id.leftImageView:
				finish();
				break;
			case R.id.HomePageProductDetailShareTextView:
				intent = new Intent(this,ShareActivity.class);
				startActivity(intent);
				break;
			case R.id.HomePageProductDetailColloctionTextView:
				if (!isCollection) {
					//添加收藏
					searchAddCollection();
				} else {
					//取消收藏
					searchNoCollection();
				}
				break;
			case R.id.HomePageProductDeatailJinTextView://斤
				HomePageProductDeatailJinTextView.setBackgroundResource(R.drawable.homepage_product_detail_unit_bg_press);
				HomePageProductDeatailFenTextView.setBackgroundResource(R.drawable.homepage_product_detail_unit_bg_normal);
				unitprice = unitprice1;
				unit = unit1;
				HomePageProductDetailMoneyTextView.setText("￥"+unitprice1 + "元");
				break;
			case R.id.HomePageProductDeatailFenTextView://份
				HomePageProductDeatailJinTextView.setBackgroundResource(R.drawable.homepage_product_detail_unit_bg_normal);
				HomePageProductDeatailFenTextView.setBackgroundResource(R.drawable.homepage_product_detail_unit_bg_press);
				unitprice = unitprice2; 
				unit = unit2;
				HomePageProductDetailMoneyTextView.setText("￥"+unitprice2 + "元");
				break;
			case R.id.HomePageProductDeatailHeTextView://盒
				break;
			case R.id.HomePageProductDeatailTiaoTextView://条
				break;
				
			case R.id.HomePageProductDeatail12TextView:
				HomePageProductDeatail12TextView.setBackgroundResource(R.drawable.homepage_product_detail_unit_fish_bg_press);
				HomePageProductDeatail23TextView.setBackgroundResource(R.drawable.homepage_product_detail_unit_fish_bg_normal);
				HomePageProductDeatail34TextView.setBackgroundResource(R.drawable.homepage_product_detail_unit_fish_bg_normal);
				HomePageProductDeatail5TextView.setBackgroundResource(R.drawable.homepage_product_detail_unit_fish_bg_normal);
				size = "1~2斤";
				break;
			case R.id.HomePageProductDeatail23TextView:
				HomePageProductDeatail12TextView.setBackgroundResource(R.drawable.homepage_product_detail_unit_fish_bg_normal);
				HomePageProductDeatail23TextView.setBackgroundResource(R.drawable.homepage_product_detail_unit_fish_bg_press);
				HomePageProductDeatail34TextView.setBackgroundResource(R.drawable.homepage_product_detail_unit_fish_bg_normal);
				HomePageProductDeatail5TextView.setBackgroundResource(R.drawable.homepage_product_detail_unit_fish_bg_normal);
				size = "2~3斤";
				break;
			case R.id.HomePageProductDeatail34TextView:
				HomePageProductDeatail12TextView.setBackgroundResource(R.drawable.homepage_product_detail_unit_fish_bg_normal);
				HomePageProductDeatail23TextView.setBackgroundResource(R.drawable.homepage_product_detail_unit_fish_bg_normal);
				HomePageProductDeatail34TextView.setBackgroundResource(R.drawable.homepage_product_detail_unit_fish_bg_press);
				HomePageProductDeatail5TextView.setBackgroundResource(R.drawable.homepage_product_detail_unit_fish_bg_normal);
				size = "3~4斤";
				break;
			case R.id.HomePageProductDeatail5TextView:
				HomePageProductDeatail12TextView.setBackgroundResource(R.drawable.homepage_product_detail_unit_fish_bg_normal);
				HomePageProductDeatail23TextView.setBackgroundResource(R.drawable.homepage_product_detail_unit_fish_bg_normal);
				HomePageProductDeatail34TextView.setBackgroundResource(R.drawable.homepage_product_detail_unit_fish_bg_normal);
				HomePageProductDeatail5TextView.setBackgroundResource(R.drawable.homepage_product_detail_unit_fish_bg_press);
				size = "5斤以上";
				break;
			case R.id.HomePageProductDetailToDetailRelativeLayout:
				intent = new Intent(this,IMGDetail.class);
				intent.putExtra(Constants.HOMEPAGE_PRODUCT_ID, goodsid);
				Log.e(TAG, "传递的goodsid = "+ goodsid);
				startActivity(intent);
				break;
			case R.id.HomePageProductDetailToCommentRelativeLayout:
				intent = new Intent(this,ProductMoreComment.class);
				intent.putExtra(Constants.HOMEPAGE_PRODUCT_COMMENT_LIST, (Serializable)listevs);
				Log.e(TAG, "传递的commentlist = "+ listevs);
				startActivity(intent);
				break;
			case R.id.ProductDetailRobotTextView:
				intent = new Intent(this,KfLoginActivity.class);
				startActivity(intent);
				break;
			case R.id.ProductDetailShopCarTextView:
				if (repertory==0) {
					ToastUtil.toast(getApplicationContext(), "库存不足了,稍后再来吧亲~");
				}else {
					addShopCar();
				}
				
				break;
			case R.id.ProductDetailBuyTextView:
				if (repertory==0) {
					ToastUtil.toast(getApplicationContext(), "库存不足了,稍后再来吧亲~");
				}else {
					intent= new Intent(this,FirmOrderActivity.class);
					intent.putExtra(Constants.CART_TYPE_LIST, FirmOrderActivity.TYPE_CART_ONE);
					intent.putExtra(Constants.HOMEPAGE_PRODUCT_DELID, delid);
					intent.putExtra(Constants.HOMEPAGE_PRODUCT_PRICE, unitprice);
					intent.putExtra(Constants.HOMEPAGE_PRODUCT_UNIT, unit);
					intent.putExtra(Constants.HOMEPAGE_PRODUCT_ID, goodsid);
					intent.putExtra(Constants.HOMEPAGE_PRODUCT_NAME, goodsName);
					intent.putExtra(Constants.HOMEPAGE_PRODUCT_DESC, goodsDesc);
					intent.putExtra(Constants.HOMEPAGE_PRODUCT_THUMB,goodsThumb);
					intent.putExtra(Constants.HOMEPAGE_PRODUCT_SIZE, size);//鱼类特有
					
					Log.e(TAG, "=========商品详情的单件商品======== ");
					Log.e(TAG, "传递的配送id = " + delid);
					Log.e(TAG, "传递的单价 = " + unitprice);
					Log.e(TAG, "传递的单位 = " + unit);
					Log.e(TAG, "鱼类传递的规格 = " + size);
					startActivity(intent);
				}
				
				break;
		}

	}
	

	private void searchAddCollection() {
		HashMap<String, String> paramMap = CommonDataUtil.getCommonParams(getApplicationContext());
		paramMap.put(Constants.HOMEPAGE_PRODUCT_ID, goodsid+"");
		HttpParam httpParam = new HttpParam(ReleaseConfigure.HOMEPAGE_VALUE_TYPE_ADD_COLLECTION, false); // GET
		addCollectionHttpTask = new HttpTask(getApplicationContext(), this);
		httpParam.setParams(paramMap);
		addCollectionHttpTask.execute(httpParam);
	}
	
	private void searchNoCollection() {
		HashMap<String, String> paramMap = CommonDataUtil.getCommonParams(getApplicationContext());
		paramMap.put(Constants.HOMEPAGE_PRODUCT_NO_COLLECTION, collectId);
		HttpParam httpParam = new HttpParam(ReleaseConfigure.HOMEPAGE_VALUE_TYPE_NO_COLLECTION, false); // GET
		noCollectionHttpTask = new HttpTask(getApplicationContext(), this);
		httpParam.setParams(paramMap);
		noCollectionHttpTask.execute(httpParam);
	}


	private void addShopCar() {
		HashMap<String, String> paramMap = CommonDataUtil.getCommonParams(getApplicationContext());
		paramMap.put(Constants.HOMEPAGE_PRODUCT_ID, goodsid+"");
		paramMap.put(Constants.HOMEPAGE_PRODUCT_NUM, 1+"");
		paramMap.put(Constants.HOMEPAGE_PRODUCT_PRICE, unitprice+"");
		paramMap.put(Constants.HOMEPAGE_PRODUCT_UNIT, unit);
		HttpParam httpParam = new HttpParam(ReleaseConfigure.HOMEPAGE_VALUE_TYPE_ADD_SHOPCAR, false); // GET
		addShopCarHttpTask = new HttpTask(getApplicationContext(), this);
		httpParam.setParams(paramMap);
		addShopCarHttpTask.execute(httpParam);
	}


	private void searchInfo() {
		HashMap<String, String> paramMap = CommonDataUtil.getCommonParams(getApplicationContext());
		paramMap.put(Constants.HOMEPAGE_PRODUCT_DETAIL_GOODSID, String.valueOf(goodsid));
		HttpParam httpParam = new HttpParam(ReleaseConfigure.HOMEPAGE_VALUE_TYPE_PRODUCT_DETAIL, false); // GET
		searchInfoHttpTask = new HttpTask(getApplicationContext(), this);
		httpParam.setParams(paramMap);
		searchInfoHttpTask.execute(httpParam);
		pd.loadDialog();
	}
	
	//查询首页广告点击进入列表
	private void searchHomePageAdDetail() {
		HashMap<String, String> paramMap = CommonDataUtil.getCommonParams(getApplicationContext());
		paramMap.put(Constants.HOMEPAGEADVID, String.valueOf(getIntent().getIntExtra(Constants.ADVID, 0)));
		HttpParam httpParam = new HttpParam(ReleaseConfigure.HOMEPAGE_VALUE_PRODUCT_LIST_HOMEPAGE_AD, false); // GET
		searchInfoHttpTask = new HttpTask(getApplicationContext(), this);
		httpParam.setParams(paramMap);
		searchInfoHttpTask.execute(httpParam);
		pd.loadDialog();
	}
	
	@Override
	public void onLoadFinish(HttpTask task, HttpResult result) {
		Log.e(TAG, result.getData());
		pd.removeDialog();	
		if (result != null && !StringUtil.isEmpty(result.getData()) && StringUtil.isGoodJson(result.getData())) {
			CommonResult commonResult = JSON.parseObject(result.getData(),CommonResult.class);
			if (null != commonResult) {
				if (task == searchInfoHttpTask) {
					if (commonResult.validate()) {
						JSONArray jsonArray = JSON.parseArray(commonResult.getData());
						if (null != jsonArray && jsonArray.size() > 0) {
							initInfo(jsonArray);
						} 
					} else {
//						ToastUtil.toast(getApplicationContext(),commonResult.getErrMsg());
					}
				}
				if (task == addShopCarHttpTask) {
					if (commonResult.validate()) {
						ToastUtil.toast(getApplicationContext(), "已添加到购物车");
					} else {
//						ToastUtil.toast(getApplicationContext(),commonResult.getErrMsg());
					}
				}
				if (task == addCollectionHttpTask) {
					if (commonResult.validate()) {
						collectId = JSON.parseObject(commonResult.getData()).getString("collectId");
						HomePageProductDetailColloctionTextView.setBackgroundResource(R.drawable.homepage_product_detail_collection_press);
						isCollection = true;
						ToastUtil.toast(getApplicationContext(), "收藏成功");
					} else {
//						ToastUtil.toast(getApplicationContext(),commonResult.getErrMsg());
					}
				}
				if (task == noCollectionHttpTask) {
					if (commonResult.validate()) {
						ToastUtil.toast(getApplicationContext(), "取消收藏");
						HomePageProductDetailColloctionTextView.setBackgroundResource(R.drawable.homepage_product_detail_collection_normal);
						isCollection = false;
					} else {
//						ToastUtil.toast(getApplicationContext(),commonResult.getErrMsg());
					}
				}
			}
		}

	}
	
	/**
	 * 商品信息info
	 * @param jsonArray 
	 * 
	 */
	private void initInfo(JSONArray jsonArray){
		JSONObject jsonObject = jsonArray.getJSONObject(0);
		if (jsonObject != null ) {
			ProductDetail productDetail = JSONObject.toJavaObject(jsonObject, ProductDetail.class);
			//是否收藏
			collectId = productDetail.getCollectId();
			if (!productDetail.isCollectgoods()) {
				HomePageProductDetailColloctionTextView.setBackgroundResource(R.drawable.homepage_product_detail_collection_normal);
				isCollection = false ;
			} else {
				HomePageProductDetailColloctionTextView.setBackgroundResource(R.drawable.homepage_product_detail_collection_press);
				isCollection = true ;
			}
			
			// 效果图集合
			List<DetailLictr> detailLictrs = productDetail.getLictr();
			if (detailLictrs != null && detailLictrs.size() > 0) {
				initViewPager(detailLictrs);
			}
			
			// 商品单位价格集合
			List<DetailListpu> listpus = productDetail.getListpu();
			if (listpus != null && listpus.size() > 0) {
				initListpu(listpus);
			}
			
			// 配送的集合
			List<DetailLictdl> lictdls = productDetail.getLictdl();
			if (lictdls != null && lictdls.size() > 0) {
				initLictdl(lictdls);
			}
			
			// 产品介绍的集合
			List<DetailListcd> listcds = productDetail.getListcd();
			if (listcds != null && listcds.size() > 0) {
				initListcd(listcds);
			}
			
			// 评价的集合
			listevs = productDetail.getListev();
			if (listevs != null && listevs.size() > 0) {
				if (listevs.size() < 3) {
					commentListAdapter.resetData(listevs);
					Log.e(TAG, "评价少于3条");
				} else {
					List<DetailComment> listevs3 = new ArrayList<DetailComment>();
					for (int i = 0; i < 3; i++) {
						listevs3.add(listevs.get(i));
					}
					commentListAdapter.resetData(listevs3);
					Log.e(TAG, "评价多于3条，只显示前三条");
				}
				ProductDetailViewPagerRelativeLayout.setFocusable(true);
				ProductDetailViewPagerRelativeLayout.setFocusableInTouchMode(true);
				ProductDetailViewPagerRelativeLayout.requestFocus();
			}
			
			// 库存数量
			 repertory = productDetail.getRepertory();
			if (repertory+"" != null) {
				HomePageProductDetailSumTextView.setText("库存：" + repertory);
			}
			
			// 产品名称
			String name = productDetail.getName();
			if (name != null) {
				HomePageProductDetailNameTextView.setText(name);
			}
			
			// 评论数量
			int comment = productDetail.getComment();
			if (comment+"" != null) {
				HomePageProductDetailCommentNumTextView.setText("[ " + comment + " ]");
			}
		}
	}
	
	private void initListpu(List<DetailListpu> listpus) {
		if (listpus.size() == 1) {
			//只有一个单位
			unit = listpus.get(0).getUnitTitle();
			unitprice = listpus.get(0).getPunitPrice();
			HomePageProductDeatailJinTextView.setVisibility(View.VISIBLE);
			HomePageProductDeatailFenTextView.setVisibility(View.GONE);
			HomePageProductDeatailJinTextView.setText(unit);
			HomePageProductDetailMoneyTextView.setText("￥" + unitprice +"元");
			HomePageProductDeatailJinTextView.setClickable(false);
		} 
		
		if (Constants.TYPE_SECKILL.equals(getIntent().getStringExtra(Constants.HOMEPAGE_PRODUCT_TYPE_SECKILL)) ) {
			Log.e(TAG, "这是秒杀传来的价格");
			HomePageProductDetailMoneyTextView.setText("￥" + goodsPrice);
			unitprice = Double.parseDouble(goodsPrice.substring(0,goodsPrice.indexOf("元")));
			Log.e(TAG, "过滤的 unitprice = " + unitprice);
//			Pattern p = Pattern.compile("^[0-9]+(.[0-9]{1})?$");   
//	        Matcher m = p.matcher(goodsPrice);   
//	        if(m.find()){   
//	            Log.e(TAG, "正则表达式过滤的 goodsPrice = " + m.group());
//	            unitprice = Double.parseDouble(m.group());
//	        }   

		}
		
		if (listpus.size() == 2) {
			//有俩单位的
			unit1 = listpus.get(0).getUnitTitle();
			unitprice1 = listpus.get(0).getPunitPrice();
			HomePageProductDeatailJinTextView.setVisibility(View.VISIBLE);
			HomePageProductDeatailJinTextView.setText(unit1);
			
			unit2 = listpus.get(1).getUnitTitle();
			unitprice2 = listpus.get(1).getPunitPrice();
			HomePageProductDeatailFenTextView.setVisibility(View.VISIBLE);
			HomePageProductDeatailFenTextView.setText(unit2);
			
			unit = unit1;
			unitprice = unitprice1;
			HomePageProductDetailMoneyTextView.setText("￥" + unitprice1 +"元" );
		}
			
			/*
			if ("斤".equals(listpu.getUnitTitle())  ) {
				HomePageProductDeatailJinTextView.setVisibility(View.VISIBLE);
				HomePageProductDeatailFenTextView.setVisibility(View.VISIBLE);
				HomePageProductDeatailTiaoTextView.setVisibility(View.GONE);
				HomePageProductDeatailHeTextView.setVisibility(View.GONE);
				HomePageProductDeatailFishUnitRelativeLayout.setVisibility(View.GONE);
				unitprice1 = listpu.getPunitPrice();
				unitprice = unitprice1;
				unit = "斤";
				HomePageProductDetailMoneyTextView.setText("￥"+unitprice1+"元");
			} 
			if ("份".equals(listpu.getUnitTitle())  ) {
				HomePageProductDeatailJinTextView.setVisibility(View.VISIBLE);
				HomePageProductDeatailFenTextView.setVisibility(View.VISIBLE);
				HomePageProductDeatailTiaoTextView.setVisibility(View.GONE);
				HomePageProductDeatailHeTextView.setVisibility(View.GONE);
				HomePageProductDeatailFishUnitRelativeLayout.setVisibility(View.GONE);
				unitprice2 = listpu.getPunitPrice();
//				unit = "份";
			} 
			if ("条".equals(listpu.getUnitTitle())) {
				HomePageProductDeatailTiaoTextView.setVisibility(View.VISIBLE);
				HomePageProductDeatailTiaoTextView.setBackgroundResource(R.drawable.homepage_product_detail_unit_bg_press);
				HomePageProductDeatailJinTextView.setVisibility(View.GONE);
				HomePageProductDeatailFenTextView.setVisibility(View.GONE);
				HomePageProductDeatailHeTextView.setVisibility(View.GONE);
				HomePageProductDeatailFishUnitRelativeLayout.setVisibility(View.VISIBLE);
				
				unitprice = listpu.getPunitPrice();
				size = "1~2斤";//鱼的规格 1-2斤
				unit = "条";
				HomePageProductDetailMoneyTextView.setText("￥"+unitprice+"元/斤");
			}
			
			if ("盒".equals(listpu.getUnitTitle())) {
				HomePageProductDeatailHeTextView.setVisibility(View.VISIBLE);
				HomePageProductDeatailHeTextView.setBackgroundResource(R.drawable.homepage_product_detail_unit_bg_press);
				HomePageProductDeatailTiaoTextView.setVisibility(View.GONE);
				HomePageProductDeatailFenTextView.setVisibility(View.GONE);
				HomePageProductDeatailJinTextView.setVisibility(View.GONE);
				HomePageProductDeatailFishUnitRelativeLayout.setVisibility(View.GONE);
				unitprice = listpu.getPunitPrice();
				unit = "盒";
				HomePageProductDetailMoneyTextView.setText("￥"+unitprice+"元/" + unit);
			}
			*/
	}

	private void initLictdl(List<DetailLictdl> lictdls) {
		String delContent = lictdls.get(0).getDelContent();
		String deltime = lictdls.get(0).getDeltime();
		delid = lictdls.get(0).getDeliverid();
		HomePageProductDetailSendViaTextView.setText(delContent);
		HomePageProductDetailSendTimeTextView.setText(deltime);
	}
	

	private void initListcd(List<DetailListcd> listcds) {
		String comDetailsContent = listcds.get(0).getGoodsdesc();
		HomePageProductDetailProductInfoTextView.setText(comDetailsContent);
		HomePageProductDetailProductImageImageView.setScaleType(ImageView.ScaleType.FIT_XY);
		ImageLoader.getInstance().displayImage(listcds.get(0).getComDetailsImages(),
				HomePageProductDetailProductImageImageView, options, new SimpleImageLoadingListener() {

					@Override
					public void onLoadingStarted(String imageUri,
							View view) {
					}

					@Override
					public void onLoadingFailed(String imageUri,View view, FailReason failReason) {
						String message = null;
						switch (failReason.getType()) {
						case IO_ERROR:
							message = "Input/Output error";
							break;
						case DECODING_ERROR:
							message = "Image can't be decoded";
							break;
						case NETWORK_DENIED:
							message = "Downloads are denied";
							break;
						case OUT_OF_MEMORY:
							message = "Out Of Memory error";
							break;
						case UNKNOWN:
							message = "Unknown error";
							break;
						}
						Log.e(TAG, message);
					}

					@Override
					public void onLoadingComplete(String imageUri,
							View view, Bitmap loadedImage) {
					}
				});
	}

	/**
	 * 初始化轮播大图
	 * @param jsonArray 
	 * 
	 */
	private void initViewPager(List<DetailLictr> detailLictrs) {
		ProductDetailImagePageList = new ArrayList<View>();
		for (int i = 0; i < detailLictrs.size(); i++) {
			if (this != null && !this.isFinishing()) {
				ImageView imageView = new ImageView(this);
				imageView.setScaleType(ImageView.ScaleType.FIT_XY);
				ImageLoader.getInstance().displayImage(detailLictrs.get(i).getComImages(),
						imageView, options, new SimpleImageLoadingListener() {

							@Override
							public void onLoadingStarted(String imageUri,
									View view) {
							}

							@Override
							public void onLoadingFailed(String imageUri,View view, FailReason failReason) {
								String message = null;
								switch (failReason.getType()) {
								case IO_ERROR:
									message = "Input/Output error";
									break;
								case DECODING_ERROR:
									message = "Image can't be decoded";
									break;
								case NETWORK_DENIED:
									message = "Downloads are denied";
									break;
								case OUT_OF_MEMORY:
									message = "Out Of Memory error";
									break;
								case UNKNOWN:
									message = "Unknown error";
									break;
								}
								Log.e(TAG, message);
							}

							@Override
							public void onLoadingComplete(String imageUri,
									View view, Bitmap loadedImage) {
							}
						});
				ProductDetailImagePageList.add(imageView);
			}
		}
		//对imageviews进行填充  
        imageViews = new ImageView[ProductDetailImagePageList.size()]; 
        //小圆点  
        for (int i = 0; i < ProductDetailImagePageList.size(); i++) { 
            imageView = new ImageView(this); 
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(12, 12);
            lp.setMargins(5, 2, 5, 5);
            imageView.setLayoutParams(lp); 
            imageViews[i] = imageView; 
            if (i == 0) { 
                imageViews[i].setBackgroundResource(R.drawable.homepage_viewpager_item_dot_select); 
            } else { 
                imageViews[i].setBackgroundResource(R.drawable.homepage_viewpager_item_dot_normal); 
            } 
            ProductDetailViewPagerLinearLayout.addView(imageViews[i]); 
        }
        ProductDetailViewPager.setAdapter(new ViewPagerAdapter(ProductDetailImagePageList)); 
        try {
			Field mField = ViewPager.class.getDeclaredField("mScroller");
			mField.setAccessible(true);
			mScroller = new FixedSpeedScroller(ProductDetailViewPager.getContext(),new AccelerateInterpolator(0.5f));
			mField.set(ProductDetailViewPager, mScroller);
		} catch (Exception e) {
			e.printStackTrace();
		}
        ProductDetailViewPager.setOnPageChangeListener(new GuidePageChangeListener()); 
        ProductDetailViewPager.setOnTouchListener(new OnTouchListener() { 
             
            @SuppressLint("ClickableViewAccessibility") @Override 
            public boolean onTouch(View v, MotionEvent event) { 
                switch (event.getAction()) { 
                case MotionEvent.ACTION_DOWN: 
                case MotionEvent.ACTION_MOVE: 
                    isContinue = false; 
                    break; 
                case MotionEvent.ACTION_UP: 
                    isContinue = true; 
                    break; 
                default: 
                    isContinue = true; 
                    break; 
                } 
                return false; 
            } 
        }); 
        
        new Thread(new Runnable() { 
        	 
            @Override 
            public void run() { 
                while (true) { 
                    if (isContinue) { 
                        viewHandler.sendEmptyMessage(what.get()); 
                        whatOption(); 
                    } 
                } 
            } 
 
        }).start(); 
        
	}
	
	private void whatOption() {
		what.incrementAndGet();
		if (what.get() > imageViews.length - 1) {
			what.getAndAdd(-4);
		}
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {

		}
	}

	@SuppressLint("HandlerLeak")
	private final Handler viewHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			ProductDetailViewPager.setCurrentItem(msg.what);
			super.handleMessage(msg);
		}

	};

	private final class GuidePageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int arg0) {
			what.getAndSet(arg0);
			for (int i = 0; i < imageViews.length; i++) {
				imageViews[arg0].setBackgroundResource(R.drawable.homepage_viewpager_item_dot_select);
				if (arg0 != i) {
					imageViews[i].setBackgroundResource(R.drawable.homepage_viewpager_item_dot_normal);
				}
			}

		}

	}
	
	@Override
	public void initData() {
		int type = getIntent().getIntExtra(Constants.HOMEPAGE_TYPE, 0);
		if(type == 2){
			searchHomePageAdDetail();
		}else{
			goodsid = getIntent().getIntExtra(Constants.HOMEPAGE_PRODUCT_ID, 0);
			goodsName = getIntent().getStringExtra(Constants.HOMEPAGE_PRODUCT_NAME);
			goodsDesc = getIntent().getStringExtra(Constants.HOMEPAGE_PRODUCT_DESC);
			goodsThumb = getIntent().getStringExtra(Constants.HOMEPAGE_PRODUCT_THUMB);
			goodsPrice = getIntent().getStringExtra(Constants.HOMEPAGE_PRODUCT_PRICE);
			// 查询信息
			searchInfo();
			// 查询轮播大图
			// searchViewPager();
		}
	}

}
