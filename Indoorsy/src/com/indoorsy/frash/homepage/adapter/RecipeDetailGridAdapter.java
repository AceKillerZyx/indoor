package com.indoorsy.frash.homepage.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.indoorsy.frash.R;
import com.indoorsy.frash.homepage.data.bean.RecipeLists;
import com.indoorsy.frash.util.ArrayUtil;

public class RecipeDetailGridAdapter extends BaseAdapter {

	@SuppressWarnings("unused")
	private static final String TAG = RecipeDetailGridAdapter.class.getSimpleName();

	private Context context;

	private List<RecipeLists> recipeListss;

	public RecipeDetailGridAdapter(Context context) {
		super();
		this.context = context;
	}

	public void resetData(List<RecipeLists> recipeListss) {
		this.recipeListss = recipeListss;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return ArrayUtil.isEmptyList(recipeListss) ? 0 : recipeListss.size();
	}

	@Override
	public Object getItem(int position) {
		return ArrayUtil.isEmptyList(recipeListss) ? null : recipeListss.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = (View) inflater.inflate(
					R.layout.homepage_recipe_detail_griditem, null);

			viewHolder.GridItemTextView = (TextView) convertView.findViewById(R.id.GridItemTextView);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		RecipeLists recipeLists = recipeListss.get(position);
		if (recipeLists != null) {
			viewHolder.GridItemTextView.setText(recipeLists.getStitle() +"\t"+ recipeLists.getScontent());
		}
		return convertView;
	}

	class ViewHolder {
		TextView GridItemTextView;
	}

}
