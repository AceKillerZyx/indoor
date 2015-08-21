package com.indoorsy.frash.common.view.expandtab;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.indoorsy.frash.R;
import com.indoorsy.frash.homepage.adapter.ExpandTabTextAdapter;

@SuppressWarnings("unused")
public class ViewRight extends RelativeLayout implements ViewBaseAction{

	private ListView mListView;
	private final String[] items = new String[] { "综合排序", "最新上线", "销量", "好评", "价格从高到低", "价格从低到高" };//显示字段
	private OnSelectListener mOnSelectListener;
	private ExpandTabTextAdapter adapter;
	private String showText = "item1";
	private Context mContext;

	public String getShowText() {
		return showText;
	}

	public ViewRight(Context context) {
		super(context);
		init(context);
	}

	public ViewRight(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public ViewRight(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		mContext = context;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.common_expandtabview_left, this, true);
//		setBackgroundDrawable(getResources().getDrawable(R.drawable.choosearea_bg_right));
		mListView = (ListView) findViewById(R.id.listView);
		adapter = new ExpandTabTextAdapter(context, items, R.drawable.choose_item_check, R.drawable.homepage_choose_plate_item_selector);
		adapter.setTextSize(16);
		for (int i = 0; i < items.length; i++) {
			adapter.setSelectedPositionNoNotify(i);
			showText = items[i];
			break;
		}
		mListView.setAdapter(adapter);
		adapter.setOnItemClickListener(new ExpandTabTextAdapter.OnItemClickListener() {

			@Override
			public void onItemClick(View view, int position) {

				if (mOnSelectListener != null) {
					showText = items[position];
					mOnSelectListener.getValue(items[position]);
				}
			}
		});
	}

	public void setOnSelectListener(OnSelectListener onSelectListener) {
		mOnSelectListener = onSelectListener;
	}

	public interface OnSelectListener {
		public void getValue(String showText);
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void show() {
		
	}

}
