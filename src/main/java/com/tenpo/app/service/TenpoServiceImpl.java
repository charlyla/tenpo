package com.tenpo.app.service;

import com.tenpo.app.dto.NumberDTO;
import com.tenpo.app.dto.PercentageDTO;
import com.tenpo.app.exceptions.ExternalServiceException;
import com.tenpo.app.model.TenpSum;
import com.tenpo.app.repository.TenpoSumRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.Duration;

import static com.tenpo.app.utils.CONSTANTS.MAXIMUM_RETRY_ALLOWED;

@Slf4j
@Service
public class TenpoServiceImpl implements ITenpoService {

    @Autowired
    private WebClient client;
    @Autowired
    private TenpoSumRepository tenpoSumRepository;
    private final ReactiveRedisTemplate<String, PercentageDTO> redisTemplate;
    private final KafkaTemplate<String, TenpSum> kafkaTemplate;
    private static final String REDIS_KEY = "percentage";
    private static final long CACHE_THIRTY_MINUTES = 1800; // In seconds
    private static final long CACHE_THIRTY_SECONDS = 30; // In seconds

    private static final String TOPIC = "tenpSum_topic";

    @Autowired
    public TenpoServiceImpl(ReactiveRedisTemplate<String, PercentageDTO> redisTemplate,
                            KafkaTemplate<String, TenpSum> kafkaTemplate) {
        this.redisTemplate = redisTemplate;
        this.kafkaTemplate = kafkaTemplate;
    }

    public Mono<TenpSum> getSumWithPercentage(NumberDTO numberDTO, String requestURL) {
        return redisTemplate.opsForValue()
                .get(REDIS_KEY)
                .switchIfEmpty(fetchAndCachePercentageDTO())
                .flatMap(percentageDTO -> calculateSumWithPercentage(numberDTO, percentageDTO, requestURL));
    }

    @Override
    public Flux<TenpSum> findAllBy(Pageable pageable) {
        return tenpoSumRepository.findAllBy(pageable);
    }

    private Mono<PercentageDTO> fetchAndCachePercentageDTO() {
        return client.get()
                .retrieve()
                .bodyToMono(PercentageDTO.class)
                .doOnNext(percentageDTO -> redisTemplate.opsForValue()
                        .set(REDIS_KEY, percentageDTO, Duration.ofSeconds(CACHE_THIRTY_MINUTES))
                        .subscribe())
                .retry(MAXIMUM_RETRY_ALLOWED)
                .onErrorResume(throwable -> {
                  log.error("Failed to fetch percentage from external service after retries.", throwable);
                  return Mono.error(new ExternalServiceException("External service is unavailable.", HttpStatus.SERVICE_UNAVAILABLE));
                });
    }

    private Mono<TenpSum> calculateSumWithPercentage(NumberDTO numberDTO, PercentageDTO percentageDTO, String requestURL) {
        TenpSum tenpRes = TenpSum.builder().value(
                (numberDTO.getNumberA() + numberDTO.getNumberB()) * (1 + (double) percentageDTO.getValue() / 100)
        )
                .ip(requestURL)
                .build();
        kafkaTemplate.send(TOPIC, tenpRes);
        return Mono.just(tenpRes);
    }

}
