package com.kakao.kakaopay.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TimeoutPreemptException extends RuntimeException {
	public TimeoutPreemptException() {
		super("시간이 만료된 요청입니다.");
	}
}
