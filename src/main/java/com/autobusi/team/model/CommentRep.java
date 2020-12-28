package com.autobusi.team.model;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface CommentRep extends CrudRepository<Comment, Integer> {
	public List<Comment> findAllByFormKeyAndMemberAndMemberRole(String formKey,String member, String memberRole);

}
