package com.indoorsy.frash.commodity.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.indoorsy.frash.R;
import com.indoorsy.frash.shopping.cart.data.bean.CartGood;
import com.indoorsy.frash.util.ArrayUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class CommodityFirmMoreListAdapter extends BaseAdapter {

	private static final String TAG = CommodityFirmMoreListAdapter.class.getSimpleName();

	private Context context;

	private ArrayList<? extends Parcelable> cartGoodlist;

	public CommodityFirmMoreListAdapter(Context context) {
		super();
		this.context = context;
	}

	public void resetData(ArrayList<? extends Parcelable> cartGoodlist) {
		this.cartGoodlist = cartGoodlist;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return ArrayUtil.isEmptyList(cartGoodlist) ? 0 : cartGoodlist.size();
	}

	@Override
	public Object getItem(int position) {
		return ArrayUtil.isEmptyList(cartGoodlist) ? null : cartGoodlist
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
			convertView = (View) inflater.inflate(R.layout.commodity_firm_more_list_item, null);

			viewHolder.FirmOrderProductMoreImageView = (ImageView) convertView.findViewById(R.id.FirmOrderProductMoreImageView);
			viewHolder.FirmOrderProductMoreNameTextView = (TextView) convertView.findViewById(R.id.FirmOrderProductMoreNameTextView);
			viewHolder.FirmOrderProductMoreInfoTextView = (TextView) convertView.findViewById(R.id.FirmOrderProductMoreInfoTextView);
			viewHolder.FirmOrderProductMorePriceTextView = (TextView) convertView.findViewById(R.id.FirmOrderProductMorePriceTextView);
			viewHolder.FirmOrderMoreWeightTextView = (TextView) convertView.findViewById(R.id.FirmOrderMoreWeightTextView);
			

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		CartGood cartGood = (CartGood) cartGoodlist.get(position);
		if (cartGood != null) {
			viewHolder.FirmOrderProductMoreNameTextView.setText(cartGood.getGoodsName());
			viewHolder.FirmOrderProductMoreInfoTextView.setText(cartGood.getGoodsBrief());
			viewHolder.FirmOrderMoreWeightTextView.setText("数量：" + cartGood.getCartNumber() + cartGood.getCartUnit());
			if ("条".equals(cartGood.getCartUnit())) {
				viewHolder.FirmOrderProductMorePriceTextView.setText(cartGood.getCartGoodsPrice()+"元/斤");
			} else {
				viewHolder.FirmOrderProductMorePriceTextView.setText(cartGood.getCartGoodsPrice()+"元/"+cartGood.getCartUnit());
			}
			
			DisplayImageOptions options = new DisplayImageOptions.Builder()
					.showImageForEmptyUri(R.drawable.commodity_firm_product_default)
					.showImageOnFail(R.drawable.commodity_firm_product_default)
					.resetViewBeforeLoading(true).cacheOnDisk(true)
					.imageScaleType(ImageScaleType.EXACTLY)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.considerExifParams(true)
					.displayer(new FadeInBitmapDisplayer(300)).build();

			ImageLoader.getInstance().displayImage(((CartGood) cartGood).getGoodsImg(),
					viewHolder.FirmOrderProductMoreImageView, options,
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
		ImageView FirmOrderProductMoreImageView;
		TextView 
		FirmOrderProductMoreNameTextView,
		FirmOrderProductMoreInfoTextView,
		FirmOrderProductMorePriceTextView,
		FirmOrderMoreWeightTextView;
	}

}
