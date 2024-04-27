package com.macle.beanname.validator;

import com.macle.beanname.validator.components.NamingConventionValidator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class MainApplication {

    public static void main(String... args) {
        ConfigurableApplicationContext context = SpringApplication.run(MainApplication.class, args);
        NamingConventionValidator validator = context.getBean(NamingConventionValidator.class);
        String basePackage = "com.macle.beanname.validator"; // Specify your base package here
        validator.validateNamingConventions(basePackage);
    }
}