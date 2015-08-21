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
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
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
import com.indoorsy.frash.http.core.HttpParam;
import com.indoorsy.frash.http.core.HttpResult;
import com.indoorsy.frash.http.core.HttpTask;
import com.indoorsy.frash.util.CommonDataUtil;
import com.indoorsy.frash.util.ImageUtil;
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
 * 发表评价
 */
@SuppressLint("InflateParams") public class EvaluationActivity extends BasicActivity implements OnRatingBarChangeListener{
	
	private static final String TAG = EvaluationActivity.class.getSimpleName();
	
	private TextView commonPhotographTextView, commonAlbumsTextView;
	private Button commonCancelButton;
	private Dialog alertDialog;
	private View commonView;
	private File imgFile;
	private String picPath;
	private static final int TAKE_PICTURE = 0;
	private static final int CHOOSE_PICTURE = 1;
	
	private CustomTitleView customTitleView;
	
	private int orderid = 0;
	
	private int type = 0;
	
	private Intent intent;
	
	private HttpTask httpTask;
	
	private String imageOneUrl,imageTwoUrl,imageThreeUrl;

	private int evaLogistics,evaQuality,evaServe,evaShipments;
	
	private CommonProgressDialog commonProgressDialog;
	
	private EditText evaluationContentEditText;
	
	private TextView evaluationNameTextView, evaluationPriceTextView,
			evaluationTextView;

	private ImageView evaluationImageView, evaluationOneImageView,
			evaluationTwoImageView, evaluationThreeImageView;
	
	private RatingBar evaluationProductQualityRatingBar,
			evaluationProductQuantityRatingBar,
			evaluationDeliverySpeedRatingBar,
			evaluationServiceAttitudeRatingBar;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.leftImageView:
				setResult(RESULT_OK, intent);
				this.finish();
				break;
			case R.id.evaluationTextView:
				if(validate()){
					evaluation();
				}
				break;
			case R.id.evaluationOneImageView:
				type = 0;
				choisePicture();
				break;
			case R.id.evaluationTwoImageView:
				type = 1;
				choisePicture();
				break;
			case R.id.evaluationThreeImageView:
				type = 2;
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
		}
	}
	
	private boolean validate(){
		if(evaLogistics == 0){
			ToastUtil.toast(getApplicationContext(), R.string.personal_myorder_evaluation_product_quantity_not_null);
			return false;
		}
		if(evaQuality == 0){
			ToastUtil.toast(getApplicationContext(), R.string.personal_myorder_evaluation_product_quality_not_null);
			return false;
		}
		if(evaServe == 0){
			ToastUtil.toast(getApplicationContext(), R.string.personal_myorder_evaluation_service_attitude_not_null);
			return false;
		}
		if(evaShipments == 0){
			ToastUtil.toast(getApplicationContext(), R.string.personal_myorder_evaluation_delivery_speed_not_null);
			return false;
		}
		String content = evaluationContentEditText.getText().toString().trim();
		if(TextUtils.isEmpty(content)){
			ToastUtil.toast(getApplicationContext(), R.string.personal_myorder_evaluation_content_not_null);
			return false;
		}
		return true;
	}
	
	private void evaluation(){
		Log.e(TAG, "imageOneUrl:" + imageOneUrl + "--imageTwoUrl:" + imageTwoUrl + "--imageThreeUrl:" + imageThreeUrl);
		JSONArray jsonArray = new JSONArray();
		if(!StringUtil.isEmpty(imageOneUrl)){
			JSONObject jsonObject = new JSONObject();
			jsonObject.put(Constants.IMAGES, imageOneUrl);
			jsonArray.add(jsonObject);
		}
		if(!StringUtil.isEmpty(imageTwoUrl)){
			JSONObject jsonObject = new JSONObject();
			jsonObject.put(Constants.IMAGES, imageTwoUrl);
			jsonArray.add(jsonObject);
		}
		if(!StringUtil.isEmpty(imageThreeUrl)){
			JSONObject jsonObject = new JSONObject();
			jsonObject.put(Constants.IMAGES, imageThreeUrl);
			jsonArray.add(jsonObject);
		}
		
		HashMap<String, String> paramMap = CommonDataUtil.getCommonParams(getApplicationContext());
		paramMap.put(Constants.ORDERID, String.valueOf(orderid));
		paramMap.put(Constants.EVACONTENT, evaluationContentEditText.getText().toString().trim());
		paramMap.put(Constants.EVALOGISTICS, String.valueOf(evaLogistics));
		paramMap.put(Constants.EVAQUALITY, String.valueOf(evaQuality));
		paramMap.put(Constants.EVASERVE, String.valueOf(evaServe));
		paramMap.put(Constants.EVASHIPMENTS, String.valueOf(evaShipments));
		
		if (StringUtil.isEmpty(imageOneUrl)) {
			Log.e(TAG, "之前 不传递图片的 JosnArray = " + jsonArray.toString());
			paramMap.put(Constants.LISTORDER, "[]");
		}else {
			paramMap.put(Constants.LISTORDER, jsonArray.toJSONString());
		}
		Log.e(TAG, "传递 不传递图片的 JosnArray = " + jsonArray.toString());
		HttpParam httpParam = new HttpParam(ReleaseConfigure.PERSONAL_VALUE_TYPE_EVALUATION, false); // GET
		httpTask = new HttpTask(getApplicationContext(), this);
		httpParam.setParams(paramMap);
		httpTask.execute(httpParam);
		commonProgressDialog.loadDialog();
	}
	
	@Override
	public int initLayout() {
		return R.layout.personal_evaluation;
	}

	@Override
	public void initUI() {
		customTitleView = (CustomTitleView) findViewById(R.id.customTitleView);
		customTitleView.setTitle(R.string.personal_fragment_myorder_evaluation);
		customTitleView.setLeftImageBg(R.drawable.comm_back);
		customTitleView.setVisibility(View.VISIBLE);
		customTitleView.setOnClickListener(this);
		
		commonProgressDialog = new CommonProgressDialog(this);
		
		evaluationImageView = (ImageView)findViewById(R.id.evaluationImageView);
		evaluationNameTextView = (TextView)findViewById(R.id.evaluationNameTextView);
		evaluationPriceTextView = (TextView)findViewById(R.id.evaluationPriceTextView);
		evaluationTextView = (TextView)findViewById(R.id.evaluationTextView);
		evaluationTextView.setOnClickListener(this);
		
		evaluationProductQualityRatingBar = (RatingBar)findViewById(R.id.evaluationProductQualityRatingBar);
		evaluationProductQualityRatingBar.setOnRatingBarChangeListener(this);
		evaluationProductQuantityRatingBar = (RatingBar)findViewById(R.id.evaluationProductQuantityRatingBar);
		evaluationProductQuantityRatingBar.setOnRatingBarChangeListener(this);
		evaluationDeliverySpeedRatingBar = (RatingBar)findViewById(R.id.evaluationDeliverySpeedRatingBar);
		evaluationDeliverySpeedRatingBar.setOnRatingBarChangeListener(this);
		evaluationServiceAttitudeRatingBar = (RatingBar)findViewById(R.id.evaluationServiceAttitudeRatingBar);
		evaluationServiceAttitudeRatingBar.setOnRatingBarChangeListener(this);
		
		evaluationContentEditText = (EditText)findViewById(R.id.evaluationContentEditText);
		
		evaluationOneImageView = (ImageView)findViewById(R.id.evaluationOneImageView);
		evaluationOneImageView.setOnClickListener(this);
		evaluationTwoImageView = (ImageView)findViewById(R.id.evaluationTwoImageView);
		evaluationTwoImageView.setOnClickListener(this);
		evaluationThreeImageView = (ImageView)findViewById(R.id.evaluationThreeImageView);
		evaluationThreeImageView.setOnClickListener(this);
		evaluationTwoImageView.setVisibility(View.INVISIBLE);
		evaluationThreeImageView.setVisibility(View.INVISIBLE);
		
		// 获得Dialog布局
		LayoutInflater inflater = LayoutInflater.from(this);
		commonView = inflater.inflate(R.layout.common_image_dialog, null);
		commonPhotographTextView = (TextView) commonView.findViewById(R.id.commonPhotographTextView);
		commonPhotographTextView.setOnClickListener(this);
		commonAlbumsTextView = (TextView) commonView.findViewById(R.id.commonAlbumsTextView);
		commonAlbumsTextView.setOnClickListener(this);
		commonCancelButton = (Button) commonView.findViewById(R.id.commonCancelButton);
		commonCancelButton.setOnClickListener(this);
	}

	@Override
	public void initData() {
		intent = getIntent();
		orderid = getIntent().getIntExtra(Constants.ORDERID, 0);
		evaluationNameTextView.setText(getIntent().getStringExtra(Constants.HOMEPAGE_PRODUCT_NAME));
		evaluationPriceTextView.setText(String.valueOf(getIntent().getDoubleExtra(Constants.HOMEPAGE_PRODUCT_PRICE, 0)));
		
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.homepage_product_detail_default)
				.showImageOnFail(R.drawable.homepage_product_detail_default)
				.resetViewBeforeLoading(true).cacheOnDisk(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.considerExifParams(true)
				.displayer(new FadeInBitmapDisplayer(300)).build();
		
		ImageLoader.getInstance().displayImage(getIntent().getStringExtra(Constants.HOMEPAGE_PRODUCT_THUMB),evaluationImageView, options,
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
	
	@Override
	public void onLoadFinish(HttpTask task, HttpResult result) {
		Log.e(TAG, result.getData());
		commonProgressDialog.removeDialog();
		if (result != null && !StringUtil.isEmpty(result.getData()) && StringUtil.isGoodJson(result.getData())) {
			CommonResult commonResult = JSON.parseObject(result.getData(),CommonResult.class);
			if (null != commonResult) {
				if (commonResult.validate()) {
					ToastUtil.toast(getApplicationContext(), "评论成功");
					setResult(RESULT_OK, intent);
					finish();
				}else{
//					ToastUtil.toast(getApplicationContext(), commonResult.getErrMsg());
				}
			}
		}
	}

	@Override
	public void onRatingChanged(RatingBar ratingBar,float rating,boolean fromUser) {
		String result = "";
		switch (ratingBar.getId()) {
			case R.id.evaluationProductQualityRatingBar:
				result = "evaluationProductQualityRatingBar--" + rating;
				evaQuality = (int)rating;
				break;
			case R.id.evaluationProductQuantityRatingBar:
				result = "evaluationProductQuantityRatingBar--" + rating;
				evaLogistics = (int)rating;
				break;
			case R.id.evaluationDeliverySpeedRatingBar:
				result = "evaluationDeliverySpeedRatingBar--" + rating;
				evaShipments = (int)rating;
				break;
			case R.id.evaluationServiceAttitudeRatingBar:
				result = "evaluationServiceAttitudeRatingBar--" + rating;
				evaServe = (int)rating;
				break;
		}
		Log.e(TAG, result);
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
			switch (type) {
				case 0:
					evaluationOneImageView.setImageBitmap(photo);
					break;
				case 1:
					evaluationTwoImageView.setImageBitmap(photo);
					break;
				case 2:
					evaluationThreeImageView.setImageBitmap(photo);
					break;
			}
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
									String imageUrl = ReleaseConfigure.ROOT_IMAGE + jsonObject.getString(Constants.PATH);
									Message msg = myHandler.obtainMessage();
									//利用bundle对象来传值
									Bundle b = new Bundle();
									b.putString("imageUrl", imageUrl);
									msg.setData(b);
									msg.sendToTarget();
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
	
	@SuppressLint("HandlerLeak") 
	private Handler myHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			//获取bundle对象的值
			Bundle b = msg.getData();
			String imageUrl = b.getString("imageUrl");
			switch (type) {
				case 0:
					imageOneUrl = imageUrl;
					evaluationTwoImageView.setVisibility(View.VISIBLE);
					Log.e(TAG, "imageOneUrl:" + imageOneUrl);
					break;
				case 1:
					imageTwoUrl = imageUrl;
					evaluationThreeImageView.setVisibility(View.VISIBLE);
					Log.e(TAG, "imageTwoUrl:" + imageTwoUrl);
					break;
				case 2:
					imageThreeUrl = imageUrl;
					Log.e(TAG, "imageThreeUrl:" + imageThreeUrl);
					break;
			}
		};
	};

}
