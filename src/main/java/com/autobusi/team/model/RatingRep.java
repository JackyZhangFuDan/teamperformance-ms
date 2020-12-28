package com.autobusi.team.model;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface RatingRep extends CrudRepository<Rating, String> {
	public List<Rating> findAllByFormKeyAndMemberAndMemberRole(String formKey,String member, String memberRole);
	public List<Rating> findAllByFormKeyAndMemberAndMemberRoleAndRatingItem(String formKey,String member, String memberRole, String ratingItem);
	//public void deleteAllByFormKeyAndMemberAndMemberRoleAndRatingItem(String formKey,String member, String memberRole,String ratingItem);
}
