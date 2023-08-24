package com.tenpo.app.controller;

import com.tenpo.app.configuration.RateLimitingFilter;
import com.tenpo.app.dto.NumberDTO;
import com.tenpo.app.model.TenpSum;
import com.tenpo.app.repository.TenpoSumRepository;
import com.tenpo.app.service.ITenpoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;

@Disabled
@WebFluxTest(TenpoController.class)
public class TenpoControllerTest {

    @MockBean
    private ITenpoService iTenpoService;

    @MockBean
    private RateLimitingFilter rateLimitingFilter;

    @MockBean
    private TenpoSumRepository tenpoSumRepository;

    @MockBean
    private ReactiveRedisTemplate<String, String> redisTemplate;

    @InjectMocks
    private TenpoController tenpoController;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        webTestClient = WebTestClient.bindToController(tenpoController).build();
    }

    @Disabled
    @Test
    public void testGetSumWithPercentage() {
        NumberDTO numberDTO = new NumberDTO();
        numberDTO.setNumberA(5);
        numberDTO.setNumberB(5);

        TenpSum tenpSum = new TenpSum();
        tenpSum.setValue(15);

        when(iTenpoService.getSumWithPercentage(numberDTO, "http://localhost:8070")).thenReturn(Mono.just(tenpSum));

        webTestClient.post()
                .uri("/v1/tenpo")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(numberDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TenpSum.class);
    }

    @Disabled
    @Test
    public void testGetHistory() {
        TenpSum tenpSum1 = new TenpSum();
        tenpSum1.setValue(15);
        TenpSum tenpSum2 = new TenpSum();
        tenpSum2.setValue(15);

        when(iTenpoService.findAllBy(null)).thenReturn(Flux.just(tenpSum1, tenpSum2));

        webTestClient.get()
                .uri("/v1/tenpo/history")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TenpSum.class);
    }
}