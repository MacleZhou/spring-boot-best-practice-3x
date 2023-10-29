package cn.javastack.domain.services;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@Slf4j
public class GetOrderTaskThrowException implements Callable<String> {

    @Override
    public String call() throws Exception {
        try {
            log.info("Query order begin");
            TimeUnit.SECONDS.sleep(1);
            throw new NullPointerException();
        } catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }
}
