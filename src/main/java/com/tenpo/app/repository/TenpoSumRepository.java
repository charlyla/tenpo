package com.tenpo.app.repository;

import com.tenpo.app.model.TenpSum;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface TenpoSumRepository extends ReactiveCrudRepository<TenpSum, Long> {
    Flux<TenpSum> findAllBy(Pageable pageable);
}
