package com.qatar.bank.repository;

import com.qatar.bank.entity.PostUser;

public interface UserCustomRepository {
	
	PostUser findQuestionByUsername(String username);
	boolean verifyAccount(String username, String answer);
	boolean updateAccount(String username, String password);

}
