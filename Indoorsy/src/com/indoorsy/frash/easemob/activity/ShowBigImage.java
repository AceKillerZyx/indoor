/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.indoorsy.frash.easemob.activity;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;

import com.easemob.chat.EMChatConfig;
import com.easemob.cloud.CloudOperationCallback;
import com.easemob.cloud.HttpFileManager;
import com.easemob.util.ImageUtils;
import com.easemob.util.PathUtil;
import com.indoorsy.frash.R;
import com.indoorsy.frash.common.activity.BasicActivity;
import com.indoorsy.frash.easemob.task.LoadLocalBigImgTask;
import com.indoorsy.frash.easemob.util.ImageCache;
import com.indoorsy.frash.easemob.widget.photoview.PhotoView;
import com.indoorsy.frash.http.core.HttpResult;
import com.indoorsy.frash.http.core.HttpTask;

/**
 * 下载显示大图
 * 
 */
public class ShowBigImage extends BasicActivity {

	private ProgressDialog pd;
	private PhotoView image;
	private int default_res = R.drawable.common_header_image;
	private String localFilePath;
	private Bitmap bitmap;
	private boolean isDownloaded;
	private ProgressBar loadLocalPb;

	/**
	 * 通过远程URL，确定下本地下载后的localurl
	 * @param remoteUrl
	 * @return
	 */
	public String getLocalFilePath(String remoteUrl){
		String localPath;
		if (remoteUrl.contains("/")){
			localPath = PathUtil.getInstance().getImagePath().getAbsolutePath() + "/"
					+ remoteUrl.substring(remoteUrl.lastIndexOf("/") + 1);
		}else{
			localPath = PathUtil.getInstance().getImagePath().getAbsolutePath() + "/" + remoteUrl;
		}
		return localPath;
	}
	
	/**
	 * 下载图片
	 * 
	 * @param remoteFilePath
	 */
	private void downloadImage(final String remoteFilePath, final Map<String, String> headers) {
		pd = new ProgressDialog(this);
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pd.setCanceledOnTouchOutside(false);
		pd.setMessage(getResources().getString(R.string.download_image));
		pd.show();
		localFilePath = getLocalFilePath(remoteFilePath);
		final HttpFileManager httpFileMgr = new HttpFileManager(this, EMChatConfig.getInstance().getStorageUrl());
		final CloudOperationCallback callback = new CloudOperationCallback() {
			public void onSuccess(String resultMsg) {

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						DisplayMetrics metrics = new DisplayMetrics();
						getWindowManager().getDefaultDisplay().getMetrics(metrics);
						int screenWidth = metrics.widthPixels;
						int screenHeight = metrics.heightPixels;

						bitmap = ImageUtils.decodeScaleImage(localFilePath, screenWidth, screenHeight);
						if (bitmap == null) {
							image.setImageResource(default_res);
						} else {
							image.setImageBitmap(bitmap);
							ImageCache.getInstance().put(localFilePath, bitmap);
							isDownloaded = true;
						}
						if (pd != null) {
							pd.dismiss();
						}
					}
				});
			}

			public void onError(String msg) {
				Log.e("###", "offline file transfer error:" + msg);
				File file = new File(localFilePath);
				if (file.exists()&&file.isFile()) {
					file.delete();
				}
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						pd.dismiss();
						image.setImageResource(default_res);
					}
				});
			}

			public void onProgress(final int progress) {
				Log.d("ease", "Progress: " + progress);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						pd.setMessage(getResources().getString(R.string.download_image_second) + progress + "%");
					}
				});
			}
		};

		new Thread(new Runnable() {
			@Override
			public void run() {
				httpFileMgr.downloadFile(remoteFilePath, localFilePath, headers, callback);
			}
		}).start();
	}

	@Override
	public void onBackPressed() {
		if (isDownloaded)
			setResult(RESULT_OK);
		finish();
	}

	@Override
	public void onClick(View arg0) {
		
	}

	@Override
	public void onLoadFinish(HttpTask task, HttpResult result) {
		
	}

	@Override
	public int initLayout() {
		return R.layout.activity_show_big_image;
	}

	@SuppressLint("NewApi") @Override
	public void initUI() {
		image = (PhotoView) findViewById(R.id.image);
		loadLocalPb = (ProgressBar) findViewById(R.id.pb_load_local);
		default_res = getIntent().getIntExtra("default_image", R.drawable.common_header_image);
		Uri uri = getIntent().getParcelableExtra("uri");
		String remotepath = getIntent().getExtras().getString("remotepath");
		String secret = getIntent().getExtras().getString("secret");
		System.err.println("show big image uri:" + uri + " remotepath:" + remotepath);

		//本地存在，直接显示本地的图片
		if (uri != null && new File(uri.getPath()).exists()) {
			System.err.println("showbigimage file exists. directly show it");
			DisplayMetrics metrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(metrics);
			// int screenWidth = metrics.widthPixels;
			// int screenHeight =metrics.heightPixels;
			bitmap = ImageCache.getInstance().get(uri.getPath());
			if (bitmap == null) {
				LoadLocalBigImgTask task = new LoadLocalBigImgTask(this, uri.getPath(), image, loadLocalPb, ImageUtils.SCALE_IMAGE_WIDTH,
						ImageUtils.SCALE_IMAGE_HEIGHT);
				if (android.os.Build.VERSION.SDK_INT > 10) {
					task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				} else {
					task.execute();
				}
			} else {
				image.setImageBitmap(bitmap);
			}
		} else if (remotepath != null) { //去服务器下载图片
			System.err.println("download remote image");
			Map<String, String> maps = new HashMap<String, String>();
			if (!TextUtils.isEmpty(secret)) {
				maps.put("share-secret", secret);
			}
			downloadImage(remotepath, maps);
		} else {
			image.setImageResource(default_res);
		}

		image.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public void initData() {
		
	}
}
