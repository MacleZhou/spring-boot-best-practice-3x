package com.macle.study.resilience4j.api;


import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ExampleController {

    private static final Logger logger = LoggerFactory.getLogger(ExampleController.class);

    @GetMapping("/hello")
    @RateLimiter(name = "exampleRateLimiter", fallbackMethod = "fallback")
    public String sayHello() {
        logger.info("Executing hello endpoint");
        return"Hello, World!";
    }

    public String fallback(Throwable t) {
        logger.warn("Fallback method called due to rate limiting", t);
        return"Too many requests, please try again later.";
    }
}