package com.indoorsy.frash.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 自定义可适应ScrollView的ListView
 * 
 * 默认显示的首项是ListView,需要手动把ScrollView滚动至最顶端。
 * 	scrollview = (ScrollView) findViewById(R.id.ScrollView);
	scrollview.smoothScrollTo(0, 0);
	
 * @author Administrator
 *
 */
public class ListViewForScrollView extends ListView {

	public ListViewForScrollView(Context context) {
		super(context);
	}

	public ListViewForScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ListViewForScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	/**
	 * 重写该方法，达到使ListView适应ScrollView的效果
	 */
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
