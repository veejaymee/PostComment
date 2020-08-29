package com.qatar.bank.util;

import com.qatar.bank.constants.Constants;
import com.qatar.bank.entity.PostUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public final class ServiceUtil {
	public static ResponseEntity<Object> buildEntity(HttpStatus status, Object entity) {
		return new ResponseEntity<Object>(entity, status);
	}
	
	public static boolean isValidAnswer(String answer, String answerFromDB){
		return getParsedAnswer(answer).equals(getParsedAnswer(answerFromDB));
	}

	public static void intializeSave(PostUser user) {
		user.setEnabled(true);
		user.setRole(Constants.ROLE_USER);
	}

	public static ResponseEntity<Object> addHeader(String username, String headerString, String tokenPrefix) {
		ResponseEntity<Object> entity = ResponseEntity.status(HttpStatus.CREATED).header(headerString, tokenPrefix + " " + "").build();
		return entity;
	}

	private static String getParsedAnswer(String answer){
		return answer.replaceAll(" ", "").toLowerCase();
	}

	private ServiceUtil() {
	}
}
