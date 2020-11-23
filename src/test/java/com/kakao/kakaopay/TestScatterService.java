package com.kakao.kakaopay;

import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.kakao.kakaopay.dao.Scatter;
import com.kakao.kakaopay.dao.ScatterRepository;
import com.kakao.kakaopay.exception.DuplicateRequestException;
import com.kakao.kakaopay.exception.ExternalUserRequestException;
import com.kakao.kakaopay.exception.FullPreemptException;
import com.kakao.kakaopay.exception.InvalidOwnerRequestExpection;
import com.kakao.kakaopay.exception.MyselfRequestException;
import com.kakao.kakaopay.exception.NotExistScatterException;
import com.kakao.kakaopay.exception.TimeoutPreemptException;
import com.kakao.kakaopay.service.ScatterService;

public class TestScatterService extends BaseTest {
    @MockBean
    private ScatterRepository scatterRepository;
    
    @Autowired ScatterService scatterService;
    
	@Test
	@DisplayName("Make Scatter")
	public void testMakeScatter() throws Exception {
		final Scatter stub= this.makeStub().get();
		when(this.scatterRepository.save(any(Scatter.class))).thenReturn(stub);
		final Scatter scatter = this.scatterService.makeScatter(this.stubUserId, this.stubRoomId, this.stubPrice, this.stubDividerNumber);
		
		assertThat(scatter.getOwnerUserId(), is(this.stubUserId));
		assertThat(scatter.getRoomId(), is(this.stubRoomId));
		assertThat(scatter.getPrice(), is(this.stubPrice));
		assertThat(scatter.getDetails().size(), is(this.stubDividerNumber));
		assertThat(scatter.getToken().length(), is(3));
	}

	@Test
	@DisplayName("Preempt Scatter")
	void testPreemptScatter() throws Exception {
		final Optional<Scatter> stub= this.makeStub();
		when(this.scatterRepository.findById(anyString())).thenReturn(stub);
		final Long price = this.scatterService.preemptScatterDetail(this.stubToken, "USER-3", this.stubRoomId);
		
		assertThat(price, is(stub.get().getDetails().get(1).getDividedPrice()));
		assertThat(true, is(stub.get().getDetails().get(1).getIsPreempted()));
	}

	@Test
	@DisplayName("Get Scatter")
	void testGetScatter() throws Exception {
		final Optional<Scatter> stub= this.makeStub();
		when(this.scatterRepository.findById(anyString())).thenReturn(stub);
		final Scatter scatter = this.scatterService.getScatterByOwner(this.stubToken, this.stubUserId);
		
		assertThat(scatter.getOwnerUserId(), is(this.stubUserId));
		assertThat(scatter.getRoomId(), is(this.stubRoomId));
		assertThat(scatter.getPrice(), is(this.stubPrice));
		assertThat(scatter.getDetails().size(), is(this.stubDividerNumber));
		assertThat(scatter.getToken().length(), is(3));
	}

    
	@Test
	@DisplayName("유저의 중복된 선점 요청이 있을경우 ")
	void testDuplicateRequestException() {
		final Optional<Scatter> stub= this.makeStub();
		when(this.scatterRepository.findById(anyString())).thenReturn(stub);
		assertThrows(DuplicateRequestException.class, () -> {
			this.scatterService.preemptScatterDetail(this.stubToken, "USER-2", this.stubRoomId);
		});
	}
	
	@Test
	@DisplayName("다 선점되었을 경우 ")
	void testFullPreemptException() {
		final Optional<Scatter> stub= this.makeStub();
		when(this.scatterRepository.findById(anyString())).thenReturn(stub);
		
		this.scatterService.preemptScatterDetail(this.stubToken, "USER-3", this.stubRoomId);
		this.scatterService.preemptScatterDetail(this.stubToken, "USER-4", this.stubRoomId);

		assertThrows(FullPreemptException.class, () -> {
			this.scatterService.preemptScatterDetail(this.stubToken, "USER-5", this.stubRoomId);
		});
	}
	
	@Test
	@DisplayName("자신이 뿌린건에 대해 선점 요청을 할 경우 ")
	void testMyselfRequestException() {
		final Optional<Scatter> stub= this.makeStub();
		when(this.scatterRepository.findById(anyString())).thenReturn(stub);
		
		assertThrows(MyselfRequestException.class, () -> {
			this.scatterService.preemptScatterDetail(this.stubToken, "USER-1", this.stubRoomId);
		});
	}
	
	@Test
	@DisplayName("다른 방 유저가 선점 요청을 할 경우 ")
	void testExternalUserRequestException() {
		final Optional<Scatter> stub= this.makeStub();
		when(this.scatterRepository.findById(anyString())).thenReturn(stub);
		
		assertThrows(ExternalUserRequestException.class, () -> {
			this.scatterService.preemptScatterDetail(this.stubToken, "USER-3", "OTHER-ROOM-ID");
		});
	}
	
	@Test
	@DisplayName("다른 방 유저가 요청을 할 경우 ")
	void testTimeoutPreemptException() {
		final Optional<Scatter> stub= this.makeStub();
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.MINUTE, -11);
		stub.get().setCreatedAt(cal.getTime());
		when(this.scatterRepository.findById(anyString())).thenReturn(stub);
		
		assertThrows(TimeoutPreemptException.class, () -> {
			this.scatterService.preemptScatterDetail(this.stubToken, "USER-3", this.stubRoomId);
		});
	}
	
	@Test
	@DisplayName("다른사람이 뿌리기 조회 요청을 할 경우 ")
	void testInvalidOwnerRequestExpection() {
		final Optional<Scatter> stub= this.makeStub();
		when(this.scatterRepository.findById(anyString())).thenReturn(stub);
		
		assertThrows(InvalidOwnerRequestExpection.class, () -> {
			this.scatterService.getScatterByOwner(this.stubToken, "OTHER-USER-ID");
		});
	}
	
//	NotExistScatterException
	@Test
	@DisplayName("토큰에 맞는 뿌리기가 없거나, 레디스에서 7일이 지나 삭제된 건 ")
	void testNotExistScatterException() {
		assertThrows(NotExistScatterException.class, () -> {
			this.scatterService.getScatterByOwner("NOT-EXIST-USERID", this.stubUserId);
		});
	}


	

}
