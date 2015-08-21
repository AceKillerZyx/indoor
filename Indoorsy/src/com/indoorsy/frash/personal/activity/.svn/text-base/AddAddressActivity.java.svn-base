package com.indoorsy.frash.personal.activity;

import java.util.HashMap;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
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
import com.indoorsy.frash.personal.data.bean.Address;
import com.indoorsy.frash.util.CommonDataUtil;
import com.indoorsy.frash.util.PhoneNumUtil;
import com.indoorsy.frash.util.SharedPreferencesUtil;
import com.indoorsy.frash.util.StringUtil;
import com.indoorsy.frash.util.ToastUtil;

/*
 * 添加收货地址
 */
public class AddAddressActivity extends BasicActivity {

	private static final String TAG = AddAddressActivity.class.getSimpleName();

	private CustomTitleView customTitleView;
	private EditText personalReceipNametEditText, personalReceipPhonetEditText,
			personalReceipAddressEditText;
	private Button personalOkButton, personalUpdateButton;

	private HttpTask newAddressHttpTask , updateAddressHttpTask;
	private CommonProgressDialog pd;
	private Address address;
	private String addressid;

	@Override
	public int initLayout() {
		return R.layout.personal_addaddress;
	}

	@Override
	public void initUI() {
		pd = new CommonProgressDialog(this);
		personalReceipNametEditText = (EditText) findViewById(R.id.personalReceipNametEditText);
		personalReceipPhonetEditText = (EditText) findViewById(R.id.personalReceipPhonetEditText);
		personalReceipAddressEditText = (EditText) findViewById(R.id.personalReceipAddressEditText);
		//自动补全地址
		if (!StringUtil.isEmpty(SharedPreferencesUtil.getLocationAddress(getApplicationContext()))) {
			personalReceipAddressEditText.setText(SharedPreferencesUtil.getLocationAddress(getApplicationContext()));
		}
		//自动补全电话
		if (!StringUtil.isEmpty(SharedPreferencesUtil.getDefaultAddressTel(getApplicationContext()))) {
			personalReceipPhonetEditText.setText(SharedPreferencesUtil.getDefaultAddressTel(getApplicationContext()));
		}
		
		
		// 新建地址
		if ("new".equals(getIntent().getStringExtra(Constants.ADDRESS))) {
			initNew();
		}
		// 修改地址
		if ("update".equals(getIntent().getStringExtra(Constants.ADDRESS))) {
			initUpdate();
		}
	}

	private void initNew() {
		customTitleView = (CustomTitleView) findViewById(R.id.customTitleView);
		customTitleView.setLeftImageBg(R.drawable.comm_back);
		customTitleView.setTitle(R.string.personal_manageaddress_add_title);
		customTitleView.setOnClickListener(this);
		customTitleView.setVisibility(View.VISIBLE);

		personalOkButton = (Button) findViewById(R.id.personalOkButton);
		personalOkButton.setVisibility(View.VISIBLE);
		personalOkButton.setOnClickListener(this);

	}

	private void initUpdate() {
		if (getIntent().getSerializableExtra(Constants.PERSONAL_ADDRESS) != null) {
			address = (Address)getIntent().getSerializableExtra(Constants.PERSONAL_ADDRESS);
			addressid = getIntent().getStringExtra(Constants.ADDERSSID);
			Log.e(TAG, "收到的addressid = "+addressid);
			customTitleView=(CustomTitleView) findViewById(R.id.customTitleView);
			customTitleView.setLeftImageBg(R.drawable.comm_back);
			customTitleView.setTitle("修改地址");
			customTitleView.setOnClickListener(this);
			customTitleView.setVisibility(View.VISIBLE);
			
			personalUpdateButton = (Button) findViewById(R.id.personalUpdateButton);
			personalUpdateButton.setVisibility(View.VISIBLE);
			personalUpdateButton.setOnClickListener(this);
			personalReceipNametEditText.setText(address.getAddConsignee()) ;
			personalReceipPhonetEditText.setText(address.getAddTel());
			personalReceipAddressEditText.setText(address.getAddAdderss());
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.leftImageView:
			this.finish();
			break;
		case R.id.personalOkButton:
			if (!StringUtil.isEmpty(personalReceipNametEditText.getText().toString().trim())
					&& !StringUtil.isEmpty(personalReceipPhonetEditText.getText().toString().trim())
					&& PhoneNumUtil.isMobileNO(personalReceipPhonetEditText.getText().toString().trim())
					&& !StringUtil.isEmpty(personalReceipAddressEditText.getText().toString().trim())) {
				addAddress();
			} else {
				ToastUtil.toast(getApplicationContext(), "信息有误");
			}

			break;
		case R.id.personalUpdateButton:
			if (!StringUtil.isEmpty(personalReceipNametEditText.getText().toString().trim())
					&& !StringUtil.isEmpty(personalReceipPhonetEditText.getText().toString().trim())
					&& PhoneNumUtil.isMobileNO(personalReceipPhonetEditText.getText().toString().trim())
					&& !StringUtil.isEmpty(personalReceipAddressEditText.getText().toString().trim())) {
				updateAddress();
			} else {
				ToastUtil.toast(getApplicationContext(), "信息有误");
			}
			break;
		default:
			break;
		}
	}

	

	// 添加收货地址
	public void addAddress() {
		pd.loadDialog();
		HashMap<String, String> paramMap = CommonDataUtil.getCommonParams(getApplicationContext());
		paramMap.put(Constants.UPDATE_NAME, personalReceipNametEditText.getText().toString().trim());
		paramMap.put(Constants.UPDATE_TEL, personalReceipPhonetEditText.getText().toString().trim());
		paramMap.put(Constants.UPDATE_ADDRESS, personalReceipAddressEditText.getText().toString().trim());
		HttpParam httpParam = new HttpParam(ReleaseConfigure.PERSONAL_VALUE_TYPE_NEWADDRESS, false); // GET
		newAddressHttpTask = new HttpTask(this, this);
		httpParam.setParams(paramMap);
		newAddressHttpTask.execute(httpParam);
	}

	// 修改收货地址
	private void updateAddress() {
		pd.loadDialog();
		HashMap<String, String> paramMap = CommonDataUtil.getCommonParams(getApplicationContext());
		paramMap.put(Constants.ADDERSSID, addressid);
		Log.e(TAG, "修改的addressid = "+addressid);
		paramMap.put(Constants.UPDATE_NAME, personalReceipNametEditText.getText().toString().trim());
		paramMap.put(Constants.UPDATE_TEL, personalReceipPhonetEditText.getText().toString().trim());
		paramMap.put(Constants.UPDATE_ADDRESS, personalReceipAddressEditText.getText().toString().trim());
		HttpParam httpParam = new HttpParam(ReleaseConfigure.PERSONAL_VALUE_TYPE_UPDATEADDRESS, false); // GET
		updateAddressHttpTask = new HttpTask(this, this);
		httpParam.setParams(paramMap);
		updateAddressHttpTask.execute(httpParam);
	}
	
	@Override
	public void onLoadFinish(HttpTask task, HttpResult result) {
		Log.e(TAG, result.getData());
		pd.removeDialog();
		if (result != null && !StringUtil.isEmpty(result.getData())
				&& StringUtil.isGoodJson(result.getData()) && this != null
				&& !this.isFinishing()) {
			CommonResult commonResult = JSON.parseObject(result.getData(),CommonResult.class);
			if (null != commonResult) {
				if (task == newAddressHttpTask) {
					if (commonResult.validate()) {
						ToastUtil.toast(this, "添加成功");
						this.finish();
					} else {
						ToastUtil.toast(this, "添加失败");
					}
				}
				if (task == updateAddressHttpTask) {
					if (commonResult.validate()) {
						ToastUtil.toast(this, "修改成功");
						setResult(RESULT_OK);
						this.finish();
					} else {
						ToastUtil.toast(this, "修改失败");
					}
				}
			}
		}
	}

	@Override
	public void initData() {

	}

}
