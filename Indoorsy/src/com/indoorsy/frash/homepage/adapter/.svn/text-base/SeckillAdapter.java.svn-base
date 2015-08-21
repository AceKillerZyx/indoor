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
import android.widget.SeekBar;
import android.widget.TextView;

import com.indoorsy.frash.R;
import com.indoorsy.frash.homepage.data.bean.SeckTk;
import com.indoorsy.frash.util.ArrayUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class SeckillAdapter extends BaseAdapter {

	private static final String TAG = SeckillAdapter.class.getSimpleName();

	private Context context;

	private List<SeckTk> seckTklist;

	public SeckillAdapter(Context context) {
		super();
		this.context = context;
	}

	public void resetData(List<SeckTk> seckTklist) {
		this.seckTklist = seckTklist;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return ArrayUtil.isEmptyList(seckTklist) ? 0 : seckTklist.size();
	}

	@Override
	public Object getItem(int position) {
		return ArrayUtil.isEmptyList(seckTklist) ? null : seckTklist
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
					R.layout.homepage_seckill_item, null);

			viewHolder.HomePageSeckillItemImageView = (ImageView) convertView.findViewById(R.id.HomePageSeckillItemImageView);
			viewHolder.HomePageSeckillItemNameTextView = (TextView) convertView.findViewById(R.id.HomePageSeckillItemNameTextView);
			viewHolder.HomePageSeckillItemBoughtNumTextView = (TextView) convertView.findViewById(R.id.HomePageSeckillItemBoughtNumTextView);
			viewHolder.HomePageSeckillItemRemainNumTextView = (TextView) convertView.findViewById(R.id.HomePageSeckillItemRemainNumTextView);
			viewHolder.HomePageSeckillItemUnitPriceTextView = (TextView) convertView.findViewById(R.id.HomePageSeckillItemUnitPriceTextView);
			viewHolder.HomePageSeckillItemSeekBar = (SeekBar) convertView.findViewById(R.id.HomePageSeckillItemSeekBar);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		SeckTk seckTk = seckTklist.get(position);
		if (seckTk != null) {
			viewHolder.HomePageSeckillItemNameTextView.setText(seckTk.getSeckillContnt());
			viewHolder.HomePageSeckillItemBoughtNumTextView.setText("已秒" + seckTk.getSeckillAlready());
			viewHolder.HomePageSeckillItemRemainNumTextView.setText("还剩" + String.valueOf(seckTk.getSeckillSurplus()));
			viewHolder.HomePageSeckillItemUnitPriceTextView.setText(String.valueOf(seckTk.getSeckillUnit()) + "");
			int max = Integer.parseInt(seckTk.getSeckillAlready() + seckTk.getSeckillSurplus());
			int progress =  Integer.parseInt(seckTk.getSeckillAlready());
//			max = 10;
//			progress = 0;
			Log.e(TAG, "max = " + max);
			Log.e(TAG, "progress = " + progress);
			viewHolder.HomePageSeckillItemSeekBar.setMax(max);
			viewHolder.HomePageSeckillItemSeekBar.setProgress(progress);
			
			DisplayImageOptions options = new DisplayImageOptions.Builder()
					.showImageForEmptyUri(R.drawable.homepage_product_detail_default)
					.showImageOnFail(R.drawable.homepage_product_detail_default)
					.resetViewBeforeLoading(true).cacheOnDisk(true)
					.imageScaleType(ImageScaleType.EXACTLY)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.considerExifParams(true)
					.displayer(new FadeInBitmapDisplayer(300)).build();

			ImageLoader.getInstance().displayImage(seckTk.getGoodsImg(),
					viewHolder.HomePageSeckillItemImageView, options,
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
		ImageView HomePageSeckillItemImageView;
		TextView HomePageSeckillItemNameTextView,
				HomePageSeckillItemBoughtNumTextView,
				HomePageSeckillItemRemainNumTextView,
				HomePageSeckillItemUnitPriceTextView;
		SeekBar HomePageSeckillItemSeekBar;
	}

}
