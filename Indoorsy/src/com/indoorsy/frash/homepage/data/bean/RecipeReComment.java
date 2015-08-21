package com.indoorsy.frash.homepage.data.bean;

import java.io.Serializable;

/**
 * 获取 评论的回复集合
 * 
 * @return
 */

public class RecipeReComment implements Serializable {

	private static final long serialVersionUID = 1L;

	private String commContent;
	private int commId;
	private int count;
	private int userId;
	private String listcom;
	private String userImages;
	private String userNames;

	/**
	 * 获取 评论内容
	 * 
	 * @return
	 */
	public String getCommContent() {
		return commContent;
	}

	public void setCommContent(String commContent) {
		this.commContent = commContent;
	}

	public int getCommId() {
		return commId;
	}

	public void setCommId(int commId) {
		this.commId = commId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getListcom() {
		return listcom;
	}

	public void setListcom(String listcom) {
		this.listcom = listcom;
	}

	public String getUserImages() {
		return userImages;
	}

	public void setUserImages(String userImages) {
		this.userImages = userImages;
	}

	public String getUserNames() {
		return userNames;
	}

	public void setUserNames(String userNames) {
		this.userNames = userNames;
	}

}
