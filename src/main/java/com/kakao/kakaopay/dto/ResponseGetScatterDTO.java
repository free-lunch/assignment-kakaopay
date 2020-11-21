package com.kakao.kakaopay.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponseGetScatterDTO {
	
	private String createdAt;
	private Long price;
	private Long preemptedPrice;
	private List<DetailDTO> details;
	
	@Getter
	@Setter
	@AllArgsConstructor
	public static class DetailDTO {
		private Long dividedPrice;
		private String preemptedUserId;
	}
}

