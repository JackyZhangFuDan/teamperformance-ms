package com.autobusi.team.dto;

import com.autobusi.team.model.FeedbackForm;

public class FormHeader {
	private String id;
	private String key;
	private Integer year;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	
	public void init(FeedbackForm form){
		this.setId(form.getId());
		this.setKey(form.getKey());
		this.setYear(form.getYear());
	}
}
