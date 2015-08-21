package com.indoorsy.frash.homepage.data.bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Ad implements Serializable {
	private int advBox;
	private int advId;
	private int advState;
	private String advImages;

	public int getAdvBox() {
		return advBox;
	}

	public void setAdvBox(int advBox) {
		this.advBox = advBox;
	}

	public int getAdvId() {
		return advId;
	}

	public void setAdvId(int advId) {
		this.advId = advId;
	}

	/**
	 * 获取图片路径
	 * 
	 * @return
	 */
	public String getAdvImages() {
		return advImages;
	}

	public void setAdvImages(String advImages) {
		this.advImages = advImages;
	}

	public int getAdvState() {
		return advState;
	}

	public void setAdvState(int advState) {
		this.advState = advState;
	}

}
