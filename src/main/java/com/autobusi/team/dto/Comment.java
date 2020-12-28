package com.autobusi.team.dto;


public class Comment {
private Integer id;
	
	private String formKey;
	
	private String employeeId;
	
	private String role;
	
	private String comment;

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

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String member) {
		this.employeeId = member;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String memberRole) {
		this.role = memberRole;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public void init(com.autobusi.team.model.Comment cmDb){
		this.formKey = cmDb.getFormKey();
		this.employeeId = cmDb.getMember();
		this.role = cmDb.getMemberRole();
		this.comment = cmDb.getComment();
	}
	
}
