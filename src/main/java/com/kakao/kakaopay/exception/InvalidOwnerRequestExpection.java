package com.kakao.kakaopay.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidOwnerRequestExpection extends BadRequestException{
	public InvalidOwnerRequestExpection() {
		super("잘못된 유저의 뿌림조회 요청입니다.", "E007");
	}
}
