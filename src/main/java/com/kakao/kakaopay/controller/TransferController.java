package com.kakao.kakaopay.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kakao.kakaopay.dto.RequestScatterDTO;

import lombok.Data;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping(value="/transfer/v1")
public class TransferController {

	@PostMapping(value = "/scatter")
	public String makeScatter(
			@RequestHeader(value="X-USER-ID") String userId,
			@RequestHeader(value="X-ROOM-ID") String roomId,
			@RequestBody RequestScatterDTO  scatterDTO

	) {
		System.out.print(scatterDTO.getPrice());
		return "hello";
		
	}

}