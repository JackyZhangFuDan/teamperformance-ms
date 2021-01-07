package com.autobusi.team.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.Tuple;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobusi.team.dto.Comment;
import com.autobusi.team.dto.FormDetail;
import com.autobusi.team.dto.FormHeader;
import com.autobusi.team.dto.MyRating;
import com.autobusi.team.dto.Rating;
import com.autobusi.team.dto.RatingAverage;
import com.autobusi.team.model.CommentRep;
import com.autobusi.team.model.FeedbackForm;
import com.autobusi.team.model.FeedbackFormRep;
import com.autobusi.team.model.RatingRep;
import com.autobusi.team.model.TeamMember;
import com.autobusi.team.model.TeamMemberRep;


@RestController
@RequestMapping(path="/formapi/*")
public class FormService {
	
	@Autowired
	private FeedbackFormRep formRep = null;
	@Autowired
	private RatingRep ratingRep = null;
	@Autowired
	private TeamMemberRep memberRep = null;
	@Autowired
	private CommentRep commentRep = null;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	//read forms list, header only
	@GetMapping("/forms")
	private List<FormHeader> getFormHeaders(){
		List<FeedbackForm> forms = this.formRep.findAllByKey(null);
		List<FeedbackForm> forms2 = this.formRep.findAllByKey("");
		forms.addAll(forms2);
		Iterator<FeedbackForm> dbIt = forms.iterator();
		List<FormHeader> result = new ArrayList<FormHeader>();
		while(dbIt.hasNext()){
			FeedbackForm db = dbIt.next();
			FormHeader fh = new FormHeader();
			fh.init(db);
			result.add(fh);
		}
		return result;
	}
	
	//read single form detail
	@GetMapping("/form/{formkey}")
	private ResponseEntity<FormDetail>  getFormDetail(@PathVariable String formkey){
		if(formkey == null || formkey.trim().length() == 0){
			return ResponseEntity.badRequest().body(null);
		}
		
		FormDetail fd = new FormDetail();
		fd.init(formkey, formRep, memberRep, ratingRep, commentRep);
		if(fd.getForm() == null || fd.getForm().getId() == null || fd.getForm().getId().isEmpty()){
			this.logger.error("can't find form with key " + formkey);
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok().body(fd);
	}
	//read single member's ratings
	@GetMapping("/myrating/{memberKey}")
	private ResponseEntity<MyRating> getMyRating(@PathVariable String memberKey){
		if(memberKey == null || memberKey.isEmpty()){
			return ResponseEntity.badRequest().body(null);
		}
		
		MyRating mr = new MyRating();
		if(mr.init(memberKey, 2020, formRep, memberRep, ratingRep, commentRep)){
			return ResponseEntity.ok().body(mr);
		}else{
			this.logger.error("can't find ratings for memeber with key " + memberKey);
			return ResponseEntity.notFound().build();
		}
	}
	
	//read average rating of a role in a year
	@GetMapping("/ratingaverage/{role}-{year}")
	private ResponseEntity<List<RatingAverage>> getAverageRoleRating(@PathVariable String role, @PathVariable Integer year){
		List<Tuple> ras = this.ratingRep.calcRatingAverageForRole(role, year);
		if(ras == null || ras.isEmpty()){
			this.logger.error("error when calculate role's average rating");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}else{
			List<RatingAverage> result = new ArrayList();
			ras.forEach(rat -> {
				RatingAverage ra = new RatingAverage();
				ra.setYear((Integer)rat.get("year"));
				ra.setRole((String)rat.get("role"));
				ra.setItem((String)rat.get("item"));
				ra.setAverage((Double)rat.get("average"));
				result.add(ra);
			});
			return ResponseEntity.ok().body(result);
		}
	}
	
	@PutMapping("/bookform/{formid}")
	private ResponseEntity<Message> bookAForm(@PathVariable String formid){

		Message msg = new Message();
		Optional<FeedbackForm> formDBOpt = this.formRep.findById(formid);
		if(!formDBOpt.isPresent()){
			return ResponseEntity.notFound().build();
		}else{
			FeedbackForm formDB = formDBOpt.get();
			if(formDB.getKey() != null && formDB.getKey().trim().length() > 0){
				msg.msg = "the form was booked by other people";
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(msg);
			}
			formDB.setKey(UUID.randomUUID().toString());
			this.formRep.save(formDB);
			
			msg.msg = formDB.getKey();
			return ResponseEntity.ok().body(msg);
		}
	}
	
	@PostMapping("/rate/{formkey}")
	private ResponseEntity<Message> rateFor(@PathVariable String formkey, @RequestBody Rating rating){
		Message msg = new Message();
		if(formkey == null || formkey.isEmpty()){
			msg.msg = "form key is wrong";
			return ResponseEntity.badRequest().body(msg);
		}
		
		if(this.formRep.findByKey(formkey) == null){
			msg.msg = "form key doesn't exist";
			return ResponseEntity.notFound().build();
		}
		
		if( rating == null || 
			rating.getEmployeeId() == null || rating.getEmployeeId().isEmpty() || 
			rating.getRole() == null || rating.getRole().isEmpty() || 
			rating.getRatingItem() == null && rating.getRatingItem().isEmpty()){
			
			msg.msg = "missing parameter";
			return ResponseEntity.badRequest().body(msg);
		}
		
		if(!formkey.equals(rating.getFormKey())){
			msg.msg = "form keys doesn't match";
			return ResponseEntity.badRequest().body(msg);
		}
		
		if(rating.getRating() > 0.0d){ //if <=0, means remove my vote for this people's this item
			//remove existed one, if it exists
			List<com.autobusi.team.model.Rating> existed = this.ratingRep.findAllByFormKeyAndMemberAndMemberRoleAndRatingItem(formkey, rating.getEmployeeId(), rating.getRole(), rating.getRatingItem());
			for(int i=0; i< existed.size(); i++){
				this.ratingRep.delete(existed.get(i));
			}
			
			//add new one
			com.autobusi.team.model.Rating ratingDB = new com.autobusi.team.model.Rating();
			ratingDB.setFormKey(formkey);
			ratingDB.setMember(rating.getEmployeeId());
			ratingDB.setMemberRole(rating.getRole());
			ratingDB.setRatingItem(rating.getRatingItem());
			ratingDB.setRating(rating.getRating());
			this.ratingRep.save(ratingDB);
			
			msg.msg = "" + ratingDB.getRating();
			return ResponseEntity.ok().body(msg);
		}else{
			//remove existed one, if it exists
			List<com.autobusi.team.model.Rating> existed = this.ratingRep.findAllByFormKeyAndMemberAndMemberRoleAndRatingItem(formkey, rating.getEmployeeId(), rating.getRole(), rating.getRatingItem());
			for(int i=0; i< existed.size(); i++){
				this.ratingRep.delete(existed.get(i));
			}
			msg.msg = "deleted";
			return ResponseEntity.ok().body(msg);
		}
		
	}
	
	@PostMapping("/comment/{formkey}")
	private ResponseEntity<Message> commentFor(@PathVariable String formkey, @RequestBody Comment cm){
		Message msg = new Message();
		if(formkey == null || formkey.isEmpty()){
			msg.msg = "form key is wrong";
			return ResponseEntity.badRequest().body(msg);
		}
		
		if(this.formRep.findByKey(formkey) == null){
			msg.msg = "form key doesn't exist";
			return ResponseEntity.notFound().build();
		}
		
		if( cm == null || 
				cm.getEmployeeId() == null || cm.getEmployeeId().isEmpty() || 
				cm.getRole() == null || cm.getRole().isEmpty() ){
			
			msg.msg = "missing parameter";
			return ResponseEntity.badRequest().body(msg);
		}
		
		if(!formkey.equals(cm.getFormKey())){
			msg.msg = "form keys doesn't match";
			return ResponseEntity.badRequest().body(msg);
		}
		
		List<com.autobusi.team.model.Comment> existedCms = this.commentRep.findAllByFormKeyAndMemberAndMemberRole(formkey, cm.getEmployeeId(), cm.getRole());
		com.autobusi.team.model.Comment cmDb = null;
		if(existedCms == null || existedCms.isEmpty())
			cmDb = new com.autobusi.team.model.Comment();
		else
			cmDb = existedCms.get(0);
		
		cmDb.setFormKey(formkey);
		cmDb.setMember(cm.getEmployeeId());
		cmDb.setMemberRole(cm.getRole());
		cmDb.setComment(cm.getComment());
		
		this.commentRep.save(cmDb);
		msg.msg = "added";
		return ResponseEntity.ok().body(msg);
	}
	
	private class Message{
		public String msg = "";
	}
}
