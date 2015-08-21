package com.indoorsy.frash.common.view.pickdate;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;

import com.indoorsy.frash.R;

public class WheelMain {

	public static final String TAG = WheelMain.class.getSimpleName();
	private View view;
	private WheelView wv_year;
	private WheelView wv_month;
	private WheelView wv_day;
	private WheelView wv_hours;
	private WheelView wv_mins;
	public int screenheight;
	private boolean hasSelectTime;
	private int START_YEAR , END_YEAR ;
	private int START_MONTH , END_MONTH ;
	private int START_DAY , END_DAY ;

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public  int getSTART_YEAR() {
		return START_YEAR;
	}

	public  void setSTART_YEAR(int sTART_YEAR) {
		START_YEAR = sTART_YEAR;
	}

	public  int getEND_YEAR() {
		return END_YEAR;
	}

	public  void setEND_YEAR(int eND_YEAR) {
		END_YEAR = eND_YEAR;
	}

	public WheelMain(){
		
	}
	
	public WheelMain(View view) {
		super();
		this.view = view;
		hasSelectTime = false;
		setView(view);
	}

	public WheelMain(View view, boolean hasSelectTime) {
		super();
		this.view = view;
		this.hasSelectTime = hasSelectTime;
		setView(view);
	}

	public void initDateTimePicker(int year, int month, int day) {
		this.initDateTimePicker(year, month, day, 0, 0);
	}

	
	
	/**
	 * @Description: TODO 弹出日期时间选择器
	 */
	public void initDateTimePicker2(int year, int month, int day, int hhhhhh, int m) {
		
		// int year = calendar.get(Calendar.YEAR);
		// int month = calendar.get(Calendar.MONTH);
		// int day = calendar.get(Calendar.DATE);
		// 添加大小月月份并将其转换为list,方便之后的判断
		String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
		String[] months_little = { "4", "6", "9", "11" };

		final List<String> list_big = Arrays.asList(months_big);
		final List<String> list_little = Arrays.asList(months_little);

		// 年
		wv_year = (WheelView) view.findViewById(R.id.year);
		wv_year.setVisibility(View.GONE);
		wv_year.setAdapter(new NumericWheelAdapter(START_YEAR, END_YEAR));// 设置"年"的显示数据
		wv_year.setCyclic(true);// 可循环滚动
		wv_year.setLabel("年");// 添加文字
		wv_year.setCurrentItem(year - START_YEAR);// 初始化时显示的数据

		// 月
		wv_month = (WheelView) view.findViewById(R.id.month);
		wv_month.setAdapter(new NumericWheelAdapter(1, 12));
		wv_month.setCyclic(true);
		wv_month.setLabel("月");
		wv_month.setCurrentItem(month);

		// 日
		wv_day = (WheelView) view.findViewById(R.id.day);
		wv_day.setCyclic(true);
		// 判断大小月及是否闰年,用来确定"日"的数据
		if (list_big.contains(String.valueOf(month + 1))) {
			wv_day.setAdapter(new NumericWheelAdapter(1, 31));
		} else if (list_little.contains(String.valueOf(month + 1))) {
			wv_day.setAdapter(new NumericWheelAdapter(1, 30));
		} else {
			// 闰年
			if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
				wv_day.setAdapter(new NumericWheelAdapter(1, 29));
			else
				wv_day.setAdapter(new NumericWheelAdapter(1, 28));
		}
		wv_day.setLabel("日");
		wv_day.setCurrentItem(day - 1);

		wv_hours = (WheelView) view.findViewById(R.id.hour);
		wv_mins = (WheelView) view.findViewById(R.id.min);
		if (hasSelectTime) {
			wv_hours.setVisibility(View.VISIBLE);
			wv_mins.setVisibility(View.VISIBLE);

//			wv_hours.setAdapter(new NumericWheelAdapter());
			wv_hours.setAdapter(new DefineWheelAdapter());
			wv_hours.setCyclic(false);// 可循环滚动
			wv_hours.setLabel("时");// 添加文字
			wv_hours.setCurrentItem(hhhhhh);
			wv_mins.setAdapter(new NumericWheelAdapter(0, 59));
			wv_mins.setCyclic(true);// 可循环滚动
			wv_mins.setLabel("分");// 添加文字
			wv_mins.setCurrentItem(m);
			wv_mins.setVisibility(View.GONE);
		} else {
			wv_hours.setVisibility(View.GONE);
			wv_mins.setVisibility(View.GONE);
		}

		// 添加"年"监听
		OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				int year_num = newValue + START_YEAR;
				// 判断大小月及是否闰年,用来确定"日"的数据
				if (list_big
						.contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 31));
				} else if (list_little.contains(String.valueOf(wv_month
						.getCurrentItem() + 1))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 30));
				} else {
					if ((year_num % 4 == 0 && year_num % 100 != 0)
							|| year_num % 400 == 0)
						wv_day.setAdapter(new NumericWheelAdapter(1, 29));
					else
						wv_day.setAdapter(new NumericWheelAdapter(1, 28));
				}
			}
		};
		// 添加"月"监听
		OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				int month_num = newValue + 1;
				// 判断大小月及是否闰年,用来确定"日"的数据
				if (list_big.contains(String.valueOf(month_num))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 31));
				} else if (list_little.contains(String.valueOf(month_num))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 30));
				} else {
					if (((wv_year.getCurrentItem() + START_YEAR) % 4 == 0 && (wv_year
							.getCurrentItem() + START_YEAR) % 100 != 0)
							|| (wv_year.getCurrentItem() + START_YEAR) % 400 == 0)
						wv_day.setAdapter(new NumericWheelAdapter(1, 29));
					else
						wv_day.setAdapter(new NumericWheelAdapter(1, 28));
				}
			}
		};
		wv_year.addChangingListener(wheelListener_year);
		wv_month.addChangingListener(wheelListener_month);

		// 根据屏幕密度来指定选择器字体的大小(不同屏幕可能不同)
		int textSize = 0;
		if (hasSelectTime)
			textSize = (screenheight / 100) * 3;
		else
			textSize = (screenheight / 100) * 4;
		wv_day.TEXT_SIZE = textSize;
		wv_month.TEXT_SIZE = textSize;
		wv_year.TEXT_SIZE = textSize;
		wv_hours.TEXT_SIZE = textSize;
		wv_mins.TEXT_SIZE = textSize;

	}

	
	
	
	
	/**
	 * @Description: TODO 弹出日期时间选择器
	 */
	@SuppressWarnings("static-access")
	@SuppressLint("SimpleDateFormat")
	public void initDateTimePicker(int year, int month, int day, int h, int m) {
		
		/*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");   
			Date date1第一天,date7第七天;
			try {
				date1 = sdf.parse(year + "-" + (month + 1)+ "-" + day + " " + h);//第一天的时间
				Calendar calendar7 = new GregorianCalendar();  
				calendar7.setTime(date1);    
				calendar7.add(calendar7.DATE,7);//把日期往后增加7天.整数往后推,负数往前移动 
				date7 = calendar7.getTime();   //这个时间就是日期往后推7天的结果 
				String strDate7 = sdf.format(date7);
				Log.e(TAG, "第一天" + year + "-" + (month + 1)+ "-" + day );
				Log.e(TAG, "第七天 的String = " + strDate7);
				
				START_YEAR = year;
				START_MONTH = month + 1;
				START_DAY = day;
				
				END_YEAR = Integer.parseInt(strDate7.substring(0, 4));
				END_MONTH = Integer.parseInt(strDate7.substring(5, 7));
				END_DAY = Integer.parseInt(strDate7.substring(8, 10));
				
				
				
				
			} catch (ParseException e) {
				e.printStackTrace();
			}*/

		
		// int year = calendar.get(Calendar.YEAR);
		// int month = calendar.get(Calendar.MONTH);
		// int day = calendar.get(Calendar.DATE);
		// 添加大小月月份并将其转换为list,方便之后的判断
		String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
		String[] months_little = { "4", "6", "9", "11" };

		final List<String> list_big = Arrays.asList(months_big);
		final List<String> list_little = Arrays.asList(months_little);

		// 年
		wv_year = (WheelView) view.findViewById(R.id.year);
		wv_year.setAdapter(new NumericWheelAdapter(START_YEAR, END_YEAR));// 设置"年"的显示数据
		wv_year.setCyclic(false);// 可循环滚动
		wv_year.setLabel("年");// 添加文字
		wv_year.setCurrentItem(year - START_YEAR);// 初始化时显示的数据

		// 月
		wv_month = (WheelView) view.findViewById(R.id.month);
//		wv_month.setAdapter(new NumericWheelAdapter(START_MONTH, END_MONTH));
		wv_month.setAdapter(new NumericWheelAdapter(1, 12));
		wv_month.setCyclic(true);
		wv_month.setLabel("月");
		wv_month.setCurrentItem(month);

		// 日
		wv_day = (WheelView) view.findViewById(R.id.day);
		wv_day.setCyclic(true);
		// 判断大小月及是否闰年,用来确定"日"的数据
		if (list_big.contains(String.valueOf(month + 1))) {
//			wv_day.setAdapter(new NumericWheelAdapter(START_DAY, END_DAY));
			wv_day.setAdapter(new NumericWheelAdapter(1, 31));
		} else if (list_little.contains(String.valueOf(month + 1))) {
			wv_day.setAdapter(new NumericWheelAdapter(1, 30));
		} else {
			// 闰年
			if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
//				wv_day.setAdapter(new NumericWheelAdapter(START_DAY, END_DAY));//29天
				wv_day.setAdapter(new NumericWheelAdapter(1, 29));//29天
			else
//				wv_day.setAdapter(new NumericWheelAdapter(START_DAY, END_DAY));//28天
				wv_day.setAdapter(new NumericWheelAdapter(1, 28));//28天
		}
		wv_day.setLabel("日");
//		wv_day.setCurrentItem(day - START_DAY);
		wv_day.setCurrentItem(day - 1);

		wv_hours = (WheelView) view.findViewById(R.id.hour);
		wv_mins = (WheelView) view.findViewById(R.id.min);
		if (hasSelectTime) {
			wv_hours.setVisibility(View.VISIBLE);
			wv_mins.setVisibility(View.VISIBLE);

			wv_hours.setAdapter(new DefineWheelAdapter());//当前 天 ，小时
			wv_hours.setCyclic(false);// 可循环滚动
			wv_hours.setLabel("时");// 添加文字
			wv_hours.setCurrentItem(h);

//			wv_mins.setAdapter(new NumericWheelAdapter(0, 59));
//			wv_mins.setCyclic(true);// 可循环滚动
//			wv_mins.setLabel("分");// 添加文字
//			wv_mins.setCurrentItem(m);
		} else {
			wv_hours.setVisibility(View.GONE);
			wv_mins.setVisibility(View.GONE);
		}

		// 添加"年"监听
		OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				int year_num = newValue + START_YEAR;
				// 判断大小月及是否闰年,用来确定"日"的数据
				if (list_big.contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
					wv_day.setAdapter(new NumericWheelAdapter(START_DAY, END_DAY));
				} else if (list_little.contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
					wv_day.setAdapter(new NumericWheelAdapter(START_DAY, END_DAY));
				} else {
					if ((year_num % 4 == 0 && year_num % 100 != 0)
							|| year_num % 400 == 0)
						wv_day.setAdapter(new NumericWheelAdapter(START_DAY, END_DAY));
					else
						wv_day.setAdapter(new NumericWheelAdapter(START_DAY, END_DAY));
				}
			}
		};
		// 添加"月"监听
		OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				int month_num = newValue + 1;
				// 判断大小月及是否闰年,用来确定"日"的数据
				if (list_big.contains(String.valueOf(month_num))) {
					wv_day.setAdapter(new NumericWheelAdapter(START_DAY, END_DAY));
				} else if (list_little.contains(String.valueOf(month_num))) {
					wv_day.setAdapter(new NumericWheelAdapter(START_DAY, END_DAY));
				} else {
					if (((wv_year.getCurrentItem() + START_YEAR) % 4 == 0 && (wv_year
							.getCurrentItem() + START_YEAR) % 100 != 0)
							|| (wv_year.getCurrentItem() + START_YEAR) % 400 == 0)
						wv_day.setAdapter(new NumericWheelAdapter(START_DAY, END_DAY));
					else
						wv_day.setAdapter(new NumericWheelAdapter(START_DAY, END_DAY));
				}
			}
		};
		wv_year.addChangingListener(wheelListener_year);
		wv_month.addChangingListener(wheelListener_month);

		// 根据屏幕密度来指定选择器字体的大小(不同屏幕可能不同)
		int textSize = 0;
		if (hasSelectTime)
			textSize = (screenheight / 100) * 3;
		else
			textSize = (screenheight / 100) * 4;
		wv_day.TEXT_SIZE = textSize;
		wv_month.TEXT_SIZE = textSize;
		wv_year.TEXT_SIZE = textSize;
		wv_hours.TEXT_SIZE = textSize;
		wv_mins.TEXT_SIZE = textSize;

	}

	public String getTime() {
		StringBuffer sb = new StringBuffer();
		Calendar calendar=Calendar.getInstance();
		int years=calendar.get(Calendar.YEAR);
		Log.e("自己设置的年为:",""+years);
		if (!hasSelectTime) {
			//append((wv_year.getCurrentItem() + START_YEAR)).append("-")
				  sb.append(years).append("-")
					.append((wv_month.getCurrentItem() + START_MONTH)).append("-")
					.append((wv_day.getCurrentItem() + START_DAY));
		}
		else {
			String text_hour = "8";
			switch (wv_hours.getCurrentItem()) {
			case 0:
				text_hour = "8"; 
				break;
			case 1:
				text_hour = "10"; 
				break;
			case 2:
				text_hour = "16"; 
				break;
			case 3:
				text_hour = "18"; 
				break;

			} 
			sb
			//.append((wv_year.getCurrentItem() + START_YEAR)).append("-")
			.append(years).append("-")
			.append((wv_month.getCurrentItem() + START_MONTH+1)).append("-")
			.append((wv_day.getCurrentItem() + START_DAY+1)).append(" ")
					.append(text_hour).append(":00");
//					.append(wv_mins.getCurrentItem());
		}
		return sb.toString();
	}
	
}