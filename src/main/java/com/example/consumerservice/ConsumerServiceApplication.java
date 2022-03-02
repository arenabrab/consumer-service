package com.example.consumerservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@Slf4j
public class ConsumerServiceApplication /*implements CommandLineRunner*/ {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerServiceApplication.class, args);
    }

    @Bean
    WebClient webClient() {
        return WebClient.create("http://localhost:8081");
    }
//
//
//    @Override
//    public void run(String... args) throws Exception {
//        webClient()
//                .get()
//                .uri("/v1/get")
//                .retrieve()
//                .bodyToMono(String.class)
//                .subscribe(log::info);
//    }
}
