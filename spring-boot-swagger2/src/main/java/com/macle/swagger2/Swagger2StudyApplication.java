package com.macle.swagger2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class Swagger2StudyApplication {
    public static void main(String[] args) {
        SpringApplication.run(Swagger2StudyApplication.class, args);
        log.info("SWAGGER:: + http://localhost:8080/swagger-ui/index.html");

    }
}
