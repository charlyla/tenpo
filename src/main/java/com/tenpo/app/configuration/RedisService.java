package com.tenpo.app.configuration;

import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class RedisService {

    private final ReactiveRedisTemplate<String, String> redisTemplate;

    public RedisService(ReactiveRedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Mono<String> getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public Mono<Boolean> setValueWithExpiration(String key, String value, Duration duration) {
        return redisTemplate.opsForValue().set(key, value, duration);
    }
}