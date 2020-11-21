package com.kakao.kakaopay.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FullPreemptException extends RuntimeException {
	public FullPreemptException() {
		super("뿌림이 완료되었습니다.");
	}

}
