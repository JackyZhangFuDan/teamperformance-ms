package com.autobusi.team.dto;

import com.autobusi.team.model.TeamMember;

public class Employee {
	private String id;
	private String role; //dev,qa,sm,po,ux
	private String name;
	private Integer status; //0:inactive; 1:active
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public void init(TeamMember tmDB){
		this.id = tmDB.getId();
		this.name = tmDB.getName();
		this.role = tmDB.getRole();
		this.status = tmDB.getActive();
	}
	
}
