package com.indoorsy.frash.commodity.activity;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.indoorsy.frash.commodity.adapter.CommodityFirmMoreListAdapter;
import com.indoorsy.frash.commodity.data.bean.MoreOrder;
import com.indoorsy.frash.commodity.data.bean.Order;
import com.indoorsy.frash.common.activity.BasicActivity;
import com.indoorsy.frash.common.data.bean.CommonResult;
import com.indoorsy.frash.common.view.CommonAlertDialog;
import com.indoorsy.frash.common.view.CommonProgressDialog;
import com.indoorsy.frash.common.view.CustomTitleView;
import com.indoorsy.frash.common.view.ListViewForScrollView;
import com.indoorsy.frash.common.view.pickdate.ScreenInfo;
import com.indoorsy.frash.common.view.pickdate.WheelMain;
import com.indoorsy.frash.http.core.HttpParam;
import com.indoorsy.frash.http.core.HttpResult;
import com.indoorsy.frash.http.core.HttpTask;
import com.indoorsy.frash.personal.activity.ManageAddressActivity;
import com.indoorsy.frash.shopping.cart.data.bean.CartGood;
import com.indoorsy.frash.util.CommonDataUtil;
import com.indoorsy.frash.util.SharedPreferencesUtil;
import com.indoorsy.frash.util.StringUtil;
import com.indoorsy.frash.util.ToastUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;


@SuppressWarnings("unused")
public class FirmOrderActivity extends BasicActivity {

	private static final String TAG = FirmOrderActivity.class.getSimpleName();
//	private CommonLockUpView lockUpView;
	private CommonProgressDialog pd;
	public static final int CHOOSE_ADDRESS = 1100; 
	public static final String TYPE_CART_MORE = "more"; 
	public static final String TYPE_CART_ONE = "one"; 
	public static final String TYPE_PRODUCT_ONE = "one"; 
	
	private String userTime;
	Date userDate;
	Date cuDate;
	private String FirmType = "more" ; 
	
	// 顶栏
	private CustomTitleView customTitleView;
	private ScrollView FirmOrderScrollView;
	private InputMethodManager manager;

	// 收货地址
	private String defaultId, defaultName, defaultTel, defaultAddress;
	private RelativeLayout FirmOrderChangeAddressRelativeLayout;
	private TextView FirmOrderNamePhoneTextView, FirmOrderAddressTextView;

	// 产品信息
	private double unitprice,
	totalprice;
	private String unit;
	DisplayImageOptions options;
	private int goodsid ; 
	private String size = "0";//鱼类规格
	private String goodsName,goodsDesc,goodsThumb ;
	private int product_count = 1;
	private ImageView FirmOrderProductImageView;
	private TextView FirmOrderProductNameTextView,FirmOrderSizeTextView,
			FirmOrderProductInfoTextView, FirmOrderNumTextView,
			FirmOrderNumSubTextView, FirmOrderNumAddTextView,
			FirmOrderProductPriceTextView, FirmOrderWeightTextView;

	// 运费其他
	private DatePickerDialog dataDialog;
	private String delid;
	private String lat ;
	private String lng ;
	private Double freight = 0.0;
	private HttpTask searchFreightHttpTask;
	private Boolean ischeck = false;
	private TextView FirmOrderTransPriceTextView, FirmOrderTotalPriceTextView,
			FirmOrderDeliverTimeTextView;
	private EditText FirmOrderMessageEditText;
	private RelativeLayout FirmOrderPointRelativeLayout;
	private TextView FirmOrderPrintBillTextView;
	
	//订单
	private String addressid = "0"; 
	ArrayList<? extends Parcelable> cartGoodList;//购物车收到的多个订单
	private Order order;
	private LinearLayout CommodityFirmOrderOneLinearLayout;
	private ListViewForScrollView CommodityFirmOrderMoreListView;
	private CommodityFirmMoreListAdapter commodityFirmMoreListAdapter;
	
	//使用积分
//	private Dialog alertDialog;
//	private View pointView;
	private boolean usePoint = false ;
	private int allowUsePoint = 0;
	private TextView TotalPointTextView;
	private EditText  UsePointEditText;
	private TextView FirmOrderUsePointTextView;
	private Button UsePointButton;
	private HttpTask searchPointHttpTask;
	private double backprice = 0;
	private int inte,inte2;
	private TextView FirmOrderPointTextView;

	// 支付
//	public int PayType;
	private HttpTask uploadAddOrderHttpTask,uploadMoreAddOrderHttpTask;
	private TextView FirmOrderRealPayTextView, FirmOrderTotalPayTextView;
	private TextView FirmOrderConfirmPayTextView,FirmOrderConfirmPayTextView2;
	
	java.text.DecimalFormat df;
	
	@Override
	public int initLayout() {
		return R.layout.commodity_firm_order;
	}

	@SuppressLint("InflateParams")
	@Override
	public void initUI() {
		//区分订单单个多个界面
		CommodityFirmOrderOneLinearLayout = (LinearLayout) findViewById(R.id.CommodityFirmOrderOneLinearLayout);
		CommodityFirmOrderMoreListView = (ListViewForScrollView) findViewById(R.id.CommodityFirmOrderMoreListView);
		commodityFirmMoreListAdapter = new CommodityFirmMoreListAdapter(getApplicationContext());
		CommodityFirmOrderMoreListView.setAdapter(commodityFirmMoreListAdapter);
		
		// 购物车 跳转 确认订单 多件商品
		if (FirmType.equals(getIntent().getStringExtra(Constants.CART_TYPE_LIST))) {
			Log.e(TAG, "=====收到的==多件=== ");
			cartGoodList = this.getIntent().getParcelableArrayListExtra(Constants.CART_GOODSLSIT);
			Log.e(TAG, "收到的cartlist = " + cartGoodList);
			if (getIntent().getStringExtra(Constants.CART_TYPE_LIST_PRICE) != null) {
				totalprice = Double.parseDouble(getIntent().getStringExtra(Constants.CART_TYPE_LIST_PRICE));
				Log.e(TAG, "收到的 合计 = " + totalprice );
			}
			CommodityFirmOrderOneLinearLayout.setVisibility(View.GONE);
			CommodityFirmOrderMoreListView.setVisibility(View.VISIBLE);
			commodityFirmMoreListAdapter.resetData(cartGoodList);
		}
		//商品详情 || 购物车 跳转 确认订单 单件商品
		if (!FirmType.equals(getIntent().getStringExtra(Constants.CART_TYPE_LIST))  ) {
			CommodityFirmOrderOneLinearLayout.setVisibility(View.VISIBLE);
			CommodityFirmOrderMoreListView.setVisibility(View.GONE);
			Log.e(TAG, "=====收到的==单件=== ");
			if (getIntent().getDoubleExtra(Constants.HOMEPAGE_PRODUCT_PRICE, 0.0)+"" != null) {
				unitprice = getIntent().getDoubleExtra(Constants.HOMEPAGE_PRODUCT_PRICE, 0.0);
				allowUsePoint = (int) (unitprice * 0.1 * 100);
				Log.e(TAG, "单价 = " + unitprice);
			}
			if (getIntent().getStringExtra(Constants.HOMEPAGE_PRODUCT_UNIT) != null) {
				unit = getIntent().getStringExtra(Constants.HOMEPAGE_PRODUCT_UNIT);
				Log.e(TAG, "单位 = " + unit);
				size = getIntent().getStringExtra(Constants.HOMEPAGE_PRODUCT_SIZE);
				Log.e(TAG, "规格 = " + size );
			}
			if (getIntent().getStringExtra(Constants.HOMEPAGE_PRODUCT_DELID) != null) {
				delid = getIntent().getStringExtra(Constants.HOMEPAGE_PRODUCT_DELID);
			}
			goodsid = getIntent().getIntExtra(Constants.HOMEPAGE_PRODUCT_ID, 0);
			goodsName = getIntent().getStringExtra(Constants.HOMEPAGE_PRODUCT_NAME);
			goodsDesc = getIntent().getStringExtra(Constants.HOMEPAGE_PRODUCT_DESC);
			goodsThumb = getIntent().getStringExtra(Constants.HOMEPAGE_PRODUCT_THUMB);
			Log.e(TAG, "intent获取到的goodsid = " + goodsid);
		}
		
		
		//运费
		if (SharedPreferencesUtil.getLocationLat(getApplicationContext()) !=null) {
			lat = SharedPreferencesUtil.getLocationLat(getApplicationContext());
			lng = SharedPreferencesUtil.getLocationLng(getApplicationContext());
		}
		//地址
		if (SharedPreferencesUtil.getDefaultAddressName(getApplicationContext()) != null) {
			defaultId = SharedPreferencesUtil.getDefaultAddressId(getApplicationContext());
			defaultName = SharedPreferencesUtil.getDefaultAddressName(getApplicationContext());
			defaultTel = SharedPreferencesUtil.getDefaultAddressTel(getApplicationContext());
			defaultAddress = SharedPreferencesUtil.getDefaultAddressAddress(getApplicationContext());
		}
		
		
		
		
		pd = new CommonProgressDialog(this);
//		lockUpView = (CommonLockUpView)findViewById(R.id.lockUpView);
		// 顶栏
		customTitleView = (CustomTitleView) findViewById(R.id.customTitleView);
		customTitleView.setTitle("确认订单");
		customTitleView.setLeftImageBg(R.drawable.comm_back);
		customTitleView.setRightImageVisibility(View.GONE);
		customTitleView.setRightTextViewVisibility(View.GONE);
		customTitleView.setOnClickListener(this);
		manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		FirmOrderScrollView = (ScrollView) findViewById(R.id.FirmOrderScrollView);
		FirmOrderScrollView.setOnTouchListener(new OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				hideKeyboard();
				return false;
			}
		});
		
		// 收货地址
		FirmOrderChangeAddressRelativeLayout = (RelativeLayout) findViewById(R.id.FirmOrderChangeAddressRelativeLayout);
		FirmOrderNamePhoneTextView = (TextView) findViewById(R.id.FirmOrderNamePhoneTextView);
		FirmOrderAddressTextView = (TextView) findViewById(R.id.FirmOrderAddressTextView);
		FirmOrderNamePhoneTextView.setText(String.format(getResources().getString(R.string.commodity_firm_address_name), defaultName , defaultTel));
		FirmOrderAddressTextView.setText(String.format(getResources().getString(R.string.commodity_firm_address_address), defaultAddress));
		FirmOrderChangeAddressRelativeLayout.setOnClickListener(this);

		// 产品信息
		FirmOrderProductImageView = (ImageView) findViewById(R.id.FirmOrderProductImageView);
		FirmOrderProductNameTextView = (TextView) findViewById(R.id.FirmOrderProductNameTextView);
		FirmOrderAddressTextView = (TextView) findViewById(R.id.FirmOrderAddressTextView);
		FirmOrderProductInfoTextView = (TextView) findViewById(R.id.FirmOrderProductInfoTextView);
		FirmOrderNumTextView = (TextView) findViewById(R.id.FirmOrderNumTextView);
		FirmOrderNumSubTextView = (TextView) findViewById(R.id.FirmOrderNumSubTextView);
		FirmOrderNumAddTextView = (TextView) findViewById(R.id.FirmOrderNumAddTextView);
		FirmOrderProductPriceTextView = (TextView) findViewById(R.id.FirmOrderProductPriceTextView);
		FirmOrderWeightTextView = (TextView) findViewById(R.id.FirmOrderWeightTextView);
		FirmOrderNumSubTextView.setOnClickListener(this);
		FirmOrderNumAddTextView.setOnClickListener(this);
		FirmOrderSizeTextView = (TextView) findViewById(R.id.FirmOrderSizeTextView);//鱼类特有
		
		// 运费其他
		FirmOrderPointTextView = (TextView)findViewById(R.id.FirmOrderPointTextView);
		FirmOrderTransPriceTextView = (TextView) findViewById(R.id.FirmOrderTransPriceTextView);
		FirmOrderTotalPriceTextView = (TextView) findViewById(R.id.FirmOrderTotalPriceTextView);
		FirmOrderDeliverTimeTextView = (TextView) findViewById(R.id.FirmOrderDeliverTimeTextView);
		FirmOrderMessageEditText = (EditText) findViewById(R.id.FirmOrderMessageEditText);
		FirmOrderPointRelativeLayout = (RelativeLayout) findViewById(R.id.FirmOrderPointRelativeLayout);
		FirmOrderPrintBillTextView = (TextView) findViewById(R.id.FirmOrderPrintBillTextView);
		FirmOrderTransPriceTextView = (TextView) findViewById(R.id.FirmOrderTransPriceTextView);
		FirmOrderDeliverTimeTextView.setOnClickListener(this);
		FirmOrderPointRelativeLayout.setOnClickListener(this);
		FirmOrderPrintBillTextView.setOnClickListener(this);
		
		
		// 支付
		FirmOrderRealPayTextView = (TextView) findViewById(R.id.FirmOrderRealPayTextView);
		FirmOrderTotalPayTextView = (TextView) findViewById(R.id.FirmOrderTotalPayTextView);
		FirmOrderConfirmPayTextView = (TextView) findViewById(R.id.FirmOrderConfirmPayTextView);
		FirmOrderConfirmPayTextView2 = (TextView) findViewById(R.id.FirmOrderConfirmPayTextView2);
		FirmOrderConfirmPayTextView.setOnClickListener(this);
		FirmOrderConfirmPayTextView2.setOnClickListener(this);
		
		
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.commodity_firm_product_default)
				.showImageOnFail(R.drawable.commodity_firm_product_default)
				.resetViewBeforeLoading(true).cacheOnDisk(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.displayer(new FadeInBitmapDisplayer(300)).build();
		
		// 使用积分布局
		FirmOrderUsePointTextView = (TextView) findViewById(R.id.FirmOrderUsePointTextView);
//		LayoutInflater inflater = LayoutInflater.from(this);
//		pointView = inflater.inflate(R.layout.homepage_usepoint_dialog, null);
//		TotalPointTextView = (TextView)pointView.findViewById(R.id.TotalPointTextView);
//		UsePointEditText = (EditText)pointView.findViewById(R.id.UsePointEditText);
//		UsePointButton = (Button)pointView.findViewById(R.id.UsePointButton);
//		UsePointButton.setOnClickListener(this);
//		alertDialog = new Dialog(this, R.style.PopDialog);
//		alertDialog.setContentView(pointView);
		 
		//初始化商品部分
		initProduct();
		
		//查询运费
		searchFreight();
		
		//查询积分
		searchPonit();
		
		df = new java.text.DecimalFormat("#.##");
	}

	

	@SuppressLint({ "InflateParams", "NewApi", "SimpleDateFormat" })
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.leftImageView:
			finish();
			break;
		case R.id.FirmOrderChangeAddressRelativeLayout:
//			ToastUtil.toast(getApplicationContext(), "去修改");
			Intent chooseAddress = new Intent(this, ManageAddressActivity.class);
			chooseAddress.putExtra(Constants.ADDRESS, ManageAddressActivity.CHOOSE_ADDRESS);
			startActivityForResult(chooseAddress, CHOOSE_ADDRESS );
			break;
		case R.id.FirmOrderNumSubTextView:
			if (FirmOrderNumTextView.getText().toString() != null) {
				product_count =Integer.parseInt(FirmOrderNumTextView.getText().toString());
			}
			if (--product_count <= 1 ) {
				product_count = 1;
				FirmOrderNumTextView.setText(1 + "");
				FirmOrderWeightTextView.setText("数量：" + 1 + unit);
				if ("条".equals(unit) ) {
					FirmOrderTotalPriceTextView.setVisibility(View.GONE);
					FirmOrderSizeTextView.setVisibility(View.VISIBLE);
					FirmOrderSizeTextView.setText("规格：" + size);
				}
				if(!"条".equals(unit)) {
					FirmOrderTotalPriceTextView.setVisibility(View.VISIBLE);
					FirmOrderSizeTextView.setVisibility(View.GONE);
					FirmOrderTotalPriceTextView.setText("合计：￥" + df.format(unitprice * 1 + freight) + "元");
				}
				
				FirmOrderRealPayTextView.setText(df.format(unitprice * 1 - backprice + freight)+ "元");
				FirmOrderTotalPayTextView.setText(String.format(getResources().getString(R.string.commodity_firm_totalpay),df.format(unitprice * 1 + freight), df.format(backprice)));
			} else {
				FirmOrderNumTextView.setText(product_count + "");
				FirmOrderWeightTextView.setText("数量：" + product_count + unit);
				if ("条".equals(unit) ) {
					FirmOrderTotalPriceTextView.setVisibility(View.GONE);
					FirmOrderSizeTextView.setVisibility(View.VISIBLE);
					FirmOrderSizeTextView.setText("规格：" + size);
				}
				if(!"条".equals(unit)) {
					FirmOrderTotalPriceTextView.setVisibility(View.VISIBLE);
					FirmOrderSizeTextView.setVisibility(View.GONE);
					FirmOrderTotalPriceTextView.setText("合计：￥" + df.format(unitprice * product_count + freight) + "元");
				}
				
				FirmOrderRealPayTextView.setText(df.format(unitprice * product_count - backprice + freight)+ "元");
				FirmOrderTotalPayTextView.setText(String.format(getResources().getString(R.string.commodity_firm_totalpay),df.format(unitprice * product_count+ freight) , backprice+"" ));
			}
			break;
		case R.id.FirmOrderNumAddTextView:
			if (FirmOrderNumTextView.getText().toString() != null) {
				product_count =Integer.parseInt(FirmOrderNumTextView.getText().toString());
			}
			FirmOrderNumTextView.setText(++product_count + "");
			FirmOrderWeightTextView.setText("数量：" + product_count + unit);
			if ("条".equals(unit) ) {
				FirmOrderTotalPriceTextView.setVisibility(View.GONE);
				FirmOrderSizeTextView.setVisibility(View.VISIBLE);
				FirmOrderSizeTextView.setText("规格：" + size);
			}
			if(!"条".equals(unit)) {
				FirmOrderTotalPriceTextView.setVisibility(View.VISIBLE);
				FirmOrderSizeTextView.setVisibility(View.GONE);
				FirmOrderTotalPriceTextView.setText("合计：￥" + df.format(unitprice * product_count + freight) + "元");
			}
			
			FirmOrderRealPayTextView.setText((unitprice * product_count - backprice + freight )+ "元"); //总价格- 积分抵现 + 快递
			FirmOrderTotalPayTextView.setText(String.format(getResources().getString(R.string.commodity_firm_totalpay),unitprice * product_count + freight + "", backprice+"" ));
			break;
		case R.id.FirmOrderPointRelativeLayout:
			if (usePoint) {
				FirmOrderUsePointTextView.setBackgroundResource(R.drawable.commodity_firm_order_print_normal);
				usePoint = false ;
				FirmOrderPointTextView.setText("此订单可使用" + allowUsePoint + "积分" );//积分可低10%
				if (FirmType.equals(getIntent().getStringExtra(Constants.CART_TYPE_LIST))) {
					//多商品
					FirmOrderRealPayTextView.setText(df.format(totalprice + freight )+ "元"); //总价格 + 快递 
					FirmOrderTotalPayTextView.setText(String.format(getResources().getString(R.string.commodity_firm_totalpay),df.format(totalprice + freight), "0.0" ));
				} else {
					//单商品
					FirmOrderRealPayTextView.setText(df.format(unitprice * product_count  + freight )+ "元"); //总价格- 积分抵现 + 快递
					FirmOrderTotalPayTextView.setText(String.format(getResources().getString(R.string.commodity_firm_totalpay),df.format(unitprice * product_count + freight) , "0.0" ));
				}
				
			} else {
				if (inte >= allowUsePoint) {
				FirmOrderUsePointTextView.setBackgroundResource(R.drawable.commodity_firm_order_print_check);
				usePoint = true ;
				backprice = allowUsePoint * 0.01;
				FirmOrderPointTextView.setText("已使用" + allowUsePoint + "积分，抵现" + df.format(backprice) + "元" );//积分可低10%
					if (FirmType.equals(getIntent().getStringExtra(Constants.CART_TYPE_LIST))) {
						FirmOrderRealPayTextView.setText(df.format(totalprice + freight - backprice)+ "元"); //总价格 + 快递 - 抵现
						FirmOrderTotalPayTextView.setText(String.format(getResources().getString(R.string.commodity_firm_totalpay),df.format(totalprice + freight), df.format(backprice) ));
					} else {
						FirmOrderRealPayTextView.setText(df.format(unitprice * product_count  + freight - backprice )+ "元"); //总价格- 积分抵现 + 快递
						FirmOrderTotalPayTextView.setText(String.format(getResources().getString(R.string.commodity_firm_totalpay),df.format(unitprice * product_count + freight), df.format(backprice) ));
					}
				} else {
					ToastUtil.toast(getApplicationContext(), "您的积分不足");
				}
			}
//				alertPoint();//使用积分
			break;
		case R.id.FirmOrderPrintBillTextView:
			if (ischeck) {
				FirmOrderPrintBillTextView.setBackgroundResource(R.drawable.commodity_firm_order_print_normal);
				ischeck = false;
			} else {
				FirmOrderPrintBillTextView.setBackgroundResource(R.drawable.commodity_firm_order_print_check);
				ischeck = true;
			}
			break;
		case R.id.FirmOrderDeliverTimeTextView:
			LayoutInflater inflater = LayoutInflater.from(FirmOrderActivity.this);
			final View timepickerview = inflater.inflate(R.layout.common_view_timepicker,null);
			ScreenInfo screenInfo = new ScreenInfo(FirmOrderActivity.this);
			final WheelMain wheelMain = new WheelMain(timepickerview , true);
			wheelMain.screenheight = screenInfo.getHeight();
			
			//初始化Calendar日历对象
			Calendar cal = Calendar.getInstance();
			final int currentyear = cal.get(Calendar.YEAR);
			final int currentmonth = cal.get(Calendar.MONTH) ;
			final int currentday = cal.get(Calendar.DAY_OF_MONTH);
			final int currenthour = cal.get(Calendar.HOUR_OF_DAY);
			
			wheelMain.initDateTimePicker2(currentyear, currentmonth, currentday ,currenthour , 0);
			
			final CommonAlertDialog dialog = new CommonAlertDialog(FirmOrderActivity.this)
			.builder()
			.setTitle("选择收货时间")
			.setView(timepickerview)
			.setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.e(TAG, "选取的时间 = " + wheelMain.getTime());
				userTime=wheelMain.getTime();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.SIMPLIFIED_CHINESE);
				try {
					 userDate=sdf.parse(userTime);
				} catch (ParseException e) {
					e.printStackTrace();
					Log.e("转换用户时间异常", "转换用户时间出错了");
				}
				Log.e("转换后的用户时间为:",""+ userDate);
				String cuTime=currentTime();
				try {
					 cuDate=sdf.parse(cuTime);
				} catch (ParseException e) {
					e.printStackTrace();
					Log.e("转换当前时间异常", "转换当前时间出错了");
				}
				Log.e("转换后的当前时间为:",""+ cuDate);
				if (userDate.getTime()<cuDate.getTime()) {
					ToastUtil.toast(getApplicationContext(), "收货时间不能小于当前时间,请重新选择");
					FirmOrderDeliverTimeTextView.setText("点击选择");
				}else {
					FirmOrderDeliverTimeTextView.setText(wheelMain.getTime());
				}
				
				}
			});
			dialog.show();
			
			/*
			dataDialog = new DatePickerDialog(this,
					new OnDateSetListener() {

						@Override
						public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {
							if (year < currentyear  //不能小于当前年
								|| year >= currentyear && monthOfYear < currentmonth //不能小于当前月
								|| year >= currentyear && monthOfYear >= currentmonth && dayOfMonth < currentday) {//不能小于当前日
								FirmOrderDeliverTimeTextView.setText(currentyear + "-" + (currentmonth + 1)+ "-" + currentday);
							} else
							FirmOrderDeliverTimeTextView.setText(year + "-" + (monthOfYear + 1)+ "-" + dayOfMonth);
						}
					}, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH));
			dataDialog.show();
			*/
			break;
		case R.id.FirmOrderConfirmPayTextView2:
//			PayType = 1;//鱼类的货到付款
		case R.id.FirmOrderConfirmPayTextView:
//			PayType = 0;//普通付款
			if (FirmOrderDeliverTimeTextView.getText().equals("点此选择")) {
				ToastUtil.toast(getApplicationContext(), "请选择收货时间");
				Log.e(TAG, "+++++++++++ =" + FirmOrderAddressTextView.getText().length());
			} else if (FirmOrderAddressTextView.getText().length() == 9 ) {
				ToastUtil.toast(getApplicationContext(), "请选择收货地址");
			}else {
				if (FirmType.equals(getIntent().getStringExtra(Constants.CART_TYPE_LIST))) {
					//上传多个订单
					uploadMoreAddOrder();
				} else {
					//上传单个订单
					uploadAddOrder();
				}
			}
			break;
		/*
		case R.id.UsePointButton:
			if (alertDialog.isShowing()) {
				if (UsePointEditText.getText().toString().trim().isEmpty() ||
						Double.parseDouble(UsePointEditText.getText().toString().trim()) > inte ) {
					ToastUtil.toast(getApplicationContext(), "输入无效，请重新输入");
				} else {
					backprice = Double.parseDouble(UsePointEditText.getText().toString().trim());
					alertDialog.dismiss();
				}
			}
			break;
			*/
		default:
			break;
		}
	}


	//得到当前时间
	public String currentTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		Log.e("当前时间", str);
		return str;

	}
	
	//计算并转换时间
	public void doTime(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.SIMPLIFIED_CHINESE);
		String currTime=currentTime();
		Date date=null;
		try {
			date = sdf.parse(currTime);
		} catch (ParseException e) {
			e.printStackTrace();
			Log.e("转换当前时间出错···", e.getMessage());
		}
		Log.e("转换后的当前时间为：", "" + date);
		//得到用户选择的时间
		Date date2=null;
		try {
			date2=sdf.parse(userTime);
		} catch (ParseException e) {
			e.printStackTrace();
			Log.e("转换用户选择时间出错···", e.getMessage());
		}
		Log.e("转换后的用户选择时间为：", "" + date2);
		
		if (date2.getTime() <date.getTime() ) {
			ToastUtil.toast(getApplicationContext(), "收货时间必须大于当前时间");	
		}
		
	}

	private void searchPonit() {
		HashMap<String, String> paramMap = CommonDataUtil.getCommonParams(getApplicationContext());
		HttpParam httpParam = new HttpParam(ReleaseConfigure.HOMEPAGE_VALUE_TYPE_POINT, false); // GET
		searchPointHttpTask = new HttpTask(getApplicationContext(), this);
		httpParam.setParams(paramMap);
		searchPointHttpTask.execute(httpParam);
		pd.loadDialog();
	}
	
	private void searchFreight() {
		HashMap<String, String> paramMap = CommonDataUtil.getCommonParams(getApplicationContext());
		HttpParam httpParam = new HttpParam(ReleaseConfigure.HOMEPAGE_VALUE_TYPE_FREIGHT, false); // GET
		searchFreightHttpTask = new HttpTask(getApplicationContext(), this);
		paramMap.put(Constants.LAT, lat);
		paramMap.put(Constants.LNG, lng);
		httpParam.setParams(paramMap);
		searchFreightHttpTask.execute(httpParam);
		pd.loadDialog();
	}

	//单个订单提交
	private void uploadAddOrder() {
		HashMap<String, String> paramMap = CommonDataUtil.getCommonParams(getApplicationContext());
		HttpParam httpParam = new HttpParam(ReleaseConfigure.HOMEPAGE_VALUE_UPLOAD_ADD_ORDER, false); // Get
		uploadAddOrderHttpTask = new HttpTask(getApplicationContext(), this);
		paramMap.put(Constants.COMMODITY_ORDERPOSTSCRIPT, FirmOrderMessageEditText.getText().toString().trim());//留言
		paramMap.put(Constants.COMMODITY_ORDERDELID, String.valueOf(1));//配送方式id 
		paramMap.put(Constants.COMMODITY_ORDERGOODSAMOUNT, unitprice + "");//购买单价
		paramMap.put(Constants.COMMODITY_UNIT, unit);//商品单位
		paramMap.put(Constants.COMMODITY_ORDERNUMBER, FirmOrderNumTextView.getText().toString());//购买数量
		paramMap.put(Constants.COMMODITY_GOODSID, goodsid + "");//商品id
		paramMap.put(Constants.COMMODITY_ORDERSHIPPINGFEE, freight + "" );//邮费
		paramMap.put(Constants.COMMODITY_ORDERRECEIVINGTIME, FirmOrderDeliverTimeTextView.getText().toString());//收货时间
		if (ischeck) {
			paramMap.put(Constants.COMMODITY_ORDERBILL, "1");//打印发票
		} else {
			paramMap.put(Constants.COMMODITY_ORDERBILL, "0");//不打印发票
		}
		paramMap.put(Constants.COMMODITY_ORDERCONVERSION, "1");//是否使用积分 默认用0积分
		if (usePoint) {
			paramMap.put(Constants.COMMODITY_INTEGRAMONEY, backprice + "");//使用积分抵现 
			paramMap.put(Constants.COMMODITY_INTEGRA, allowUsePoint + "");//使用积分 
			Log.e(TAG, "提交积分 = " + allowUsePoint);
			Log.e(TAG, "提交抵现 = " + backprice);
		} else {
			paramMap.put(Constants.COMMODITY_INTEGRAMONEY, "0.0");//使用积分抵现 
			paramMap.put(Constants.COMMODITY_INTEGRA, "0");//使用积分 
		}
		paramMap.put(Constants.COMMODITY_ORDERAMOUNT, FirmOrderTotalPriceTextView.getText().toString().replace("合计：￥", "").replace("元", ""));//总支付多少钱
		Log.e(TAG, "实际支付金额 = " + FirmOrderRealPayTextView.getText().toString().replace("元", ""));
		paramMap.put(Constants.COMMODITY_ORDERTOTAL, FirmOrderRealPayTextView.getText().toString().replace("元", ""));//应该掏的钱
		//-------------------------------------------------------
		paramMap.put(Constants.COMMODITY_ADDID, addressid);//收货地址id
		Log.e(TAG, "提交单个订单的 addid = " + addressid);
		//----------------------------------------------------
		httpParam.setParams(paramMap);
		uploadAddOrderHttpTask.execute(httpParam);
		pd.loadDialog();
	}
	
	//多个订单提交
	private void uploadMoreAddOrder() {
		HashMap<String, String> paramMap = new HashMap<String, String>();
		HttpParam httpParam = new HttpParam(ReleaseConfigure.HOMEPAGE_VALUE_UPLOAD_MORE_ADD_ORDER, true); // GET
		List<MoreOrder> moreOrderList = new ArrayList<MoreOrder>();
		for (int i = 0; i < cartGoodList.size(); i++) {
			MoreOrder moreOrder = new MoreOrder();
			CartGood cartGood = (CartGood)cartGoodList.get(i);
			Log.e(TAG, "存放到多订单位置  = " + i);
			moreOrder.setCartid(cartGood.getCartId());
			moreOrder.setUsid(SharedPreferencesUtil.getUsid(getApplicationContext()));
			moreOrder.setOrderPostscript(FirmOrderMessageEditText.getText().toString().trim());
			moreOrder.setOrderDelId(1);
			if (ischeck) {
				moreOrder.setOrderBill("1");//打印发票
			} else {
				moreOrder.setOrderBill("0");//不打印发票
			}
			moreOrder.setOrderGoodsAmount(cartGood.getCartGoodsPrice());
			moreOrder.setOrderShippingFee(freight);
			moreOrder.setOrderNumber(cartGood.getCartNumber());
			moreOrder.setOrderConversion(0);
			if (usePoint) {
				moreOrder.setOrderIntegral(allowUsePoint);
				moreOrder.setOrderIntegralMoney(backprice);
			} else {
				moreOrder.setOrderIntegral(0);
				moreOrder.setOrderIntegralMoney(0.0);
			}
			moreOrder.setOrderAmount(totalprice);//总价
			Log.e(TAG, "cartGoodList size= " + cartGoodList.size() + "--totalprice=" + totalprice + "--" + freight);
			if (cartGoodList.size() >= 5 && totalprice >= 100){
				Log.e(TAG, "超过5件产品并且产品价格超过100");
				moreOrder.setOrderTotal(totalprice - backprice);//实付金额
			}else{
				Log.e(TAG, "未超过5件产品或价格不到100");
				moreOrder.setOrderTotal(totalprice + freight - backprice);//实付金额
			}
			moreOrder.setOrderunit(cartGood.getCartUnit());
			moreOrder.setGoodsid(cartGood.getGoodsid());
			//------------------------
			moreOrder.setAddid(addressid);
			//------------------------
			moreOrderList.add(moreOrder);
		}
		String str = JSONArray.toJSONString(moreOrderList);
		
		paramMap.put(Constants.COMMODITY_LISTORDER, str);
		
		uploadMoreAddOrderHttpTask = new HttpTask(this, this);
		httpParam.setParams(paramMap);
		uploadMoreAddOrderHttpTask.execute(httpParam);
		pd.loadDialog();
	}
	
	
	@Override
	public void onLoadFinish(HttpTask task, HttpResult result) {
		pd.removeDialog();	
		Log.e(TAG, result.getData());
		if (result != null && !StringUtil.isEmpty(result.getData()) && StringUtil.isGoodJson(result.getData()) && this !=null && !this.isFinishing() ) {
			CommonResult commonResult = JSON.parseObject(result.getData(),CommonResult.class);
			if (null != commonResult) {
				if (task == searchPointHttpTask) {
					if (commonResult.validate()) {
						JSONArray jsonArray = JSON.parseArray(commonResult.getData());
						if (null != jsonArray && jsonArray.size() > 0) {
							initPoint(jsonArray);
						} 
					} else {
//						ToastUtil.toast(this,commonResult.getErrMsg());
					}
				}
				
				if (task == searchFreightHttpTask) {
					if (commonResult.validate()) {
						JSONObject jsonObject = JSONObject.parseObject(commonResult.getData());
						if (null != jsonObject) {
							freight = jsonObject.getDouble("freight");//运费
							if (freight!= null) {
								FirmOrderTransPriceTextView.setText("运费：" + freight + "元");
								if ("条".equals(unit) ) {
									FirmOrderTotalPriceTextView.setVisibility(View.GONE);
									FirmOrderSizeTextView.setVisibility(View.VISIBLE);
									FirmOrderSizeTextView.setText("规格：" + size);
									FirmOrderRealPayTextView.setText((unitprice * product_count - backprice + freight)+ "元"); //总价格- 积分抵现 + 快递
									FirmOrderTotalPayTextView.setText(String.format(getResources().getString(R.string.commodity_firm_totalpay),unitprice * product_count + freight + "", backprice+"" ));
								} else {
									FirmOrderTotalPriceTextView.setVisibility(View.VISIBLE);
									FirmOrderSizeTextView.setVisibility(View.GONE);
									if (FirmType.equals(getIntent().getStringExtra(Constants.CART_TYPE_LIST))) {
										if (cartGoodList.size() >= 5 && totalprice >= 100) {
											//满5种商品 且 满100块钱
											List<Integer> oldnums = new java.util.ArrayList<Integer>();
											for (int i = 0; i < cartGoodList.size(); i++) {
												oldnums.add(((CartGood)cartGoodList.get(i)).getCartId());
												Log.e(TAG, "oldnums 添加 = " + ((CartGood)cartGoodList.get(i)).getCartId());
											}
											 List<Integer> newnums = new java.util.ArrayList<Integer>();//去重后的lsit
											 for (int i = 0; i < cartGoodList.size(); i++) {
									             if (!newnums.contains(oldnums.get(i))) {
									            	 newnums.add(oldnums.get(i)); 
									             }
									         }
											Log.e(TAG, "去重后的newnums = " + newnums.size());
											if (newnums.size() >= 5) {
												FirmOrderTotalPriceTextView.setText("合计：￥" + df.format(totalprice) + "元");
												FirmOrderRealPayTextView.setText(df.format(totalprice) + "元"); //总价格 + 免快递
												FirmOrderTotalPayTextView.setText(String.format(getResources().getString(R.string.commodity_firm_totalpay), df.format(totalprice) , backprice+"" ));
												FirmOrderTransPriceTextView.setText("运费：" + "免运费");
											}
										}else {
											FirmOrderTotalPriceTextView.setText("合计：￥" + df.format(totalprice + freight) + "元");
											FirmOrderRealPayTextView.setText(df.format(totalprice + freight)+ "元"); //总价格 + 快递
											FirmOrderTotalPayTextView.setText(String.format(getResources().getString(R.string.commodity_firm_totalpay),df.format(totalprice + freight), backprice+"" ));
										}
									} else {
										FirmOrderTotalPriceTextView.setText("合计：￥" + (unitprice * product_count + freight) + "元");
										FirmOrderRealPayTextView.setText((unitprice * product_count - backprice + freight)+ "元"); //总价格- 积分抵现 + 快递
										FirmOrderTotalPayTextView.setText(String.format(getResources().getString(R.string.commodity_firm_totalpay), df.format(unitprice * product_count + freight), backprice+"" ));
									}
									
								}
							} else {
								FirmOrderTransPriceTextView.setText("运费：0元");
							}
						} 
					} else {
//						ToastUtil.toast(this,commonResult.getErrMsg());
					}
				}
				
				if (task == uploadAddOrderHttpTask) {
					if (commonResult.validate()) {
						if (commonResult.validate()) {
//							if (PayType == 0) {
								JSONArray jsonArray = JSON.parseArray(commonResult.getData());
								if (null != jsonArray && jsonArray.size() > 0) {
									initOrder(jsonArray);
								} 
//							}
//							if (PayType == 1) {
//								//货到付款 直接进入代收货
//								ShowSuccess();
//							}
							
						} else {
//							ToastUtil.toast(this,commonResult.getErrMsg());
						}
					} else {
//						ToastUtil.toast(this,commonResult.getErrMsg());
					}
				}
				
				if (task == uploadMoreAddOrderHttpTask) {
					if (commonResult.validate()) {
						if (commonResult.validate()) {
//							if (PayType == 0) {
								JSONArray jsonArray = JSON.parseArray(commonResult.getData());
								if (null != jsonArray && jsonArray.size() > 0) {
									initOrder(jsonArray);
								} 
//							}
							
						} else {
//							ToastUtil.toast(this,commonResult.getErrMsg());
						}
					} else {
//						ToastUtil.toast(this,commonResult.getErrMsg());
					}
				}
				
			}
		}
		
	}
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.e(TAG, "返回到这里 ");
			if (data != null) {
//				Address address = (Address)data.getSerializableExtra(Constants.PERSONAL_CHOOSE_ADDRESS);
				Log.e(TAG, "收到的data = " + data);
				addressid = data.getStringExtra(Constants.UPDATE_ID);
				String name = data.getStringExtra(Constants.UPDATE_NAME);
				String tel = data.getStringExtra(Constants.UPDATE_TEL);
				String address = data.getStringExtra(Constants.UPDATE_ADDRESS);
				Log.e(TAG, "收到的地址 = " + addressid + data.getStringExtra(Constants.UPDATE_NAME));
				FirmOrderNamePhoneTextView.setText(String.format(getResources().getString(R.string.commodity_firm_address_name), name , tel));
				FirmOrderAddressTextView.setText(String.format(getResources().getString(R.string.commodity_firm_address_address), address ));
			}
		}
	
	private void initProduct() {
		FirmOrderProductNameTextView.setText(goodsName);
		FirmOrderProductInfoTextView.setText(goodsDesc);
		FirmOrderWeightTextView.setText("数量：" + 1 + unit);
		if ("条".equals(unit) ) {
			FirmOrderTotalPriceTextView.setVisibility(View.GONE);
			FirmOrderSizeTextView.setVisibility(View.VISIBLE);
			FirmOrderSizeTextView.setText("规格：" + size);
			FirmOrderProductPriceTextView.setText("￥" + unitprice + "元/斤");
			FirmOrderConfirmPayTextView.setVisibility(View.GONE);
			FirmOrderConfirmPayTextView2.setVisibility(View.VISIBLE);
		}
		if (!"条".equals(unit) ) {
			FirmOrderTotalPriceTextView.setVisibility(View.VISIBLE);
			FirmOrderSizeTextView.setVisibility(View.GONE);
			FirmOrderProductPriceTextView.setText("￥" + unitprice + "元/" + unit);
			FirmOrderConfirmPayTextView.setVisibility(View.VISIBLE);
			FirmOrderConfirmPayTextView2.setVisibility(View.GONE);
			if (FirmType.equals(getIntent().getStringExtra(Constants.CART_TYPE_LIST))) {
//				FirmOrderTotalPriceTextView.setText("合计：￥" + df.format(totalprice + freight) + "元");
			} else {
//				FirmOrderTotalPriceTextView.setText("合计：￥" + df.format(unitprice * product_count + freight) + "元");
			}
		}
		ImageLoader.getInstance().displayImage(goodsThumb,FirmOrderProductImageView, options,
				new SimpleImageLoadingListener() {
					@Override
					public void onLoadingStarted(String imageUri, View view) {
					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
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
	
	
	private void initPoint(JSONArray jsonArray) {
		inte = jsonArray.getJSONObject(0).getInteger("inte");//可用积分
//		inte2 = jsonArray.getJSONObject(0).getInteger("inte2");//全部积分
		Log.e(TAG, "用户共有" + inte + "积分");
		if (FirmType.equals(getIntent().getStringExtra(Constants.CART_TYPE_LIST))) {
			allowUsePoint = (int)(totalprice * 100 * 0.1);
		}
		FirmOrderPointTextView.setText("此订单可使用" + allowUsePoint + "积分" );//积分可低10%
	}
	
	
	@SuppressLint("DefaultLocale")
	private void initOrder(JSONArray jsonArray) {
		JSONObject jsonObject= jsonArray.getJSONObject(0);
		order = JSONObject.toJavaObject(jsonObject, Order.class);
		Intent intent = new Intent(FirmOrderActivity.this,PayModeActivity.class);
		BigDecimal doubletotal = new BigDecimal(order.getOrderTotal());
		doubletotal = doubletotal.setScale(2,BigDecimal.ROUND_HALF_UP);
		Log.e(TAG, "获取的 amount = " + order.getOrderTotal());
		Log.e(TAG, "转成String 的 amount = " + doubletotal);
		intent.putExtra(Constants.COMMODITY_AMOUNT, doubletotal+"");
		intent.putExtra(Constants.ORDERNUMBER_SN, order.getOrderno());
		startActivity(intent);
	}
	
	@Override
	public void initData() {
	}
	
	
	@Override
	protected void onResume() {
		if (SharedPreferencesUtil.getDefaultAddressName(getApplicationContext()) != null) {
			
			defaultId = SharedPreferencesUtil.getDefaultAddressId(getApplicationContext());
			if ("0".equals(addressid) ) {
				addressid = defaultId;
				Log.e(TAG, "默认地址id = " + addressid);
			}
			
			defaultName = SharedPreferencesUtil.getDefaultAddressName(getApplicationContext());
			defaultTel = SharedPreferencesUtil.getDefaultAddressTel(getApplicationContext());
			defaultAddress = SharedPreferencesUtil.getDefaultAddressAddress(getApplicationContext());
			
//			FirmOrderNamePhoneTextView.setText(String.format(getResources().getString(R.string.commodity_firm_address_name), defaultName , defaultTel));
//			FirmOrderAddressTextView.setText(String.format(getResources().getString(R.string.commodity_firm_address_address), defaultAddress));
		} else {
			FirmOrderNamePhoneTextView.setText(String.format(getResources().getString(R.string.commodity_firm_address_name), "" , ""));
			FirmOrderAddressTextView.setText(String.format(getResources().getString(R.string.commodity_firm_address_address), ""));
		}
		super.onResume();
	}
	
	
	@SuppressLint("NewApi")
	private void ShowSuccess() {
		final AlertDialog dialog = new AlertDialog.Builder(this,R.style.PopDialog).create();
		dialog.setCancelable(false);
		dialog.show();
		Window window = dialog.getWindow();
		window.setContentView(R.layout.homepage_paysuccess_dialog);
		Button BackButton = (Button) window.findViewById(R.id.BackButton);
		BackButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		
	}
	
	
	/**
	 * 使用积分AlertDialog
	 * 
	 */
	/*
	@SuppressLint("InflateParams")
	private void alertPoint() {
		TotalPointTextView.setText(String.format(getResources().getString(R.string.homepage_point), inte2+"" , inte+""));
		alertDialog.show();
		alertDialog.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface arg0) {
				if (backprice != null) {
					FirmOrderPointTextView.setText("使用" + backprice + "积分，抵现" + backprice/100 + "元"); //积分抵现计算
				}
			}
		});
		
	}
	
	*/
	/**
	 * 隐藏软键盘
	 */
	private void hideKeyboard() {
		if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getCurrentFocus() != null)
				manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
}
