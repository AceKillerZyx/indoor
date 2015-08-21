package com.indoorsy.frash.homepage.data.bean;

import java.io.Serializable;

/**
 * 产品介绍集合
 * 
 * @author
 * 
 */
public class DetailListcd implements Serializable {

	private static final long serialVersionUID = 1L;

	private int comDetailsId;// 产品简介id
	private String comDetailsContent;// 产品简介内容
	private String comDetailsImages;// 产品简介图片
	private String goodsdesc;//新简介
	

	/**
	 * 获取 产品简介id
	 * 
	 * @return
	 */
	public int getComDetailsId() {
		return comDetailsId;
	}

	public void setComDetailsId(int comDetailsId) {
		this.comDetailsId = comDetailsId;
	}

	/**
	 * 获取 产品简介内容
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
	 * 获取 产品简介图片
	 * 
	 * @return
	 */
	public String getComDetailsImages() {
		return comDetailsImages;
	}

	public void setComDetailsImages(String comDetailsImages) {
		this.comDetailsImages = comDetailsImages;
	}

	public String getGoodsdesc() {
		return goodsdesc;
	}

	public void setGoodsdesc(String goodsdesc) {
		this.goodsdesc = goodsdesc;
	}

}
