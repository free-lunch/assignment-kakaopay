package com.kakao.kakaopay;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.kakao.kakaopay.dao.Scatter;

public class TestScatter extends BaseTest {
	
	
	@Test
	@DisplayName("주어진 금액, 참가자수대로 잘 나누는지 체크 ")
	void testMakeDetails() throws Exception{
		Scatter scatter = new Scatter();
		scatter.makeDetails(3000L, 3);
		final Long dividedPriceSum = scatter.getDetails().stream()
			.mapToLong(d -> d.getDividedPrice())
			.sum();
		
		assertThat(scatter.getDetails().size(), is(3));
		assertThat(dividedPriceSum, is(3000L));
	}
	
	@DisplayName("토큰 생성 체크 ")
	void testMakeToken() throws Exception{
		Scatter scatter = new Scatter();
		scatter.makeToken(3);
		
		assertThat(scatter.getToken().length(), is(3));
	}

}
