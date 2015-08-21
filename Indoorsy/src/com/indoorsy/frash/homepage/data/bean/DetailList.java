package com.indoorsy.frash.homepage.data.bean;

import java.io.Serializable;

/**
 * 
 * 商品详情-评论集合-评论图片
 *
 */
public class DetailList implements Serializable {

	private static final long serialVersionUID = 1L;

	private String evaimgImages;// 评论图

	/**
	 * 获取评论图片
	 * 
	 * @return
	 */
	public String getEvaimgImages() {
		return evaimgImages;
	}

	public void setEvaimgImages(String evaimgImages) {
		this.evaimgImages = evaimgImages;
	}

}
