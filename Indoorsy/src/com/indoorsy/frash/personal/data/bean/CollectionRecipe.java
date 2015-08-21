package com.indoorsy.frash.personal.data.bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CollectionRecipe implements Serializable{
	
	private int corecipeid;
	
	private String recipeTitle;
	
	private String recipeImages;
	
	private String recipeTime;
	
	private int recipeId;

	public int getCorecipeid() {
		return corecipeid;
	}

	public void setCorecipeid(int corecipeid) {
		this.corecipeid = corecipeid;
	}

	public String getRecipeTitle() {
		return recipeTitle;
	}

	public void setRecipeTitle(String recipeTitle) {
		this.recipeTitle = recipeTitle;
	}

	public String getRecipeImages() {
		return recipeImages;
	}

	public void setRecipeImages(String recipeImages) {
		this.recipeImages = recipeImages;
	}

	public String getRecipeTime() {
		return recipeTime;
	}

	public void setRecipeTime(String recipeTime) {
		this.recipeTime = recipeTime;
	}

	public int getRecipeId() {
		return recipeId;
	}

	public void setRecipeId(int recipeId) {
		this.recipeId = recipeId;
	}
	
}
