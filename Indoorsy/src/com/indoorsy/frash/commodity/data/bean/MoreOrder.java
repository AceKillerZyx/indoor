package com.indoorsy.frash.commodity.data.bean;

import java.io.Serializable;

public class MoreOrder implements Serializable {

	/**
	 * 购物车多订单
	 */
	private static final long serialVersionUID = 1L;

	private String addid ;
	
	private int usid;	
	
	private int cartid;	
	
	private String orderPostscript;
	
	private int orderDelId;
	
	private String orderBill;
	
	private Double orderGoodsAmount;
	
	private Double orderShippingFee;
	
	private int orderNumber;
	
	private int orderConversion;//是否使用积分
	
	private int orderIntegral;
	
	private double orderIntegralMoney;
	
	private double orderAmount;
	
	private double orderTotal;
	
	private String orderunit;
	
	private int goodsid;

	public int getCartid() {
		return cartid;
	}

	public void setCartid(int cartid) {
		this.cartid = cartid;
	}

	public String getOrderPostscript() {
		return orderPostscript;
	}

	public void setOrderPostscript(String orderPostscript) {
		this.orderPostscript = orderPostscript;
	}

	public int getOrderDelId() {
		return orderDelId;
	}

	public void setOrderDelId(int orderDelId) {
		this.orderDelId = orderDelId;
	}

	public String getOrderBill() {
		return orderBill;
	}

	public void setOrderBill(String orderBill) {
		this.orderBill = orderBill;
	}

	public Double getOrderGoodsAmount() {
		return orderGoodsAmount;
	}

	public void setOrderGoodsAmount(Double orderGoodsAmount) {
		this.orderGoodsAmount = orderGoodsAmount;
	}

	public Double getOrderShippingFee() {
		return orderShippingFee;
	}

	public void setOrderShippingFee(Double orderShippingFee) {
		this.orderShippingFee = orderShippingFee;
	}

	public int getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}

	public int getOrderConversion() {
		return orderConversion;
	}

	public void setOrderConversion(int orderConversion) {
		this.orderConversion = orderConversion;
	}

	public int getOrderIntegral() {
		return orderIntegral;
	}

	public void setOrderIntegral(int orderIntegral) {
		this.orderIntegral = orderIntegral;
	}

	public double getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(double orderAmount) {
		this.orderAmount = orderAmount;
	}

	public double getOrderTotal() {
		return orderTotal;
	}

	public void setOrderTotal(double orderTotal) {
		this.orderTotal = orderTotal;
	}

	public String getOrderunit() {
		return orderunit;
	}

	public void setOrderunit(String orderunit) {
		this.orderunit = orderunit;
	}

	public int getGoodsid() {
		return goodsid;
	}

	public void setGoodsid(int goodsid) {
		this.goodsid = goodsid;
	}

	public int getUsid() {
		return usid;
	}

	public void setUsid(int usid) {
		this.usid = usid;
	}

	public double getOrderIntegralMoney() {
		return orderIntegralMoney;
	}

	public void setOrderIntegralMoney(double orderIntegralMoney) {
		this.orderIntegralMoney = orderIntegralMoney;
	}

	public String getAddid() {
		return addid;
	}

	public void setAddid(String addid) {
		this.addid = addid;
	}

	

	
}
