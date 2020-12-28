package com.autobusi.team.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name="form")
public class FeedbackForm {
	@Id
	@Column(name="id")
	private String id;
	
	@Column(name="key")
	private String key;
	
	@Column(name="year")
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
	
	
}
