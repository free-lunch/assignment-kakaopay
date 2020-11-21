package com.kakao.kakaopay.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MyselfRequestException extends BadRequestException {
	public MyselfRequestException() {
		super("자신이 뿌린 건 입니다.", "E004");
	}
}
