package com.tenpo.app.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

import static com.tenpo.app.utils.CONSTANTS.*;

@Component
public class RateLimitingFilter implements WebFilter {

    @Autowired
    private ReactiveRedisTemplate<String, String> redisTemplate;

    private static final Duration EXPIRATION = Duration.ofMinutes(1);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String ip = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
        String key = "rate_limit_" + ip;

        ReactiveValueOperations<String, String> opsForValue = redisTemplate.opsForValue();

        return opsForValue.get(key)
                .defaultIfEmpty("0")
                .flatMap(currentCount -> {
                    int count = Integer.parseInt(currentCount);
                    if (count >= MAXIMUM_CONNECTIONS_ALLOWED) {
                        exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                        byte[] bytes = THERE_ARE_NO_MORE_CONNECTIONS.getBytes(StandardCharsets.UTF_8);
                        exchange.getResponse().getHeaders().setContentType(MediaType.TEXT_PLAIN);
                        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(bytes)));
                    } else {
                        return opsForValue.increment(key)
                                .flatMap(newCount -> {
                                    if (newCount == 1) {
                                        return redisTemplate.expire(key, EXPIRATION)
                                                .then(chain.filter(exchange));
                                    }
                                    return chain.filter(exchange);
                                });
                    }
                });
    }
}