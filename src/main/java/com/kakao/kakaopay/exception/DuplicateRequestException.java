package com.kakao.kakaopay.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DuplicateRequestException extends RuntimeException {
	public DuplicateRequestException() {
		super("중복된 요청 에러입니다.");
	}
}
