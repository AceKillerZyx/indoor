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
import com.indoorsy.frash.personal.data.bean.CollectionRecipe;
import com.indoorsy.frash.util.ArrayUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

@SuppressLint("InflateParams") 
public class CollectionRecipeAdapter  extends BaseAdapter {
	
	private static final String TAG = CollectionRecipeAdapter.class.getSimpleName();
	
	private Context context;

	private List<CollectionRecipe> collectionRecipeList;
	
	public CollectionRecipeAdapter(Context context){
		super();
		this.context = context;
	}
	
	public void resetData(List<CollectionRecipe> collectionRecipeList){
		this.collectionRecipeList = collectionRecipeList;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return ArrayUtil.isEmptyList(collectionRecipeList) ? 0 : collectionRecipeList.size();
	}

	@Override
	public Object getItem(int position) {
		return ArrayUtil.isEmptyList(collectionRecipeList) ? null : collectionRecipeList.get(position);
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
			convertView = (View) inflater.inflate(R.layout.personal_mycollection_recipe_item, null);
			
			viewHolder.mycollectionRecipeItemImageView = (ImageView)convertView.findViewById(R.id.mycollectionRecipeItemImageView);
			viewHolder.mycollectionRecipeItemNameTextView = (TextView)convertView.findViewById(R.id.mycollectionRecipeItemNameTextView);
			viewHolder.mycollectionRecipeItemDateTextView = (TextView)convertView.findViewById(R.id.mycollectionRecipeItemDateTextView);
			viewHolder.mycollectionRecipeItemDeleteImageView = (ImageView)convertView.findViewById(R.id.mycollectionRecipeItemDeleteImageView);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		CollectionRecipe collectionRecipe = collectionRecipeList.get(position);
		if(collectionRecipe != null){
			viewHolder.mycollectionRecipeItemNameTextView.setText(collectionRecipe.getRecipeTitle());
			viewHolder.mycollectionRecipeItemDateTextView.setText(collectionRecipe.getRecipeTime().substring(0, 10));
			DisplayImageOptions options = new DisplayImageOptions.Builder()
					.showImageForEmptyUri(R.drawable.homepage_product_detail_default)
					.showImageOnFail(R.drawable.homepage_product_detail_default)
					.resetViewBeforeLoading(true).cacheOnDisk(true)
					.imageScaleType(ImageScaleType.EXACTLY)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.considerExifParams(true)
					.displayer(new FadeInBitmapDisplayer(300)).build();
		
			ImageLoader.getInstance().displayImage(collectionRecipe.getRecipeImages(),viewHolder.mycollectionRecipeItemImageView, options,
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
			viewHolder.mycollectionRecipeItemDeleteImageView.setOnClickListener( new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (mListener != null) {
	                    mListener.onMyCollectionRecipeItemClick(v, position);
	                }
				}
			});
		}
		return convertView;
	}
	
	class ViewHolder {
		ImageView mycollectionRecipeItemImageView,
				mycollectionRecipeItemDeleteImageView;
		TextView mycollectionRecipeItemNameTextView,
				mycollectionRecipeItemDateTextView;
	}
	
	/**
     * 单击事件监听器
     */
    private onMyCollectionRecipeItemClickListener mListener = null;
    
    public void setOnMyCollectionRecipeItemClickListener(onMyCollectionRecipeItemClickListener listener){
    	mListener = listener;
    }

    public interface onMyCollectionRecipeItemClickListener {
        void onMyCollectionRecipeItemClick(View v, int position);
    }

}
