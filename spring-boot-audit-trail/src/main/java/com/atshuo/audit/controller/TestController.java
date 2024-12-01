package com.atshuo.audit.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @RequestMapping(value = "/hello")
    public String hello(){
        return "Hello World";
    }

    @RequestMapping(value = "/hello/{id}")
    public String hello2(@PathVariable  String id){
        return "Hello World "+id;
    }
}
