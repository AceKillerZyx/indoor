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
import android.widget.ListView;
import android.widget.TextView;

import com.indoorsy.frash.R;
import com.indoorsy.frash.homepage.data.bean.RecipeListc;
import com.indoorsy.frash.util.ArrayUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class RecipeCommentListAdapter extends BaseAdapter {

	private static final String TAG = RecipeCommentListAdapter.class.getSimpleName();

	private Context context;

	private List<RecipeListc> recipeListcss;
	
	public RecipeCommentListAdapter(Context context) {
		super();
		this.context = context;
	}

	public void resetData(List<RecipeListc> recipeListcss) {
		this.recipeListcss = recipeListcss;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return ArrayUtil.isEmptyList(recipeListcss) ? 0 : recipeListcss.size();
	}

	@Override
	public Object getItem(int position) {
		return ArrayUtil.isEmptyList(recipeListcss) ? null : recipeListcss.get(position);
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
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = (View) inflater.inflate(R.layout.homepage_recipe_detail_comment_item, null);

			viewHolder.HomePageRecipeDetailListItemHeadCircleImageView = (ImageView) convertView.findViewById(R.id.HomePageRecipeDetailListItemHeadCircleImageView);
			viewHolder.HomePageRecipeDetailListItemUserNameTextView = (TextView) convertView.findViewById(R.id.HomePageRecipeDetailListItemUserNameTextView);
			viewHolder.HomePageRecipeDetailListItemUserCommentTextView = (TextView) convertView.findViewById(R.id.HomePageRecipeDetailListItemUserCommentTextView);
			viewHolder.HomePageRecipeDetailListItemListView = (ListView) convertView.findViewById(R.id.HomePageRecipeDetailListItemListView);
			

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		RecipeListc recipeListc = recipeListcss.get(position);
		if (recipeListc != null) {
			
			viewHolder.HomePageRecipeDetailListItemUserNameTextView.setText(recipeListc.getUserNames());
			viewHolder.HomePageRecipeDetailListItemUserCommentTextView.setText(recipeListc.getCommContent());
			
			DisplayImageOptions options = new DisplayImageOptions.Builder()
					.showImageForEmptyUri(R.drawable.commodity_firm_product_default)
					.showImageOnFail(R.drawable.commodity_firm_product_default)
					.resetViewBeforeLoading(true).cacheOnDisk(true)
					.imageScaleType(ImageScaleType.EXACTLY)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.considerExifParams(true)
					.displayer(new FadeInBitmapDisplayer(300)).build();

			ImageLoader.getInstance().displayImage(recipeListc.getUserImages(),viewHolder.HomePageRecipeDetailListItemHeadCircleImageView, options,
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
		ImageView HomePageRecipeDetailListItemHeadCircleImageView;
		TextView HomePageRecipeDetailListItemUserNameTextView,
				HomePageRecipeDetailListItemUserCommentTextView,
				HomePageRecipeDetailListItemReplyTextView;//回复
		ListView HomePageRecipeDetailListItemListView;
	}

}
