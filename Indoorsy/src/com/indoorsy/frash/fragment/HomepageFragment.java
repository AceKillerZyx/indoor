package com.indoorsy.frash.fragment;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.indoorsy.frash.Constants;
import com.indoorsy.frash.R;
import com.indoorsy.frash.ReleaseConfigure;
import com.indoorsy.frash.common.data.bean.CommonResult;
import com.indoorsy.frash.common.view.CommonProgressDialog;
import com.indoorsy.frash.common.view.ListViewForScrollView;
import com.indoorsy.frash.common.view.PullScrollView;
import com.indoorsy.frash.common.view.PullScrollView.OnPullListener;
import com.indoorsy.frash.fragment.data.bean.ProductType;
import com.indoorsy.frash.homepage.activity.HomePageSearchActivtiy;
import com.indoorsy.frash.homepage.activity.ProductDetailActivity;
import com.indoorsy.frash.homepage.activity.ProductListActivity;
import com.indoorsy.frash.homepage.activity.ProductListActivity2;
import com.indoorsy.frash.homepage.activity.RecipeShareActivtiy;
import com.indoorsy.frash.homepage.activity.SeckillActivity;
import com.indoorsy.frash.homepage.adapter.HomePageListAdapter;
import com.indoorsy.frash.homepage.adapter.ViewPagerAdapter;
import com.indoorsy.frash.homepage.data.bean.Ad;
import com.indoorsy.frash.homepage.data.bean.Goods;
import com.indoorsy.frash.http.core.HttpListener;
import com.indoorsy.frash.http.core.HttpParam;
import com.indoorsy.frash.http.core.HttpResult;
import com.indoorsy.frash.http.core.HttpTask;
import com.indoorsy.frash.util.CommonDataUtil;
import com.indoorsy.frash.util.SharedPreferencesUtil;
import com.indoorsy.frash.util.StringUtil;
import com.indoorsy.frash.util.UpdateManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
/**
 * 首页
 */
@SuppressLint("InflateParams") 
public class HomepageFragment extends Fragment implements HttpListener,OnClickListener,OnPullListener,OnItemClickListener{

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();	
		mLocationClient.requestLocation();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		mLocationClient.stop();
	}

	private static final String TAG = HomepageFragment.class.getSimpleName();
	private CommonProgressDialog pd;
	private View ShowView;
	
	//顶栏
	private TextView HomepageTitlebarProvinceTextView,HomepageTitlebarCityTextView;
	private TextView HomePageTitleBarSearchTextView;
	
	//下拉刷新控件
	private PullScrollView HomePagePullToRefreshView;
	private LinearLayout HomePageContentLayout;
	
	//导航大图标
//	private GridViewForScrollView homePageMyGridView;
	private HttpTask searchTypeHttpTask;
//	private ProductTypeAdapter productTypeAdapter;
	private LinearLayout homepageNavVegtablesLinearLayout,homepageNavMeatLinearLayout,homepageNavWaterLinearLayout, homepageNavForzenLinearLayout,
	homepageNavEggLinearLayout,homepageNavFruitLinearLayout,homepageNavSpicesLinearLayout,homepageNavOtherLinearLayout;
	
	//最新上线/食谱推荐。。
	RelativeLayout HomePageNewOnlineLinearLayout,
				HomePageSeckillLinearLayout,
				HomePageRecipesLinearLayout;
	
	//轮播大图
	private ViewPager HomePageViewPager;
	private ViewGroup HomePageViewPagerLinearLayout;
	private ImageView[] imageViews = null;
	private ImageView imageView = null;
	private ArrayList<View> HomePageViewPagerImageList;
	private boolean isContinue = true;
	private AtomicInteger what = new AtomicInteger(0);
//	private FixedSpeedScroller mScroller;// 滚动速度
	private DisplayImageOptions options;
	private HttpTask searchViewPagerHttpTask;
	
	//特价产品
	private ListViewForScrollView HomePageListView;
	private HomePageListAdapter homePageListAdapter;
	private HttpTask goodsHttpTask;
	
	private UpdateManager updateManager;
	
	// 百度定位
		private LocationMode tempMode = LocationMode.Hight_Accuracy;
		private String tempcoor = "gcj02";
		private int frequence = 1000 * 60 * 60; // 定位自动刷新平率
		private Double lat, lon;
		private String address, address_province, address_city, address_code;
		public static final int FLAG = 1;
		private LocationClient mLocationClient;
		public MyLocationListener mMyLocationListener;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
			
		return inflater.inflate(R.layout.homepage_fragment, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		pd = new CommonProgressDialog(getActivity());
		
		updateManager = new UpdateManager(getActivity(), 0);
		updateManager.checkUpdateInfo();
		
		HomePageContentLayout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.homepage_content_layout, null);
		ShowView = HomePageContentLayout.findViewById(R.id.ShowView);
		
		//顶栏
		HomepageTitlebarProvinceTextView = (TextView) getActivity().findViewById(R.id.HomepageTitlebarProvinceTextView);
		HomepageTitlebarCityTextView = (TextView) getActivity().findViewById(R.id.HomepageTitlebarCityTextView);
//		if (SharedPreferencesUtil.getLocationProvince(getActivity()) != null) {
//			HomepageTitlebarProvinceTextView.setText(SharedPreferencesUtil.getLocationProvince(getActivity()));
//			HomepageTitlebarCityTextView.setText(SharedPreferencesUtil.getLocationCity(getActivity()));
//		}
		HomepageTitlebarProvinceTextView.setText("定位中...");
	
		
		
		HomePageTitleBarSearchTextView = (TextView) getActivity().findViewById(R.id.HomePageTitleBarSearchTextView);
		HomePageTitleBarSearchTextView.setOnClickListener(this);
		
		//下拉刷新
		HomePagePullToRefreshView = (PullScrollView)getView().findViewById(R.id.HomePagePullToRefreshView);  
		HomePagePullToRefreshView.addBodyView(HomePageContentLayout);
		HomePagePullToRefreshView.setOnPullListener(this);
		
		
		//轮播大图
		HomePageViewPager = (ViewPager) HomePageContentLayout.findViewById(R.id.HomePageViewPager);
		HomePageViewPagerLinearLayout = (ViewGroup) HomePageContentLayout.findViewById(R.id.HomePageViewPagerLinearLayout);
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.homepage_product_detail_default)
				.showImageOnFail(R.drawable.homepage_product_detail_default)
				.resetViewBeforeLoading(true).cacheOnDisk(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.displayer(new FadeInBitmapDisplayer(300)).build();
		
		//导航大图标
//		productTypeAdapter = new ProductTypeAdapter(getActivity());
//		homePageMyGridView = (GridViewForScrollView) HomePageContentLayout.findViewById(R.id.homePageMyGridView);
//		homePageMyGridView.setOnItemClickListener(this);
//		homePageMyGridView.setAdapter(productTypeAdapter);
//		homePageMyGridView.setHapticFeedbackEnabled(true);
		homepageNavVegtablesLinearLayout = (LinearLayout) HomePageContentLayout.findViewById(R.id.homepageNavVegtablesLinearLayout);
		homepageNavMeatLinearLayout = (LinearLayout) HomePageContentLayout.findViewById(R.id.homepageNavMeatLinearLayout);
		homepageNavWaterLinearLayout = (LinearLayout) HomePageContentLayout.findViewById(R.id.homepageNavWaterLinearLayout);
		homepageNavForzenLinearLayout = (LinearLayout) HomePageContentLayout.findViewById(R.id.homepageNavForzenLinearLayout);
		homepageNavEggLinearLayout = (LinearLayout) HomePageContentLayout.findViewById(R.id.homepageNavEggLinearLayout);
		homepageNavFruitLinearLayout = (LinearLayout) HomePageContentLayout.findViewById(R.id.homepageNavFruitLinearLayout);
		homepageNavSpicesLinearLayout = (LinearLayout) HomePageContentLayout.findViewById(R.id.homepageNavSpicesLinearLayout);
		homepageNavOtherLinearLayout = (LinearLayout) HomePageContentLayout.findViewById(R.id.homepageNavOtherLinearLayout);
		homepageNavVegtablesLinearLayout.setOnClickListener(this);
		homepageNavMeatLinearLayout.setOnClickListener(this);
		homepageNavWaterLinearLayout.setOnClickListener(this);
		homepageNavForzenLinearLayout.setOnClickListener(this);
		homepageNavEggLinearLayout.setOnClickListener(this);
		homepageNavFruitLinearLayout.setOnClickListener(this);
		homepageNavSpicesLinearLayout.setOnClickListener(this);
		homepageNavOtherLinearLayout.setOnClickListener(this);
		
		//最新上线-秒杀-食谱分享
		HomePageNewOnlineLinearLayout = (RelativeLayout) HomePageContentLayout.findViewById(R.id.HomePageNewOnlineLinearLayout);
		HomePageSeckillLinearLayout = (RelativeLayout) HomePageContentLayout.findViewById(R.id.HomePageSeckillLinearLayout);
		HomePageRecipesLinearLayout = (RelativeLayout) HomePageContentLayout.findViewById(R.id.HomePageRecipesLinearLayout);
		HomePageNewOnlineLinearLayout.setOnClickListener(this);
		HomePageSeckillLinearLayout.setOnClickListener(this);
		HomePageRecipesLinearLayout.setOnClickListener(this);
		
		//特价产品
		HomePageListView = (ListViewForScrollView) HomePageContentLayout.findViewById(R.id.HomePageListView);
		homePageListAdapter = new HomePageListAdapter(getActivity());
		HomePageListView.setAdapter(homePageListAdapter);
		HomePageListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				Intent  intent = new Intent(getActivity(),ProductDetailActivity.class);
				intent.putExtra(Constants.HOMEPAGE_PRODUCT_ID, ((Goods)arg0.getAdapter().getItem(arg2)).getGoodsid());
				intent.putExtra(Constants.HOMEPAGE_PRODUCT_NAME, ((Goods)arg0.getAdapter().getItem(arg2)).getGoodsName());
				intent.putExtra(Constants.HOMEPAGE_PRODUCT_DESC, ((Goods)arg0.getAdapter().getItem(arg2)).getGoodsDesc());
				intent.putExtra(Constants.HOMEPAGE_PRODUCT_THUMB, ((Goods)arg0.getAdapter().getItem(arg2)).getGoodsThumb());
				startActivity(intent);
			}
		});
		
		//商品接口列表
//		searchType();
		
		//查询轮播大图
		searchViewPager();
		
		//查询特价产品
		searchGoods();
		
		//滚动到顶端显示
		ShowView.setFocusable(true);
		ShowView.setFocusableInTouchMode(true);
		ShowView.requestFocus();
		
		//百度定位初始化
				mLocationClient = new LocationClient(getActivity().getApplicationContext());
				mMyLocationListener = new MyLocationListener();
				mLocationClient.registerLocationListener(mMyLocationListener);
				initLocation();
				mLocationClient.start();                       
				
		
	}
	public void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(tempMode);
		option.setCoorType(tempcoor);
		option.setScanSpan(frequence);
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);
	}
	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			lat = location.getLatitude();
			lon = location.getLongitude();
			Log.e("定位结果坐标：", " 纬度 = "+lat + "\n经度 = " + lon);
			SharedPreferencesUtil.putString(getActivity().getApplicationContext(), SharedPreferencesUtil.USER_INFO_FILE_NAME,
					SharedPreferencesUtil.USER_INFO_KEY_LOCATION_LAT, lat+"");
			SharedPreferencesUtil.putString(getActivity().getApplicationContext(), SharedPreferencesUtil.USER_INFO_FILE_NAME,
					SharedPreferencesUtil.USER_INFO_KEY_LOCATION_LNG, lon+"");
			baiduAutoLocation();
		}
	}
	
	public void baiduAutoLocation() {
		// 省份-城市-城市代码
		new AsyncTask<String, Void, Void>() {
			StringBuilder builder = new StringBuilder(100000);

			@Override
			protected Void doInBackground(String... params) {
				try {
					URL url = new URL(params[0]);
					URLConnection connection = url.openConnection();
					InputStream is = connection.getInputStream();
					InputStreamReader isr = new InputStreamReader(is, "utf-8");
					BufferedReader br = new BufferedReader(isr);
					String line;
					while ((line = br.readLine()) != null) {
						builder.append(line);
					}

					JSONObject jo = JSON.parseObject(builder.toString());
					JSONObject result = jo.getJSONObject("result");
					address_code = result.getString("cityCode");
					address = result.getString("formatted_address");
					JSONObject addressComponent = result
							.getJSONObject("addressComponent");
					address_province = addressComponent.getString("province");
					address_city = addressComponent.getString("city");
					
					
//					address_city = address_city.replace("市", "");
					
					if (jo != null) {
						Message msg = new Message();
						msg.what = FLAG;
						mHandler.sendMessage(msg);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				Log.i("chengshi",builder.toString());
				return null;
			}
//			String htp="http://api.map.baidu.com/geocoder/v2/?ak=" 
//					+ Constants.BAIDU_AK + "&location="
//					+ lat
//					+ ","
//					+ lon
//					+ "&output=json&pois=1&mcode=" + Constants.ANDROID_SHA1 
//					+ ";"+ Constants.PACKAGE_NAME
			
		}.execute("http://api.map.baidu.com/geocoder?output=json&location="+lat+","+lon+"&key="+Constants.BAIDU_AK);
	}
	
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case FLAG:
				Log.e("省份：", address_province);
				Log.e("城市：", address_city);
				Log.e("城市代码：", address_code);
				Log.e(TAG,"地址：" + address);
				
				//显示数据
				HomepageTitlebarProvinceTextView.setText(address_province);
				HomepageTitlebarCityTextView.setText(address_city);
				
				//加入内存
				SharedPreferencesUtil.putString(getActivity().getApplicationContext(), SharedPreferencesUtil.USER_INFO_FILE_NAME,
						SharedPreferencesUtil.USER_INFO_KEY_LOCATION_PROVINCE, address_province);
				SharedPreferencesUtil.putString(getActivity().getApplicationContext(), SharedPreferencesUtil.USER_INFO_FILE_NAME,
						SharedPreferencesUtil.USER_INFO_KEY_LOCATION_CITY, address_city);
				SharedPreferencesUtil.putString(getActivity().getApplicationContext(), SharedPreferencesUtil.USER_INFO_FILE_NAME,
						SharedPreferencesUtil.USER_INFO_KEY_LOCATION_ADDRESS, address);			
				break;
			}
		}
	};
	

	@Override
	public void onClick(View v) {
		Intent  intent ;
		switch (v.getId()) {
			case R.id.homepageNavVegtablesLinearLayout://蔬菜
				intent = new Intent(getActivity(),ProductListActivity2.class);
				intent.putExtra(Constants.HOMEPAGE_TYPE, 0);
				intent.putExtra(Constants.HOMEPAGE_PRODUCT_ID, 1);
				intent.putExtra(Constants.HOMEPAGE_PRODUCT_NAME, "蔬菜");
				startActivity(intent);
				break;
			case R.id.homepageNavMeatLinearLayout://肉类
				intent = new Intent(getActivity(),ProductListActivity2.class);
				intent.putExtra(Constants.HOMEPAGE_TYPE, 0);
				intent.putExtra(Constants.HOMEPAGE_PRODUCT_ID, 2);
				intent.putExtra(Constants.HOMEPAGE_PRODUCT_NAME, "肉类");
				startActivity(intent);
				break;
			case R.id.homepageNavWaterLinearLayout://水产品
				intent = new Intent(getActivity(),ProductListActivity2.class);
				intent.putExtra(Constants.HOMEPAGE_TYPE, 0);
				intent.putExtra(Constants.HOMEPAGE_PRODUCT_ID, 3);
				intent.putExtra(Constants.HOMEPAGE_PRODUCT_NAME, "水产品");
				startActivity(intent);
				break;
			case R.id.homepageNavForzenLinearLayout://速冻烧烤
				intent = new Intent(getActivity(),ProductListActivity2.class);
				intent.putExtra(Constants.HOMEPAGE_TYPE, 0);
				intent.putExtra(Constants.HOMEPAGE_PRODUCT_ID, 4);
				intent.putExtra(Constants.HOMEPAGE_PRODUCT_NAME, "户外烧烤");
				startActivity(intent);
				break;
			case R.id.homepageNavEggLinearLayout://蛋奶类
				intent = new Intent(getActivity(),ProductListActivity2.class);
				intent.putExtra(Constants.HOMEPAGE_TYPE, 0);
				intent.putExtra(Constants.HOMEPAGE_PRODUCT_ID, 5);
				intent.putExtra(Constants.HOMEPAGE_PRODUCT_NAME, "蛋奶类");
				startActivity(intent);
				break;
			case R.id.homepageNavFruitLinearLayout://水果
				intent = new Intent(getActivity(),ProductListActivity2.class);
				intent.putExtra(Constants.HOMEPAGE_TYPE, 0);
				intent.putExtra(Constants.HOMEPAGE_PRODUCT_ID, 6);
				intent.putExtra(Constants.HOMEPAGE_PRODUCT_NAME, "水果");
				startActivity(intent);
				break;
			case R.id.homepageNavSpicesLinearLayout://调料副食
				intent = new Intent(getActivity(),ProductListActivity2.class);
				intent.putExtra(Constants.HOMEPAGE_TYPE, 0);
				intent.putExtra(Constants.HOMEPAGE_PRODUCT_ID, 7);
				intent.putExtra(Constants.HOMEPAGE_PRODUCT_NAME, "调料干菜");
				startActivity(intent);
				break;
			case R.id.homepageNavOtherLinearLayout://粮油米面
				intent = new Intent(getActivity(),ProductListActivity2.class);
				intent.putExtra(Constants.HOMEPAGE_TYPE, 0);
				intent.putExtra(Constants.HOMEPAGE_PRODUCT_ID, 8);
				intent.putExtra(Constants.HOMEPAGE_PRODUCT_NAME, "粮油副食");
				startActivity(intent);
				break;
				//-------------------华丽分割线---------------
			case R.id.HomePageNewOnlineLinearLayout:
				intent = new Intent(getActivity(),ProductListActivity.class);
				intent.putExtra(Constants.HOMEPAGE_PRODUCT_NAME, "最新上线");
				intent.putExtra(Constants.HOMEPAGE_TYPE, 1);
				startActivity(intent);
				break; 
			case R.id.HomePageSeckillLinearLayout:
				startActivity(new Intent(getActivity(),SeckillActivity.class));
				break;
			case R.id.HomePageRecipesLinearLayout:
				startActivity(new Intent(getActivity(),RecipeShareActivtiy.class));
				break;
			case R.id.HomePageTitleBarSearchTextView:
				startActivity(new Intent(getActivity(),HomePageSearchActivtiy.class));
				break;
		}
		
	}
	
	/*private void searchType() {
		HashMap<String, String> paramMap = CommonDataUtil.getCommonParams(getActivity());
		HttpParam httpParam = new HttpParam(ReleaseConfigure.HOMEPAGE_VALUE_TYPE_TYPE, false); // GET
		searchTypeHttpTask = new HttpTask(getActivity(), this);
		httpParam.setParams(paramMap);
		searchTypeHttpTask.execute(httpParam);
	}*/
	
	private void searchViewPager() {
		HashMap<String, String> paramMap = CommonDataUtil.getCommonParams(getActivity());
		HttpParam httpParam = new HttpParam(ReleaseConfigure.HOMEPAGE_VALUE_TYPE_VIEWPAGER, false); // GET
		searchViewPagerHttpTask = new HttpTask(getActivity(), this);
		httpParam.setParams(paramMap);
		searchViewPagerHttpTask.execute(httpParam);
		pd.loadDialog();
	}


	private void searchGoods() {
		HashMap<String, String> paramMap = CommonDataUtil.getCommonParams(getActivity());
		HttpParam httpParam = new HttpParam(ReleaseConfigure.HOMEPAGE_VALUE_TYPE_GOODS, false); // GET
		goodsHttpTask = new HttpTask(getActivity(), this);
		httpParam.setParams(paramMap);
		goodsHttpTask.execute(httpParam);
		pd.loadDialog();
	}
	
	@Override
	public void noNet(HttpTask task) {
		pd.removeDialog();		
	}

	@Override
	public void noData(HttpTask task, HttpResult result) {
		pd.removeDialog();	
		
	}

	@Override
	public void onLoadFailed(HttpTask task, HttpResult result) {
		pd.removeDialog();	
		
	}

	@Override
	public void onLoadFinish(HttpTask task, HttpResult result) {
		Log.e(TAG, result.getData());
		pd.removeDialog();	
		if (result != null && !StringUtil.isEmpty(result.getData()) && StringUtil.isGoodJson(result.getData()) && getActivity() !=null && !getActivity().isFinishing() ) {
			CommonResult commonResult = JSON.parseObject(result.getData(),CommonResult.class);
			if (null != commonResult) {
				if (task == searchViewPagerHttpTask) {
					if (commonResult.validate()) {
						JSONArray jsonArray = JSON.parseArray(commonResult.getData());
						if (null != jsonArray && jsonArray.size() > 0) {
							initViewPager(jsonArray);
						} else {
							HomePageViewPager.setAdapter(null);
						}
					} else {
//						ToastUtil.toast(getActivity(),commonResult.getErrMsg());
					}
				}
				if (task == searchTypeHttpTask) {
					if (commonResult.validate()) {
						JSONArray jsonArray = JSON.parseArray(commonResult.getData());
						if (null != jsonArray && jsonArray.size() > 0) {
//							initType(jsonArray);
						} 
					} else {
//						ToastUtil.toast(getActivity(),commonResult.getErrMsg());
					}
				}
				if (task == goodsHttpTask) {
					HomePagePullToRefreshView.setfooterViewReset();
					HomePagePullToRefreshView.setheaderViewReset();
					if (commonResult.validate()) {
						JSONArray jsonArray = JSON.parseArray(commonResult.getData());
						if (null != jsonArray && jsonArray.size() > 0) {
							initGoods(jsonArray);
						} 
					} else {
//						ToastUtil.toast(getActivity(),commonResult.getErrMsg());
					}
				}
				
			}
		}
	}

	/**
	 * 初始化商品类别
	 * @param jsonArray 
	 * 
	 */
	/*private void initType(JSONArray jsonArray) {
		List<ProductType> productTypeList = new ArrayList<ProductType>();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			ProductType productType = new ProductType();
			productType.setTId(jsonObject.getIntValue("TId"));
			productType.setTImages(jsonObject.getString("TImages"));
			productType.setTName(jsonObject.getString("TName"));
			productType.setTDesc(jsonObject.getString("TDesc"));
			productType.setTState(jsonObject.getIntValue("TState"));
			productTypeList.add(productType);
		}
		if (productTypeList != null && productTypeList.size() > 0) {
//			productTypeAdapter.resetData(productTypeList);
		}
	}*/

	/**
	 * 初始化轮播大图
	 * @param jsonArray 
	 * 
	 */
	private void initViewPager(final JSONArray jsonArray) {
		HomePageViewPagerImageList = new ArrayList<View>();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			final Ad ad = JSONObject.toJavaObject(jsonObject,Ad.class);
			if (this != null && !getActivity().isFinishing()) {
				ImageView imageView = new ImageView(getActivity());
				imageView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent();
						Log.e(TAG, "advbox:" + ad.getAdvBox() + "--AdvId:" + ad.getAdvId());
						if(ad.getAdvBox() == 1){
							intent.setClass(getActivity(), ProductListActivity.class);
						}else{
							intent.setClass(getActivity(), ProductDetailActivity.class);
						}
						intent.putExtra(Constants.HOMEPAGE_PRODUCT_NAME, "商品");
						intent.putExtra(Constants.HOMEPAGE_TYPE, 2);
						intent.putExtra(Constants.ADVID, ad.getAdvId());
						startActivity(intent);
					}
				});
				imageView.setScaleType(ImageView.ScaleType.FIT_XY);
				ImageLoader.getInstance().displayImage(ad.getAdvImages(),imageView, options, new SimpleImageLoadingListener() {
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
				HomePageViewPagerImageList.add(imageView);
			}
		}
		//对imageviews进行填充  
        imageViews = new ImageView[HomePageViewPagerImageList.size()]; 
        //小圆点  
        for (int i = 0; i < HomePageViewPagerImageList.size(); i++) { 
            imageView = new ImageView(getActivity()); 
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(12, 12);
            lp.setMargins(5, 2, 5, 5);
            imageView.setLayoutParams(lp); 
            imageViews[i] = imageView; 
            if (i == 0) { 
                imageViews[i].setBackgroundResource(R.drawable.homepage_viewpager_item_dot_select); 
            } else { 
                imageViews[i].setBackgroundResource(R.drawable.homepage_viewpager_item_dot_normal); 
            } 
            HomePageViewPagerLinearLayout.addView(imageViews[i]); 
        }
        HomePageViewPager.setAdapter(new ViewPagerAdapter(HomePageViewPagerImageList)); 
        //设置滚动速度
//        try {
//			Field mField = ViewPager.class.getDeclaredField("mScroller");
//			mField.setAccessible(true);
//			mScroller = new FixedSpeedScroller(HomePageViewPager.getContext(),new AccelerateInterpolator(0.5f));
//			mField.set(HomePageViewPager, mScroller);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
        HomePageViewPager.setOnPageChangeListener(new GuidePageChangeListener()); 
        HomePageViewPager.setOnTouchListener(new OnTouchListener() { 
             
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
			HomePageViewPager.setCurrentItem(msg.what);
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

	/**
	 * 初始化特价商品
	 * @param jsonArray 
	 * 
	 */
	private void initGoods(JSONArray jsonArray) {
		List<Goods> goodslist = new ArrayList<Goods>();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			Goods goods = JSONObject.toJavaObject(jsonObject, Goods.class);
			goodslist.add(goods);
		}

		if (goodslist != null && goodslist.size() > 0) {
			homePageListAdapter.resetData(goodslist);
		}
		
	}

	
	//下拉刷新
	@Override
	public void refresh() {
		//查询特价产品
		searchGoods();
	}

	
	//上拉加载更多
	@Override
	public void loadMore() {
		//查询特价产品
		searchGoods();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent  intent = new Intent(getActivity(),ProductListActivity.class);
		intent.putExtra(Constants.HOMEPAGE_TYPE, 0);
		intent.putExtra(Constants.HOMEPAGE_PRODUCT_ID, ((ProductType)arg0.getAdapter().getItem(arg2)).getTId());
		intent.putExtra(Constants.HOMEPAGE_PRODUCT_NAME, ((ProductType)arg0.getAdapter().getItem(arg2)).getTName());
		startActivity(intent);
	}
		
}