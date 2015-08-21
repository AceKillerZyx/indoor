package com.indoorsy.frash.homepage.data.bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class SeckTk implements Serializable {

	private int goodsid;
	private String goodsImg;
	private String seckillAlready;
	private String seckillContnt;
	private String seckillSurplus;
	private String seckillUnit;
	private String countdown;

	/**
	 * 获取 当前秒杀图片
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
	 * 获取 当前已秒
	 * 
	 * @return
	 */
	public String getSeckillAlready() {
		return seckillAlready;
	}

	public void setSeckillAlready(String seckillAlready) {
		this.seckillAlready = seckillAlready;
	}

	/**
	 * 获取 当前秒杀 内容
	 * 
	 * @return
	 */
	public String getSeckillContnt() {
		return seckillContnt;
	}

	public void setSeckillContnt(String seckillContnt) {
		this.seckillContnt = seckillContnt;
	}

	/**
	 * 获取 当前秒杀 剩余
	 * 
	 * @return
	 */
	public String getSeckillSurplus() {
		return seckillSurplus;
	}

	public void setSeckillSurplus(String seckillSurplus) {
		this.seckillSurplus = seckillSurplus;
	}

	/**
	 * 获取 当前秒杀 单位
	 * 
	 * @return
	 */
	public String getSeckillUnit() {
		return seckillUnit;
	}

	public void setSeckillUnit(String seckillUnit) {
		this.seckillUnit = seckillUnit;
	}

	public int getGoodsid() {
		return goodsid;
	}

	public void setGoodsid(int goodsid) {
		this.goodsid = goodsid;
	}

	public String getCountdown() {
		return countdown;
	}

	public void setCountdown(String countdown) {
		this.countdown = countdown;
	}

}
