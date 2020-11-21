package com.kakao.kakaopay.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kakao.kakaopay.dao.Scatter;
import com.kakao.kakaopay.dto.RequestScatterDTO;
import com.kakao.kakaopay.exception.DuplicateRequestException;
import com.kakao.kakaopay.exception.ExternalUserRequestException;
import com.kakao.kakaopay.exception.MyselfRequestException;
import com.kakao.kakaopay.exception.NotExistScatterException;
import com.kakao.kakaopay.exception.TimeoutPreemptException;
import com.kakao.kakaopay.service.ScatterService;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping(value="/transfer/v1")
public class TransferController {
	
	@Autowired
	private ScatterService scatterService;


	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(value = "/scatter", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Object> makeScatter(
			@RequestHeader(value="X-USER-ID") String userId,
			@RequestHeader(value="X-ROOM-ID") String roomId,
			@RequestBody RequestScatterDTO  scatterDTO

	) {
		final Scatter scatter = this.scatterService.makeScatter(userId, roomId, scatterDTO.getPrice(), scatterDTO.getDividerNumber());

		JSONObject response = new JSONObject();
		response.put("token", scatter.getToken());
		return new ResponseEntity<>(response.toMap(), HttpStatus.CREATED);
	}

	@ExceptionHandler({
		DuplicateRequestException.class,
		ExternalUserRequestException.class,
		MyselfRequestException.class,
		NotExistScatterException.class,
		TimeoutPreemptException.class
		
	})
	@PutMapping(value = "/scatter/{token:[a-zA-Z]{3}}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> preemptScatter(
			@RequestHeader(value="X-USER-ID") String userId,
			@RequestHeader(value="X-ROOM-ID") String roomId,
			@PathVariable(value="token") String token
	) {
		final Long price = this.scatterService.preemptScatterDetail(token, userId, roomId);

		JSONObject response = new JSONObject();
		response.put("price", price);
		return new ResponseEntity<Object>(response.toMap(), HttpStatus.OK);
	}
	
	@GetMapping(value = "/scatter/{token:[a-zA-Z]{3}}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getScatter(
			@RequestHeader(value="X-USER-ID") String userId,
			@RequestHeader(value="X-ROOM-ID") String roomId,
			@PathVariable(value="token") String token
	) {
		
		Scatter scatter = this.scatterService.getScatterByOwner(token, userId);

		JSONObject response = new JSONObject();
		response.put("createdAt", scatter.getCreatedAt().toInstant().toString());
		response.put("price", scatter.getPrice());
		response.put("details", scatter.getDetails());

		return new ResponseEntity<Object>(response.toMap(), HttpStatus.OK);
	}



}