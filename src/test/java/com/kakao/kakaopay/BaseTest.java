package com.kakao.kakaopay;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakao.kakaopay.dao.Scatter;

@SpringBootTest
@AutoConfigureMockMvc
abstract public class BaseTest {
	
	
    @Autowired
    protected MockMvc mvc;

	@Autowired
	protected ObjectMapper objectMapper;
	
	protected String stubToken = "ABC";
	protected String stubUserId = "USER-1";
	protected String stubRoomId = "ROOM-1";
	protected Long stubPrice= 3000L;
	protected int stubDividerNumber= 3;
	protected String stubPreemtUserId = "USER-2";
	
	
	protected Optional<Scatter> makeStub() {
		Scatter scatter = new Scatter();
		scatter.setToken(this.stubToken);
		scatter.setPrice(this.stubPrice);
		scatter.setOwnerUserId(this.stubUserId);
		scatter.setRoomId(this.stubRoomId);
		scatter.setDividerNumber(this.stubDividerNumber);
		scatter.makeDetails(this.stubPrice, this.stubDividerNumber);
		scatter.preemtDetail(this.stubPreemtUserId);
		return Optional.ofNullable(scatter);
		
	}
}
