package com.autobusi.team.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="rating")
public class Rating {
	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="formkey")
	private String formKey;
	
	@Column(name="memberid")
	private String member;
	
	@Column(name="memberrole")
	private String memberRole;
	
	@Column(name="item")
	private String ratingItem;
	
	@Column(name="rating")
	private Double rating;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFormKey() {
		return formKey;
	}

	public void setFormKey(String formKey) {
		this.formKey = formKey;
	}

	public String getMember() {
		return member;
	}

	public void setMember(String member) {
		this.member = member;
	}

	public String getMemberRole() {
		return memberRole;
	}

	public void setMemberRole(String memberRole) {
		this.memberRole = memberRole;
	}

	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}

	public String getRatingItem() {
		return ratingItem;
	}

	public void setRatingItem(String ratingItem) {
		this.ratingItem = ratingItem;
	}
	
}