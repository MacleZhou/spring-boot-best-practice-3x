package com.macle.study.anti.shaking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@SpringBootApplication
public class AntiShakingApplication {
    public static void main(String[] args) {
        SpringApplication.run(AntiShakingApplication.class, args);
    }
}
