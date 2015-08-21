package com.indoorsy.frash.common.view.expandtab;

import java.util.ArrayList;
import java.util.LinkedList;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.indoorsy.frash.R;
import com.indoorsy.frash.homepage.adapter.ExpandTabTextAdapter;

public class ViewMiddle extends LinearLayout implements ViewBaseAction {
	private ListView regionListView;
	private ListView plateListView;
	private ArrayList<String> groups;
	private LinkedList<String> childrenItem = new LinkedList<String>();
	private SparseArray<LinkedList<String>> children;
	private ExpandTabTextAdapter plateListViewAdapter;
	private ExpandTabTextAdapter earaListViewAdapter;
	private OnSelectListener mOnSelectListener;
	private int tEaraPosition = 0;
	private int tBlockPosition = 0;
	private String showString = "不限";

	public ViewMiddle(Context context) {
		super(context);
		init(context);
	}

	public ViewMiddle(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public void updateShowText(String showArea, String showBlock) {
		if (showArea == null || showBlock == null) {
			return;
		}
		for (int i = 0; i < groups.size(); i++) {
			if (groups.get(i).equals(showArea)) {
				earaListViewAdapter.setSelectedPosition(i);
				childrenItem.clear();
				if (i < children.size()) {
					childrenItem.addAll(children.get(i));
				}
				tEaraPosition = i;
				break;
			}
		}
		for (int j = 0; j < childrenItem.size(); j++) {
			if (childrenItem.get(j).replace("不限", "").equals(showBlock.trim())) {
				plateListViewAdapter.setSelectedPosition(j);
				tBlockPosition = j;
				break;
			}
		}
		setDefaultSelect();
	}

	@SuppressWarnings("deprecation")
	private void init(Context context) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.common_expandtabview_middle, this, true);
		regionListView = (ListView) findViewById(R.id.commonExpandTabOneListView);
		plateListView = (ListView) findViewById(R.id.commonExpandTabTwoListView);
		setBackgroundDrawable(getResources().getDrawable(R.drawable.choosearea_bg_left));
	}
	
	public void setAdapter(Context context,ArrayList<String> groups,final SparseArray<LinkedList<String>> children){
		earaListViewAdapter = new ExpandTabTextAdapter(context, groups,
				R.drawable.choose_item_selected,
				R.drawable.homepage_choose_plate_item_selector);
		earaListViewAdapter.setTextSize(15);
		earaListViewAdapter.setSelectedPositionNoNotify(tEaraPosition);
		regionListView.setAdapter(earaListViewAdapter);
		earaListViewAdapter.setOnItemClickListener(new ExpandTabTextAdapter.OnItemClickListener() {
					@Override
					public void onItemClick(View view, int position) {
						if (position < children.size()) {
							childrenItem.clear();
							childrenItem.addAll(children.get(position));
							plateListViewAdapter.notifyDataSetChanged();
						}
					}
				});
		if (tEaraPosition < children.size())
			childrenItem.addAll(children.get(tEaraPosition));
		plateListViewAdapter = new ExpandTabTextAdapter(context, childrenItem,R.drawable.choose_item_right,R.drawable.homepage_choose_plate_item_selector);
		plateListViewAdapter.setTextSize(15);
		plateListViewAdapter.setSelectedPositionNoNotify(tBlockPosition);
		plateListView.setAdapter(plateListViewAdapter);
		plateListViewAdapter.setOnItemClickListener(new ExpandTabTextAdapter.OnItemClickListener() {
					@Override
					public void onItemClick(View view, final int position) {
						showString = childrenItem.get(position);
						if (mOnSelectListener != null) {
							mOnSelectListener.getValue(showString);
						}
					}
				});
		if (tBlockPosition < childrenItem.size())
			showString = childrenItem.get(tBlockPosition);
		if (showString.contains("不限")) { 
			showString = showString.replace("不限", "");
		}
		setDefaultSelect();
	}

	public void setDefaultSelect() {
		regionListView.setSelection(tEaraPosition);
		plateListView.setSelection(tBlockPosition);
	}

	public String getShowText() {
		return showString;
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
