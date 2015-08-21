package com.indoorsy.frash.homepage.data.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 食谱分享详细
 * 
 */
@SuppressWarnings("serial")
public class RecipeMethod implements Serializable {

	private String corecipeid;//收藏食谱 id
	
	private String rtitle;

	private String commentcount;

	private boolean collect;

	private List<RecipeLists> lists;// 佐料集合

	private List<RecipeListr> listr;// 制作步骤集合

	private List<RecipeListc> lisc;// 评论集合

	/**
	 * 获取 评论数量
	 * 
	 * @return
	 */
	public String getCommentcount() {
		return commentcount;
	}

	public void setCommentcount(String commentcount) {
		this.commentcount = commentcount;
	}

	/**
	 * 获取 评论集合
	 * 
	 * @return
	 */
	public List<RecipeListc> getLisc() {
		return lisc;
	}

	public void setLisc(List<RecipeListc> lisc) {
		this.lisc = lisc;
	}

	/**
	 * 获取 食谱标题
	 * 
	 * @return
	 */
	public String getRtitle() {
		return rtitle;
	}

	public void setRtitle(String rtitle) {
		this.rtitle = rtitle;
	}

	/**
	 * 获取 作料的集合
	 * 
	 * @return
	 */
	public List<RecipeLists> getLists() {
		return lists;
	}

	public void setLists(List<RecipeLists> lists) {
		this.lists = lists;
	}

	/**
	 * 获取 食谱详细描述集合
	 * 
	 * @return
	 */
	public List<RecipeListr> getListr() {
		return listr;
	}

	public void setListr(List<RecipeListr> listr) {
		this.listr = listr;
	}

	/**
	 * 获取 收藏食谱
	 * 
	 * @return boolean
	 */
	public boolean isCollect() {
		return collect;
	}

	public void setCollect(boolean collect) {
		this.collect = collect;
	}

	
	/**
	 * 用于食谱收藏  获取 收藏id
	 * 
	 */
	public String getCorecipeid() {
		return corecipeid;
	}

	public void setCorecipeid(String corecipeid) {
		this.corecipeid = corecipeid;
	}

}
