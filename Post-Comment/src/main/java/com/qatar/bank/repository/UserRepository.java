package com.qatar.bank.repository;

import com.qatar.bank.entity.PostUser;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<PostUser, String>, UserCustomRepository {
	
}
