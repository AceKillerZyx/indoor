package com.indoorsy.frash.homepage.adapter;

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
import com.indoorsy.frash.homepage.data.bean.Imgs;
import com.indoorsy.frash.util.ArrayUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class ProductDetailImgsListAdapter extends BaseAdapter {

	private static final String TAG = ProductDetailImgsListAdapter.class.getSimpleName();

	private Context context;

	private List<Imgs> imglists;

	public ProductDetailImgsListAdapter(Context context) {
		super();
		this.context = context;
	}

	public void resetData(List<Imgs> imglists) {
		this.imglists = imglists;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return ArrayUtil.isEmptyList(imglists) ? 0 : imglists.size();
	}

	@Override
	public Object getItem(int position) {
		return ArrayUtil.isEmptyList(imglists) ? null : imglists
				.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = (View) inflater.inflate(
					R.layout.homepage_recipe_detail_imagelist_item, null);

			viewHolder.RecipeDetailImageListImageView = (ImageView) convertView.findViewById(R.id.RecipeDetailImageListImageView);
			viewHolder.RecipeDetailImageListTextView = (TextView) convertView.findViewById(R.id.RecipeDetailImageListTextView);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		Imgs imgs = imglists.get(position);
		if (imgs != null) {
			viewHolder.RecipeDetailImageListTextView.setText(imgs.getComDetailsContent());
			
			DisplayImageOptions options = new DisplayImageOptions.Builder()
					.showImageForEmptyUri(R.drawable.homepage_product_detail_default)
					.showImageOnFail(R.drawable.homepage_product_detail_default)
					.resetViewBeforeLoading(true).cacheOnDisk(true)
					.imageScaleType(ImageScaleType.EXACTLY)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.considerExifParams(true)
					.displayer(new FadeInBitmapDisplayer(300)).build();

			ImageLoader.getInstance().displayImage(imgs.getComDetailsImages(),
					viewHolder.RecipeDetailImageListImageView, options,
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

		return convertView;
	}

	class ViewHolder {
		ImageView RecipeDetailImageListImageView;
		TextView RecipeDetailImageListTextView;
	}

}
