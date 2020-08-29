package com.qatar.bank.controller;

import static org.springframework.http.HttpStatus.FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import javax.annotation.Resource;

import com.qatar.bank.constants.Constants;
import com.qatar.bank.entity.PostUser;
import com.qatar.bank.repository.UserRepository;
import com.qatar.bank.util.ServiceUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import static org.springframework.http.HttpStatus.OK;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/users", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
public class UserController {

	private UserRepository userRepository;
	private ResponseEntity<Object> invalidRequest;
	private ResponseEntity<Object> noContent;
	private ResponseEntity<Object> userAlreadyExist;
	private ResponseEntity<Object> invalidAnswer;
	private PasswordEncoder passwordEncoder;
	private String headerString;
	private String tokenPrefix;

	@RequestMapping(method = POST)
	public ResponseEntity<Object> saveOrUpdateUser(@RequestBody PostUser user) {
		if (null != user && StringUtils.isNoneBlank(user.getUsername(), user.getPassword())) {
			String username = user.getUsername();
			if (!userRepository.exists(username)) {
				ServiceUtil.intializeSave(user);
				userRepository.save(user);
				ResponseEntity<Object> entity = ServiceUtil.addHeader(username, headerString, tokenPrefix);
				return entity;
			} else {
				return userAlreadyExist;
			}
		} else {
			return invalidRequest;
		}

	}
	
	@RequestMapping(value = "/{username}", method = GET)
	public ResponseEntity<Object> getQuestionForUsername(@PathVariable("username") String username){
		if(StringUtils.isNotBlank(username)){
			if(userRepository.exists(username)){
				return ServiceUtil.buildEntity(FOUND, userRepository.findQuestionByUsername(username));
			}else{
				return noContent;
			}
		}else{
			return invalidRequest;
		}
	}

	@RequestMapping(value = "/{id}", method = DELETE)
	public ResponseEntity<Object> deleteUser(@PathVariable("id") String id) {
		userRepository.delete(id);
		return noContent;
	}

	@Autowired
	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Autowired
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
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
	public void setUserAlreadyExist(ResponseEntity<Object> userAlreadyExist) {
		this.userAlreadyExist = userAlreadyExist;
	}
	
	public void setInvalidAnswer(ResponseEntity<Object> invalidAnswer) {
		this.invalidAnswer = invalidAnswer;
	}

	@Value(Constants.HEADER_STRING)
	public void setHeaderString(String headerString) {
		this.headerString = headerString;
	}
	
	@Value(Constants.TOKEN_PREFIX)
	public void setTokenPrefix(String tokenPrefix) {
		this.tokenPrefix = tokenPrefix;
	}
	
	

}
