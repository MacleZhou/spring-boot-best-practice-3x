package com.macle.study.conditional.api;

import com.macle.study.conditional.config.ConditionalOnApi2;
import jakarta.annotation.PostConstruct;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api2")
@ConditionalOnApi2("pack.api.enabled2")
public class ApiController2 {
    @PostConstruct
    public void init() {
        System.err.println("ApiController2 init...") ;
    }
}
