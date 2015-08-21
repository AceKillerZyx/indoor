package com.indoorsy.frash.homepage.data.bean;

import java.io.Serializable;

public class Imgs implements Serializable {

	private static final long serialVersionUID = 1L;

	private String comDetailsContent;// 图文详情介绍
	private String comDetailsImages;// 图文详情图片

	/**
	 * 图文详情介绍
	 * 
	 * @return
	 */
	public String getComDetailsContent() {
		return comDetailsContent;
	}

	public void setComDetailsContent(String comDetailsContent) {
		this.comDetailsContent = comDetailsContent;
	}

	/**
	 * 图文详情图片
	 * 
	 * @return
	 */
	public String getComDetailsImages() {
		return comDetailsImages;
	}

	public void setComDetailsImages(String comDetailsImages) {
		this.comDetailsImages = comDetailsImages;
	}

}
