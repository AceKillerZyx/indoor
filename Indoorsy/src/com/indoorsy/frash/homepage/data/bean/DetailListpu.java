package com.indoorsy.frash.homepage.data.bean;

import java.io.Serializable;

/**
 * 商品单位价格集合
 * 
 * @author gq
 * 
 */
public class DetailListpu implements Serializable {

	private static final long serialVersionUID = 1L;

	private int punitId;
	private double punitPrice;
	private String unitTitle;

	/**
	 * 获取商品 单位id  eg：1
	 * 
	 * @return
	 */
	public int getPunitId() {
		return punitId;
	}

	public void setPunitId(int punitId) {
		this.punitId = punitId;
	}
	/**
	 * 获取商品 单位价格 eg：6
	 * 
	 * @return
	 */
	public double getPunitPrice() {
		return punitPrice;
	}

	public void setPunitPrice(double punitPrice) {
		this.punitPrice = punitPrice;
	}
	/**
	 * 获取商品 单位 eg：斤/份
	 * 
	 * @return
	 */
	public String getUnitTitle() {
		return unitTitle;
	}

	public void setUnitTitle(String unitTitle) {
		this.unitTitle = unitTitle;
	}

}
