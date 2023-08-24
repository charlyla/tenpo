package com.tenpo.app.service;

import com.tenpo.app.dto.NumberDTO;
import com.tenpo.app.model.TenpSum;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ITenpoService {

    Mono<TenpSum> getSumWithPercentage(NumberDTO numberDTO, String requestURL);

    Flux<TenpSum> findAllBy(Pageable pageable);
}
