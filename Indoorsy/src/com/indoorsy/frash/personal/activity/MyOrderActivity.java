package com.indoorsy.frash.personal.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import com.indoorsy.frash.http.core.HttpParam;
import com.indoorsy.frash.http.core.HttpResult;
import com.indoorsy.frash.http.core.HttpTask;
import com.indoorsy.frash.personal.adapter.MyOrderAdapter;
import com.indoorsy.frash.personal.adapter.MyOrderAdapter.onMyOrderCancelItemClickListener;
import com.indoorsy.frash.personal.adapter.MyOrderAdapter.onMyOrderConfirmItemClickListener;
import com.indoorsy.frash.personal.adapter.MyOrderAdapter.onMyOrderEvaluationClickListener;
import com.indoorsy.frash.personal.adapter.MyOrderAdapter.onMyOrderRefundItemClickListener;
import com.indoorsy.frash.personal.data.bean.MyOrder;
import com.indoorsy.frash.util.CommonDataUtil;
import com.indoorsy.frash.util.StringUtil;
import com.indoorsy.frash.util.ToastUtil;

/*
 * 我的订单
 */
public class MyOrderActivity extends BasicActivity implements OnItemClickListener{
	
	private static final String TAG = MyOrderActivity.class.getSimpleName();

	private CustomTitleView customTitleView;
	
	private int type = 0;
	
	private ListView myorderListView;
	
	private MyOrderAdapter myOrderAdapter;
	
	private CommonProgressDialog commonProgressDialog;
	
	private HttpTask searchAllHttpTask, searchStateHttpTask,
			searchRefundHttpTask, cancelOrderHttpTask, refundOrderHttpTask,conifrmOrderHttpTask;

	private View myorderAllView, myorderDaiFuKuanView, myorderDaiFaHuoView,
			myorderDaiShouHuoView, myorderDaiPingJiaView,
			myorderTuiKuanJiLuView;

	private LinearLayout myorderAllLinearLayout, myorderDaiFuKuanLinearLayout,
			myorderDaiFaHuoLinearLayout, myorderDaiShouHuoLinearLayout,
			myorderDaiPingJiaLinearLayout, myorderTuiKuanJiLuLinearLayout;
	
	private ArrayList<View> viewList = new ArrayList<View>();
	
	private List<MyOrder> list;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.leftImageView:
				this.finish();
				break;
			case R.id.myorderAllLinearLayout:
				type = 0;
				setType(0);
				searchAll();
				break;
			case R.id.myorderDaiFuKuanLinearLayout:
				type = 1;
				setType(1);
				searchState(0,0,0);
				break;
			case R.id.myorderDaiFaHuoLinearLayout:
				type = 2;
				setType(2);
				searchState(1,0,1);
				break;
			case R.id.myorderDaiShouHuoLinearLayout://代收货
				type = 3;
				setType(3);
				searchState(1,1,1);
				break;
			case R.id.myorderDaiPingJiaLinearLayout:
				type = 4;
				setType(4);
				searchState(5,2,1);
				break;
			case R.id.myorderTuiKuanJiLuLinearLayout:
				type = 5;
				setType(5);
				searchRefund();
				break;
		}
	}
	
	@SuppressWarnings("deprecation")
	private void setType(int position) {
        int size = viewList.size();
        for (int i = 0; i < size; i++) {
        	viewList.get(i).setSelected(i == position);
            if(viewList.get(i).isSelected()){
            	viewList.get(i).setBackgroundDrawable(getResources().getDrawable(R.drawable.homepage_viewpager_item_dot_select));
            }else{
            	viewList.get(i).setBackgroundDrawable(getResources().getDrawable(R.drawable.homepage_viewpager_item_dot_normal));
            }
        }
    }
	
	//查询所有
	private void searchAll(){
		HashMap<String,String> paHashMap=CommonDataUtil.getCommonParams(getApplicationContext());
		HttpParam httpParam=new HttpParam(ReleaseConfigure.HOMEPAGE_VALUE_TYPE_ORDER_ALL, false);
		searchAllHttpTask=new HttpTask(getApplicationContext(), this);
		httpParam.setParams(paHashMap);
		searchAllHttpTask.execute(httpParam);
		commonProgressDialog.loadDialog();
	}
	
	//查询不同状态
	private void searchState(int orderstate,int shippingstate,int paystate){
		HashMap<String,String> paHashMap=CommonDataUtil.getCommonParams(getApplicationContext());
		paHashMap.put(Constants.ORDERSTATE, String.valueOf(orderstate));
		paHashMap.put(Constants.SHIPPINGSTATE, String.valueOf(shippingstate));
		paHashMap.put(Constants.PAYSTATE, String.valueOf(paystate));
		HttpParam httpParam=new HttpParam(ReleaseConfigure.HOMEPAGE_VALUE_TYPE_ORDER_STATE, false);
		searchStateHttpTask=new HttpTask(getApplicationContext(), this);
		httpParam.setParams(paHashMap);
		searchStateHttpTask.execute(httpParam);
		commonProgressDialog.loadDialog();
	}
	
	//取消订单
	private void cancleOrder(String orderid){
		HashMap<String,String> paHashMap=CommonDataUtil.getCommonParams(getApplicationContext());
		paHashMap.put(Constants.ORDERID, orderid);
		HttpParam httpParam=new HttpParam(ReleaseConfigure.HOMEPAGE_VALUE_TYPE_ORDER_CANCEL, false);
		cancelOrderHttpTask=new HttpTask(getApplicationContext(), this);
		httpParam.setParams(paHashMap);
		cancelOrderHttpTask.execute(httpParam);
		commonProgressDialog.loadDialog();
	}
	
	//申请退款
	private void refundOrder(String orderid){
		HashMap<String,String> paHashMap=CommonDataUtil.getCommonParams(getApplicationContext());
		paHashMap.put(Constants.ORDERID, orderid);
		paHashMap.put("orderstate", "4");
		paHashMap.put("shippingstate", "4");
		paHashMap.put("paystate", "1");
		HttpParam httpParam=new HttpParam(ReleaseConfigure.HOMEPAGE_VALUE_TYPE_ORDER_CONFIRM, false);
		refundOrderHttpTask=new HttpTask(getApplicationContext(), this);
		httpParam.setParams(paHashMap);
		refundOrderHttpTask.execute(httpParam);
		commonProgressDialog.loadDialog();
	}
	
	//确认收货
	private void confirmOrder(String orderid){
		HashMap<String,String> paHashMap=CommonDataUtil.getCommonParams(getApplicationContext());
		paHashMap.put("orderstate", "5");
		paHashMap.put("shippingstate", "2");
		paHashMap.put("paystate", "1");
		paHashMap.put(Constants.ORDERID, orderid);
		HttpParam httpParam=new HttpParam(ReleaseConfigure.HOMEPAGE_VALUE_TYPE_ORDER_CONFIRM, false);
		conifrmOrderHttpTask=new HttpTask(getApplicationContext(), this);
		httpParam.setParams(paHashMap);
		conifrmOrderHttpTask.execute(httpParam);
		commonProgressDialog.loadDialog();
	}
	
	//查询退款记录
	private void searchRefund(){
		HashMap<String,String> paHashMap=CommonDataUtil.getCommonParams(getApplicationContext());
		HttpParam httpParam=new HttpParam(ReleaseConfigure.HOMEPAGE_VALUE_TYPE_ORDER_REFUND, false);
		searchRefundHttpTask=new HttpTask(getApplicationContext(), this);
		httpParam.setParams(paHashMap);
		searchRefundHttpTask.execute(httpParam);
		commonProgressDialog.loadDialog();
	}

	@Override
	public int initLayout() {
		return R.layout.personal_myorder;
	}

	@Override
	public void initUI() {
		customTitleView = (CustomTitleView) findViewById(R.id.customTitleView);
		customTitleView.setTitle(R.string.personal_fragment_myorder_title);
		customTitleView.setLeftImageBg(R.drawable.comm_back);
		customTitleView.setVisibility(View.VISIBLE);
		customTitleView.setOnClickListener(this);
		
		commonProgressDialog = new CommonProgressDialog(this);
		
		myorderAllLinearLayout = (LinearLayout)findViewById(R.id.myorderAllLinearLayout);
		myorderAllLinearLayout.setOnClickListener(this);
		myorderDaiFuKuanLinearLayout = (LinearLayout)findViewById(R.id.myorderDaiFuKuanLinearLayout);
		myorderDaiFuKuanLinearLayout.setOnClickListener(this);
		myorderDaiFaHuoLinearLayout = (LinearLayout)findViewById(R.id.myorderDaiFaHuoLinearLayout);
		myorderDaiFaHuoLinearLayout.setOnClickListener(this);
		myorderDaiShouHuoLinearLayout = (LinearLayout)findViewById(R.id.myorderDaiShouHuoLinearLayout);
		myorderDaiShouHuoLinearLayout.setOnClickListener(this);
		myorderDaiPingJiaLinearLayout = (LinearLayout)findViewById(R.id.myorderDaiPingJiaLinearLayout);
		myorderDaiPingJiaLinearLayout.setOnClickListener(this);
		myorderTuiKuanJiLuLinearLayout = (LinearLayout)findViewById(R.id.myorderTuiKuanJiLuLinearLayout);
		myorderTuiKuanJiLuLinearLayout.setOnClickListener(this);
		myorderAllView = (View)findViewById(R.id.myorderAllView);
		viewList.add(myorderAllView);
		myorderDaiFuKuanView = (View)findViewById(R.id.myorderDaiFuKuanView);
		viewList.add(myorderDaiFuKuanView);
		myorderDaiFaHuoView = (View)findViewById(R.id.myorderDaiFaHuoView);
		viewList.add(myorderDaiFaHuoView);
		myorderDaiShouHuoView = (View)findViewById(R.id.myorderDaiShouHuoView);
		viewList.add(myorderDaiShouHuoView);
		myorderDaiPingJiaView = (View)findViewById(R.id.myorderDaiPingJiaView);
		viewList.add(myorderDaiPingJiaView);
		myorderTuiKuanJiLuView = (View)findViewById(R.id.myorderTuiKuanJiLuView);
		viewList.add(myorderTuiKuanJiLuView);
		myOrderAdapter = new MyOrderAdapter(this);
		myorderListView = (ListView)findViewById(R.id.myorderListView);
		myorderListView.setAdapter(myOrderAdapter);
		myorderListView.setOnItemClickListener(this);
	}
	
	@Override
	public void initData() {
		type = getIntent().getIntExtra(Constants.TYPE, 0);
		byTypeSearch();
	}
	
	private void byTypeSearch(){
		switch (type) {
			case 0:
				type = 0;
				setType(0);
				searchAll();
				break;
			case 1:
				type = 1;
				setType(1);
				searchState(0,0,0);
				break;
			case 2:
				type = 2;
				setType(2);
				searchState(1,0,1);
				break;
			case 3:
				type = 3;
				setType(3);
				searchState(1,1,1);
				break;
			case 4:
				type = 4;
				setType(4);
				searchState(5,2,1);
				break;
			case 5:
				type = 5;
				setType(5);
				searchRefund();
				break;
		}
	}

	@Override
	public void onLoadFinish(HttpTask task, HttpResult result) {
		Log.e(TAG, result.getData());
		commonProgressDialog.removeDialog();
		Log.e(TAG, result.getData());
		if (result != null && !StringUtil.isEmpty(result.getData()) && StringUtil.isGoodJson(result.getData())) {
			CommonResult commonResult = JSON.parseObject(result.getData(),CommonResult.class);
			if (null != commonResult) {
				if(task == searchAllHttpTask){
					if (commonResult.validate()) {
						JSONArray jsonArray = JSON.parseArray(commonResult.getData());
						if(jsonArray != null && jsonArray.size() > 0){
							initMyOrder(jsonArray);
						}else{
							myOrderAdapter.resetData(null, type);
						}
					}else{
						myOrderAdapter.resetData(null, type);
//						ToastUtil.toast(getApplicationContext(), commonResult.getErrMsg());
					}
				}
				
				if(task == searchStateHttpTask){
					if (commonResult.validate()) {
						JSONArray jsonArray = JSON.parseArray(commonResult.getData());
						if(jsonArray != null && jsonArray.size() > 0){
							initMyOrder(jsonArray);
						}else{
							myOrderAdapter.resetData(null, type);
						}
					}else{
						myOrderAdapter.resetData(null, type);
//						ToastUtil.toast(getApplicationContext(), commonResult.getErrMsg());
					}
				}
				
				if(task == searchRefundHttpTask){
					if (commonResult.validate()) {
						JSONArray jsonArray = JSON.parseArray(commonResult.getData());
						if(jsonArray != null && jsonArray.size() > 0){
							initMyOrder(jsonArray);
						}else{
							myOrderAdapter.resetData(null, type);
						}
					}else{
						myOrderAdapter.resetData(null, type);
//						ToastUtil.toast(getApplicationContext(), commonResult.getErrMsg());
					}
				}
				
				if(task == cancelOrderHttpTask){
					if (commonResult.validate()) {
						ToastUtil.toast(getApplicationContext(), "取消订单成功");
						type = 1;
						setType(1);
						searchState(0,0,0);
					}else{
//						ToastUtil.toast(getApplicationContext(), commonResult.getErrMsg());
					}
				}
				
				if(task == refundOrderHttpTask){
					if (commonResult.validate()) {
						ToastUtil.toast(getApplicationContext(), "申请已提交");
						type = 2;
						setType(2);
						searchState(1,0,1);
					}else{
//						ToastUtil.toast(getApplicationContext(), commonResult.getErrMsg());
					}
				}
				
				if(task == conifrmOrderHttpTask){
					if (commonResult.validate()) {
						ToastUtil.toast(getApplicationContext(), "确认收货成功");
						type = 3;
						setType(3);
						searchState(1,1,1);
					}else{
//						ToastUtil.toast(getApplicationContext(), commonResult.getErrMsg());
					}
				}
			}
		}
	}
	
	private void initMyOrder(JSONArray jsonArray){
		final List<MyOrder> myOrderList = new ArrayList<MyOrder>();
		list=myOrderList;
		for (int i = 0; i < jsonArray.size(); i++) {
			MyOrder myOrder = JSONObject.toJavaObject(jsonArray.getJSONObject(i), MyOrder.class);
			myOrderList.add(myOrder);
		}
		
		
		if(myOrderList != null && myOrderList.size() > 0){
			myOrderAdapter.resetData(myOrderList, type);
			myOrderAdapter.setOnMyOrderCancelItemClickListener(new onMyOrderCancelItemClickListener() {
				@Override
				public void onMyOrderCancelItemClick(View v, int position) {
					String orderid = myOrderList.get(position).getOrderId() + "";
					//ToastUtil.toast(getApplicationContext(), "订单号:"+orderid);
					cancleOrder(orderid);
				}
			});
			myOrderAdapter.setOnMyOrderRefundItemClick(new onMyOrderRefundItemClickListener() {
				
				@Override
				public void onMyOrderRefundItemClick(View v, int position) {
					String orderid = myOrderList.get(position).getOrderId() + "";
					Log.e(TAG, "申请退款ordersn = " + orderid);
					refundOrder(orderid);
				}
			});
				
			myOrderAdapter.setOnMyOrderConfirmItemClickListener(new onMyOrderConfirmItemClickListener() {
				
				@Override
				public void onMyOrderConfirmItemClick(View v, int position) {
					String orderid = myOrderList.get(position).getOrderId() + "";
					confirmOrder(orderid);
				}
			});
			myOrderAdapter.setOnMyOrderEvaluationClickListener(new onMyOrderEvaluationClickListener() {
				
				@Override
				public void onMyOrderEvaluationClick(View v, int position) {
					Intent intent = new Intent(MyOrderActivity.this,EvaluationActivity.class);
					intent.putExtra(Constants.ORDERID, ((MyOrder) myOrderAdapter.getItem(position)).getOrderId());
					intent.putExtra(Constants.HOMEPAGE_PRODUCT_NAME, ((MyOrder) myOrderAdapter.getItem(position)).getGoodsname());
					intent.putExtra(Constants.HOMEPAGE_PRODUCT_THUMB,((MyOrder) myOrderAdapter.getItem(position)).getGoodsthumb());
					intent.putExtra(Constants.HOMEPAGE_PRODUCT_PRICE,((MyOrder) myOrderAdapter.getItem(position)).getOrderamount());
					startActivityForResult(intent, 0);
				}
			});
		}
			
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent intent=new Intent(MyOrderActivity.this,OrderDetailsActivity.class);
		//Intent  intent = new Intent(this,ProductDetailActivity.class);
		/*intent.putExtra(Constants.HOMEPAGE_PRODUCT_ID, ((MyOrder)arg0.getAdapter().getItem(arg2)).getGoodsid());
		intent.putExtra(Constants.HOMEPAGE_PRODUCT_NAME, ((MyOrder)arg0.getAdapter().getItem(arg2)).getGoodsname());
		intent.putExtra(Constants.HOMEPAGE_PRODUCT_DESC, ((MyOrder)arg0.getAdapter().getItem(arg2)).getGoodsdesc());
		intent.putExtra(Constants.HOMEPAGE_PRODUCT_THUMB, ((MyOrder)arg0.getAdapter().getItem(arg2)).getGoodsthumb());*/
		//ToastUtil.toast(getApplicationContext(), "订单ID:"+list.get(arg2).getOrdersn());
		intent.putExtra(Constants.ORDERNUMBER_SN, list.get(arg2).getOrdersn());
		startActivity(intent);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK){
			if (requestCode == 0) {
				searchState(5,2,1);
			} else {
				byTypeSearch();
			}
		}
	}

}
