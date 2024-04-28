package com.macle.rsocket.client.controller;

import com.macle.rsocket.client.service.PackRSocketService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest 服务调用接口
 * 微信公众号：Java技术栈
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class MessageClientController {

    @Resource
    private PackRSocketService packRSocketService;

    //4.1 Request-Response模式
    @GetMapping("/request-response/send-message")
    public Object message() {
        packRSocketService.sendMessage(String.valueOf(System.nanoTime())) ;
        return "message" ;
    }

    //4.2 Request-Stream模式
    @GetMapping("/request-stream/send-message")
    public Object message2() {
        packRSocketService.sendStream();
        return "message";
    }

    //4.3 Request-Stream模式
    @GetMapping("/channel/send-message")
    public Object message3() {
        packRSocketService.sendChannel();
        return "message" ;
    }

    //4.4 Fire-and-Forget
    @GetMapping("/fire-and-forget/send-message")
    public Object message4() {
        packRSocketService.sendFireAndForget();
        return "message" ;
    }

    //4.4 Fire-and-Forget
    @GetMapping("/fire-and-forget2/send-message")
    public Object message5() {
        return packRSocketService.sendFireAndForget2();
    }
}
