package com.macle.study.conditional.api;

import com.macle.study.conditional.config.ConditionalOnApi;
import jakarta.annotation.PostConstruct;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@ConditionalOnApi
public class ApiController {
    @PostConstruct
    public void init() {
        System.err.println("ApiController init...") ;
    }
}
