package com.macle.study.deployment.controller;

import com.macle.study.deployment.service.Calculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private Calculator calculator;

    @GetMapping("/add/{a}/{b}")
    public int add(@PathVariable int a, @PathVariable int b){
        return calculator.add(a, b);
    }

    @GetMapping("/calculate/{a}/{b}")
    public int calculate(@PathVariable int a, @PathVariable int b){
        return calculator.calculate(a, b);
    }
}
