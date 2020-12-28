package com.autobusi.team.model;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface FeedbackFormRep extends CrudRepository<FeedbackForm, String> {
	public FeedbackForm findByKey(String key);
	public List<FeedbackForm> findAllByKey(String key);
}
