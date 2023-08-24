package com.tenpo.app.configuration;

import com.tenpo.app.dto.PercentageDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public ReactiveRedisTemplate<String, PercentageDTO> reactiveRedisTemplate(ReactiveRedisConnectionFactory factory) {
        RedisSerializationContext<String, PercentageDTO> serializationContext = RedisSerializationContext
                .<String, PercentageDTO>newSerializationContext(new StringRedisSerializer())
                .value(new Jackson2JsonRedisSerializer<>(PercentageDTO.class))
                .build();

        return new ReactiveRedisTemplate<>(factory, serializationContext);
    }
}
