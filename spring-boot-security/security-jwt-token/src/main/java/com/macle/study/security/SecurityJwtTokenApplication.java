package com.macle.study.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.macle.study.security")
@SpringBootApplication
public class SecurityJwtTokenApplication {
    public static void main(String[] args) {
        SpringApplication.run(SecurityJwtTokenApplication.class, args);
    }
}
