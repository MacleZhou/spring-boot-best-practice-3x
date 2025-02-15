package com.macle.study.conditional.api;

import com.macle.study.conditional.config.ConditionalOnApi2;
import jakarta.annotation.PostConstruct;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api2")
@ConditionalOnApi2("pack.api.enabled3")
public class ApiController3 {
    @PostConstruct
    public void init() {
        System.err.println("ApiController3 init...") ;
    }
}
