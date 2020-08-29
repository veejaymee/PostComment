package com.qatar.bank.repositoryImpl;

import java.util.List;

import com.qatar.bank.constants.Constants;
import com.qatar.bank.dto.PostDTO;
import com.qatar.bank.entity.Message;
import com.qatar.bank.entity.Post;
import com.qatar.bank.repository.PostCustomRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public class PostRepositoryImpl implements PostCustomRepository {

	private MongoTemplate mongoTemplate;

	@Override
	public Post findAndUpdateMessage(String postName, Message message) {
		Query query = new Query();
		if (StringUtils.isNotBlank(postName)) {
			query.addCriteria(Criteria.where("postName").regex(postName));
		}
		Update update = new Update();
		update.currentDate("updated");
		update.addToSet("messages", message);
		Post post = mongoTemplate.findAndModify(query, update, FindAndModifyOptions.options().returnNew(true),
				Post.class);
		return post;
	}

	@Override
	public PostDTO findByAuthorOrderByCreatedDesc(String author, long page) {
		Query query = new Query();
		Query countQuery = new Query();
		if(StringUtils.isNotBlank(author)){
			CriteriaDefinition authorCriteria = Criteria.where("author").regex(author);
			query.addCriteria(authorCriteria);
			countQuery.addCriteria(authorCriteria);
		}
		long total = mongoTemplate.count(countQuery, Post.class);
		int initPageToSkip = (int)(page-1) * Constants.LIMIT;
		int finalPageToSkip = (initPageToSkip > total)? 0 : initPageToSkip;
		page = (finalPageToSkip == 0) ? 1 : page;
		query.fields().include("postName");
		query.with(new Sort(new Order(Direction.DESC, "created")));
		query.skip(finalPageToSkip);
		query.limit(Constants.LIMIT);
		List<Post> posts = mongoTemplate.find(query, Post.class);
		PostDTO postDTO = new PostDTO(posts, total, page);
		return postDTO;
	}
	
	@Override
	public PostDTO findByAuthorAndPostName(String author, String postName, long page) {
		Query query = new Query();
		Query countQuery = new Query();
		CriteriaDefinition authorCriteria = Criteria.where("author").regex(author, "i");
		CriteriaDefinition postNameCriteria = Criteria.where("_id").regex(postName, "i");
		countQuery.addCriteria(authorCriteria);
		countQuery.addCriteria(postNameCriteria);
		long total = mongoTemplate.count(countQuery, PostDTO.class);
		query.addCriteria(authorCriteria);
		query.addCriteria(postNameCriteria);
		int initPageToSkip = (int)(page-1) * Constants.LIMIT;
		int finalPageToSkip = (initPageToSkip > total)? 0 : initPageToSkip;
		page = (finalPageToSkip == 0) ? 1 : page;
		query.fields().include("postName");
		query.skip(finalPageToSkip);
		query.limit(Constants.LIMIT);
		List<Post> posts = mongoTemplate.find(query, Post.class);
		PostDTO postDTO = new PostDTO(posts, total, page);
		return postDTO;
	}

	@Autowired
	public void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

}