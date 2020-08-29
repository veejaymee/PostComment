package com.qatar.bank.repository;

import com.qatar.bank.dto.PostDTO;
import com.qatar.bank.entity.Message;
import com.qatar.bank.entity.Post;


public interface PostCustomRepository {
	Post findAndUpdateMessage(String postName, Message message);
	PostDTO findByAuthorOrderByCreatedDesc(String author, long page);
	PostDTO findByAuthorAndPostName(String author, String topicName, long page);
}
