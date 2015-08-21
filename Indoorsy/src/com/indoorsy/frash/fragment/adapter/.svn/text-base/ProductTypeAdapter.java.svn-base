package com.indoorsy.frash.fragment.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.indoorsy.frash.R;
import com.indoorsy.frash.fragment.data.bean.ProductType;
import com.indoorsy.frash.util.ArrayUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

@SuppressLint("InflateParams") 
public class ProductTypeAdapter extends BaseAdapter {

	private static final String TAG = ProductTypeAdapter.class.getSimpleName();
	
	private Context context;

	private List<ProductType> productTypeList;
	
	public ProductTypeAdapter(Context context){
		super();
		this.context = context;
	}
	
	public void resetData(List<ProductType> productTypeList){
		this.productTypeList = productTypeList;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return ArrayUtil.isEmptyList(productTypeList) ? 0 : productTypeList.size();
	}

	@Override
	public Object getItem(int position) {
		return ArrayUtil.isEmptyList(productTypeList) ? null : productTypeList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = (View) inflater.inflate(R.layout.homepage_product_type_item, null);
			
			viewHolder.productTypeItemImageView = (ImageView)convertView.findViewById(R.id.productTypeItemImageView);
			viewHolder.productTypeItemTextView = (TextView)convertView.findViewById(R.id.productTypeItemTextView);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		ProductType productType = productTypeList.get(position);
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.logo)
				.showImageOnFail(R.drawable.logo)
				.resetViewBeforeLoading(true).cacheOnDisk(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.displayer(new FadeInBitmapDisplayer(300)).build();
		if(productType != null){
			ImageLoader.getInstance().displayImage(productType.getTImages(),viewHolder.productTypeItemImageView, options, new SimpleImageLoadingListener() {
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
							Log.e(TAG, "message--" + message);
						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
						}
					});
			viewHolder.productTypeItemTextView.setText(productType.getTName());
		}
		return convertView;
	}
	
	class ViewHolder {
		ImageView productTypeItemImageView;
		TextView productTypeItemTextView;
	}

}
