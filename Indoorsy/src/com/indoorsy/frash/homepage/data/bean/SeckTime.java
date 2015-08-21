package com.indoorsy.frash.homepage.data.bean;

import java.io.Serializable;

public class SeckTime implements Serializable {

	private static final long serialVersionUID = 1L;

	private String content;
	private String time;

	/**
	 * 获取 秒杀文字
	 * 
	 * @return
	 */
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * 获取 秒杀时间点
	 * 
	 * @return
	 */
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
