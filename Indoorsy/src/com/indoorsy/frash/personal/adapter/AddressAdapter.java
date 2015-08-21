package com.indoorsy.frash.personal.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.indoorsy.frash.Constants;
import com.indoorsy.frash.R;
import com.indoorsy.frash.personal.activity.AddAddressActivity;
import com.indoorsy.frash.personal.activity.ManageAddressActivity;
import com.indoorsy.frash.personal.data.bean.Address;
import com.indoorsy.frash.util.ArrayUtil;

public class AddressAdapter  extends BaseAdapter {
	
	private Context context;
	private int addressid;
	private List<Address> addresslist;
	private int position;
	private String[] longClickMenu = new String[] { "设为收货地址", "删除" };
	public AddressAdapter(Context context ){
		super();
		this.context = context;
		
	}
	
	public void resetData(List<Address> addresslist ){
		this.addresslist = addresslist;
		this.notifyDataSetChanged();
	}
	
	public void resetDefault(List<Address> addresslist , int position){
		this.position = position;
		this.addresslist = addresslist;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return ArrayUtil.isEmptyList(addresslist) ? 0 : addresslist.size();
	}

	@Override
	public Object getItem(int position) {
		return ArrayUtil.isEmptyList(addresslist) ? null : addresslist.get(position);
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
			convertView = (View) inflater.inflate(R.layout.personal_address_list_item, null);
			viewHolder.id=position;
			viewHolder.OutLineLinearLayout = (LinearLayout)convertView.findViewById(R.id.OutLineLinearLayout);
			viewHolder.personalDefaultAddressTextView = (TextView)convertView.findViewById(R.id.personalDefaultAddressTextView);
			
			viewHolder.personalConsigneeNameTextView = (TextView)convertView.findViewById(R.id.personalConsigneeNameTextView);
			viewHolder.personalConsigneePhoneTextView = (TextView)convertView.findViewById(R.id.personalConsigneePhoneTextView);
			viewHolder.personalAddressValueTextView = (TextView)convertView.findViewById(R.id.personalAddressValueTextView);
			viewHolder.personalManageAddressUpdateAddressImageView = (ImageView)convertView.findViewById(R.id.personalManageAddressUpdateAddressImageView);
			viewHolder.personalAddressDelect=(ImageButton)convertView.findViewById(R.id.personalAddressDelect);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		final Address address = addresslist.get(position);
		if(address != null){
			viewHolder.personalConsigneeNameTextView.setText(address.getAddConsignee());
			viewHolder.personalConsigneePhoneTextView.setText(address.getAddTel());
			viewHolder.personalAddressValueTextView.setText(address.getAddAdderss());
			viewHolder.personalAddressDelect.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					//删除地址操作				
					if(delect!=null){
						delect.showDelect(viewHolder.id);
					}				
				}
			});
			viewHolder.personalManageAddressUpdateAddressImageView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					//修改地址
					Intent intentAddAddress = new Intent(context , AddAddressActivity.class);
					intentAddAddress.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS); 
					intentAddAddress.putExtra(Constants.ADDRESS, ManageAddressActivity.UPDATE_ADDRESS);
					intentAddAddress.putExtra(Constants.ADDERSSID, addresslist.get(position).getAddId()+"");
					Log.e("地址", "传递的addressid = " + addressid);
					Bundle bundle = new Bundle();
					bundle.putSerializable(Constants.PERSONAL_ADDRESS, address);
					intentAddAddress.putExtras(bundle);
					context.startActivity(intentAddAddress);
				}
			});
			
		}
		
		if (position == this.position) {
			Log.e("============", "存放position = " + position);
			viewHolder.OutLineLinearLayout.setBackgroundResource(R.color.homepage_green_money);
			viewHolder.personalDefaultAddressTextView.setVisibility(View.VISIBLE);
			
		} else {
			viewHolder.OutLineLinearLayout.setBackgroundResource(R.color.white);
			viewHolder.personalDefaultAddressTextView.setVisibility(View.INVISIBLE);
		}
		
		
		return convertView;
	}
	
	class ViewHolder {
		int id;
		LinearLayout OutLineLinearLayout;
		TextView 
		personalDefaultAddressTextView,
		personalConsigneeNameTextView,
		personalConsigneePhoneTextView,
		personalAddressValueTextView;
		ImageView personalManageAddressUpdateAddressImageView;
		ImageButton personalAddressDelect;
	}
	
	public interface Delect{
		public void showDelect(int id);
	}
	public Delect delect;
	public void setDelect(Delect delect){
		this.delect=delect;
	}

	
}
