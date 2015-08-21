package com.indoorsy.frash.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.indoorsy.frash.Constants;
import com.indoorsy.frash.R;
import com.indoorsy.frash.ReleaseConfigure;
import com.indoorsy.frash.commodity.activity.FirmOrderActivity;
import com.indoorsy.frash.common.data.bean.CommonResult;
import com.indoorsy.frash.common.view.CommonProgressDialog;
import com.indoorsy.frash.common.view.CustomTitleView;
import com.indoorsy.frash.common.view.ListViewForScrollView;
import com.indoorsy.frash.http.core.HttpListener;
import com.indoorsy.frash.http.core.HttpParam;
import com.indoorsy.frash.http.core.HttpResult;
import com.indoorsy.frash.http.core.HttpTask;
import com.indoorsy.frash.shopping.cart.adapter.ShoppingcarListAdapter;
import com.indoorsy.frash.shopping.cart.adapter.ShoppingcarListAdapter.CartCancelClickListener;
import com.indoorsy.frash.shopping.cart.adapter.ShoppingcarListAdapter.CartNumClickListener;
import com.indoorsy.frash.shopping.cart.adapter.ShoppingcarListAdapter.onCheckedChanged;
import com.indoorsy.frash.shopping.cart.data.bean.CartGood;
import com.indoorsy.frash.util.CommonDataUtil;
import com.indoorsy.frash.util.StringUtil;
import com.indoorsy.frash.util.ToastUtil;
/**
 * 购物车
 */
/**
 * 购物车
 */
@SuppressLint("NewApi")
public class ShoppingCartFragment extends Fragment implements HttpListener,OnClickListener, onCheckedChanged{

	public static final String TAG = ShoppingCartFragment.class.getSimpleName();
	
	private CommonProgressDialog pd;

	private CustomTitleView customTitleView;
	private ScrollView scrollView;
	private RelativeLayout ShoppingcarBottomRelativeLayout;
	private TextView ShoppingCartNoneTextView;
	
	// 购物车列表
	private List<CartGood> cartlist ;//点击结算传递的商品列表
	private HttpTask deleteCartHttpTask;
	private int cartId = 0;
	private boolean[] is_choice;
	private List<CartGood> cartGoodlist;
	private HttpTask searchShopCarHttpTask;
	private ListViewForScrollView ShoppingCarListView;
	private ShoppingcarListAdapter shoppingcarListAdapter;
	
	// 底栏
	private Double totalpay = 0.0;
	private CheckBox ShoppingcarAllCheckedCheckBox;
	private TextView ShoppingcarConfirmPayTextView,ShoppingcarTotalPayTextView ;
	private Drawable all_normal;
	private Drawable all_checked;
	
	private List<Integer> positionList;
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		return inflater.inflate(R.layout.shopping_cart_fragment, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		//顶栏
		ShoppingCartNoneTextView = (TextView) getView().findViewById(R.id.ShoppingCartNoneTextView);
		ShoppingcarBottomRelativeLayout =  (RelativeLayout) getView().findViewById(R.id.ShoppingcarBottomRelativeLayout);
		scrollView = (ScrollView) getView().findViewById(R.id.ScrollView);
		customTitleView = (CustomTitleView)getView().findViewById(R.id.customTitleView);
        customTitleView.setTitle(R.string.home_shopping_cart);
		
		pd = new CommonProgressDialog(getActivity());
		pd.loadDialog();
		
		// 购物车列表
		cartlist = new ArrayList<CartGood>() ;
		ShoppingCarListView = (ListViewForScrollView) getView().findViewById(R.id.ShoppingCarListView);
		shoppingcarListAdapter = new ShoppingcarListAdapter(getActivity());
		shoppingcarListAdapter.setOnCheckedChanged(this);
		ShoppingCarListView.setAdapter(shoppingcarListAdapter);
		
		// 底栏
		all_normal = getResources().getDrawable(R.drawable.commodity_firm_order_print_normal);  
		all_checked = getResources().getDrawable(R.drawable.commodity_firm_order_print_check);  
		all_normal.setBounds(0, 0, all_normal.getMinimumWidth(), all_normal.getMinimumHeight()); 
		all_checked.setBounds(0, 0, all_checked.getMinimumWidth(), all_checked.getMinimumHeight()); 
		ShoppingcarAllCheckedCheckBox = (CheckBox) getView().findViewById(R.id.ShoppingcarAllCheckedCheckBox);
		ShoppingcarAllCheckedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// 记录列表每一行的选中状态数量
				int isChoice_all = 0;
				if (arg1) {
					// 设置全选
					for (int i = 0; i < cartGoodlist.size(); i++) {
						// 如果选中了全选，那么就将列表的每一行都选中
						((CheckBox) (ShoppingCarListView.getChildAt(i)).findViewById(R.id.ShoppingcarIsCheckedCheckBox)).setChecked(true);
					}
				} else {
					// 设置全部取消
					for (int i = 0; i < cartGoodlist.size(); i++) {
						// 判断列表每一行是否处于选中状态，如果处于选中状态，则计数+1
						if (((CheckBox) (ShoppingCarListView.getChildAt(i)).findViewById(R.id.ShoppingcarIsCheckedCheckBox)).isChecked()) {
							// 计算出列表选中状态的数量
							isChoice_all += 1;
						}
					}
					// 判断列表选中数是否等于列表的总数，如果等于，那么就需要执行全部取消操作
					if (isChoice_all == cartGoodlist.size()) {
						// 如果没有选中了全选，那么就将列表的每一行都不选
						for (int i = 0; i < cartGoodlist.size(); i++) {
							// 列表每一行都取消
							((CheckBox) (ShoppingCarListView.getChildAt(i)).findViewById(R.id.ShoppingcarIsCheckedCheckBox)).setChecked(false);
						}
					}
				}
				
			}
		});
		ShoppingcarConfirmPayTextView = (TextView) getView().findViewById(R.id.ShoppingcarConfirmPayTextView);
		ShoppingcarTotalPayTextView = (TextView) getView().findViewById(R.id.ShoppingcarTotalPayTextView);
		ShoppingcarConfirmPayTextView.setOnClickListener(this);
		
		//查询购物车
		searchShopCar();
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.ShoppingcarConfirmPayTextView:
				//传递多选订单
				Intent cartintent = new Intent(getActivity(),FirmOrderActivity.class);
				/*for (int i = 0; i < is_choice.length; i++) {
					if (is_choice[i]) {
						//选择的了订单
						cartlist.add(cartGoodlist.get(i));
						Log.e(TAG, "选择的订单位置 = " + i);
					}
				}*/
				if (cartlist.size() >= 2) {
					//多件商品
					cartintent.putExtra(Constants.CART_TYPE_LIST, FirmOrderActivity.TYPE_CART_MORE);
					cartintent.putExtra(Constants.CART_TYPE_LIST_PRICE, totalpay + "");
					cartintent.putParcelableArrayListExtra(Constants.CART_GOODSLSIT, (ArrayList<? extends Parcelable>)cartlist );
					startActivity(cartintent);
					Log.e(TAG, "===========多件商品 = " + (ArrayList<? extends Parcelable>)cartlist);
				} else if(cartlist.size() == 1){
					//单件商品
					cartintent.putExtra(Constants.CART_TYPE_LIST, FirmOrderActivity.TYPE_CART_ONE);
					cartintent.putExtra(Constants.CART_TYPE_LIST_PRICE, totalpay+"");
					CartGood cartgood = cartlist.get(0);
					cartintent.putExtra(Constants.HOMEPAGE_PRODUCT_DELID, "1");
					cartintent.putExtra(Constants.HOMEPAGE_PRODUCT_PRICE, cartgood.getCartGoodsPrice());
					cartintent.putExtra(Constants.HOMEPAGE_PRODUCT_UNIT, cartgood.getCartUnit());
					cartintent.putExtra(Constants.HOMEPAGE_PRODUCT_ID, cartgood.getGoodsid());
					cartintent.putExtra(Constants.HOMEPAGE_PRODUCT_NUM,cartgood.getCartNumber());
					cartintent.putExtra(Constants.HOMEPAGE_PRODUCT_NAME, cartgood.getGoodsName());
					cartintent.putExtra(Constants.HOMEPAGE_PRODUCT_DESC, cartgood.getGoodsBrief());
					cartintent.putExtra(Constants.HOMEPAGE_PRODUCT_THUMB,cartgood.getGoodsImg());
					cartintent.putExtra(Constants.HOMEPAGE_PRODUCT_SIZE, /*size*/"1~2斤");//鱼类特有
					Log.e(TAG, "===========单件商品 ======= " + cartgood);
					Log.e(TAG, "传递的配送id = " + 1);
					Log.e(TAG, "传递的单价 = " + cartgood.getCartGoodsPrice());
					Log.e(TAG, "传递的单位 = " + cartgood.getCartUnit());
					Log.e(TAG, "传递的数量 = " + cartgood.getCartNumber());
					Log.e(TAG, "鱼类传递的规格 = " + "1~2斤");//默认购物车为1-2
					startActivity(cartintent);
				} else {
					//一个都不选
					ToastUtil.toast(getActivity(), "请先选择商品");
				}
				break;
		}
		
	}
	
	private void searchShopCar() {
		HashMap<String, String> paramMap = CommonDataUtil.getCommonParams(getActivity());
		HttpParam httpParam = new HttpParam(ReleaseConfigure.SHOPPING_VALUE_TYPE_SEARCH, false); // GET
		searchShopCarHttpTask = new HttpTask(getActivity(), this);
		httpParam.setParams(paramMap);
		searchShopCarHttpTask.execute(httpParam);
		pd.loadDialog();
	}
	
	
	
	
	@Override
	public void noNet(HttpTask task) {
		pd.removeDialog();		
	}

	@Override
	public void noData(HttpTask task, HttpResult result) {
		pd.removeDialog();	
		
	}

	@Override
	public void onLoadFailed(HttpTask task, HttpResult result) {
		pd.removeDialog();	
		
	}

	@Override
	public void onLoadFinish(HttpTask task, HttpResult result) {
		pd.removeDialog();	
		Log.e(TAG, result.getData());
		if (result != null && !StringUtil.isEmpty(result.getData()) && StringUtil.isGoodJson(result.getData()) && getActivity() !=null && !getActivity().isFinishing() ) {
			CommonResult commonResult = JSON.parseObject(result.getData(),CommonResult.class);
			if (null != commonResult) {
				if (task == searchShopCarHttpTask) {
					if (commonResult.validate()) {
						JSONArray jsonArray = JSON.parseArray(commonResult.getData());
						if (null != jsonArray && jsonArray.size() > 0) {
							initShopCar(jsonArray);
						}
					}
					else {
						ShoppingCartNoneTextView.setVisibility(View.VISIBLE);
						scrollView.setVisibility(View.GONE);
						ShoppingcarBottomRelativeLayout.setVisibility(View.GONE);
					}
				}
				if (task == deleteCartHttpTask) {
					searchShopCar();
				} else {
//					ToastUtil.toast(getActivity(), commonResult.getErrMsg());
				}
			}
		}
	}

	private void initShopCar(JSONArray jsonArray) {
		ShoppingCartNoneTextView.setVisibility(View.GONE);
		scrollView.setVisibility(View.VISIBLE);
		ShoppingcarBottomRelativeLayout.setVisibility(View.VISIBLE);
		cartGoodlist = new ArrayList<CartGood>();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			CartGood cartGood = JSONObject.toJavaObject(jsonObject, CartGood.class);
			cartGoodlist.add(cartGood);
		}
		is_choice = new boolean[cartGoodlist.size()];
		if (cartGoodlist != null && cartGoodlist.size() > 0) {
			//点击删除
			shoppingcarListAdapter.resetData(cartGoodlist);
			shoppingcarListAdapter.setOnCartCancelClick(new CartCancelClickListener() {
				
				@Override
				public void onCartCancelClick(View view, int position) {
					cartId = cartGoodlist.get(position).getCartId();
					showDelete();
				}
			});
			//选中圆点才能点击加减
			
			shoppingcarListAdapter.setOnCartNumClick(new CartNumClickListener() {
				
				@Override
				public void onCartNumClick(View view, int position, Boolean isAdd) {
					if (positionList != null && positionList.size() >0) {
						for (int i = 0; i < positionList.size(); i++) {
							if (positionList.get(i) == position) {
								Log.e(TAG, "这个点被选中，可以加减数量价格");
								if (isAdd) {
									totalpay += (cartGoodlist.get(position).getCartGoodsPrice() );	//数量+1
									Log.e(TAG, "+1 总计 = " + totalpay);
								} else {
									if ((cartGoodlist.get(position).getCartNumber() - 1) >= 1) {
										totalpay -= (cartGoodlist.get(position).getCartGoodsPrice() );	//数量-1;
										Log.e(TAG, "-1 总计 = " + totalpay);
									}
								}
								java.text.DecimalFormat  df = new java.text.DecimalFormat("#.##");
								ShoppingcarTotalPayTextView.setText("总计：￥" + df.format(totalpay) + "元") ;
							} else {
								Log.e(TAG, "这个点未被选中");
							}
						}
					}
				}
			});
		}
	}

	protected void showDelete() {
		new AlertDialog.Builder(getActivity())
		.setMessage("确定删除此商品？")
		.setPositiveButton("删除", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				//删除购物车
				deleteCart();
			}
		})
		.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				arg0.dismiss();
			}
		})
		.create().show();
	}
		

	protected void deleteCart() {
		HashMap<String, String> paramMap = CommonDataUtil.getCommonParams(getActivity());
		HttpParam httpParam = new HttpParam(ReleaseConfigure.SHOPPING_VALUE_TYPE_DELETE, false); // GET
		deleteCartHttpTask = new HttpTask(getActivity(), this);
		paramMap.put(Constants.CART_ID, cartId+"");
		httpParam.setParams(paramMap);
		deleteCartHttpTask.execute(httpParam);
		
	}

	@Override
	public void getChoiceData(int position, boolean isChoice) {
		//得到点击的哪一行
		positionList = new ArrayList<Integer>();
		Log.e(TAG, "得到点击的哪一行ischose = " + isChoice);
		if (isChoice) {
			if (cartGoodlist != null && cartGoodlist.size() > 0) {
				cartlist.add(cartGoodlist.get(position));
				Log.e(TAG, "添加的订单位置 = " + position);
				Log.e(TAG, "添加的订单cartlist.size() = " + cartlist.size());
				Log.e(TAG, "添加的订单GoodsPrice = " + cartGoodlist.get(position).getCartGoodsPrice());
				Log.e(TAG, "添加的订单CartNumber = " + cartGoodlist.get(position).getCartNumber());
				totalpay += (cartGoodlist.get(position).getCartGoodsPrice() * cartGoodlist.get(position).getCartNumber());	//数量×价格
				java.text.DecimalFormat  df = new java.text.DecimalFormat("#.##");
				ShoppingcarTotalPayTextView.setText("总计：￥" + df.format(totalpay) + "元") ;
			}
		} else {
			if (cartGoodlist != null && cartGoodlist.size() > 0) {
				cartlist.remove(cartGoodlist.get(position));
				Log.e(TAG, "删除的订单位置 = " + position);
//				positionList.remove(position);//订单选中的位置列表，用于记录点击加减号的时候是否选中了这一行，若选中则点击会触发价格变动
				Log.e(TAG, "删除的订单cartlist.size() = " + cartlist.size());
				Log.e(TAG, "删除的订单GoodsPrice = " + cartGoodlist.get(position).getCartGoodsPrice());
				Log.e(TAG, "删除的订单CartNumber = " + cartGoodlist.get(position).getCartNumber());
				totalpay -= (cartGoodlist.get(position).getCartGoodsPrice() * cartGoodlist.get(position).getCartNumber());	//数量×价格;
				java.text.DecimalFormat df = new java.text.DecimalFormat("#.##");
				ShoppingcarTotalPayTextView.setText("总计：￥" + df.format(totalpay) + "元") ;
			}
		}
		// 记录列表处于选中状态的数量
		int num_choice = 0;
		for (int i = 0; i < cartGoodlist.size(); i++) {
			// 判断列表中每一行的选中状态，如果是选中，计数加1
			if (null != ShoppingCarListView.getChildAt(i) && ((CheckBox) (ShoppingCarListView.getChildAt(i)).findViewById(R.id.ShoppingcarIsCheckedCheckBox)).isChecked()) {
				// 列表处于选中状态的数量+1
				num_choice += 1;
				is_choice[i]=true;
				positionList.add(i);//订单选中的位置列表，用于记录点击加减号的时候是否选中了这一行，若选中则点击会触发价格变动
				Log.e(TAG, "positionList的size = " + positionList.size());
			}
		}
		
		if (num_choice == 0) {
			totalpay = 0.0 ;
			ShoppingcarTotalPayTextView.setText("总计：￥" + totalpay + "元") ;
		}
		
		// 判断列表中的CheckBox是否全部选择
		if (num_choice == cartGoodlist.size()) {
			// 如果选中的状态数量=列表的总数量，那么就将全选设置为选中
			ShoppingcarAllCheckedCheckBox.setChecked(true);
		} else {
			// 如果选中的状态数量！=列表的总数量，那么就将全选设置为取消
			ShoppingcarAllCheckedCheckBox.setChecked(false);
		}
//		System.out.println("点击item的位置--->"+position);
	}

	
}
