package com.indoorsy.frash.homepage.data.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 食谱评论
 * 
 */
public class RecipeListc implements Serializable {

	private static final long serialVersionUID = 1L;

	private String commContent;
	private String commDate;
	private String userImages;
	private String userNames;
	private int commId;
	private int count;
	private int userId;

	private List<RecipeReComment> listcom; // 对回复的回复集合

	public String getCommContent() {
		return commContent;
	}

	public void setCommContent(String commContent) {
		this.commContent = commContent;
	}

	public String getCommDate() {
		return commDate;
	}

	public void setCommDate(String commDate) {
		this.commDate = commDate;
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

	/**
	 * 获取 评论的回复集合
	 */
	public List<RecipeReComment> getListcom() {
		return listcom;
	}

	public void setListcom(List<RecipeReComment> listcom) {
		this.listcom = listcom;
	}

}
