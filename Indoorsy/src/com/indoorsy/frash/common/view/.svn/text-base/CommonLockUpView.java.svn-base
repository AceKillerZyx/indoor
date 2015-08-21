package com.indoorsy.frash.common.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

public class CommonLockUpView extends LinearLayout {

	protected boolean goneOnTouch;

	protected View childView;

	protected int childViewAnim;

	public CommonLockUpView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (isGoneOnTouch() && event.getAction() == MotionEvent.ACTION_UP)
			invisible();
		return true;
	}

	public boolean isVisible() {
		return getVisibility() == VISIBLE;
	}

	public void invisible() {
		if (null != childView && childViewAnim > 0) {
			Animation animation = AnimationUtils.loadAnimation(getContext(),
					childViewAnim);
			childView.startAnimation(animation);
			postDelayed(new Runnable() {
				@Override
				public void run() {
					setVisibility(GONE);
				}
			}, animation.getDuration());
		} else {
			setVisibility(GONE);
		}
	}

	public boolean isGoneOnTouch() {
		return goneOnTouch;
	}

	public void setGoneOnTouch(boolean goneOnTouch) {
		this.goneOnTouch = goneOnTouch;
	}

	public void setChildViewAndAnim(View child, int anim) {
		childView = child;
		childViewAnim = anim;
	}

}
