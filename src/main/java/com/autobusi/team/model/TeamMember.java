package com.autobusi.team.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name="member")
public class TeamMember {
	@Id
	@Column(name="id")
	private String id;
	
	@Column(name="role")
	private String role;
	
	@Column(name="name")
	private String name;
	
	@Column(name="key")
	private String key;
	
	@Column(name="active")
	private Integer active;

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

	public Integer getActive() {
		return active;
	}

	public void setActive(Integer active) {
		this.active = active;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
}
