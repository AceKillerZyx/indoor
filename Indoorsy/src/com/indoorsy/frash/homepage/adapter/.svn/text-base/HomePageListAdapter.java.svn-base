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
import com.indoorsy.frash.homepage.data.bean.Goods;
import com.indoorsy.frash.util.ArrayUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class HomePageListAdapter extends BaseAdapter {

	private static final String TAG = HomePageListAdapter.class.getSimpleName();

	private Context context;

	private List<Goods> goodslist;

	public HomePageListAdapter(Context context) {
		super();
		this.context = context;
	}

	public void resetData(List<Goods> goodslist) {
		this.goodslist = goodslist;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return ArrayUtil.isEmptyList(goodslist) ? 0 : goodslist.size();
	}

	@Override
	public Object getItem(int position) {
		return ArrayUtil.isEmptyList(goodslist) ? null : goodslist
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
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = (View) inflater.inflate(R.layout.homepage_list_item, null);

			viewHolder.HomePageListImageView = (ImageView) convertView.findViewById(R.id.HomePageListImageView);
			viewHolder.HomePageListNameTextView = (TextView) convertView.findViewById(R.id.HomePageListNameTextView);
			viewHolder.HomePageListInfoTextView = (TextView) convertView.findViewById(R.id.HomePageListInfoTextView);
			viewHolder.HomePageListMoneyTextView = (TextView) convertView.findViewById(R.id.HomePageListMoneyTextView);
			viewHolder.HomePageListWeighTextView = (TextView) convertView.findViewById(R.id.HomePageListWeighTextView);
			viewHolder.HomePageListNowPriceTextView = (TextView) convertView.findViewById(R.id.HomePageListNowPriceTextView);
			viewHolder.HomePageListSaleNumTextView = (TextView) convertView.findViewById(R.id.HomePageListSaleNumTextView);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		Goods goods = goodslist.get(position);
		if (goods != null) {
			viewHolder.HomePageListNameTextView.setText(goods.getGoodsName());
			viewHolder.HomePageListInfoTextView.setText( goods.getGoodsDesc());
			viewHolder.HomePageListMoneyTextView.setText(goods.getPunitPrice());
			viewHolder.HomePageListWeighTextView.setText( "元/" + goods.getUnitTitle());
			if ("1".equals(goods.getGoodsCurrentPrices()) ) {
				viewHolder.HomePageListNowPriceTextView.setVisibility(View.GONE);
			} else {
				viewHolder.HomePageListNowPriceTextView.setVisibility(View.GONE);
			}
			viewHolder.HomePageListSaleNumTextView.setText( "已售" + String.valueOf(goods.getGoodsSales()));
			
			DisplayImageOptions options = new DisplayImageOptions.Builder()
					.showImageForEmptyUri(R.drawable.homepage_product_detail_default)
					.showImageOnFail(R.drawable.homepage_product_detail_default)
					.resetViewBeforeLoading(true).cacheOnDisk(true)
					.imageScaleType(ImageScaleType.EXACTLY)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.considerExifParams(true)
					.displayer(new FadeInBitmapDisplayer(300)).build();

			ImageLoader.getInstance().displayImage(goods.getGoodsThumb(),viewHolder.HomePageListImageView, options,
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
		ImageView HomePageListImageView;
		TextView HomePageListNameTextView,
				HomePageListInfoTextView,
				HomePageListMoneyTextView,
				HomePageListWeighTextView,
				HomePageListNowPriceTextView,
				HomePageListSaleNumTextView;
	}

}
