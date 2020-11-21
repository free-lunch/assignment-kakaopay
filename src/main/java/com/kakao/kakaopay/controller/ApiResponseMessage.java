package com.kakao.kakaopay.controller;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
 
@Setter
@Getter
@ToString
public class ApiResponseMessage {
     // HttpStatus
    private HttpStatus status;
    private Object body;
    private String errorMessage;
    private String errorCode;
 

    public static ApiResponseMessage of(HttpStatus status, Object body) {
    	return new ApiResponseMessage(status, body);
    	
    }

 
    public ApiResponseMessage(HttpStatus status, Object body ) {
        this.status = status;
        this.body = body;
    }

    public ApiResponseMessage(HttpStatus status, Object body,  String errorCode, String errorMessage) {
        this.status = status;
        this.body = body;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
