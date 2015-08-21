package com.indoorsy.frash.common.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.indoorsy.frash.R;

public class CustomTitleView  extends RelativeLayout {

	private Context mContext;

	private TextView titleTextView, rightTextView;

	private ImageView leftImageView, rightImageView;

	public CustomTitleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
		init();
	}

	public CustomTitleView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CustomTitleView(Context context) {
		this(context, null);
	}

	private void init() {
		if(!isInEditMode()){
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			inflater.inflate(R.layout.common_title, this);
	
			titleTextView = (TextView) findViewById(R.id.titleTextView);
			leftImageView = (ImageView) findViewById(R.id.leftImageView);
			rightImageView = (ImageView) findViewById(R.id.rightImageView);
			rightTextView = (TextView) findViewById(R.id.rightTextView);
		}
	}

	public void setOnClickListener(OnClickListener listener) {
		leftImageView.setOnClickListener(listener);
		rightImageView.setOnClickListener(listener);
		rightTextView.setOnClickListener(listener);
	}

	public void setTitle(String title) {
		titleTextView.setText(title);
	}

	public void setTitle(int resid) {
		titleTextView.setText(resid);
	}

	public void setLeftImageVisibility(int visibility) {
		leftImageView.setVisibility(visibility);
	}

	public void setRightImageVisibility(int visibility) {
		rightImageView.setVisibility(visibility);
	}

	public void setLeftImageBg(int resid) {
		leftImageView.setBackgroundResource(resid);
	}

	public void setRightImageBg(int resid) {
		rightImageView.setBackgroundResource(resid);
	}

	public ImageView getLeftImageView() {
		return leftImageView;
	}

	public void setRightTextView(int resid) {
		rightTextView.setTextColor(Color.WHITE);
		rightTextView.setText(resid);
	}
	public void setRightTextViewVisibility(int visibility) {
		rightTextView.setVisibility(visibility);
	}

}
