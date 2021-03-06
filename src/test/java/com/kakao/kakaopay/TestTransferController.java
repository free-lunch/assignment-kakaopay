package com.kakao.kakaopay;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import com.kakao.kakaopay.dao.Scatter;
import com.kakao.kakaopay.dao.ScatterRepository;
import com.kakao.kakaopay.dto.RequestScatterDTO;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasLength;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class TestTransferController extends BaseTest {
    @MockBean
    private ScatterRepository scatterRepository;

	@Test
	public void postScatterTest() throws Exception {
		// given
		Optional<Scatter> stub = this.makeStub();
		final RequestScatterDTO request = new RequestScatterDTO(3000L, 3);
		when(this.scatterRepository.save(any(Scatter.class))).thenReturn(stub.get());

		// when
		this.mvc.perform(post("/transfer/v1/scatter")
				.content(this.objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON)
				.header("X-USER-ID", "USER-1")
				.header("X-ROOM-ID", "ROOM-1")
				.accept(MediaType.APPLICATION_JSON))
		//then
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.token").exists())
				.andExpect(jsonPath("$.token").isString())
				.andExpect(jsonPath("$.token", hasLength(3)))
				.andDo(print());
	}
	
	@Test
	public void preemptScatterTest() throws Exception {
		// given
		Optional<Scatter> stub = this.makeStub();
		when(this.scatterRepository.findById(anyString())).thenReturn(stub);
		
		// when
		this.mvc.perform(put(String.format("/transfer/v1/scatter/%s", this.stubToken))
				.contentType(MediaType.APPLICATION_JSON)
				.header("X-USER-ID", "USER-3")
				.header("X-ROOM-ID", "ROOM-1")
				.accept(MediaType.APPLICATION_JSON))
		//then
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.price").exists())
				.andDo(print());
	}

	@Test
	public void getScatterTest() throws Exception {
		// given
		Optional<Scatter> stub = this.makeStub();
		when(this.scatterRepository.findById(anyString())).thenReturn(stub);
		
		// when
		this.mvc.perform(get(String.format("/transfer/v1/scatter/%s", this.stubToken))
				.contentType(MediaType.APPLICATION_JSON)
				.header("X-USER-ID", "USER-1")
				.header("X-ROOM-ID", "ROOM-1")
				.accept(MediaType.APPLICATION_JSON))
		//then
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.createdAt").exists())
				.andExpect(jsonPath("$.price", is(stub.get().getPrice().intValue())))
				.andExpect(jsonPath("$.preemptedPrice").exists())
				.andExpect(jsonPath("$.details").exists())
				.andExpect(jsonPath("$.details.[0].dividedPrice", is(stub.get().getDetails().get(0).getDividedPrice().intValue())))
				.andExpect(jsonPath("$.details.[0].preemptedUserId", is(stub.get().getDetails().get(0).getPreemptedUserId())))
				.andDo(print());
	}
}
