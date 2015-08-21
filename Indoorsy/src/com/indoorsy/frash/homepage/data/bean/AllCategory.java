package com.indoorsy.frash.homepage.data.bean;

import java.io.Serializable;
import java.util.List;
/**
 * 全部类别
 */
@SuppressWarnings("serial")
public class AllCategory implements Serializable {
	private String typename;
	private List<Category> listc;
	public String getTypename() {
		return typename;
	}
	public void setTypename(String typename) {
		this.typename = typename;
	}
	public List<Category> getListc() {
		return listc;
	}
	public void setListc(List<Category> listc) {
		this.listc = listc;
	}

}
