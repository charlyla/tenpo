package com.tenpo.app.controller;

import com.tenpo.app.dto.NumberDTO;
import com.tenpo.app.model.PaginationRequest;
import com.tenpo.app.model.TenpSum;
import com.tenpo.app.service.ITenpoService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
@RequestMapping("/v1/tenpo")
public class TenpoController {

    @Autowired
    private ITenpoService iTenpoService;

    @PostMapping
    public Mono<ResponseEntity<Mono<TenpSum>>> getSumWithPercentage(@Valid @RequestBody NumberDTO numberDTO, ServerHttpRequest request) {
        String requestURL = request.getURI().toString();

        log.info("Start controller");
        return Mono.just(
                ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(iTenpoService.getSumWithPercentage(numberDTO, requestURL)));
    };

    @GetMapping("/history")
    public Mono<ResponseEntity<Flux<TenpSum>>> getHistory(@ModelAttribute PaginationRequest paginationRequest) {
        Pageable pageable = PageRequest.of(paginationRequest.getPage(), paginationRequest.getSize());
        iTenpoService.findAllBy(pageable);
        return Mono.just(
                ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(iTenpoService.findAllBy(pageable)));
    }

}
