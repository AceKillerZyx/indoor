package com.indoorsy.frash.personal.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.indoorsy.frash.R;
import com.indoorsy.frash.personal.data.bean.CollectionGoods;
import com.indoorsy.frash.util.ArrayUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

@SuppressLint("InflateParams") 
public class CollectionGoodsAdapter  extends BaseAdapter {
	
	private static final String TAG = CollectionGoodsAdapter.class.getSimpleName();
	
	private Context context;

	private List<CollectionGoods> collectionGoodList;
	
	public CollectionGoodsAdapter(Context context){
		super();
		this.context = context;
	}
	
	public void resetData(List<CollectionGoods> collectionGoodList){
		this.collectionGoodList = collectionGoodList;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return ArrayUtil.isEmptyList(collectionGoodList) ? 0 : collectionGoodList.size();
	}

	@Override
	public Object getItem(int position) {
		return ArrayUtil.isEmptyList(collectionGoodList) ? null : collectionGoodList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = (View) inflater.inflate(R.layout.personal_mycollection_goods_item, null);
			
			viewHolder.mycollectionGoodsItemImageView = (ImageView)convertView.findViewById(R.id.mycollectionGoodsItemImageView);
			viewHolder.mycollectionGoodsItemNameTextView = (TextView)convertView.findViewById(R.id.mycollectionGoodsItemNameTextView);
			viewHolder.mycollectionGoodsItemInfoTextView = (TextView)convertView.findViewById(R.id.mycollectionGoodsItemInfoTextView);
			viewHolder.mycollectionGoodsItemTimeTextView = (TextView)convertView.findViewById(R.id.mycollectionGoodsItemTimeTextView);
			viewHolder.mycollectionGoodsItemDeleteImageView = (ImageView)convertView.findViewById(R.id.mycollectionGoodsItemDeleteImageView);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		CollectionGoods collectionGood = collectionGoodList.get(position);
		if(collectionGood != null){
			viewHolder.mycollectionGoodsItemNameTextView.setText(collectionGood.getGoodsName());
			viewHolder.mycollectionGoodsItemInfoTextView.setText(collectionGood.getGoodsDesc());
			viewHolder.mycollectionGoodsItemTimeTextView.setText(collectionGood.getCollectAddTime().substring(0, 10));
			DisplayImageOptions options = new DisplayImageOptions.Builder()
					.showImageForEmptyUri(R.drawable.homepage_product_detail_default)
					.showImageOnFail(R.drawable.homepage_product_detail_default)
					.resetViewBeforeLoading(true).cacheOnDisk(true)
					.imageScaleType(ImageScaleType.EXACTLY)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.considerExifParams(true)
					.displayer(new FadeInBitmapDisplayer(300)).build();
		
			ImageLoader.getInstance().displayImage(collectionGood.getGoodsThumb(),viewHolder.mycollectionGoodsItemImageView, options,
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
			viewHolder.mycollectionGoodsItemDeleteImageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (mListener != null) {
	                    mListener.onMyCollectionGoodsItemClick(v, position);
	                }
				}
			});
		}
		return convertView;
	}
	
	class ViewHolder {
		ImageView mycollectionGoodsItemImageView,
				mycollectionGoodsItemDeleteImageView;
		TextView mycollectionGoodsItemNameTextView,
				mycollectionGoodsItemInfoTextView,
				mycollectionGoodsItemTimeTextView;
	}
	
	/**
     * 单击事件监听器
     */
    private onMyCollectionGoodsItemClickListener mListener = null;
    
    public void setOnMyCollectionGoodsItemClickListener(onMyCollectionGoodsItemClickListener listener){
    	mListener = listener;
    }

    public interface onMyCollectionGoodsItemClickListener {
        void onMyCollectionGoodsItemClick(View v, int position);
    }

}
