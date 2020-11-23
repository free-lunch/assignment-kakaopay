package com.kakao.kakaopay.config;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import java.io.IOException;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.data.connection.RedissonConnectionFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableRedisRepositories
public class RedisConfig {
	private final RedisProperties redisProperties;

	
	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
        Config config = new Config();
        config.useSingleServer()
        .setAddress(String.format("redis://%s:%s", redisProperties.getHost(), redisProperties.getPort()));
		 return new RedissonConnectionFactory(config);
	}
	
	 @Bean
	 public RedissonClient redisson() throws IOException {
		 Config config = new Config();
	     config.useSingleServer()
	     .setAddress(String.format("redis://%s:%s", redisProperties.getHost(), redisProperties.getPort()));
	     
	     return Redisson.create(config);
	 }
	
    @Bean
    public RedisTemplate<?, ?> redisTemplate() {
        RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }
}
