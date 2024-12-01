package com.sgz.cg.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping(path = "/user", produces = "application/json;charset=utf-8")
@RequestMapping(path = "/test")
public class TestController {

    @GetMapping("/t")
    public String test(@RequestParam String msg) {
        return "返回测试数值" + msg;
    }
}
