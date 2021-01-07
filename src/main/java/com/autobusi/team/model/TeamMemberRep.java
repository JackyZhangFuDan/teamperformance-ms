package com.autobusi.team.model;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface TeamMemberRep extends CrudRepository<TeamMember, String> {
	public List<TeamMember> findAllByActive(Integer active);
	public TeamMember findByKey(String key);
}
