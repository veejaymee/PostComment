package com.qatar.bank.repository;

import com.qatar.bank.entity.Post;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface PostRepository extends MongoRepository<Post, String>, PostCustomRepository {
	
}
