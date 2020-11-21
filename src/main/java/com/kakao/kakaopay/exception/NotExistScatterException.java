package com.kakao.kakaopay.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotExistScatterException extends BadRequestException {
	public NotExistScatterException() {
		super("요청한 토큰에 맞는 뿌리기가 없습니다.", "E005");
	}
}
