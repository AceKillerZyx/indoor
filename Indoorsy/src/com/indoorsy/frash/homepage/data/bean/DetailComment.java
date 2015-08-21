package com.indoorsy.frash.homepage.data.bean;

import java.io.Serializable;
import java.util.List;

public class DetailComment implements Serializable {

	private static final long serialVersionUID = 1L;

	private String evaContent;// 评论内容
	private List<DetailList> list;// 评论附带的图片

	private String userImages;// 评论人头像
	private String userNames;// 评论人昵称

	/**
	 * 获取评论内容
	 * 
	 * @return
	 */
	public String getEvaContent() {
		return evaContent;
	}

	public void setEvaContent(String evaContent) {
		this.evaContent = evaContent;
	}

	/**
	 * 获取评论评论附带的图片集合
	 * 
	 * @return
	 */
	public List<DetailList> getList() {
		return list;
	}

	public void setList(List<DetailList> list) {
		this.list = list;
	}

	/**
	 * 获取评论人头像
	 * 
	 * @return
	 */
	public String getUserImages() {
		return userImages;
	}

	public void setUserImages(String userImages) {
		this.userImages = userImages;
	}

	/**
	 * 获取评论人昵称
	 * 
	 * @return
	 */
	public String getUserNames() {
		return userNames;
	}

	public void setUserNames(String userNames) {
		this.userNames = userNames;
	}

}
