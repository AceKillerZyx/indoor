package com.indoorsy.frash.personal.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

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
import com.indoorsy.frash.http.core.HttpParam;
import com.indoorsy.frash.http.core.HttpResult;
import com.indoorsy.frash.http.core.HttpTask;
import com.indoorsy.frash.personal.adapter.AddressAdapter;
import com.indoorsy.frash.personal.adapter.AddressAdapter.Delect;
import com.indoorsy.frash.personal.data.bean.Address;
import com.indoorsy.frash.util.CommonDataUtil;
import com.indoorsy.frash.util.SharedPreferencesUtil;
import com.indoorsy.frash.util.StringUtil;
import com.indoorsy.frash.util.ToastUtil;
/*
 * 管理收货地址
 */
public class ManageAddressActivity extends BasicActivity   {
	
	private static final String  TAG = ManageAddressActivity.class.getSimpleName();
	
	public static final String NEW_ADDRESS = "new";
	public static final String UPDATE_ADDRESS = "update";
	public static final String CHOOSE_ADDRESS = "choose";
	
	
	private CommonProgressDialog pd;
	private HttpTask searchAddressHttpTask,deleteAddressHttpTask;
	private CustomTitleView customTitleView;
	private ListView personalAddressListView;
	private AddressAdapter addressAdapter;
	private String[] longClickMenu = new String[] { "设为收货地址", "删除" };
	private List<Address> addressList;
	
	private int position ;
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				addressAdapter.resetDefault(addressList,position);
				SharedPreferencesUtil.putString(getApplicationContext(), SharedPreferencesUtil.USER_INFO_FILE_NAME, 
						SharedPreferencesUtil.USER_INFO_KEY_DEAULT_ADDRESS_ID ,
						addressList.get(position).getAddId() + "");
				SharedPreferencesUtil.putString(getApplicationContext(), SharedPreferencesUtil.USER_INFO_FILE_NAME, 
						SharedPreferencesUtil.USER_INFO_KEY_DEAULT_ADDRESS_NAME,
						addressList.get(position).getAddConsignee());
				SharedPreferencesUtil.putString(getApplicationContext(), SharedPreferencesUtil.USER_INFO_FILE_NAME, 
						SharedPreferencesUtil.USER_INFO_KEY_DEAULT_ADDRESS_TEL,
						addressList.get(position).getAddTel());
				SharedPreferencesUtil.putString(getApplicationContext(), SharedPreferencesUtil.USER_INFO_FILE_NAME, 
						SharedPreferencesUtil.USER_INFO_KEY_DEAULT_ADDRESS_ADDRESS,
						addressList.get(position).getAddAdderss());
				
				break;
			case 1:
				deleteAddress();
				break;
			}

		};
	};

	@Override
	public int initLayout() {
		return R.layout.personal_manageaddress;
	}

	class MyDelect implements AddressAdapter.Delect{
		@Override
		public void showDelect(int id) {
			// TODO Auto-generated method stub
			showDelectDialog(id);
		}
		
	}
	@Override
	public void initUI() {
		pd = new CommonProgressDialog(this);
		customTitleView=(CustomTitleView) findViewById(R.id.customTitleView);
		customTitleView.setLeftImageBg(R.drawable.comm_back);
		customTitleView.setTitle("地址管理");
		customTitleView.setOnClickListener(this);
		customTitleView.setRightImageBg(R.drawable.personal_manageaddress_addaddress);
		customTitleView.setVisibility(View.VISIBLE);
		
		personalAddressListView = (ListView) findViewById(R.id.personalAddressListView);
		addressAdapter = new AddressAdapter(getApplicationContext());
		addressAdapter.setDelect(new MyDelect(){});
		personalAddressListView.setAdapter(addressAdapter);
		
		if ("choose".equals(getIntent().getStringExtra(Constants.ADDRESS))) {
			personalAddressListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					Intent intent = new Intent(ManageAddressActivity.this,FirmOrderActivity.class);
//				    Bundle bundle = new Bundle();
//				    bundle.putSerializable(Constants.PERSONAL_CHOOSE_ADDRESS, ((Address)addressAdapter.getItem(arg2)));
//				    intent.putExtras(bundle);
				    
					intent.putExtra(Constants.UPDATE_ID, ((Address)addressAdapter.getItem(arg2)).getAddId()+"");
				    intent.putExtra(Constants.UPDATE_NAME, ((Address)addressAdapter.getItem(arg2)).getAddConsignee());
				    intent.putExtra(Constants.UPDATE_TEL, ((Address)addressAdapter.getItem(arg2)).getAddTel());
				    intent.putExtra(Constants.UPDATE_ADDRESS, ((Address)addressAdapter.getItem(arg2)).getAddAdderss());
				    Log.e(TAG, "选择的地址 = " + arg2 + " = " + ((Address)addressAdapter.getItem(arg2)).getAddConsignee() +
				    		" " +((Address)addressAdapter.getItem(arg2)).getAddTel() + " " + ((Address)addressAdapter.getItem(arg2)).getAddAdderss());
				    setResult(RESULT_OK, intent);
				    ManageAddressActivity.this.finish();
				}
			});
		}
		
		personalAddressListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				//showDelectDialog(arg2);
				return true;
			}
		});
		
		
//		searchAddress();
	}
	
	public void showDelectDialog(final int newposition){
		
		new AlertDialog.Builder(ManageAddressActivity.this)
		.setItems(longClickMenu, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog,int which) {
				Message msg = new Message();
				position = newposition;
				switch (which) {
					case 0:
						// 设为默认收货地址
						msg.what = 0;
						mHandler.sendMessage(msg);
						break;
					case 1:
						// 删除地址
						msg.what = 1;
						mHandler.sendMessage(msg);
						break;
				}
			}
		}).create().show();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.leftImageView:
			this.finish();
			break;
		case R.id.rightImageView:
			Intent intentAddAddress = new Intent(ManageAddressActivity.this,AddAddressActivity.class);
			intentAddAddress.putExtra(Constants.ADDRESS, NEW_ADDRESS);
			startActivity(intentAddAddress);
		default:
			break;
		}
	}
	
	private void searchAddress() {
		pd.loadDialog();
		HashMap<String, String> paramMap = CommonDataUtil.getCommonParams(getApplicationContext());
		HttpParam httpParam = new HttpParam(ReleaseConfigure.PERSONAL_VALUE_TYPE_SEARCH_ADDRESS, false); // GET
		searchAddressHttpTask = new HttpTask(this, this);
		httpParam.setParams(paramMap);
		searchAddressHttpTask.execute(httpParam);
	}
	
	protected void deleteAddress() {
		pd.loadDialog();
		HashMap<String, String> paramMap = CommonDataUtil.getCommonParams(getApplicationContext());
		paramMap.put(Constants.ADDERSSID, addressList.get(position).getAddId()+"" );
		Log.e(TAG, "删除addressid = " + addressList.get(position).getAddId()+"");
		HttpParam httpParam = new HttpParam(ReleaseConfigure.PERSONAL_VALUE_TYPE_DELETEADDRESS, false); // GET
		deleteAddressHttpTask = new HttpTask(this, this);
		httpParam.setParams(paramMap);
		deleteAddressHttpTask.execute(httpParam);
	}

	
	@Override
	public void onLoadFinish(HttpTask task, HttpResult result) {
		Log.e(TAG, result.getData());
		pd.removeDialog();	
		if (result != null && !StringUtil.isEmpty(result.getData()) && StringUtil.isGoodJson(result.getData()) && this !=null && !this.isFinishing() ) {
			CommonResult commonResult = JSON.parseObject(result.getData(),CommonResult.class);
			if (null != commonResult) {
				if (task == searchAddressHttpTask) {
					if (commonResult.validate()) {
						JSONArray jsonArray = JSON.parseArray(commonResult.getData());
						if (null != jsonArray && jsonArray.size() > 0) {
							initAddress(jsonArray);
						} else {
							personalAddressListView.setAdapter(null);
						}
					} else {
//						ToastUtil.toast(this,commonResult.getErrMsg());
					}
				}
				if (task == deleteAddressHttpTask) {
					if (commonResult.validate()) {
						addressList.remove(position);
						addressAdapter.resetData(addressList);
						ToastUtil.toast(this,"删除成功");
						if (addressList.size() == 0) {
							SharedPreferencesUtil.putString(getApplicationContext(), SharedPreferencesUtil.USER_INFO_FILE_NAME, 
									SharedPreferencesUtil.USER_INFO_KEY_DEAULT_ADDRESS_ID, "");
							SharedPreferencesUtil.putString(getApplicationContext(), SharedPreferencesUtil.USER_INFO_FILE_NAME, 
									SharedPreferencesUtil.USER_INFO_KEY_DEAULT_ADDRESS_NAME, "");
							SharedPreferencesUtil.putString(getApplicationContext(), SharedPreferencesUtil.USER_INFO_FILE_NAME, 
									SharedPreferencesUtil.USER_INFO_KEY_DEAULT_ADDRESS_TEL, "");
							SharedPreferencesUtil.putString(getApplicationContext(), SharedPreferencesUtil.USER_INFO_FILE_NAME, 
									SharedPreferencesUtil.USER_INFO_KEY_DEAULT_ADDRESS_ADDRESS, "");
						}
					} else {
//						ToastUtil.toast(this,commonResult.getErrMsg());
					}
				}
			}
		}
	}
	
	private void initAddress(JSONArray jsonArray) {
		addressList = new ArrayList<Address>();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			Address address = JSONObject.toJavaObject(jsonObject, Address.class);
			addressList.add(address);
		}

		if (addressList != null && addressList.size() > 0) {
			addressAdapter.resetDefault(addressList, position);
			if(StringUtil.isEmpty(SharedPreferencesUtil.getDefaultAddressName(getApplicationContext()))){
				SharedPreferencesUtil.putString(getApplicationContext(), SharedPreferencesUtil.USER_INFO_FILE_NAME, 
						SharedPreferencesUtil.USER_INFO_KEY_DEAULT_ADDRESS_ID,
						addressList.get(0).getAddId() + "");
				SharedPreferencesUtil.putString(getApplicationContext(), SharedPreferencesUtil.USER_INFO_FILE_NAME, 
						SharedPreferencesUtil.USER_INFO_KEY_DEAULT_ADDRESS_NAME,
						addressList.get(0).getAddConsignee());
				SharedPreferencesUtil.putString(getApplicationContext(), SharedPreferencesUtil.USER_INFO_FILE_NAME, 
						SharedPreferencesUtil.USER_INFO_KEY_DEAULT_ADDRESS_TEL,
						addressList.get(0).getAddTel());
				SharedPreferencesUtil.putString(getApplicationContext(), SharedPreferencesUtil.USER_INFO_FILE_NAME, 
						SharedPreferencesUtil.USER_INFO_KEY_DEAULT_ADDRESS_ADDRESS,
						addressList.get(0).getAddAdderss());
			}
//			addressAdapter.resetData(addressList, addressList.get(position).getAddId());
		}

	}



	
	@Override
	protected void onResume() {
		//查询收货地址
		searchAddress();
		super.onResume();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK &&  addressList.size() == 1) {
			Log.e(TAG, "如果只有一条地址，且修改当前地址的时候，就把修改的地址保存");
			SharedPreferencesUtil.putString(getApplicationContext(), SharedPreferencesUtil.USER_INFO_FILE_NAME, 
					SharedPreferencesUtil.USER_INFO_KEY_DEAULT_ADDRESS_ID ,
					addressList.get(0).getAddId() + "");
			SharedPreferencesUtil.putString(getApplicationContext(), SharedPreferencesUtil.USER_INFO_FILE_NAME, 
					SharedPreferencesUtil.USER_INFO_KEY_DEAULT_ADDRESS_NAME,
					addressList.get(0).getAddConsignee());
			SharedPreferencesUtil.putString(getApplicationContext(), SharedPreferencesUtil.USER_INFO_FILE_NAME, 
					SharedPreferencesUtil.USER_INFO_KEY_DEAULT_ADDRESS_TEL,
					addressList.get(0).getAddTel());
			SharedPreferencesUtil.putString(getApplicationContext(), SharedPreferencesUtil.USER_INFO_FILE_NAME, 
					SharedPreferencesUtil.USER_INFO_KEY_DEAULT_ADDRESS_ADDRESS,
					addressList.get(0).getAddAdderss());
		}
	}
	
	
	@Override
	public void initData() {
		
	}

}
