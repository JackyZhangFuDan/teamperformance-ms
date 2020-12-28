package com.autobusi.team.dto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.autobusi.team.model.CommentRep;
import com.autobusi.team.model.FeedbackForm;
import com.autobusi.team.model.FeedbackFormRep;
import com.autobusi.team.model.RatingRep;
import com.autobusi.team.model.TeamMember;
import com.autobusi.team.model.TeamMemberRep;

public class FormDetail {
	private FormHeader form;
	private List<Employee> employees;
	private List<Rating> ratings;
	private List<Comment> comments;
	
	public FormHeader getForm() {
		return form;
	}
	public void setForm(FormHeader form) {
		this.form = form;
	}
	public List<Employee> getEmployees() {
		return employees;
	}
	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}
	public List<Rating> getRatings() {
		return ratings;
	}
	public void setRatings(List<Rating> ratings) {
		this.ratings = ratings;
	}
	public List<Comment> getComments() {
		return comments;
	}
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	
	public void init(String formKey, FeedbackFormRep formRep, TeamMemberRep memberRep, RatingRep ratingRep, CommentRep commentRep){
		this.form = new FormHeader();
		this.employees = new ArrayList<Employee>();
		this.ratings = new ArrayList<Rating>();
		this.comments = new ArrayList<Comment>();
		
		FeedbackForm formDB= formRep.findByKey(formKey);
		if(formDB == null) return;
		form.init(formDB);
		
		List<TeamMember> members = memberRep.findAllByActive(1);
		Iterator<TeamMember> memIt = members.iterator();
		while(memIt.hasNext()){
			TeamMember tm = memIt.next();
			Employee emp = new Employee();
			emp.init(tm);
			this.employees.add(emp);
			
			List<com.autobusi.team.model.Rating> ratingsDB = ratingRep.findAllByFormKeyAndMemberAndMemberRole(formKey, tm.getId(), tm.getRole());
			Iterator<com.autobusi.team.model.Rating> ratingsDBIt = ratingsDB.iterator();
			while(ratingsDBIt.hasNext()){
				com.autobusi.team.model.Rating ratingDB = ratingsDBIt.next();
				Rating rt = new Rating();
				rt.init(ratingDB);
				this.ratings.add(rt);
			}
			
			List<com.autobusi.team.model.Comment> commentsDB = commentRep.findAllByFormKeyAndMemberAndMemberRole(formKey, tm.getId(), tm.getRole());
			Iterator<com.autobusi.team.model.Comment> commentsDBIt = commentsDB.iterator();
			while(commentsDBIt.hasNext()){
				com.autobusi.team.model.Comment commentDB = commentsDBIt.next();
				Comment cm = new Comment();
				cm.init(commentDB);
				this.comments.add(cm);
			}
		}
	}
}
