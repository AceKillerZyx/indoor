package com.indoorsy.frash.personal.data.bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MyOrder implements Serializable{
	
	private String ordersn;
	
	private int goodsid;
	
	private String goodsname;
	
	private String goodsthumb;
	
	private int orderId;
	
	private double orderamount;
	
	private double ordergoodsamount;
	
	private int ordernumber;
	
	private String orderstate;
	
	private String unit;
	
	private String goodsdesc;

	public int getGoodsid() {
		return goodsid;
	}

	public void setGoodsid(int goodsid) {
		this.goodsid = goodsid;
	}

	public String getGoodsname() {
		return goodsname;
	}

	public void setGoodsname(String goodsname) {
		this.goodsname = goodsname;
	}

	public String getGoodsthumb() {
		return goodsthumb;
	}

	public void setGoodsthumb(String goodsthumb) {
		this.goodsthumb = goodsthumb;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public double getOrderamount() {
		return orderamount;
	}

	public void setOrderamount(double orderamount) {
		this.orderamount = orderamount;
	}

	public double getOrdergoodsamount() {
		return ordergoodsamount;
	}

	public void setOrdergoodsamount(double ordergoodsamount) {
		this.ordergoodsamount = ordergoodsamount;
	}

	public int getOrdernumber() {
		return ordernumber;
	}

	public void setOrdernumber(int ordernumber) {
		this.ordernumber = ordernumber;
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

	public String getGoodsdesc() {
		return goodsdesc;
	}

	public void setGoodsdesc(String goodsdesc) {
		this.goodsdesc = goodsdesc;
	}

	public String getOrdersn() {
		return ordersn;
	}

	public void setOrdersn(String ordersn) {
		this.ordersn = ordersn;
	}
	
}
