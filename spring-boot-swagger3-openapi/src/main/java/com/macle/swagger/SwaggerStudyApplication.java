package com.macle.swagger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class SwaggerStudyApplication {
    public static void main(String[] args) {
        SpringApplication.run(SwaggerStudyApplication.class, args);
        log.info("SWAGGER:: + http://localhost:8085/test-swagger/swagger-ui/index.html");

    }
}
