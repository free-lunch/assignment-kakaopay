package com.kakao.kakaopay;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakao.kakaopay.controller.TransferController;
import com.kakao.kakaopay.dto.RequestScatterDTO;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(TransferController.class)
public class TestTransferController {
	@Autowired
    private MockMvc mvc;
	
	@MockBean
	private TransferController testTransferController;
	
	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void postScatterTest() throws Exception {
		final RequestScatterDTO dto = new RequestScatterDTO();
		dto.setDividerNumber(3L);
		dto.setPrice(3000L);
		
		given(testTransferController.makeScatter("111", "123", dto))
			.willReturn("hello");
		
		String content = objectMapper.writeValueAsString(dto);

		mvc.perform(post("/transfer/v1/scatter")
				.content(content)
				.contentType(MediaType.APPLICATION_JSON)
				.header("X-USER-ID", "123")
				.header("X-ROOM-ID", "123")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andDo(print());
	}
}
