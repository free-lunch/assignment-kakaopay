package com.kakao.kakaopay.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import com.kakao.kakaopay.exception.DuplicateRequestException;
import com.kakao.kakaopay.exception.FullPreemptException;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@RedisHash(value = "scatter", timeToLive = 60 * 60 * 24 * 7)
public class Scatter implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	private String token;
	private String roomId;
	private String ownerUserId;
	private Long price;
	private int dividerNumber;
	private ArrayList<ScatterDetail> details;
	private Date createdAt = new Date();
	
	
	/**
	 * @param price 나누길 원하는 총 금액 
	 * @param dividerNumber 참가인원수 
	 * @return void
	 */
	public void makeDetails(Long price, int dividerNumber) {
		final ArrayList<ScatterDetail> details = this.dividePrice(price, dividerNumber);
		this.setDetails(details);
	}
	
	public void makeToken(int number) {
		this.setToken(RandomStringUtils.randomAlphabetic(number));
	}
	
	/**
	 * 주어진 금액을 인원수대로 나눈다. 모든 인원은 1원이상을 받을 수 있도록 계산되었으며, 최소 단위는 1원이다.
	 * @param price 나누길 원하는 총 금액 
	 * @param dividerNumber 참가인원수 
	 * @return 나눠진 결과 
	 */
	private ArrayList<ScatterDetail> dividePrice(Long price, int dividerNumber) {
		final ArrayList<ScatterDetail> details = new ArrayList<ScatterDetail>();
		
		Long mod = price - dividerNumber;
		for (int i = 0; i < dividerNumber; i++) {
			final ScatterDetail detail = new ScatterDetail();
			if (i == (dividerNumber - 1)) {
				detail.setDividedPrice(mod + 1);
			} else {
				final long randLong = ThreadLocalRandom.current().nextLong(0, mod);
				detail.setDividedPrice(randLong + 1);
				mod = mod - randLong;
			}
			details.add(detail);
		}
		return details;
	}
}

