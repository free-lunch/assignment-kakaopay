package com.kakao.kakaopay.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.kakao.kakaopay.dto.ExceptionResponseDTO;

@RestControllerAdvice
public class ExceptionAdviceHandler {
	@ExceptionHandler({
		DuplicateRequestException.class,
		ExternalUserRequestException.class,
		MyselfRequestException.class,
		NotExistScatterException.class,
		TimeoutPreemptException.class
	})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ExceptionResponseDTO handleException(BadRequestException e) {
    	return new ExceptionResponseDTO(e.getErrorCode(), e.getMessage());
    }

}
