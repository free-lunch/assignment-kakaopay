package com.kakao.kakaopay.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kakao.kakaopay.dao.Scatter;
import com.kakao.kakaopay.dto.RequestScatterDTO;
import com.kakao.kakaopay.service.ScatterService;

import lombok.Data;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping(value="/transfer/v1")
public class TransferController {
	
	@Autowired
	private ScatterService scatterService;


	@PostMapping(value = "/scatter")
	public String makeScatter(
			@RequestHeader(value="X-USER-ID") String userId,
			@RequestHeader(value="X-ROOM-ID") String roomId,
			@RequestBody RequestScatterDTO  scatterDTO

	) {
		System.out.print(scatterDTO.getPrice());
		final Scatter scatter = this.scatterService.makeScatter(userId, roomId, scatterDTO.getPrice(), scatterDTO.getDividerNumber());
		return scatter.getToken();
	}

	@PutMapping(value = "/scatter/{token:[a-zA-Z]{3}}")
	public String receiveScatter(
			@RequestHeader(value="X-USER-ID") String userId,
			@RequestHeader(value="X-ROOM-ID") String roomId,
			@PathVariable(value="token") String token
	) {
		// redis lock using redisson 
		// check roomId
		// check limit time (10 min)
		// check duplicated user
		// check owner user
		return "hello";
	}
	
	@GetMapping(value = "/scatter/{token:[a-zA-Z]{3}}")
	public String getScatter(
			@RequestHeader(value="X-USER-ID") String userId,
			@RequestHeader(value="X-ROOM-ID") String roomId,
			@PathVariable(value="token") String token
	) {
		// check owner user
		// check limit time 7 day
		return "hello";
	}



}