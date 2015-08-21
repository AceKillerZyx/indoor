package com.indoorsy.frash.homepage.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.indoorsy.frash.R;
import com.indoorsy.frash.homepage.activity.ShareActivity;
import com.indoorsy.frash.homepage.data.bean.Recipe;
import com.indoorsy.frash.util.ArrayUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class RecipeShareGridAdapter extends BaseAdapter{
	
	private static final String TAG = RecipeShareGridAdapter.class.getSimpleName();
	
	private Activity activity;
	
	private List<Recipe> recipeList;
	
	public RecipeShareGridAdapter(Activity activity){
		super();
		this.activity = activity;
	}
	
	public void resetData(List<Recipe> recipeList){
		this.recipeList = recipeList;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return ArrayUtil.isEmptyList(recipeList) ? 0 : recipeList.size();
	}

	@Override
	public Object getItem(int position) {
		return ArrayUtil.isEmptyList(recipeList) ? null : recipeList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams") @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = (View) inflater.inflate(R.layout.homepage_recipe_share_item, null);
			viewHolder.homepageRecipeShareItemLinearLayout = (LinearLayout)convertView.findViewById(R.id.homepageRecipeShareItemLinearLayout);
			viewHolder.RecipeShareItemImageView = (ImageView)convertView.findViewById(R.id.RecipeShareItemImageView);
			viewHolder.RecipeShareNameTextView = (TextView)convertView.findViewById(R.id.RecipeShareNameTextView);
			viewHolder.RecipeShareDateTextView = (TextView)convertView.findViewById(R.id.RecipeShareDateTextView);
			viewHolder.RecipeShareShareTextView = (TextView)convertView.findViewById(R.id.RecipeShareShareTextView);
			viewHolder.RecipeShareCommentTextView = (TextView)convertView.findViewById(R.id.RecipeShareCommentTextView);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		// 食谱列表
		WindowManager manager = activity.getWindowManager();         
		Display display = manager.getDefaultDisplay();
		@SuppressWarnings("deprecation")
		int screenWidth = display.getWidth();  //屏幕宽度    
		
		GridView.LayoutParams params = new GridView.LayoutParams(screenWidth/2,LayoutParams.WRAP_CONTENT);
		viewHolder.homepageRecipeShareItemLinearLayout.setLayoutParams(params);
		
		final Recipe recipe = recipeList.get(position);
		if(recipe != null){
			viewHolder.RecipeShareNameTextView.setText(recipe.getRecipeTitle());
			viewHolder.RecipeShareDateTextView.setText(recipe.getRecipeTime());
			viewHolder.RecipeShareShareTextView.setText("评论"+recipe.getCommentcount());
			viewHolder.RecipeShareCommentTextView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// 弹出分享界面
					Intent intent = new Intent(activity,ShareActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					activity.startActivity(intent);
				}
			});
			
			DisplayImageOptions options = new DisplayImageOptions.Builder()
					.showImageForEmptyUri(R.drawable.homepage_product_detail_default)
					.showImageOnFail(R.drawable.homepage_product_detail_default)
					.resetViewBeforeLoading(true).cacheOnDisk(true)
					.imageScaleType(ImageScaleType.EXACTLY)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.considerExifParams(true)
					.displayer(new FadeInBitmapDisplayer(300)).build();
			ImageLoader.getInstance().displayImage(recipe.getRecipeImages(),viewHolder.RecipeShareItemImageView, options, new SimpleImageLoadingListener() {
				
				@Override
				public void onLoadingStarted(String imageUri, View view) {
				}

				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
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
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				}
			});
			
			/*viewHolder.RecipeShareItemImageView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context,RecipeDetailActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra(Constants.HOMEPAGE_RECIPE_SHARE_ID, recipe.getRecipeId());
					context.startActivity(intent);
				}
			});*/
		}
		
		return convertView;
	}
	class ViewHolder {
		LinearLayout homepageRecipeShareItemLinearLayout;
		ImageView RecipeShareItemImageView;
		TextView RecipeShareNameTextView, 
				RecipeShareDateTextView,
				RecipeShareShareTextView,
				RecipeShareCommentTextView;
	}
	
	
	
}
