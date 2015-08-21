package com.indoorsy.frash.shopping.cart.data.bean;

import java.io.Serializable;

/**
 * 购物车
 * 
 */
public class CartGood  implements Serializable{

	private static final long serialVersionUID = 1L;
	
	
	private double cartGoodsPrice;
	private int cartId;
	private int goodsid;
	private int cartNumber;
	private String cartPath;
	private String cartUnit;
	private String goodsBrief;
	private String goodsImg;
	private String goodsName;

	/**
	 * 获取价格
	 * 
	 * @return
	 */
	public double getCartGoodsPrice() {
		return cartGoodsPrice;
	}

	public void setCartGoodsPrice(double cartGoodsPrice) {
		this.cartGoodsPrice = cartGoodsPrice;
	}

	/**
	 * 获取购物车id
	 * 
	 * @return
	 */
	public int getCartId() {
		return cartId;
	}

	public void setCartId(int cartId) {
		this.cartId = cartId;
	}

	/**
	 * 获取数量
	 * 
	 * @return
	 */
	public int getCartNumber() {
		return cartNumber;
	}

	public void setCartNumber(int cartNumber) {
		this.cartNumber = cartNumber;
	}

	/**
	 * 获取来源 eg:是否是秒杀
	 * 
	 * @return
	 */
	public String getCartPath() {
		return cartPath;
	}

	public void setCartPath(String cartPath) {
		this.cartPath = cartPath;
	}

	/**
	 * 获取单位
	 * 
	 * @return
	 */
	public String getCartUnit() {
		return cartUnit;
	}

	public void setCartUnit(String cartUnit) {
		this.cartUnit = cartUnit;
	}

	/**
	 * 获取商品介绍
	 * 
	 * @return
	 */
	public String getGoodsBrief() {
		return goodsBrief;
	}

	public void setGoodsBrief(String goodsBrief) {
		this.goodsBrief = goodsBrief;
	}

	/**
	 * 获取商品图片
	 * 
	 * @return
	 */
	public String getGoodsImg() {
		return goodsImg;
	}

	public void setGoodsImg(String goodsImg) {
		this.goodsImg = goodsImg;
	}

	/**
	 * 获取商品名字
	 * 
	 * @return
	 */
	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	/**
	 * 获取商品id
	 * 
	 * @return
	 */
	public int getGoodsid() {
		return goodsid;
	}

	public void setGoodsid(int goodsid) {
		this.goodsid = goodsid;
	}


}
