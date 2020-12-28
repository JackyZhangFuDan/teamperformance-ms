package com.autobusi.team.dto;

public class Rating {
	private String formKey;
	private String employeeId;
	private String role;
	private String ratingItem;
	private Double rating;
	
	public String getFormKey() {
		return formKey;
	}
	public void setFormKey(String formKey) {
		this.formKey = formKey;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getRatingItem() {
		return ratingItem;
	}
	public void setRatingItem(String ratingItem) {
		this.ratingItem = ratingItem;
	}
	public Double getRating() {
		return rating;
	}
	public void setRating(Double rating) {
		this.rating = rating;
	}
	
	public void init(com.autobusi.team.model.Rating ratingDB){
		this.formKey = ratingDB.getFormKey();
		this.employeeId = ratingDB.getMember();
		this.role = ratingDB.getMemberRole();
		this.ratingItem = ratingDB.getRatingItem();
		this.rating = ratingDB.getRating();
	}
	
}
