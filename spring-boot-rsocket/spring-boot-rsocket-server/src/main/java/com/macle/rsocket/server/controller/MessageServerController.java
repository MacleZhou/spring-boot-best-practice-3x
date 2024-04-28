package com.macle.rsocket.server.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Random;

/**
 * Rest 服务调用接口
 * 微信公众号：Java技术栈
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class MessageServerController {

    //4.1 Request-Response模式
    @MessageMapping("/request-response/received-message")
    public Mono<String> handleMessage(Mono<String> message) {
        return message.doOnNext(msg -> {
            System.out.printf("接收到消息：%s%n", msg) ;
        }).map(msg -> "服务器成功收到了你的消息！！！") ;
    }


    //4.2 Request-Stream模式
    // 必须返回Flux
    @MessageMapping("/request-stream/received-message")
    public Flux<String> handleStream() {
        return Flux
                .interval(Duration.ofSeconds(2))
                // 随机生成
                .map(i -> String.valueOf(new Random().nextInt(10000000)))
                // 只在此通道中获取10个值
                .take(10)
                .doOnComplete(() -> {
                    System.out.println("completed...") ;
                }) ;
    }


    //4.3 Channel
    @MessageMapping("/channel/received-message")
    public Flux<String> handleChannel(Flux<String> datas) {
        return datas.doOnNext(ret -> {
            System.out.printf("【server】%s - 接收到数据: %s%n", Thread.currentThread().getName(), ret) ;
        }).map(ret -> {
            return ret + " - " + new Random().nextInt(1000) ;
        }) ;
    }


    //4.4 Fire-and-Forget
    @MessageMapping("/fire-and-forget/received-message")
    public Mono<Void> handleFireAndForget(Mono<String> data) {
        return data.doOnNext(ret -> {
            System.out.printf("【server】%s - 接收到数据: %s%n", Thread.currentThread().getName(), ret) ;
        }).then() ;
    }

    //4.4 Fire-and-Forget
    @MessageMapping("/fire-and-forget2/received-message")
    public Mono<String> handleFireAndForget2(Mono<String> data) {
        return data.doOnNext(ret -> {
            System.out.printf("【server】%s - 接收到数据: %s%n", Thread.currentThread().getName(), ret) ;
        }).thenReturn("mono-message from server side fire-and-forget2");
    }
}
