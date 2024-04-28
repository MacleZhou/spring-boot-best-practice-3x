package com.macle.rsocket.client.service;


import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Random;

@Service
public class PackRSocketService {
    private final RSocketRequester rsocketRequester;
    public PackRSocketService(RSocketRequester.Builder rsocketRequesterBuilder) {
        this.rsocketRequester = rsocketRequesterBuilder.tcp("localhost", 9898) ;
    }


    // Request-Response 发送一条信息，接收一条信息。
    public void sendMessage(String body) {
        this.rsocketRequester
                .route("/request-response/received-message")
                .data(body)
                .retrieveMono(String.class)
                .subscribe(System.out::println) ;
    }


    // Request-Stream 发送一条消息并接收返回的消息流。
    public void sendStream() {
        this.rsocketRequester
                .route("/request-stream/received-message")
                .retrieveFlux(String.class)
                .subscribe(ret -> {
                    System.out.printf("%s - 接受到数据: %s%n", Thread.currentThread().getName(), ret) ;
                }) ;
    }


    // Channel 双向发送消息流。
    public void sendChannel() {
        this.rsocketRequester
                .route("/channel/received-message")
                .data(Flux.just("1", "2", "3", "4", "5", "6").delayElements(Duration.ofSeconds(1)))
                .retrieveFlux(String.class)
                .subscribe(ret -> {
                    System.out.printf("【client】%s - 接受到数据: %s%n", Thread.currentThread().getName(), ret) ;
                }) ;
    }


    // Fire-and-Forget 发送单向消息。
    public void sendFireAndForget() {
        this.rsocketRequester
                .route("/fire-and-forget/received-message")
                .data(Mono.just(String.valueOf(new Random().nextInt(1000))))
                .send()
                .subscribe();
    }

    // Fire-and-Forget 发送单向消息。
    public Mono<String> sendFireAndForget2() {
        return this.rsocketRequester
                .route("/fire-and-forget2/received-message")
                .data(Mono.just(String.valueOf(new Random().nextInt(1000))))
                .retrieveMono(String.class);
    }
}