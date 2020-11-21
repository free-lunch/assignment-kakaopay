package com.kakao.kakaopay.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.kakao.kakaopay.dao.Scatter;
import com.kakao.kakaopay.dao.ScatterDetail;
import com.kakao.kakaopay.dao.ScatterRepository;
import com.kakao.kakaopay.exception.DuplicateRequestException;
import com.kakao.kakaopay.exception.ExternalUserRequestException;
import com.kakao.kakaopay.exception.MyselfRequestException;
import com.kakao.kakaopay.exception.NotExistScatterException;
import com.kakao.kakaopay.exception.TimeoutPreemptException;

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
		scatter.setDividerNumber(dividerNumber);
		scatter.setPrice(price);

		scatter.makeToken(this.defaultTokenLength);
		scatter.makeDetails(price, dividerNumber);
		scatterRepository.save(scatter);
		return scatter;
	}
	
	@Override
	public Long preemptScatterDetail(String token, String userId, String roomId) {
		System.out.println("preemptScatterDetail");
		Scatter scatter = this.getScatter(token, userId);
		System.out.println("1111");
		// Timeout for 10 minutes
		if ((new Date()).getTime() - scatter.getCreatedAt().getTime() > 10 * 60 * 1000) {
			System.out.println((new Date()).getTime() - scatter.getCreatedAt().getTime() );
			throw new TimeoutPreemptException();
		}
		System.out.println("2222");
		
		// Request by owner
		if (scatter.getOwnerUserId().equals(userId)) {
			throw new MyselfRequestException();
		}
		System.out.println("3333");
		
		// Request by not same room
		if (!scatter.getRoomId().equals(roomId)) {
			throw new ExternalUserRequestException();
		}
		System.out.println("4444");

		Long preemptedPrice = scatter.preemtDetail(userId);
		scatterRepository.save(scatter);
		return preemptedPrice;
		
	}
	
	@Override
	public Scatter getScatterByOwner(String token, String userId) {
		Scatter scatter = this.getScatter(token, userId);

		if (!scatter.getOwnerUserId().equals(userId)) {
			throw new Error();
		}
		
		return scatter;
	}

	
	private Scatter getScatter(String token, String userId) {
		final Optional<Scatter> _scatter = this.scatterRepository.findById(token);
		if (_scatter.isPresent()) {
			return _scatter.get();
		} else {
			throw new NotExistScatterException();
		}
		
	}


}
    