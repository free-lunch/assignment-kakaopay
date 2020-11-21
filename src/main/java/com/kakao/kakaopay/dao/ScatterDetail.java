package com.kakao.kakaopay.dao;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScatterDetail { 
	private Long dividedPrice;
	private String preemptedUserId = null;
	private Boolean isPreempted = false;
}
