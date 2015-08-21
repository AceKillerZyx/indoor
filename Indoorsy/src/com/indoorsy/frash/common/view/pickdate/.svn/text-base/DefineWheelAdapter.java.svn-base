/*
 *  Copyright 2010 Yuri Kanivets
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.indoorsy.frash.common.view.pickdate;

import java.util.Calendar;

/**
 * 小时 Wheel adapter.
 */
public class DefineWheelAdapter implements WheelAdapter {
	
	int currentday ;
	int currenthour ;
	Calendar calendar = Calendar.getInstance();
	
	// Values
	private int[] values = new int[]{ 8, 10, 16, 18};
	
	// format
	private String format;
	
	/**
	 * Default constructor
	 */
	public DefineWheelAdapter() {
	}


	@Override
	public String getItem(int index) {
		if (index >= 0 && index < getItemsCount()) {
			int value = values[index];
			return format != null ? String.format(format, value) : Integer.toString(value);
		}
		return null;
	}

	@Override
	public int getItemsCount() {
		return 4;
	}
	
	@Override
	public int getMaximumLength() {
		int max = Math.max(Math.abs(18), Math.abs(8));
		int maxLen = Integer.toString(max).length();
		return maxLen;
	}
}
