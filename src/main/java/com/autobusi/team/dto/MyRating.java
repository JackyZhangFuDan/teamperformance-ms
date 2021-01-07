package com.autobusi.team.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.autobusi.team.model.CommentRep;
import com.autobusi.team.model.FeedbackForm;
import com.autobusi.team.model.FeedbackFormRep;
import com.autobusi.team.model.RatingRep;
import com.autobusi.team.model.TeamMember;
import com.autobusi.team.model.TeamMemberRep;

public class MyRating {
	private String employeeId;
	private String employeeName;
	private String employeeRole;
	private List<RatingCount> ratingCounts = new ArrayList();
	private List<String> comments = new ArrayList();
	
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getEmployeeRole() {
		return employeeRole;
	}
	public void setEmployeeRole(String employeeRole) {
		this.employeeRole = employeeRole;
	}
	public List<RatingCount> getRatingCounts() {
		return ratingCounts;
	}
	public void setRatingCounts(List<RatingCount> ratingCounts) {
		this.ratingCounts = ratingCounts;
	}
	public List<String> getComments() {
		return comments;
	}
	public void setComments(List<String> comments) {
		this.comments = comments;
	}
	
	public boolean init(String memberKey, int year, FeedbackFormRep formRep, TeamMemberRep memberRep, RatingRep ratingRep, CommentRep commentRep){
		TeamMember member = memberRep.findByKey(memberKey);
		if(member == null ) return false;
		
		this.employeeId = member.getId();
		this.employeeName = member.getName();
		this.employeeRole = member.getRole();
		
		List<FeedbackForm> forms = formRep.findAllByYear(year);
		if(forms == null || forms.isEmpty()) return false;
		
		Iterator<FeedbackForm> formsIt = forms.iterator();
		while(formsIt.hasNext()){
			FeedbackForm form = formsIt.next();
			//ratings
			List<com.autobusi.team.model.Rating> ratings = ratingRep.findAllByFormKeyAndMemberAndMemberRole(form.getKey(), member.getId(), member.getRole());
			if(ratings != null && !ratings.isEmpty()){
				Iterator<com.autobusi.team.model.Rating> ratingsIt = ratings.iterator();
				while(ratingsIt.hasNext()){
					com.autobusi.team.model.Rating rating = ratingsIt.next();
					boolean found = false;
					for(int i = 0 ; i < this.ratingCounts.size(); i++){
						RatingCount rc = this.ratingCounts.get(i);
						if(rc.getRatingItem().equals(rating.getRatingItem()) && rc.getRating().equals(rating.getRating())){
							rc.setCount(rc.getCount() + 1);
							found = true;
						}
					}
					if(!found){
						RatingCount rc = new RatingCount();
						rc.setRatingItem(rating.getRatingItem());
						rc.setRating(rating.getRating());
						rc.setCount(1);
						this.ratingCounts.add(rc);
					}
				}
			}
			//comments
			List<com.autobusi.team.model.Comment> comments = commentRep.findAllByFormKeyAndMemberAndMemberRole(form.getKey(), member.getId(), member.getRole());
			if(comments != null && !comments.isEmpty()){
				Iterator<com.autobusi.team.model.Comment> commentsIt = comments.iterator();
				while(commentsIt.hasNext()){
					com.autobusi.team.model.Comment c = commentsIt.next();
					if(c.getComment() != null && !c.getComment().trim().isEmpty())
						this.comments.add(c.getComment());
				}
			}
		}
		
		return true;
	}
}
