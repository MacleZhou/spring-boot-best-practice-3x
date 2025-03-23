package com.macle.study.resilience4j;

import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "PT30S")
public class Resilience4jRateLimitingApplication {

    public static void main(String[] args) {
        SpringApplication.run(Resilience4jRateLimitingApplication.class, args);
    }
}