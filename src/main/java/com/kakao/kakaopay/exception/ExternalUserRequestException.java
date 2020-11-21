package com.kakao.kakaopay.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ExternalUserRequestException extends RuntimeException {
	public ExternalUserRequestException() {
		super("같은 방의 요청이 아닙니다.");
	}

}
