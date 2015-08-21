package com.indoorsy.frash.homepage.data.bean;

import java.io.Serializable;

/**
 * 佐料
 * 
 */
public class RecipeLists implements Serializable {

	private static final long serialVersionUID = 1L;

	private String scontent;// 半斤
	private int sid;
	private String stitle;// 肉类

	/**
	 * 获取 佐料分量
	 * 
	 * @return
	 */
	public String getScontent() {
		return scontent;
	}

	public void setScontent(String scontent) {
		this.scontent = scontent;
	}

	/**
	 * 获取 佐料id
	 * 
	 * @return
	 */
	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	/**
	 * 获取 佐料主食
	 * 
	 * @return
	 */
	public String getStitle() {
		return stitle;
	}

	public void setStitle(String stitle) {
		this.stitle = stitle;
	}

}
