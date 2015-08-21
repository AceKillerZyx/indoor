package com.indoorsy.frash.personal.data.bean;

import java.io.Serializable;
@SuppressWarnings("serial")
public class OrderDetails implements Serializable{
	
	private String addAdderss;
	private String addTel;
	private String goodsBrief;
	private int goodsId;
	private String goodsName;
	private String goodsThumb;
	private String orderAddTime;
	private double orderAmount;
	private double orderGoodsAmount;
	private int orderNumber;
	private int orderShippingFee;
	private String orderSn;
	private String orderstate;
	private String unit;
	private String whzUsername;
	
	
	
	
	public double getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(double orderAmount) {
		this.orderAmount = orderAmount;
	}
	public double getOrderGoodsAmount() {
		return orderGoodsAmount;
	}
	public void setOrderGoodsAmount(double orderGoodsAmount) {
		this.orderGoodsAmount = orderGoodsAmount;
	}
	public String getAddAdderss() {
		return addAdderss;
	}
	public void setAddAdderss(String addAdderss) {
		this.addAdderss = addAdderss;
	}
	public String getAddTel() {
		return addTel;
	}
	public void setAddTel(String addTel) {
		this.addTel = addTel;
	}
	public String getGoodsBrief() {
		return goodsBrief;
	}
	public void setGoodsBrief(String goodsBrief) {
		this.goodsBrief = goodsBrief;
	}
	public int getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getGoodsThumb() {
		return goodsThumb;
	}
	public void setGoodsThumb(String goodsThumb) {
		this.goodsThumb = goodsThumb;
	}
	public String getOrderAddTime() {
		return orderAddTime;
	}
	public void setOrderAddTime(String orderAddTime) {
		this.orderAddTime = orderAddTime;
	}
	public void setOrderGoodsAmount(int orderGoodsAmount) {
		this.orderGoodsAmount = orderGoodsAmount;
	}
	public int getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}
	public int getOrderShippingFee() {
		return orderShippingFee;
	}
	public void setOrderShippingFee(int orderShippingFee) {
		this.orderShippingFee = orderShippingFee;
	}
	public String getOrderSn() {
		return orderSn;
	}
	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}
	public String getOrderstate() {
		return orderstate;
	}
	public void setOrderstate(String orderstate) {
		this.orderstate = orderstate;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getWhzUsername() {
		return whzUsername;
	}
	public void setWhzUsername(String whzUsername) {
		this.whzUsername = whzUsername;
	}
	
	
	
	

}
