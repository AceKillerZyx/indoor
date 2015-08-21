package com.indoorsy.frash.homepage.data.bean;

import java.io.Serializable;
/**
 * 商品类别
 */
@SuppressWarnings("serial")
public class Category implements Serializable {

	private int catId;
	private String catImages;
	private String catName;
	private String catType;
	private String catDesc;

	/**
	 * 获取商品类别的 分类id
	 * @return int catId
	 */
	public int getCatId() {
		return catId;
	}

	public void setCatId(int catId) {
		this.catId = catId;
	}

	/**
	 * 获取商品类别的 图片
	 * @return String catImages
	 */
	public String getCatImages() {
		return catImages;
	}

	public void setCatImages(String catImages) {
		this.catImages = catImages;
	}

	/**
	 * 获取商品类别的 分类名称
	 * @return String catName
	 */
	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}

	/**
	 * 获取商品类别的 可用状态
	 * @return String catType
	 */
	public String getCatType() {
		return catType;
	}

	public void setCatType(String catType) {
		this.catType = catType;
	}

	/**
	 * 获取商品类别的 描述
	 * @return String catDesc
	 */
	public String getCatDesc() {
		return catDesc;
	}

	public void setCatDesc(String catDesc) {
		this.catDesc = catDesc;
	}

}
