package com.indoorsy.frash.shopping.cart.adapter;

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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.indoorsy.frash.R;
import com.indoorsy.frash.shopping.cart.data.bean.CartGood;
import com.indoorsy.frash.util.ArrayUtil;
import com.indoorsy.frash.util.StringUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class ShoppingcarListAdapter extends BaseAdapter {

	private static final String TAG = ShoppingcarListAdapter.class.getSimpleName();
	public static final int DELETE_CART = 3333;

	private Context context;
	private List<CartGood> cartlist;
	public boolean itemIsChecked = false;
	public int itemNumber;
	
	public ShoppingcarListAdapter(Context context){
		this.context = context;
	}
	


	public void resetData(List<CartGood> cartlist) {
		this.cartlist = cartlist;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return ArrayUtil.isEmptyList(cartlist) ? 0 : cartlist.size();
	}

	@Override
	public Object getItem(int position) {
		return ArrayUtil.isEmptyList(cartlist) ? null : cartlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = (View) inflater.inflate(R.layout.shoppingcar_list_item, null);

			viewHolder.ShoppingcarIsCheckedCheckBox = (CheckBox) convertView.findViewById(R.id.ShoppingcarIsCheckedCheckBox);
			
			viewHolder.ShoppingcarProductImageView = (ImageView) convertView.findViewById(R.id.ShoppingcarProductImageView);
			viewHolder.ShoppingcarProductNameTextView = (TextView) convertView.findViewById(R.id.ShoppingcarProductNameTextView);
			viewHolder.ShoppingcarProductInfoTextView = (TextView) convertView.findViewById(R.id.ShoppingcarProductInfoTextView);
			viewHolder.ShoppingcarProductPriceTextView = (TextView) convertView.findViewById(R.id.ShoppingcarProductPriceTextView);
			viewHolder.ShoppingcarNumTextView = (TextView) convertView.findViewById(R.id.ShoppingcarNumTextView);
			viewHolder.ShoppingcarNumAddTextView = (TextView) convertView.findViewById(R.id.ShoppingcarNumAddTextView);
			viewHolder.ShoppingcarNumSubTextView = (TextView) convertView.findViewById(R.id.ShoppingcarNumSubTextView);
			viewHolder.ShoppingcarIsSeckill = (ImageView) convertView.findViewById(R.id.ShoppingcarIsSeckill);
			viewHolder.ShoppingcarDelete = (ImageView) convertView.findViewById(R.id.ShoppingcarDelete);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		final CartGood cartGood = cartlist.get(position);
		if (cartGood != null) {
//			itemNumber = cartGood.getCartNumber();
			viewHolder.ShoppingcarProductNameTextView.setText(cartGood.getGoodsName());
			viewHolder.ShoppingcarProductInfoTextView.setText(cartGood.getGoodsBrief());
			viewHolder.ShoppingcarProductPriceTextView.setText(String.format(context.getResources().getString(R.string.homepage_list_item_money),String.valueOf(cartGood.getCartGoodsPrice()),String.valueOf(cartGood.getCartUnit())));
			viewHolder.ShoppingcarNumTextView.setText(String.valueOf(cartGood.getCartNumber()));
			if (StringUtil.isEmpty(cartGood.getCartPath())) {
				viewHolder.ShoppingcarIsSeckill.setVisibility(View.GONE);
			} else {
				viewHolder.ShoppingcarIsSeckill.setVisibility(View.VISIBLE);
			}
			
			DisplayImageOptions options = new DisplayImageOptions.Builder()
					.showImageForEmptyUri(R.drawable.commodity_firm_product_default)
					.showImageOnFail(R.drawable.commodity_firm_product_default)
					.resetViewBeforeLoading(true).cacheOnDisk(true)
					.imageScaleType(ImageScaleType.EXACTLY)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.considerExifParams(true)
					.displayer(new FadeInBitmapDisplayer(300)).build();

			ImageLoader.getInstance().displayImage(cartGood.getGoodsImg(),viewHolder.ShoppingcarProductImageView, options,
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
			//选择小点
			viewHolder.ShoppingcarIsCheckedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean choice) {
					listener.getChoiceData(position, choice);
				}
			});
			viewHolder.ShoppingcarNumAddTextView.setOnClickListener(new OnClickListener() {
				//购物车 数量+
				@Override
				public void onClick(View v) {
					itemNumber = Integer.parseInt(viewHolder.ShoppingcarNumTextView.getText().toString());
					Log.e(TAG, "+ 前 itemNumber = " + itemNumber);
					viewHolder.ShoppingcarNumTextView.setText(String.valueOf(++itemNumber));
					Log.e(TAG, "+ 后 itemNumber = " + itemNumber);
					cartGood.setCartNumber(itemNumber);
					if(numListener != null){
						numListener.onCartNumClick(v, position, true);
					}
				}
			});
			viewHolder.ShoppingcarNumSubTextView.setOnClickListener(new OnClickListener() {
				//购物车 数量-
				@Override
				public void onClick(View v) {
					if(numListener != null){
						numListener.onCartNumClick(v, position, false);
					}
					itemNumber = Integer.parseInt(viewHolder.ShoppingcarNumTextView.getText().toString());
					if (itemNumber > 1) {
						viewHolder.ShoppingcarNumTextView.setText(String.valueOf(--itemNumber));
						cartGood.setCartNumber(itemNumber);
					} else {
						viewHolder.ShoppingcarNumTextView.setText("1");
						itemNumber = 1;
						cartGood.setCartNumber(itemNumber);
					}
					
				}
			});
			
			viewHolder.ShoppingcarDelete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(mListener != null){
						mListener.onCartCancelClick(v, position);
					}
				}
			});
		}
		return convertView;
	}
	
	

	class ViewHolder {
		ImageView ShoppingcarProductImageView,ShoppingcarIsSeckill,ShoppingcarDelete;
		CheckBox ShoppingcarIsCheckedCheckBox;
		TextView ShoppingcarProductNameTextView;
		TextView ShoppingcarProductInfoTextView;
		TextView ShoppingcarProductPriceTextView;
		TextView ShoppingcarNumTextView;
		TextView ShoppingcarNumSubTextView;
		TextView ShoppingcarNumAddTextView;
	}


	/**
     * 点击勾选
     */
	private onCheckedChanged listener;
	public interface onCheckedChanged{
		public void getChoiceData(int position, boolean isChoice);
	}
	public void setOnCheckedChanged(onCheckedChanged listener){
		this.listener = listener;
	}
	
	/**
     * 删除商品
     */
    private CartCancelClickListener mListener = null;
    
    public void setOnCartCancelClick(CartCancelClickListener listener){
    	mListener = listener;
    }

    public interface CartCancelClickListener {
        void onCartCancelClick(View view, int position);
    }
    
    /**
     * 点击加减数量
     */
    private CartNumClickListener numListener = null;
    
    public void setOnCartNumClick(CartNumClickListener listener){
    	numListener = listener;
    }
    
    public interface CartNumClickListener {
    	void onCartNumClick(View view, int position , Boolean isAdd);
    }


}
