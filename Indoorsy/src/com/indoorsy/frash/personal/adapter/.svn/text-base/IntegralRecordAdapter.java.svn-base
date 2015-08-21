package com.indoorsy.frash.personal.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.indoorsy.frash.R;
import com.indoorsy.frash.personal.data.bean.IntegralRecord;
import com.indoorsy.frash.util.ArrayUtil;

@SuppressLint("InflateParams") 
public class IntegralRecordAdapter  extends BaseAdapter {
	
	private Context context;

	private List<IntegralRecord> integralRecordList;
	
	public IntegralRecordAdapter(Context context){
		super();
		this.context = context;
	}
	
	public void resetData(List<IntegralRecord> integralRecordList){
		this.integralRecordList = integralRecordList;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return ArrayUtil.isEmptyList(integralRecordList) ? 0 : integralRecordList.size();
	}

	@Override
	public Object getItem(int position) {
		return ArrayUtil.isEmptyList(integralRecordList) ? null : integralRecordList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = (View) inflater.inflate(R.layout.personal_integralrecord_item, null);
			
			viewHolder.integralRecordItemLinearLayout = (LinearLayout)convertView.findViewById(R.id.integralRecordItemLinearLayout);
			viewHolder.integralRecordItemDetailTextView = (TextView)convertView.findViewById(R.id.integralRecordItemDetailTextView);
			viewHolder.integralRecordItemNumberTextView = (TextView)convertView.findViewById(R.id.integralRecordItemNumberTextView);
			viewHolder.integralRecordItemTimeTextView = (TextView)convertView.findViewById(R.id.integralRecordItemTimeTextView);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		if((position + 1)%2 == 0){
			viewHolder.integralRecordItemLinearLayout.setBackgroundColor(Color.WHITE);
		}else{
			viewHolder.integralRecordItemLinearLayout.setBackgroundColor(context.getResources().getColor(R.color.gray));
		}
		IntegralRecord integralRecord = integralRecordList.get(position);
		if(integralRecord != null){
			viewHolder.integralRecordItemDetailTextView.setText(integralRecord.getIrecordOrder());
			if(integralRecord.getIrecordCategory() == 1){
				viewHolder.integralRecordItemNumberTextView.setTextColor(context.getResources().getColor(R.color.homepage_red_seckill));
				viewHolder.integralRecordItemNumberTextView.setText("+" + integralRecord.getIrecordIntegral());
			}else{
				viewHolder.integralRecordItemNumberTextView.setTextColor(context.getResources().getColor(R.color.common_title_bg));
				viewHolder.integralRecordItemNumberTextView.setText("-" + integralRecord.getIrecordIntegral());
			}
			viewHolder.integralRecordItemTimeTextView.setText(integralRecord.getIrecordTime().substring(0, 10));
		}
		return convertView;
	}
	
	class ViewHolder {
		LinearLayout integralRecordItemLinearLayout;
		TextView integralRecordItemDetailTextView,
				integralRecordItemNumberTextView,
				integralRecordItemTimeTextView;
	}

}
