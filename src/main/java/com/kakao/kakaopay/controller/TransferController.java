package com.kakao.kakaopay.controller;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kakao.kakaopay.dao.Scatter;
import com.kakao.kakaopay.dto.RequestScatterDTO;
import com.kakao.kakaopay.dto.ResponseGetScatterDTO;
import com.kakao.kakaopay.dto.ResponseGetScatterDTO.DetailDTO;
import com.kakao.kakaopay.dto.ResponseMakeScatterDTO;
import com.kakao.kakaopay.dto.ResponsePreemptScatterDTO;
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
	public ResponseEntity<ResponseMakeScatterDTO> makeScatter(
			@RequestHeader(value="X-USER-ID") String userId,
			@RequestHeader(value="X-ROOM-ID") String roomId,
			@RequestBody RequestScatterDTO  scatterDTO

	) {
		final Scatter scatter = this.scatterService.makeScatter(userId, roomId, scatterDTO.getPrice(), scatterDTO.getDividerNumber());

		ResponseMakeScatterDTO response = new ResponseMakeScatterDTO(scatter.getToken());
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PutMapping(value = "/scatter/{token:[a-zA-Z]{3}}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponsePreemptScatterDTO> preemptScatter(
			@RequestHeader(value="X-USER-ID") String userId,
			@RequestHeader(value="X-ROOM-ID") String roomId,
			@PathVariable(value="token") String token
	) {
		final Long price = this.scatterService.preemptScatterDetail(token, userId, roomId);

		ResponsePreemptScatterDTO response = new ResponsePreemptScatterDTO(price);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping(value = "/scatter/{token:[a-zA-Z]{3}}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseGetScatterDTO> getScatter(
			@RequestHeader(value="X-USER-ID") String userId,
			@RequestHeader(value="X-ROOM-ID") String roomId,
			@PathVariable(value="token") String token
	) {
		
		Scatter scatter = this.scatterService.getScatterByOwner(token, userId);

		final ResponseGetScatterDTO response = new ResponseGetScatterDTO(
				scatter.getCreatedAt().toInstant().toString(),
				scatter.getPrice(),
				scatter.getDetails().stream()
					.filter(d -> d.getIsPreempted())
					.mapToLong(d -> d.getDividedPrice())
					.sum(),
				scatter.getDetails().stream()
					.filter(d -> d.getIsPreempted())
					.map(d -> new DetailDTO(d.getDividedPrice(), d.getPreemptedUserId()))
					.collect(Collectors.toList())
		);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}