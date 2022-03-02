package com.example.consumerservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Slf4j
@Service
public class ConsumerService {

    private final WebClient webClient;

    public Mono<String> go() {
        return webClient
                .get()
                .uri("/v1/get")
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> goV2() {
        return webClient
                .get()
                .uri("/v2/get")
                .retrieve()
                .bodyToMono(String.class);
    }
}
