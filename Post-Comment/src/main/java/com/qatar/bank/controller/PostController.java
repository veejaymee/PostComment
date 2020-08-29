package com.qatar.bank.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;

import com.qatar.bank.dto.PostDTO;
import com.qatar.bank.entity.Message;
import com.qatar.bank.entity.Post;
import com.qatar.bank.repository.PostRepository;
import com.qatar.bank.util.ServiceUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/posts", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
public class PostController {

	private PostRepository postRepository;
	private ResponseEntity<Object> noRecordFound;
	private ResponseEntity<Object> invalidRequest;
	private ResponseEntity<Object> noContent;
	private ResponseEntity<Object> postSearchValidation;
	private ResponseEntity<Object> postAlreadyExist;

	@RequestMapping(method = POST)
	public ResponseEntity<Object> save(@RequestBody Post post) {
		if (null != post && StringUtils.isNotBlank(post.getPostName())
				&& StringUtils.isNotBlank(post.getAuthor())) {
			if (!postRepository.exists(post.getPostName())) {
				Date date = Calendar.getInstance().getTime();
				post.setCreated(date);
				post.setUpdated(date);
				return ServiceUtil.buildEntity(CREATED, postRepository.save(post));
			} else {
				return postAlreadyExist;
			}
		} else {
			return invalidRequest;
		}

	}
	
	@RequestMapping("/{postName}")
	public ResponseEntity<Object> findBypostName(@PathVariable("postName") String postName) {
		Post post = postRepository.findOne(postName);
		if (null != post) {
			return ServiceUtil.buildEntity(FOUND, post);
		} else {
			return noRecordFound;
		}
	}

	@RequestMapping("/byAuthor")
	public ResponseEntity<Object> findByAuthor(@RequestParam(name = "author", required = false) String author,
			@RequestParam(name = "skip", required = false) Integer skip) {
		if (StringUtils.isNotBlank(author)) {
			PostDTO postDTO = postRepository.findByAuthorOrderByCreatedDesc(author, skip==null?1:skip);
				return ServiceUtil.buildEntity(FOUND, postDTO);
		} else {
			return postSearchValidation;
		}
	}

	@RequestMapping(value = "/{postName}", method = PUT)
	public ResponseEntity<Object> findAndUpdateMessage(@PathVariable("postName") String postName,
			@RequestBody Message message) {
		if (StringUtils.isNotBlank(postName) && null != message) {
			message.setPosted(new Date());
			return ServiceUtil.buildEntity(OK, postRepository.findAndUpdateMessage(postName, message));
		}

		return invalidRequest;

	}

	@RequestMapping(value = "/{postName}", method = DELETE)
	public ResponseEntity<Object> delete(@PathVariable("postName") String postName) {
		postRepository.delete(postName);
		return noContent;
	}
	
	@Autowired
	public void setpostRepository(PostRepository postRepository) {
		this.postRepository = postRepository;
	}

	@Resource
	public void setNoRecordFound(ResponseEntity<Object> noRecordFound) {
		this.noRecordFound = noRecordFound;
	}

	@Resource
	public void setInvalidRequest(ResponseEntity<Object> invalidRequest) {
		this.invalidRequest = invalidRequest;
	}

	@Resource
	public void setNoContent(ResponseEntity<Object> noContent) {
		this.noContent = noContent;
	}

	@Resource
	public void setpostAlreadyExist(ResponseEntity<Object> postAlreadyExist) {
		this.postAlreadyExist = postAlreadyExist;
	}
	
}
