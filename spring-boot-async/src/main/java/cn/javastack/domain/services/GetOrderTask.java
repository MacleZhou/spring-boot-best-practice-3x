package cn.javastack.domain.services;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@Slf4j
public class GetOrderTask implements Callable<String> {

    @Override
    public String call() throws Exception {
        try {
            log.info("Query order begin");
            TimeUnit.SECONDS.sleep(3);
            log.info("Query order end");
        } catch (Exception ex){
            throw new RuntimeException(ex);
        }
        return "order info";
    }
}
