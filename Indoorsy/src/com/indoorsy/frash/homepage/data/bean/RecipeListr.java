package com.indoorsy.frash.homepage.data.bean;

import java.io.Serializable;

public class RecipeListr implements Serializable {

	private static final long serialVersionUID = 1L;

	private String rdetailednessContent;
	private int rdetailednessId;
	private String rdetailednessImages;


	/**
	 * 获取 步骤名称：先到水
	 */
	public String getRdetailednessContent() {
		return rdetailednessContent;
	}

	public void setRdetailednessContent(String rdetailednessContent) {
		this.rdetailednessContent = rdetailednessContent;
	}

	/**
	 * 获取 步骤id：1
	 */
	public int getRdetailednessId() {
		return rdetailednessId;
	}

	public void setRdetailednessId(int rdetailednessId) {
		this.rdetailednessId = rdetailednessId;
	}
	/**
	 * 获取 步骤图片
	 */
	public String getRdetailednessImages() {
		return rdetailednessImages;
	}

	public void setRdetailednessImages(String rdetailednessImages) {
		this.rdetailednessImages = rdetailednessImages;
	}

}
