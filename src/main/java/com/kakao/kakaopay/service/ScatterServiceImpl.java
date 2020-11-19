package com.kakao.kakaopay.service;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.kakao.kakaopay.dao.Scatter;
import com.kakao.kakaopay.dao.ScatterDetail;
import com.kakao.kakaopay.dao.ScatterRepository;

@Service
public class ScatterServiceImpl implements ScatterService {
	
	@Autowired
	RedisTemplate<String, Object> redisTemplate;
	
	@Autowired
	ScatterRepository scatterRepository;
	
	private int defaultTokenLength = 3;


	@Override
	public Scatter makeScatter(String userId, String roomId, Long price, int dividerNumber) {
		final Scatter scatter = new Scatter();
		scatter.setOwnerUserId(userId);
		scatter.setRoomId(roomId);
		scatter.setToken(this.makeToken(defaultTokenLength)); 
		scatter.setDividerNumber(dividerNumber);
		scatter.setPrice(price);
		scatter.setDetails(this.makeScatterDetails(price, dividerNumber));
		scatterRepository.save(scatter);
		return scatter;
	}
	
	private String makeToken(int number) {
		return RandomStringUtils.randomAlphabetic(number);
	}

	private ArrayList<ScatterDetail> makeScatterDetails(Long price, int dividerNumber) {
		final ArrayList<ScatterDetail> details = new ArrayList<ScatterDetail>();
		System.out.println("divderNumber");
		System.out.println(dividerNumber);
		System.out.println("price");
		System.out.print(price);

		
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
			System.out.println("mod");
			System.out.println(mod);
			System.out.println("devidedPrice");
			System.out.println(detail.getDividedPrice());

		}
		
		return details;
	}
}
    