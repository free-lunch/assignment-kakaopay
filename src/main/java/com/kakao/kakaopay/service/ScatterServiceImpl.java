package com.kakao.kakaopay.service;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RedissonClient;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.kakao.kakaopay.dao.Scatter;
import com.kakao.kakaopay.dao.ScatterDetail;
import com.kakao.kakaopay.dao.ScatterRepository;
import com.kakao.kakaopay.exception.DuplicateRequestException;
import com.kakao.kakaopay.exception.ExternalUserRequestException;
import com.kakao.kakaopay.exception.FullPreemptException;
import com.kakao.kakaopay.exception.InvalidOwnerRequestExpection;
import com.kakao.kakaopay.exception.MyselfRequestException;
import com.kakao.kakaopay.exception.NotExistScatterException;
import com.kakao.kakaopay.exception.TimeoutPreemptException;

@Service
public class ScatterServiceImpl implements ScatterService {
	
	@Autowired
	RedisTemplate<String, Object> redisTemplate;
	
	@Autowired
	ScatterRepository scatterRepository;
	
	@Autowired
	RedissonClient redissonClient;
	
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
		return scatterRepository.save(scatter);
	}
	
	@Override
	public Long preemptScatterDetail(String token, String userId, String roomId) {
		Scatter scatter = this.getScatter(token, userId);
		// Timeout for 10 minutes
		if ((new Date()).getTime() - scatter.getCreatedAt().getTime() > 10 * 60 * 1000) {
			System.out.println((new Date()).getTime() - scatter.getCreatedAt().getTime() );
			throw new TimeoutPreemptException();
		}
		
		// Request by owner
		if (scatter.getOwnerUserId().equals(userId)) {
			throw new MyselfRequestException();
		}
		
		// Request by not same room
		if (!scatter.getRoomId().equals(roomId)) {
			throw new ExternalUserRequestException();
		}
		
		// Redis lock
		RLock lock = this.redissonClient.getLock(token);
		Long preemptedPrice = null;

		try {
			boolean res = lock.tryLock(60, 10, TimeUnit.SECONDS);
			if (!res) {
				throw new RuntimeException("락 획득에 실패하였습니다.");
			}

			for (ScatterDetail detail: scatter.getDetails()) {
				if (userId.equals(detail.getPreemptedUserId()))  {
					// Request by duplicated request
					throw new DuplicateRequestException();
				}

				// Preempt price
				if (!detail.getIsPreempted()) {
					detail.setIsPreempted(true);
					detail.setPreemptedUserId(userId);
					preemptedPrice = detail.getDividedPrice();
					break;
				}
			}
			
			if (preemptedPrice == null) {
				throw new FullPreemptException();
			}
			scatterRepository.save(scatter);
			
		} catch (InterruptedException e) {
			throw new Error("락 획득 과정에서 오류가 발생 하였습니다.");
			
		}finally {
			lock.unlock();
		}

		return preemptedPrice;
		
	}
	
	@Override
	public Scatter getScatterByOwner(String token, String userId) {
		Scatter scatter = this.getScatter(token, userId);

		if (!scatter.getOwnerUserId().equals(userId)) {
			throw new InvalidOwnerRequestExpection();
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
    