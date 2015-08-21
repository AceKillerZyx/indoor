package com.indoorsy.frash.homepage.data.bean;

import java.io.Serializable;

public class DetailLictr implements Serializable {

	private static final long serialVersionUID = 1L;

	private String comImages;
	private int comid;

	/**
	 * 获取商品详情 滚动图片
	 * 
	 * @return
	 */
	public String getComImages() {
		return comImages;
	}

	public void setComImages(String comImages) {
		this.comImages = comImages;
	}

	/**
	 * 获取商品详情 图片id
	 * 
	 * @return
	 */
	public int getComid() {
		return comid;
	}

	public void setComid(int comid) {
		this.comid = comid;
	}

}
