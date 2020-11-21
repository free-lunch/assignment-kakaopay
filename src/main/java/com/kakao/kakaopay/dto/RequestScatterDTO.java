package com.kakao.kakaopay.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RequestScatterDTO {
	private Long price;
	private int dividerNumber;
}
