package com.kakao.kakaopay.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@RedisHash("scatter")
public class Scatter implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	private String token;
	private String roomId;
	private String ownerUserId;
	private Long price;
	private int dividerNumber;
	private ArrayList<ScatterDetail> details;
	private Date createdAt = new Date();
}

