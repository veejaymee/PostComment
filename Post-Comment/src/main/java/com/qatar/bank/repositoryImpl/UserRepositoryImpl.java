package com.qatar.bank.repositoryImpl;

import com.qatar.bank.entity.PostUser;
import com.qatar.bank.repository.UserCustomRepository;
import com.qatar.bank.util.ServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.WriteResult;

public class UserRepositoryImpl implements UserCustomRepository {
	
	private MongoTemplate mongoTemplate;

	@Override
	public PostUser findQuestionByUsername(String username) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").regex(username));
		query.fields().include("question");
		PostUser user = mongoTemplate.findOne(query, PostUser.class);
		return user;
	}
	
	@Override
	public boolean verifyAccount(String username, String answer) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").regex(username));
		query.fields().include("answer");
		PostUser user = mongoTemplate.findOne(query, PostUser.class);
		return ServiceUtil.isValidAnswer(answer, user.getAnswer());
	}
	
	@Override
	public boolean updateAccount(String username, String password) {
		
		Update update = new Update();
		update.set("password", password);
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").regex(username));
		WriteResult result = mongoTemplate.updateFirst(query, update, PostUser.class);
		return result.isUpdateOfExisting();
	}
	
	
	@Autowired
	public void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}


}
