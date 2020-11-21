package com.kakao.kakaopay.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RequestScatterDTO {
	private Long price;
	private int dividerNumber;
	
	static public RequestScatterDTO of(Long price, int dividerNumber) {
		return new RequestScatterDTO(price, dividerNumber);
	}
}
