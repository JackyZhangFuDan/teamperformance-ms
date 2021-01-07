package com.autobusi.team.model;

import java.util.List;

import javax.persistence.Tuple;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.autobusi.team.dto.RatingAverage;

public interface RatingRep extends CrudRepository<Rating, String> {
	public List<Rating> findAllByFormKeyAndMemberAndMemberRole(String formKey,String member, String memberRole);
	public List<Rating> findAllByFormKeyAndMemberAndMemberRoleAndRatingItem(String formKey,String member, String memberRole, String ratingItem);
	//public void deleteAllByFormKeyAndMemberAndMemberRoleAndRatingItem(String formKey,String member, String memberRole,String ratingItem);
	
	@Query(value="select f.\"year\", r.memberrole as role, r.item, cast(cast(avg(r.rating) as decimal(2,1)) as float) as \"average\" from team.rating r left join team.form f on r.formkey = f.\"key\" where f.\"year\" = ?2 and r.memberrole = ?1 group by f.\"year\",r.memberrole, r.item", nativeQuery=true)
	public List<Tuple> calcRatingAverageForRole(String role, int year);
}
