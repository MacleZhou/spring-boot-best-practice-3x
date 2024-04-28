package com.macle.rsocket.server.handler;

import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.util.DefaultPayload;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class MessageRSocketHandler implements RSocket {
    @Override
    public Mono<Void> fireAndForget(Payload payload) { //无响应 用于日志
        String message = payload.getDataUtf8(); //获取数据
        log.info("[fireAndForget]接收请求数据:{}",message);
        return Mono.empty();
    }

    @Override
    public Mono<Payload> requestResponse(Payload payload) { //传统模式 有请求有响应
        String message = payload.getDataUtf8(); //获取数据
        log.info("[RequestAndResponse]接收请求数据:{}",message);
        return Mono.just(DefaultPayload.create("[echo]"+message));
    }

    @Override
    public Flux<Payload> requestStream(Payload payload) { //处理流数据
        String message = payload.getDataUtf8(); //获取数据
        log.info("[RequestStream]接收请求数据:{}",message);
        return Flux.fromStream(message.chars()          //将接收到的字符串转换为int型数据流
                .mapToObj(c->Character.toUpperCase(c)) //将里边的每一个字符编码大写
                .map(Object::toString)//将字符转为String
                .map(DefaultPayload::create)); //创建payload附加数据
    }

    @Override
    public Flux<Payload> requestChannel(Publisher<Payload> publisher) { //双向流

        return Flux.from(publisher).map(Payload::getDataUtf8).map(msg->{
            log.info("【RequestChannel】接收请求数据:{}",msg);
            return msg;
        }).map(DefaultPayload::create);
    }

    @Override
    public Mono<Void> metadataPush(Payload payload) {
        return null;
    }

    @Override
    public Mono<Void> onClose() {
        return null;
    }

    @Override
    public void dispose() {

    }
}