package com.kakao.kakaopay.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public abstract class BadRequestException extends RuntimeException {
	private String errorCode;
	
	public BadRequestException(String message, String errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

}