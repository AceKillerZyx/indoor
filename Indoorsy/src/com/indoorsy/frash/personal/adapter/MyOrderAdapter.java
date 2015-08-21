package com.indoorsy.frash.personal.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.indoorsy.frash.Constants;
import com.indoorsy.frash.R;
import com.indoorsy.frash.commodity.activity.PayModeActivity;
import com.indoorsy.frash.easemob.activity.KfLoginActivity;
import com.indoorsy.frash.personal.data.bean.MyOrder;
import com.indoorsy.frash.util.ArrayUtil;
import com.indoorsy.frash.util.ToastUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

@SuppressLint("InflateParams") 
public class MyOrderAdapter extends BaseAdapter {
	
	private static final String TAG = MyOrderAdapter.class.getSimpleName();
	
	private int type;
	
	private Activity activity;

	private List<MyOrder> myOrderList;
	
	public MyOrderAdapter(Activity activity){
		super();
		this.activity = activity;
	}
	
	public void resetData(List<MyOrder> myOrderList,int type){
		this.myOrderList = myOrderList;
		this.type = type;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return ArrayUtil.isEmptyList(myOrderList) ? 0 : myOrderList.size();
	}

	@Override
	public Object getItem(int position) {
		return ArrayUtil.isEmptyList(myOrderList) ? null : myOrderList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = (View) inflater.inflate(R.layout.personal_myorder_item, null);
			
			viewHolder.myorderItemImageView = (ImageView)convertView.findViewById(R.id.myorderItemImageView);
			viewHolder.myorderItemNameTextView = (TextView)convertView.findViewById(R.id.myorderItemNameTextView);
			viewHolder.myorderItemPriceValueTextView = (TextView)convertView.findViewById(R.id.myorderItemPriceValueTextView);
			viewHolder.myorderItemYuanAndUnitTextView = (TextView)convertView.findViewById(R.id.myorderItemYuanAndUnitTextView);
			viewHolder.myorderItemNumberTextView = (TextView)convertView.findViewById(R.id.myorderItemNumberTextView);
			viewHolder.myorderItemNumberUnitTextView = (TextView)convertView.findViewById(R.id.myorderItemNumberUnitTextView);
			viewHolder.myorderItemAmountsTextView = (TextView)convertView.findViewById(R.id.myorderItemAmountsTextView);
			viewHolder.myorderItemAmountsValueTextView = (TextView)convertView.findViewById(R.id.myorderItemAmountsValueTextView);
			viewHolder.myorderItemStateTextView = (TextView)convertView.findViewById(R.id.myorderItemStateTextView);
			viewHolder.myorderItemDaiFuKuanRobotTextView = (TextView)convertView.findViewById(R.id.myorderItemDaiFuKuanRobotTextView);
			viewHolder.myorderItemCancelOrderTextView = (TextView)convertView.findViewById(R.id.myorderItemCancelOrderTextView);
			viewHolder.myorderItemPayTextView = (TextView)convertView.findViewById(R.id.myorderItemPayTextView);
			viewHolder.myorderItemDaiFaHuoRobotTextView = (TextView)convertView.findViewById(R.id.myorderItemDaiFaHuoRobotTextView);
			viewHolder.myorderItemRefundTextView = (TextView)convertView.findViewById(R.id.myorderItemRefundTextView);
			viewHolder.myorderItemRemindTextView = (TextView)convertView.findViewById(R.id.myorderItemRemindTextView);
			viewHolder.myorderItemDaiShouHuoRobotTextView = (TextView)convertView.findViewById(R.id.myorderItemDaiShouHuoRobotTextView);
			viewHolder.myorderItemQueRenShouHuoTextView = (TextView)convertView.findViewById(R.id.myorderItemQueRenShouHuoTextView);
			viewHolder.myorderItemEvaluationTextView = (TextView)convertView.findViewById(R.id.myorderItemEvaluationTextView);
			viewHolder.myorderItemRefundValueTextView = (TextView)convertView.findViewById(R.id.myorderItemRefundValueTextView);
			viewHolder.myorderItemDaiFuKuanLinearLayout = (LinearLayout)convertView.findViewById(R.id.myorderItemDaiFuKuanLinearLayout);
			viewHolder.myorderItemDaiFaHuoLinearLayout = (LinearLayout)convertView.findViewById(R.id.myorderItemDaiFaHuoLinearLayout);
			viewHolder.myorderItemRefundLinearLayout = (LinearLayout)convertView.findViewById(R.id.myorderItemRefundLinearLayout);
			viewHolder.myorderItemDaiShouHuoLinearLayout = (LinearLayout)convertView.findViewById(R.id.myorderItemDaiShouHuoLinearLayout);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		final MyOrder myOrder = myOrderList.get(position);
		if(myOrder != null){
			viewHolder.myorderItemNameTextView.setText(myOrder.getGoodsname());
			viewHolder.myorderItemPriceValueTextView.setText(String.valueOf(myOrder.getOrdergoodsamount()));
			viewHolder.myorderItemYuanAndUnitTextView.setText("元/" + myOrder.getUnit());
			viewHolder.myorderItemNumberTextView.setText(String.valueOf(myOrder.getOrdernumber()));
			viewHolder.myorderItemNumberUnitTextView.setText(myOrder.getUnit());
			viewHolder.myorderItemAmountsValueTextView.setText(String.valueOf(myOrder.getOrderamount()));
			viewHolder.myorderItemStateTextView.setText(myOrder.getOrderstate());
			viewHolder.myorderItemRefundValueTextView.setText(String.valueOf(myOrder.getOrderamount()));
			DisplayImageOptions options = new DisplayImageOptions.Builder()
					.showImageForEmptyUri(R.drawable.commodity_firm_product_default)
					.showImageOnFail(R.drawable.commodity_firm_product_default)
					.resetViewBeforeLoading(true).cacheOnDisk(true)
					.imageScaleType(ImageScaleType.EXACTLY)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.considerExifParams(true)
					.displayer(new FadeInBitmapDisplayer(300)).build();
			ImageLoader.getInstance().displayImage(myOrder.getGoodsthumb(),viewHolder.myorderItemImageView, options,
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
			Log.e(TAG, "type:" + type);
			switch (type) {
				case 0:
					viewHolder.myorderItemAmountsTextView.setText("应付款");
					viewHolder.myorderItemStateTextView.setVisibility(View.VISIBLE);
					viewHolder.myorderItemDaiFuKuanLinearLayout.setVisibility(View.GONE);
					viewHolder.myorderItemDaiFaHuoLinearLayout.setVisibility(View.GONE);
					viewHolder.myorderItemDaiShouHuoLinearLayout.setVisibility(View.GONE);
					viewHolder.myorderItemEvaluationTextView.setVisibility(View.GONE);
					viewHolder.myorderItemRefundLinearLayout.setVisibility(View.GONE);
					break;
				case 1:
					viewHolder.myorderItemAmountsTextView.setText("应付款");
					viewHolder.myorderItemDaiFuKuanLinearLayout.setVisibility(View.VISIBLE);
					viewHolder.myorderItemStateTextView.setVisibility(View.GONE);
					viewHolder.myorderItemDaiFaHuoLinearLayout.setVisibility(View.GONE);
					viewHolder.myorderItemDaiShouHuoLinearLayout.setVisibility(View.GONE);
					viewHolder.myorderItemEvaluationTextView.setVisibility(View.GONE);
					viewHolder.myorderItemRefundLinearLayout.setVisibility(View.GONE);
					break;
				case 2:
					viewHolder.myorderItemAmountsTextView.setText("交易金额");
					viewHolder.myorderItemDaiFaHuoLinearLayout.setVisibility(View.VISIBLE);
					viewHolder.myorderItemStateTextView.setVisibility(View.GONE);
					viewHolder.myorderItemDaiFuKuanLinearLayout.setVisibility(View.GONE);
					viewHolder.myorderItemDaiShouHuoLinearLayout.setVisibility(View.GONE);
					viewHolder.myorderItemEvaluationTextView.setVisibility(View.GONE);
					viewHolder.myorderItemRefundLinearLayout.setVisibility(View.GONE);
					break;
				case 3:
					viewHolder.myorderItemAmountsTextView.setText("交易金额");
					viewHolder.myorderItemDaiShouHuoLinearLayout.setVisibility(View.VISIBLE);
					viewHolder.myorderItemStateTextView.setVisibility(View.GONE);
					viewHolder.myorderItemDaiFuKuanLinearLayout.setVisibility(View.GONE);
					viewHolder.myorderItemDaiFaHuoLinearLayout.setVisibility(View.GONE);
					viewHolder.myorderItemEvaluationTextView.setVisibility(View.GONE);
					viewHolder.myorderItemRefundLinearLayout.setVisibility(View.GONE);
					break;
				case 4:
					viewHolder.myorderItemAmountsTextView.setText("交易金额");
					viewHolder.myorderItemEvaluationTextView.setVisibility(View.VISIBLE);
					viewHolder.myorderItemDaiShouHuoLinearLayout.setVisibility(View.GONE);
					viewHolder.myorderItemStateTextView.setVisibility(View.GONE);
					viewHolder.myorderItemDaiFuKuanLinearLayout.setVisibility(View.GONE);
					viewHolder.myorderItemDaiFaHuoLinearLayout.setVisibility(View.GONE);
					viewHolder.myorderItemRefundLinearLayout.setVisibility(View.GONE);
					break;
				case 5:
					viewHolder.myorderItemAmountsTextView.setText("已付金额");
					viewHolder.myorderItemRefundLinearLayout.setVisibility(View.VISIBLE);
					viewHolder.myorderItemEvaluationTextView.setVisibility(View.GONE);
					viewHolder.myorderItemDaiShouHuoLinearLayout.setVisibility(View.GONE);
					viewHolder.myorderItemStateTextView.setVisibility(View.GONE);
					viewHolder.myorderItemDaiFuKuanLinearLayout.setVisibility(View.GONE);
					viewHolder.myorderItemDaiFaHuoLinearLayout.setVisibility(View.GONE);
					break;
			}
			//代付款客服
			viewHolder.myorderItemDaiFuKuanRobotTextView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(activity,KfLoginActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					activity.startActivity(intent);
				}
			});
			//代付款取消订单
			viewHolder.myorderItemCancelOrderTextView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(mListener != null){
						mListener.onMyOrderCancelItemClick(v, position);
					}
				}
			});
			//代付款付款
			viewHolder.myorderItemPayTextView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent= new Intent(activity,PayModeActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//					Bundle bundle = new Bundle();
//					bundle.putInt(Constants.HOMEPAGE_PRODUCT_DELID, 1);
//					bundle.putDouble(Constants.HOMEPAGE_PRODUCT_PRICE, myOrder.getOrdergoodsamount());
//					bundle.putString(Constants.HOMEPAGE_PRODUCT_UNIT, myOrder.getUnit());
//					intent.putExtra(Constants.HOMEPAGE_PRODUCT_ID, myOrder.getGoodsid());
//					intent.putExtra(Constants.HOMEPAGE_PRODUCT_NAME, myOrder.getGoodsname());
//					intent.putExtra(Constants.HOMEPAGE_PRODUCT_DESC, myOrder.getGoodsdesc());
//					intent.putExtra(Constants.HOMEPAGE_PRODUCT_THUMB,myOrder.getGoodsthumb());
//					intent.putExtras(bundle);
					intent.putExtra(Constants.COMMODITY_AMOUNT, myOrder.getOrderamount()+"");//共付金额
					intent.putExtra(Constants.ORDERNUMBER_SN, myOrder.getOrdersn());
					activity.startActivity(intent);
				}
			});
			
			//代发货客服
			viewHolder.myorderItemDaiFaHuoRobotTextView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(activity,KfLoginActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					activity.startActivity(intent);
				}
			});
			
			//代发货提醒发货
			viewHolder.myorderItemRemindTextView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ToastUtil.toast(activity, "成功提醒卖家发货");
				}
			});
			
			//代发货 申请退款
			viewHolder.myorderItemRefundTextView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(refundListener != null){
						refundListener.onMyOrderRefundItemClick(v, position);
						viewHolder.myorderItemRefundTextView.setClickable(false);
						viewHolder.myorderItemRefundTextView.setBackgroundResource(R.color.personal_refund_clicked);
					}
				}
			});
			
			//代收款客服
			viewHolder.myorderItemDaiShouHuoRobotTextView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(activity,KfLoginActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					activity.startActivity(intent);
				}
			});
			//确认收货
			viewHolder.myorderItemQueRenShouHuoTextView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(confirmListener != null){
						confirmListener.onMyOrderConfirmItemClick(v, position);
					}
				}
			});
			
			//代评价
			viewHolder.myorderItemEvaluationTextView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(evaluationClickListener != null){
						evaluationClickListener.onMyOrderEvaluationClick(v, position);
					}
					/*
					Intent intent = new Intent(activity,EvaluationActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra(Constants.ORDERID, myOrder.getOrderId());
					intent.putExtra(Constants.HOMEPAGE_PRODUCT_NAME, myOrder.getGoodsname());
					intent.putExtra(Constants.HOMEPAGE_PRODUCT_THUMB,myOrder.getGoodsthumb());
					intent.putExtra(Constants.HOMEPAGE_PRODUCT_PRICE,myOrder.getOrderamount());
					activity.startActivityForResult(intent, 0);
					*/
				}
			});
		}
		return convertView;
	}
	
	
	class ViewHolder {
		ImageView myorderItemImageView;
		LinearLayout myorderItemDaiFuKuanLinearLayout,myorderItemDaiShouHuoLinearLayout,
				myorderItemDaiFaHuoLinearLayout, myorderItemRefundLinearLayout;
		TextView myorderItemNameTextView, myorderItemPriceValueTextView,
				myorderItemYuanAndUnitTextView, myorderItemNumberTextView,
				myorderItemNumberUnitTextView, myorderItemAmountsTextView,
				myorderItemAmountsValueTextView, myorderItemStateTextView,
				myorderItemDaiFuKuanRobotTextView,myorderItemRefundTextView,
				myorderItemQueRenShouHuoTextView,
				myorderItemCancelOrderTextView, myorderItemPayTextView,
				myorderItemDaiFaHuoRobotTextView, myorderItemRemindTextView,
				myorderItemDaiShouHuoRobotTextView,
				myorderItemEvaluationTextView, myorderItemRefundValueTextView;
	}
	
	/**
     *  取消订单
     */
    private onMyOrderCancelItemClickListener mListener = null;
    
    public void setOnMyOrderCancelItemClickListener(onMyOrderCancelItemClickListener listener){
    	mListener = listener;
    }

    public interface onMyOrderCancelItemClickListener {
        void onMyOrderCancelItemClick(View v, int position);
    }
    
    /**
     *  申请退款
     */
    private onMyOrderRefundItemClickListener refundListener = null;
    
    public void setOnMyOrderRefundItemClick(onMyOrderRefundItemClickListener listener){
    	refundListener = listener;
    }
    
    public interface onMyOrderRefundItemClickListener {
    	void onMyOrderRefundItemClick(View v, int position);
    }
    
    /**
     *  确认收货
     */
    private onMyOrderConfirmItemClickListener confirmListener = null;
    
    public void setOnMyOrderConfirmItemClickListener(onMyOrderConfirmItemClickListener listener){
    	confirmListener = listener;
    }

    public interface onMyOrderConfirmItemClickListener {
        void onMyOrderConfirmItemClick(View v, int position);
    }
    
    /**
     *  待评价
     */
  private onMyOrderEvaluationClickListener evaluationClickListener = null;
    
  public void setOnMyOrderEvaluationClickListener(onMyOrderEvaluationClickListener listener){
	evaluationClickListener = listener;
  }

  public interface onMyOrderEvaluationClickListener {
    void onMyOrderEvaluationClick(View v, int position);
  }

}
