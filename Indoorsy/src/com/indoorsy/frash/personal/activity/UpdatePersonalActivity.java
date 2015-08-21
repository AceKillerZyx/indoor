package com.indoorsy.frash.personal.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.indoorsy.frash.Constants;
import com.indoorsy.frash.R;
import com.indoorsy.frash.ReleaseConfigure;
import com.indoorsy.frash.common.activity.BasicActivity;
import com.indoorsy.frash.common.data.bean.CommonResult;
import com.indoorsy.frash.common.view.CircleImageView;
import com.indoorsy.frash.common.view.CommonProgressDialog;
import com.indoorsy.frash.common.view.CustomTitleView;
import com.indoorsy.frash.http.core.HttpParam;
import com.indoorsy.frash.http.core.HttpResult;
import com.indoorsy.frash.http.core.HttpTask;
import com.indoorsy.frash.myinfo.activity.UpdatePasswordActivity;
import com.indoorsy.frash.util.CommonDataUtil;
import com.indoorsy.frash.util.ImageUtil;
import com.indoorsy.frash.util.SharedPreferencesUtil;
import com.indoorsy.frash.util.StorageUtil;
import com.indoorsy.frash.util.StringUtil;
import com.indoorsy.frash.util.ToastUtil;
import com.indoorsy.frash.util.UploadUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * 修改资料
 */
public class UpdatePersonalActivity extends BasicActivity {
	private static final String TAG = UpdatePersonalActivity.class.getSimpleName();
	private CustomTitleView customTitleView;
	private RelativeLayout personalUpdatePwdRelativateLayout,
			personalManageAddressRelativelayout;
	private CircleImageView myinfoHeadLogoCircleImageView;
	private TextView commonPhotographTextView, commonAlbumsTextView,personalUpdatePersonalInvitationTextView;
	private Button commonCancelButton;
	private Dialog alertDialog;
	private View commonView;
	private File imgFile;
	private static final int TAKE_PICTURE = 0;
	private static final int CHOOSE_PICTURE = 1;
	private Spinner personalUpdatePersonalSexSpinner;
	private EditText personalUpdatePersonalNameEditText,myinfoAccountManagerAgeEditText;
	private HttpTask updateHttptask,searchHttpTask;
	private String myinfoAccountManagerHeaderPath;
	private String picPath;
	private CommonProgressDialog commonProgressDialog;
	
	private Intent intent = null;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.leftImageView:
				setResult(RESULT_OK, intent);
				this.finish();
				break;
			case R.id.personalUpdatePwdRelativateLayout:
				Intent intentUpdatePwd = new Intent(UpdatePersonalActivity.this,UpdatePasswordActivity.class);
				startActivity(intentUpdatePwd);
				break;
			case R.id.personalManageAddressRelativelayout:
				Intent intentManageAddress = new Intent(UpdatePersonalActivity.this, ManageAddressActivity.class);
				startActivity(intentManageAddress);
				break;
			case R.id.myinfoHeadLogoCircleImageView:
				// 换头像
				choisePicture();
				break;
			case R.id.commonPhotographTextView:
				// 拍照
				takePicture();
				if (alertDialog != null && alertDialog.isShowing()) {
					alertDialog.dismiss();
				}
				break;
			case R.id.commonAlbumsTextView:
				fromPhotoAlbum();
				if (alertDialog != null && alertDialog.isShowing()) {
					alertDialog.dismiss();
				}
				break;
			case R.id.commonCancelButton:
				if (alertDialog != null && alertDialog.isShowing()) {
					alertDialog.dismiss();
				}
				break;
			case R.id.rightTextView:
				updateUserInfo();
				break;
		}
	}

	// 选择图片方式
	@SuppressWarnings("deprecation")
	private void choisePicture() {
		if (alertDialog == null) {
			alertDialog = new Dialog(this, R.style.commonDialog);
			alertDialog.setContentView(commonView);
			Display d = getWindowManager().getDefaultDisplay();
			ToastUtil.setDialogLocation(this, alertDialog, d.getWidth() - 60,d.getHeight() / 2 - 100, 30, d.getHeight() / 3);
		}
		if (alertDialog.isShowing()) {
			alertDialog.dismiss();
		} else {
			alertDialog.show();
		}
	}

	// 拍照
	private void takePicture() {
		String name = "img_" + System.currentTimeMillis();
		imgFile = new File(StorageUtil.getTempDirectory(getApplicationContext()), name);
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imgFile));
		startActivityForResult(intent, TAKE_PICTURE);
	}

	// 从相册获取
	private void fromPhotoAlbum() {
		Intent openAlbumIntent = new Intent(Intent.ACTION_PICK);
		openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
		startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
	}
	
	//修改个人资料
	public void updateUserInfo(){
		HashMap<String,String> paHashMap=CommonDataUtil.getCommonParams(getApplicationContext());
		paHashMap.put(Constants.USERIMAGES, myinfoAccountManagerHeaderPath);
		paHashMap.put(Constants.UNAME, personalUpdatePersonalNameEditText.getText().toString().trim());
		paHashMap.put(Constants.AGE, myinfoAccountManagerAgeEditText.getText().toString().trim());
		paHashMap.put(Constants.SEX, personalUpdatePersonalSexSpinner.getSelectedItem().toString());
		Log.e(TAG, "sex = " + personalUpdatePersonalSexSpinner.getSelectedItem().toString());
		HttpParam httpParam=new HttpParam(ReleaseConfigure.PERSONAL_VALUE_TYPE_UPDATEUSERINFO, false);
		updateHttptask=new HttpTask(getApplicationContext(), this);
		httpParam.setParams(paHashMap);
		updateHttptask.execute(httpParam);
		commonProgressDialog.loadDialog();
	}
	
	//查询个人资料
	public void searchUserInfo(){
		HashMap<String,String> paHashMap=CommonDataUtil.getCommonParams(getApplicationContext());
		HttpParam httpParam=new HttpParam(ReleaseConfigure.PERSONAL_VALUE_TYPE_SEARCHUSERINFO, false);
		searchHttpTask=new HttpTask(getApplicationContext(), this);
		httpParam.setParams(paHashMap);
		searchHttpTask.execute(httpParam);
		commonProgressDialog.loadDialog();
	}

	@Override
	public void onLoadFinish(HttpTask task, HttpResult result) {
		Log.e(TAG, result.getData());
		commonProgressDialog.removeDialog();
		if (result != null && !StringUtil.isEmpty(result.getData()) && StringUtil.isGoodJson(result.getData())) {
			CommonResult commonResult = JSON.parseObject(result.getData(),CommonResult.class);
			if (null != commonResult) {
				if (task == updateHttptask) {
					if (commonResult.validate()) {
						ToastUtil.toast(getApplicationContext(),R.string.personal_updatepersonal_success);
						SharedPreferencesUtil.putString(getApplicationContext(),SharedPreferencesUtil.USER_INFO_FILE_NAME,SharedPreferencesUtil.USER_INFO_KEY_HEADER_IMAGE,myinfoAccountManagerHeaderPath);
						setResult(RESULT_OK);
						this.finish();
					} else {
						ToastUtil.toast(getApplicationContext(),commonResult.getErrMsg());
					}
				}
				
				if (task == searchHttpTask) {
					if (commonResult.validate()) {
						JSONObject jsonObject = JSON.parseObject(commonResult.getData());
						if(jsonObject != null && jsonObject.size() > 0){
							initUserInfo(jsonObject);
						}
					} else {
//						ToastUtil.toast(getApplicationContext(),commonResult.getErrMsg());
					}
				}
			}
		}
	}
	
	private void initUserInfo(JSONObject jsonObject){
		myinfoAccountManagerHeaderPath = jsonObject.getString(Constants.USERIMAGES);
		SharedPreferencesUtil.putString(getApplicationContext(),SharedPreferencesUtil.USER_INFO_FILE_NAME,SharedPreferencesUtil.USER_INFO_KEY_HEADER_IMAGE,myinfoAccountManagerHeaderPath);
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.personal_header_img)
				.showImageOnFail(R.drawable.personal_header_img)
				.resetViewBeforeLoading(true).cacheOnDisk(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.displayer(new FadeInBitmapDisplayer(300)).build();
		ImageLoader.getInstance().displayImage(SharedPreferencesUtil.getHeaderImage(getApplicationContext()), myinfoHeadLogoCircleImageView,options, new SimpleImageLoadingListener(){
			@Override
			public void onLoadingStarted(String imageUri, View view) {
			}
			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
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
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				
			}
		});
		personalUpdatePersonalNameEditText.setText(jsonObject.getString(Constants.USERNAMES));
		myinfoAccountManagerAgeEditText.setText(jsonObject.getString(Constants.USERAGE));
		String sex = jsonObject.getString(Constants.USERSEX);
		int position = 0;
		if("男".equals(sex)){
			position = 0;
		}else {
			position = 1;
		}
		personalUpdatePersonalSexSpinner.setSelection(position);
		personalUpdatePersonalInvitationTextView.setText(jsonObject.getString(Constants.USERINVITATION));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_CANCELED) {
			switch (requestCode) {
            	case CHOOSE_PICTURE:
            		ContentResolver resolver = getContentResolver();
            		//照片的原始资源地址
            		 Uri originalUri = data.getData();
					try {
						//使用ContentProvider通过URI获取原始图片
	            		 Bitmap smallBitmap = MediaStore.Images.Media.getBitmap(resolver, originalUri);
	            		 initImageView(smallBitmap);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
            		break;
            	case TAKE_PICTURE:
					try {
						BitmapFactory.Options options = new BitmapFactory.Options();
		                options.inTempStorage = new byte[1024 * 1024 * 2];
		                options.inSampleSize = 2;
		                Bitmap smallBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),options);
		                initImageView(smallBitmap);
					} catch (Exception e) {
						e.printStackTrace();
					}
	            	break;
            }
		}
	}
	
	//初始化图片
	public void initImageView(Bitmap bitmap){
		picPath=null;
		if (bitmap!=null) {
			Bitmap photo=ImageUtil.zoomBitmap(bitmap, bitmap.getWidth()/5, bitmap.getHeight()/5);
			//由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
			bitmap.recycle();
            //将处理过的图片显示在界面上，并保存到本地
			myinfoHeadLogoCircleImageView.setImageBitmap(photo);
			String name = String.valueOf(System.currentTimeMillis()) + ".jpg";
			ImageUtil.savePhotoToSDCard(photo,ImageUtil.UPLOAD_ADD_BOOK, name);
			if(picPath == null){
				picPath = ImageUtil.UPLOAD_ADD_BOOK + name;
			}
			new Thread(){
				public void run(){
					Looper.prepare();
					try {
						String resultValue = UploadUtil.doUPloadFile(getApplicationContext(),picPath, ReleaseConfigure.COMMON_UPDATE_IMAGE);
						if(resultValue.equals("error")){
//							ToastUtil.toast(getApplicationContext(), getResources().getString(R.string.common_albums));
						}else{
							Log.e(TAG, "resultValue"+resultValue);
							CommonResult commonResult = JSON.parseObject(resultValue, CommonResult.class);
							if(commonResult != null && commonResult.validate()){
								JSONArray jsonArray = JSON.parseArray(commonResult.getData());
								if(jsonArray != null && jsonArray.size() > 0){
									JSONObject jsonObject = jsonArray.getJSONObject(0);
									myinfoAccountManagerHeaderPath = ReleaseConfigure.ROOT_IMAGE + jsonObject.getString(Constants.PATH);
									Log.e(TAG, "myinfoAccountManagerHeaderPath:" + myinfoAccountManagerHeaderPath);
								}
							}else{
//								ToastUtil.toast(getApplicationContext(), commonResult.getErrMsg());
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						Log.e(TAG, e.getMessage());
					}
				}
			}.start();
		}
	}
	
	@Override
	public int initLayout() {
		return R.layout.personal_updatepersonal;
	}

	@SuppressLint("InflateParams") @Override
	public void initUI() {
		
		commonProgressDialog = new CommonProgressDialog(this);
		customTitleView = (CustomTitleView) findViewById(R.id.customTitleView);
		customTitleView.setLeftImageBg(R.drawable.comm_back);
		customTitleView.setTitle(R.string.personal_updatepsrsonal_title);
		customTitleView.setRightTextView(R.string.personal_updatepersonal_ok);
		customTitleView.setVisibility(View.VISIBLE);
		customTitleView.setOnClickListener(this);
		personalUpdatePwdRelativateLayout = (RelativeLayout) findViewById(R.id.personalUpdatePwdRelativateLayout);
		personalUpdatePwdRelativateLayout.setOnClickListener(this);
		if(SharedPreferencesUtil.isThirdLogin(getApplicationContext())){
			personalUpdatePwdRelativateLayout.setVisibility(View.GONE);
		}else{
			personalUpdatePwdRelativateLayout.setVisibility(View.VISIBLE);
		}
		personalManageAddressRelativelayout = (RelativeLayout) findViewById(R.id.personalManageAddressRelativelayout);
		personalManageAddressRelativelayout.setOnClickListener(this);
		myinfoHeadLogoCircleImageView = (CircleImageView) findViewById(R.id.myinfoHeadLogoCircleImageView);
		myinfoHeadLogoCircleImageView.setOnClickListener(this);
		
		personalUpdatePersonalNameEditText=(EditText) findViewById(R.id.personalUpdatePersonalNameEditText);
		myinfoAccountManagerAgeEditText=(EditText) findViewById(R.id.myinfoAccountManagerAgeEditText);
		personalUpdatePersonalInvitationTextView = (TextView)findViewById(R.id.personalUpdatePersonalInvitationTextView);
		//建立下拉框的数据源
		String[] sexItems = getResources().getStringArray(R.array.personal_updatesex_spinner_item);
		// 建立Adapter并且绑定数据源
		ArrayAdapter<String> sexAdapter=new ArrayAdapter<String>(getApplicationContext(), R.layout.common_spinner_selected, sexItems);
		sexAdapter.setDropDownViewResource(R.layout.common_spinner_dropdown);
		personalUpdatePersonalSexSpinner=(Spinner) findViewById(R.id.personalUpdatePersonalSexSpinner);
		//设置适配器
		personalUpdatePersonalSexSpinner.setAdapter(sexAdapter);
		// 获得Dialog布局
		LayoutInflater inflater = LayoutInflater.from(this);
		commonView = inflater.inflate(R.layout.common_image_dialog, null);
		commonPhotographTextView = (TextView) commonView.findViewById(R.id.commonPhotographTextView);
		commonPhotographTextView.setOnClickListener(this);
		commonAlbumsTextView = (TextView) commonView.findViewById(R.id.commonAlbumsTextView);
		commonAlbumsTextView.setOnClickListener(this);
		commonCancelButton = (Button) commonView.findViewById(R.id.commonCancelButton);
		commonCancelButton.setOnClickListener(this);
		
		searchUserInfo();
	}

	@Override
	public void initData() {
		intent = getIntent();
	}

}
