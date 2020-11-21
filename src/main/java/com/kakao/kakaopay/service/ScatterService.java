package com.kakao.kakaopay.service;

import com.kakao.kakaopay.dao.Scatter;

public interface ScatterService {
	
	Scatter makeScatter(String userId, String roomId, Long price, int dividerNumber);
	Long preemptScatterDetail(String token, String userId, String roomId);
	Scatter getScatterByOwner(String token, String userId);
}